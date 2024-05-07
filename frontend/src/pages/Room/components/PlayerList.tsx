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
};

export default function PlayerList({ isHost }: PlayerListProps) {
  const players = Array.from({ length: 30 });
  const playersData = (
    <ListBlock $col={3} gap='3.2rem'>
      {players.map((value, index) => {
        return (
          <PlayerNickname key={index} nickname={`김싸피`} color={`white`} />
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
