import { UserDataType } from '../../../types/UserTypes.ts';

import styled from 'styled-components';
import { RegularText } from '../../../styles/fonts';
import { FlexLayout } from '../../../styles/layout';

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
  userData: UserDataType;
  openDialog: () => void;
};

export default function LobbyHeader({
  userData,
  openDialog,
}: LobbyHeaderProps) {
  return (
    <HeaderBlock>
      <TextBlock>
        <FlexLayout gap='1.6rem'>
          <RegularText>{`내 이름 :`}</RegularText>
          <RegularText color={userData.color}>{userData.nickname}</RegularText>
        </FlexLayout>
        <RegularText>{`참여할 수 있는 게임`}</RegularText>
      </TextBlock>
      <NewRoomButton handleClick={openDialog} />
    </HeaderBlock>
  );
}
