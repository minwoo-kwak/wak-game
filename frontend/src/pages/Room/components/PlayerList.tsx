import { PlayerTypes } from '../../../types/RoomTypes';

import styled from 'styled-components';
import { GridLayout } from '../../../styles/layout';
import WhiteBox from '../../../components/WhiteBox';
import PlayerNickname from '../../../components/PlayerNickname';

const ListBlock = styled(GridLayout)`
  width: 100%;
  overflow-y: auto;
`;

type PlayerListProps = {
  isHost: boolean;
  players: PlayerTypes[];
};

export default function PlayerList({ isHost, players }: PlayerListProps) {
  const playersData = (
    <ListBlock $col={3} gap='3.2rem'>
      {players.map((value, index) => {
        return (
          <PlayerNickname
            key={index}
            nickname={value.nickname}
            color={value.color}
          />
        );
      })}
    </ListBlock>
  );

  return isHost ? (
    <WhiteBox mode='SHORT' width='77.2rem'>
      {playersData}
    </WhiteBox>
  ) : (
    <WhiteBox mode='MEDIUM' width='77.2rem'>
      {playersData}
    </WhiteBox>
  );
}
