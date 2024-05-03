import Background from '../../components/Background';
import { FlexLayout } from '../../styles/layout';

import RoomHeader from './components/RoomHeader';
import PlayerList from './components/PlayerList';
import RoomSetting from './components/RoomSetting';
import ButtonGroup from './components/ButtonGroup';
import ChatBox from '../../components/ChatBox';

export default function RoomPage() {
  const isHost = true;
  return (
    <Background>
      <FlexLayout gap='4rem'>
        <FlexLayout $isCol gap='2.8rem'>
          <RoomHeader isPublic={true} isHost={isHost} />
          <FlexLayout $isCol gap='1.6rem'>
            <PlayerList isHost={isHost} />
            {isHost && <RoomSetting />}
          </FlexLayout>
          <ButtonGroup isHost={isHost} />
        </FlexLayout>
        <div>
          <ChatBox text={`방 채팅`} />
        </div>
      </FlexLayout>
    </Background>
  );
}
