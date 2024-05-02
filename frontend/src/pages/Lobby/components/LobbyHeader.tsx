import styled from 'styled-components';
import { RegularText } from '../../../styles/fonts';
import NewRoomButton from './NewRoomButton';

const HeaderBlock = styled.div`
  place-self: stretch;
  display: flex;
  justify-content: space-between;
  align-items: end;
`;

const TextBlock = styled.div`
  display: flex;
  flex-direction: column;
  gap: 2rem;
`;

type LobbyHeaderProps = {
  openDialog: () => void;
};

export default function LobbyHeader({ openDialog }: LobbyHeaderProps) {
  return (
    <HeaderBlock>
      <TextBlock>
        <RegularText>{`내 이름 : 김싸피`}</RegularText>
        <RegularText>{`참여할 수 있는 게임`}</RegularText>
      </TextBlock>
      <NewRoomButton handleClick={openDialog} />
    </HeaderBlock>
  );
}
