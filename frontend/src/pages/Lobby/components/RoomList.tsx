import styled, { css } from 'styled-components';
import { FlexLayout, GridLayout } from '../../../styles/layout';
import { LargeText } from '../../../styles/fonts';

import WhiteRoundBox from '../../../components/WhiteRoundBox';

import arrow from '../../../assets/img-arrow.png';

const PageButton = styled.button<{ $right?: boolean }>`
  width: 3.6rem;
  height: 4.5rem;
  border: 0;
  background-color: transparent;
  background-image: url(${arrow});
  background-position: center;
  background-repeat: no-repeat;
  &:hover {
    background-size: cover;
  }
  ${(props) =>
    props.$right &&
    css`
      transform: rotate(0.5turn);
    `};
`;

export default function RoomList() {
  const ROOM_NUM_SINGLE_PAGE = 6;
  const rooms = Array.from({ length: ROOM_NUM_SINGLE_PAGE });

  return (
    <FlexLayout $isCol gap='3.6rem'>
      <GridLayout $col={2} gap='2.4rem'>
        {rooms.map((value, index) => {
          return <WhiteRoundBox key={index} width='36rem'></WhiteRoundBox>;
        })}
      </GridLayout>
      <FlexLayout gap='8rem'>
        <PageButton />
        <LargeText>1</LargeText>
        <PageButton $right />
      </FlexLayout>
    </FlexLayout>
  );
}
