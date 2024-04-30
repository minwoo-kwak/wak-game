import { useState } from 'react';
import { FlexLayout } from '../../styles/layout';

import Background from '../../components/Background';
import ChatBox from '../../components/ChatBox';
import LobbyHeader from './components/LobbyHeader';
import RoomList from './components/RoomList';
import NewRoomDialog from './components/NewRoomDialog';

export default function LobbyPage() {
  const [isOpen, setIsOpen] = useState(false);

  return (
    <>
      <Background>
        <FlexLayout gap='4rem'>
          <FlexLayout $isCol gap='3.2rem'>
            <LobbyHeader openDialog={() => setIsOpen(true)} />
            <RoomList />
          </FlexLayout>
          <div>
            <ChatBox text={`전체 채팅`} />
          </div>
        </FlexLayout>
      </Background>
      {isOpen && <NewRoomDialog closeDialog={() => setIsOpen(false)} />}
    </>
  );
}
