import { useEffect, useRef, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import SockJS from 'sockjs-client';
import { CompatClient, Stomp } from '@stomp/stompjs';

import { getRoomInfo } from '../../services/room';
import { BASE_URL, getAccessToken } from '../../constants/api';
import { RoomPlayTypes } from '../../types/RoomTypes';
import useRoomStore from '../../store/roomStore';
import useGameStore from '../../store/gameStore';

import { FlexLayout } from '../../styles/layout';
import Background from '../../components/Background';
import ChatBox from '../../components/ChatBox';
import RoomHeader from './components/RoomHeader';
import PlayerList from './components/PlayerList';
import RoomSetting from './components/RoomSetting';
import ButtonGroup from './components/ButtonGroup';
import StartCheckDialog from './components/StartCheckDialog';

export default function RoomPage() {
  const navigate = useNavigate();
  const ACCESS_TOKEN = getAccessToken();
  const header = {
    Authorization: `Bearer ${ACCESS_TOKEN}`,
    'Content-Type': 'application/json',
  };
  const { id } = useParams();
  const { roomData, setRoomData } = useRoomStore();
  const { gameData, setGameData } = useGameStore();
  const [isOpen, setIsOpen] = useState(false);
  const [isFetched, setIsFetched] = useState(false);
  const [playInfo, setPlayInfo] = useState<RoomPlayTypes | null>(null);
  const clientRef = useRef<CompatClient | null>(null);

  const connectHandler = () => {
    const socket = new SockJS(`${BASE_URL}/socket`);
    clientRef.current = Stomp.over(socket);
    clientRef.current.connect(header, () => {
      clientRef.current?.subscribe(
        `/topic/rooms/${id}`,
        (message) => {
          if (message.body === 'ROOM IS EXPIRED') {
            // clear session
            navigate(`/lobby`);
          } else if (message.body === 'GAME START') {
            setIsFetched(true);
          } else {
            setPlayInfo(JSON.parse(message.body));
          }
        },
        header
      );

      showRoomInfo();
    });
  };

  const showRoomInfo = async () => {
    try {
      const fetchedData = id && (await getRoomInfo(parseInt(id)));
      setRoomData(fetchedData.data);
    } catch (error: any) {
      console.error('방 정보 가져오기 에러', error);
      navigate(`/error`);
    }
  };

  useEffect(() => {
    connectHandler();
    return () => {
      clientRef.current?.disconnect(() => {
        clientRef.current?.unsubscribe(`/topic/rooms/${id}`);
      }, header);
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [ACCESS_TOKEN]);

  useEffect(() => {
    if (isFetched) {
      if (!roomData.isHost) {
        setGameData({
          ...gameData,
          roundId: -1,
          roomName: roomData.roomName,
          players:
            playInfo?.users.map((user) => ({
              roundId: -1,
              userId: user.userId,
              nickname: user.nickname,
              color: user.color,
              team: user.team,
              stamina: 1,
            })) || [],
        });
      }
      navigate(`/game/${id}`);
    }
    // navigate(`/game/${id}`, { replace: true });
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [isFetched]);

  const checkStart = () => {
    if (playInfo) {
      return playInfo.currentPlayers > 2;
    }
    return false;
  };

  return (
    <>
      <Background>
        <FlexLayout gap='4rem'>
          <FlexLayout $isCol gap='2.8rem'>
            <RoomHeader
              roomName={roomData.roomName}
              currentPlayers={playInfo?.currentPlayers || 0}
              limitPlayers={roomData.limitPlayers}
              isPublic={roomData.isPublic}
              isHost={roomData.isHost}
            />
            <FlexLayout $isCol gap='1.6rem'>
              <PlayerList
                isHost={roomData.isHost}
                players={playInfo?.users || []}
              />
              {roomData.isHost && <RoomSetting />}
            </FlexLayout>
            <ButtonGroup
              isHost={roomData.isHost}
              canStart={checkStart()}
              users={playInfo?.users || []}
              openDialog={() => setIsOpen(true)}
            />
          </FlexLayout>
          <div>
            <ChatBox mode='ROOM' text={`방 채팅`} />
          </div>
        </FlexLayout>
      </Background>
      {isOpen && <StartCheckDialog closeDialog={() => setIsOpen(false)} />}
    </>
  );
}
