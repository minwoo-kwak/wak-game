package com.wak.game.application.facade;

import com.wak.game.application.request.GameStartRequest;
import com.wak.game.application.response.GameStartResponse;
import com.wak.game.application.response.SummaryCountResponse;
import com.wak.game.application.response.socket.*;
import com.wak.game.application.vo.RoomVO;
import com.wak.game.domain.player.Player;
import com.wak.game.domain.player.PlayerService;
import com.wak.game.domain.player.dto.PlayerInfo;
import com.wak.game.domain.playerLog.PlayerLog;
import com.wak.game.domain.playerLog.PlayerLogService;
import com.wak.game.domain.rank.dto.RankInfo;
import com.wak.game.domain.room.dto.RoomInfo;
import com.wak.game.domain.room.Room;
import com.wak.game.domain.room.RoomService;
import com.wak.game.domain.round.Round;
import com.wak.game.domain.round.RoundService;
import com.wak.game.domain.round.dto.ClickDTO;
import com.wak.game.domain.user.User;
import com.wak.game.domain.user.UserService;
import com.wak.game.global.error.ErrorInfo;
import com.wak.game.global.error.exception.BusinessException;
import com.wak.game.global.util.RedisUtil;
import com.wak.game.global.util.SocketUtil;
import com.wak.game.global.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoundFacade {

    private final RoundService roundService;
    private final PlayerService playerService;
    private final PlayerLogService playerLogService;
    private final RoomService roomService;
    private final UserService userService;
    private final RedisUtil redisUtil;
    private final SocketUtil socketUtil;
    private final TimeUtil timeUtil;

    public GameStartResponse startGame(GameStartRequest gameStartRequest, Long roomId, Long userId) {
        User user = userService.findById(userId);
        Room room = roomService.findById(roomId);

        roomService.isHost(user, room);
        roomService.isInGame(room);

        RoomInfo roomInfo = redisUtil.getLobbyRoomInfo(room.getId());

        roomInfo.gameStart();
        redisUtil.saveData("roomInfo", String.valueOf(room.getId()), roomInfo);
        redisUtil.saveData("mention", String.valueOf(room.getId()), gameStartRequest.getComment());

        roomService.gameStart(room);
        return startRound(gameStartRequest, room);
    }

    public GameStartResponse startRound(GameStartRequest gameStartRequest, Room room) {
        Round round = roundService.startRound(room, gameStartRequest);
        int playerCnt = initializeGameStatuses(room, round);

        System.out.println("playerCnt:" + playerCnt);

        roundService.startThread(room.getId(), round.getId(), playerCnt);
        System.out.println("player");

        socketUtil.sendMessage("/rooms/" + room.getId().toString(), new RoundInfoResponse(round.getId(), round.getShowNickname()));

        return GameStartResponse.of(round.getId());
    }

    public int initializeGameStatuses(Room room, Round round) {
        Map<String, RoomVO> map = redisUtil.getRoomUsersInfo(room.getId());

        List<Player> players = new ArrayList<>();
        List<PlayerInfo> p = new ArrayList<>();

        for (Map.Entry<String, RoomVO> entry : map.entrySet()) {
            RoomVO roomUser = entry.getValue();
            PlayerInfo gameUser = new PlayerInfo(round.getId(), roomUser.userId(), roomUser.color(), roomUser.nickname(), roomUser.team(), roomUser.isHost(), 1);
            RankInfo rankInfo = RankInfo.builder()
                    .killCnt(0)
                    .nickname(roomUser.nickname())
                    .userId(roomUser.userId())
                    .color(roomUser.color())
                    .build();
            p.add(gameUser);

            String userKey = "roomId:" + room.getId() + ":users";
            String rankKey = "roomId:" + room.getId() + ":ranks";

            redisUtil.saveData(userKey, roomUser.userId().toString(), gameUser);
            redisUtil.saveData(rankKey, roomUser.userId().toString(), rankInfo);

            User user = userService.findById(roomUser.userId());

            Player player = Player.builder()
                    .user(user)
                    .round(round)
                    .stamina(1)
                    .aliveTime("0")
                    .killCount(0)
                    .rank(0)
                    .isPlayer(true)
                    .team(roomUser.team().equals("A") ? 1 : 2)
                    .isQueen(false)
                    .build();

            players.add(player);
        }

        BattleFeildInGameResponse battleFeildInGameResponse = new BattleFeildInGameResponse(false, p);

        playerService.savePlayers(players);
        socketUtil.sendMessage("/games/" + room.getId().toString() + "/battle-field", battleFeildInGameResponse);
        return players.size();
    }

    public Round startNextRound(Round previousRound) {
        return roundService.startNextRound(previousRound);
    }

    public void endRound(Long roomId, Long roundId) {
        Round round = roundService.findById(roundId);
        round.finish();
        Room room = roomService.findById(round.getRoom().getId());
        roomService.isNotInGame(room);

        List<ClickDTO> attacks = redisUtil.getListData("roomId:" + roomId + ":availableClicks", ClickDTO.class);
        Map<Long, Player> playerMap = playerService.getPlayerMap(roundId);

        Long startTime = timeUtil.toNanoOfEpoch(round.getCreatedAt());

        for (ClickDTO attack : attacks) {
            Long murderId = attack.getUserId();
            Long victimId = attack.getVictimId();
            Player murderPlayer = playerMap.get(murderId);
            Player victimPlayer = playerMap.get(victimId);

            if (victimPlayer == null && murderPlayer == null)
                throw new BusinessException(ErrorInfo.PLAYER_NOT_FOUND);

            Long attackTime = attack.getNanoSec();
            long aliveTime = attackTime - startTime;

            victimPlayer.updateOnAttack(murderPlayer, Long.toString(aliveTime));

            playerService.save(victimPlayer);
        }

        updateRanks(roomId, room, round, playerMap);

        savePlayerLogs(roomId, roundId);
        clearRedis(roomId);
        checkAndEndGame(room, round);
    }

    private void savePlayerLogs(Long roomId, Long roundId) {
        List<ClickDTO> data = redisUtil.getListData("roomId:" + roomId + ":clicks", ClickDTO.class);

        if (data == null || data.isEmpty()) {
            throw new BusinessException(ErrorInfo.ClICK_LOG_IS_EMPTY);
        }

        Map<Long, Player> playerMap = playerService.getPlayerMap(roundId);

        List<PlayerLog> logs = data.stream()
                .map(click -> playerLogService.createPlayerLog(click, playerMap))
                .collect(Collectors.toList());

        playerLogService.saveAll(logs);
    }


    private void updateRanks(Long roomId, Room room, Round round, Map<Long, Player> playersMap) {
        Map<String, RankInfo> ranks = redisUtil.getData("roomId:" + room.getId() + ":ranks", RankInfo.class);

        List<RankInfo> sortedRanks = new ArrayList<>(ranks.values());
        sortedRanks.sort((r1, r2) -> Integer.compare(r2.getKillCnt(), r1.getKillCnt()));

        int rank = 1;
        for (RankInfo rankInfo : sortedRanks) {
            Long userId = rankInfo.getUserId();
            Player player = playersMap.get(userId);

            if (player == null)
                throw new BusinessException(ErrorInfo.PLAYER_NOT_FOUND);

            player.updateRankAncKillCnt(rankInfo.getKillCnt(), rank++);
        }
    }

    private void clearRedis(Long roomId) {
        String firstKey = "roomId:" + roomId;

        redisUtil.deleteKey(firstKey + ":users");
        redisUtil.deleteKey(firstKey + ":ranks");
        redisUtil.deleteKey(firstKey + ":clicks");
        redisUtil.deleteKey(firstKey + ":availableClicks");
    }

    private void checkAndEndGame(Room room, Round round) {
        boolean isSoloMode = room.getMode().toString().equals("SOLO");
        boolean isTeamMode = room.getMode().toString().equals("TEAM");
        boolean isFinalSoloRound = isSoloMode && round.getRoundNumber() == 3;

        if (isFinalSoloRound || isTeamMode) {
            endGame(room);
        }
    }

    public void endGame(Room room) {
        RoomInfo roomInfo = redisUtil.getLobbyRoomInfo(room.getId());

        roomInfo.gameEnd();
        roomService.gameEnd(room);
        redisUtil.saveData("roomInfo", String.valueOf(room.getId()), roomInfo);
        socketUtil.sendRoomList();
        socketUtil.sendMessage("/rooms", room.getId().toString(), "ROUND END");
        socketUtil.sendMessage("/rooms", room.getId().toString(), "GAME END");
    }

    public void sendDashBoard(long roomId, int roundNumber) {

        SummaryCountResponse summaryCount = roundService.getSummaryCount(roomId, roundNumber);

        socketUtil.sendMessage("/games/" + roomId + "/dashboard", summaryCount);
    }

    public void sendBattleField(long roomId, boolean isFinished) {
        roomService.findById(roomId);

        String key = "roomId:" + roomId + ":users";
        Map<String, PlayerInfo> data = redisUtil.getData(key, PlayerInfo.class);

        List<PlayerInfo> players = new ArrayList<>();

        for (Map.Entry<String, PlayerInfo> entry : data.entrySet()) {
            players.add(entry.getValue());
        }

        socketUtil.sendMessage("/games/" + roomId + "/battle-field", new BattleFeildInGameResponse(isFinished, players));
    }

    public void sendMention(Long roomId) {

    }
}
