import styled from 'styled-components';
import { GridLayout } from '../../styles/layout';

import Background from '../../components/Background';
import LobbyText from './components/LobbyText';
import RoomList from './components/RoomList';
import WhiteBox from '../../components/WhiteBox';

const LeftGrid = styled.div`
  grid-column: span 2 / span 2;
  display: flex;
  flex-direction: column;
  gap: 2rem;
`;
const RightGrid = styled.div`
  grid-column: span 1 / span 1;
`;

export default function LobbyPage() {
  return (
    <Background>
      <GridLayout>
        <LeftGrid>
          <LobbyText />
          <RoomList />
        </LeftGrid>
        <RightGrid>
          <WhiteBox height='60rem' />
        </RightGrid>
      </GridLayout>
    </Background>
  );
}
