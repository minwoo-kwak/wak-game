import styled, { css } from 'styled-components';
import { RegularText } from '../../../styles/fonts';

import NewRoomButton from './NewRoomButton';

const HeaderBlock = styled.div`
  place-self: stretch;
  display: flex;
  justify-content: space-between;
  align-items: end;
`;

const TextBlock = styled.div<{ $isCol?: boolean }>`
  display: flex;
  ${(props) =>
    props.$isCol &&
    css`
      flex-direction: column;
    `}
  gap: 2rem;
`;

type LobbyHeaderProps = {
  openDialog: () => void;
};

export default function LobbyHeader({ openDialog }: LobbyHeaderProps) {
  const userStore = sessionStorage.getItem('userStore');
  const { color, nickname } = userStore && JSON.parse(userStore).state.userData;

  return (
    <HeaderBlock>
      <TextBlock $isCol>
        <TextBlock>
          <RegularText>{`내 이름 :`}</RegularText>
          <RegularText color={color}>{nickname}</RegularText>
        </TextBlock>
        <RegularText>{`참여할 수 있는 게임`}</RegularText>
      </TextBlock>
      <NewRoomButton handleClick={openDialog} />
    </HeaderBlock>
  );
}
