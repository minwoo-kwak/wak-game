import { useEffect, useRef, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import SockJS from 'sockjs-client';
import { CompatClient, Stomp } from '@stomp/stompjs';

import { getRoomInfo } from '../../services/room';
import { BASE_URL, getAccessToken } from '../../constants/api';
import { RoomPlayTypes } from '../../types/RoomTypes';
import useRoomStore from '../../store/roomStore';

import { FlexLayout } from '../../styles/layout';
import Background from '../../components/Background';
import ChatBox from '../../components/ChatBox';
import RoomHeader from './components/RoomHeader';
import PlayerList from './components/PlayerList';
import RoomSetting from './components/RoomSetting';
import ButtonGroup from './components/ButtonGroup';

export default function RoomPage() {
  const navigate = useNavigate();
  const ACCESS_TOKEN = getAccessToken();
  const { id } = useParams();

  const { roomData, setRoomData } = useRoomStore();
  const [playInfo, setPlayInfo] = useState<RoomPlayTypes | null>(null);
  const client = useRef<CompatClient | null>(null);

  const connectHandler = () => {
    const socket = new SockJS(`${BASE_URL}/socket`);
    const header = {
      Authorization: `Bearer ${ACCESS_TOKEN}`,
      'Content-Type': 'application/json',
    };
    client.current = Stomp.over(socket);
    client.current.connect(header, () => {
      client.current?.subscribe(
        `/topic/rooms/${id}`,
        (message) => {
          setPlayInfo(JSON.parse(message.body));
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
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [id]);

  return (
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
          <ButtonGroup isHost={roomData.isHost} />
        </FlexLayout>
        <div>
          <ChatBox text={`방 채팅`} />
        </div>
      </FlexLayout>
    </Background>
  );
}
