import { RoomListTypes } from '../../../types/RoomTypes';

import styled, { css } from 'styled-components';
import { FlexLayout, GridLayout } from '../../../styles/layout';
import { LargeText, SmallText, textStyles } from '../../../styles/fonts';

import WhiteRoundBox from '../../../components/WhiteRoundBox';

import arrow from '../../../assets/img-arrow.png';

const TextBlock = styled(FlexLayout)`
  width: 100%;
  position: relative;
  align-items: start;
  padding-left: 1rem;
  ${textStyles}
`;

const LockImg = styled.img.attrs({
  alt: '자물쇠',
})`
  width: 2.4rem;
  position: absolute;
  top: -1rem;
  right: 1rem;
`;

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

type RoomListProps = {
  rooms: RoomListTypes[];
  openDialog: (id: number, isPublic: boolean) => void;
};

export default function RoomList({ rooms, openDialog }: RoomListProps) {
  const ROOM_NUM_SINGLE_PAGE = 6;
  const roomBlocks = Array.from({ length: ROOM_NUM_SINGLE_PAGE });

  const handleClick = (index: number) => {
    if (index < rooms.length) {
      openDialog(rooms[index].roomId, rooms[index].isPublic);
    }
  };
  const clickRight = () => {};
  const clickLeft = () => {};

  const RoomBlock = (index: number) => {
    return (
      <WhiteRoundBox
        key={index}
        width='36rem'
        clickable
        onClick={() => handleClick(index)}
      >
        {index < rooms.length && (
          <TextBlock $isCol gap='1.2rem'>
            <SmallText>{`${rooms[index].roomName}`}</SmallText>
            <>{`⌐╦╦═─ ${rooms[index].mode === 'SOLO' ? `개인전` : `팀전`} ( ${
              rooms[index].currentPlayers
            } / ${rooms[index].limitPlayers} 명 )`}</>
            <LockImg
              src={
                rooms[index].isPublic
                  ? require('../../../assets/img-unlock.png')
                  : require('../../../assets/img-lock.png')
              }
            />
          </TextBlock>
        )}
      </WhiteRoundBox>
    );
  };

  return (
    <FlexLayout $isCol gap='3.6rem'>
      <GridLayout $col={2} gap='2.4rem'>
        {roomBlocks.map((_, index) => {
          return RoomBlock(index);
        })}
      </GridLayout>
      <FlexLayout gap='8rem'>
        <PageButton onClick={clickLeft} />
        <LargeText>1</LargeText>
        <PageButton $right onClick={clickRight} />
      </FlexLayout>
    </FlexLayout>
  );
}
