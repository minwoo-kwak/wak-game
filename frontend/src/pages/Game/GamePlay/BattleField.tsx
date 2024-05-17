import { useEffect, useRef, useState } from 'react';
import { CompatClient } from '@stomp/stompjs';
import { useNavigate } from 'react-router-dom';
import { getAccessToken } from '../../../constants/api';
import useUserStore from '../../../store/userStore';
import useGameStore from '../../../store/gameStore';
import useResultStore from '../../../store/resultStore';
import { PlayersTypes } from '../../../types/GameTypes';
import { isOverlap } from '../../../utils/isOverlap';

import styled from 'styled-components';
import GrayBox from '../../../components/GrayBox';
import PlayerNickname from '../../../components/PlayerNickname';
import { getBattleField } from '../../../services/game';

const BattleFieldLayout = styled.div`
  position: relative;
  width: 100%;
  height: 100%;
`;

const Dot = styled.div<{ $top: string; $left: string }>`
  position: absolute;
  top: ${(props) => props.$top};
  left: ${(props) => props.$left};
`;

type DotPosition = {
  top: string;
  left: string;
};

type BattleFieldProps = {
  client: CompatClient;
};

export default function BattleField({ client }: BattleFieldProps) {
  const navigate = useNavigate();
  const ACCESS_TOKEN = getAccessToken();
  const header = {
    Authorization: `Bearer ${ACCESS_TOKEN}`,
    'Content-Type': 'application/json',
  };
  const { userId } = useUserStore().userData;
  const { roundId, playersNumber } = useGameStore().gameData;
  const { setResultData } = useResultStore();
  const [playerList, setPlayerList] = useState<PlayersTypes[]>([]);
  const [dots, setDots] = useState<DotPosition[]>([]);
  const currentTime = new Date().toISOString();
  const subscribedRef = useRef(false);

  const subscribeToTopic = () => {
    if (!subscribedRef.current) {
      client.subscribe(
        `/topic/games/${roundId}/battle-field`,
        (message) => {
          if (message.body === 'ROOM IS EXPIRED') {
            navigate(`/lobby`);
          } else if (JSON.parse(message.body).isFinished) {
            setResultData(JSON.parse(message.body));
          } else {
            console.log('요청');
            setPlayerList(JSON.parse(message.body).players);
          }
        },
        header
      );
      subscribedRef.current = true;
    }
    showBattleField();
  };

  const showBattleField = async () => {
    try {
      await getBattleField(roundId);
    } catch (error: any) {
      console.error('배틀필드 요청 에러', error);
      navigate(`/error`);
    }
  };

  useEffect(() => {
    if (client && client.connected) {
      subscribeToTopic();
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [client]);

  useEffect(() => {
    const generatedDots: DotPosition[] = [];
    const generateRandomPosition = () => ({
      top: `${Math.random() * 92}%`,
      left: `${Math.random() * 92}%`,
    });

    while (generatedDots.length < playersNumber) {
      const newPosition = generateRandomPosition();
      if (!isOverlap(newPosition, generatedDots)) {
        generatedDots.push(newPosition);
      }
    }

    setDots(generatedDots);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [roundId]);

  const handleClick = (victimId: number) => {
    if (userId && parseInt(userId) !== victimId) {
      const message = JSON.stringify({
        roundId: roundId,
        userId: userId,
        victimId: victimId,
        clickTime: currentTime,
      });
      client.send(`/app/click/${roundId}`, header, message);
    }
  };

  return (
    <GrayBox mode={'MEDIUM'} width={'79.2rem'}>
      <BattleFieldLayout>
        {dots.map((dot, index) => (
          <Dot key={index} $top={dot.top} $left={dot.left}>
            {index < playerList.length && playerList[index].stamina > 0 && (
              <PlayerNickname
                isCol
                nickname={playerList[index].nickname}
                color={playerList[index].color}
                onClick={() => handleClick(playerList[index].userId)}
              />
            )}
          </Dot>
        ))}
      </BattleFieldLayout>
    </GrayBox>
  );
}
