import { useEffect, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import SockJS from 'sockjs-client';
import { CompatClient, Stomp } from '@stomp/stompjs';

import { BASE_URL, getAccessToken } from '../../constants/api';
import { getRoomlist } from '../../services/room';

import { FlexLayout } from '../../styles/layout';

import Background from '../../components/Background';
import ChatBox from '../../components/ChatBox';
import LobbyHeader from './components/LobbyHeader';
import RoomList from './components/RoomList';
import NewRoomDialog from './components/NewRoomDialog';
import EnterCheckDialog from './components/EnterCheckDialog';

export default function LobbyPage() {
  const navigate = useNavigate();
  const ACCESS_TOKEN = getAccessToken();
  const client = useRef<CompatClient | null>(null);

  const [isOpen, setIsOpen] = useState({
    newRoomDialog: false,
    enterCheckDialog: false,
  });
  const [roomPage, setRoomPage] = useState<{
    totalPage: number;
    rooms: [];
  } | null>(null);
  const [clickedRoom, setClickedRoom] = useState({
    roomId: 0,
    isPublic: false,
  });

  const connectHandler = () => {
    const socket = new SockJS(`${BASE_URL}/socket`);
    const header = {
      Authorization: `Bearer ${ACCESS_TOKEN}`,
      'Content-Type': 'application/json',
    };
    client.current = Stomp.over(socket);
    client.current.connect(header, () => {
      client.current?.subscribe(
        `/topic/lobby/1`,
        (message) => {
          setRoomPage(JSON.parse(message.body));
        },
        header
      );
      showRoomList();
      // Array.from({ length: roomPage?.totalPage || 1 }).forEach((_, page) => {
      //   client.current?.subscribe(
      //     `/topic/lobby/${page + 1}`,
      //     (message) => {
      //       setRoomPage(JSON.parse(message.body));
      //     },
      //     header
      //   );
      // });
    });
  };

  const showRoomList = async () => {
    try {
      await getRoomlist();
    } catch (error: any) {
      console.error('방 목록 가져오기 에러', error);
      navigate(`/error`);
    }
  };

  useEffect(() => {
    connectHandler();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [ACCESS_TOKEN]);

  const handleCheckDialog = (id: number, isPublic: boolean) => {
    setClickedRoom({ roomId: id, isPublic: isPublic });
    setIsOpen({ ...isOpen, enterCheckDialog: true });
  };

  return (
    <>
      <Background>
        <FlexLayout gap='4rem'>
          <FlexLayout $isCol gap='3.2rem'>
            <LobbyHeader
              openDialog={() => setIsOpen({ ...isOpen, newRoomDialog: true })}
            />
            <RoomList
              rooms={roomPage?.rooms || []}
              openDialog={(id: number, isPublic: boolean) =>
                handleCheckDialog(id, isPublic)
              }
            />
          </FlexLayout>
          <div>
            <ChatBox text={`전체 채팅`} />
          </div>
        </FlexLayout>
      </Background>
      {isOpen.newRoomDialog && (
        <NewRoomDialog
          closeDialog={() => setIsOpen({ ...isOpen, newRoomDialog: false })}
        />
      )}
      {isOpen.enterCheckDialog && (
        <EnterCheckDialog
          clickedRoom={clickedRoom}
          closeDialog={() => setIsOpen({ ...isOpen, enterCheckDialog: false })}
        />
      )}
    </>
  );
}
