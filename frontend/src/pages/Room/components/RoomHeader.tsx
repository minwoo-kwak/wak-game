import styled from 'styled-components';
import { RegularText, SmallText } from '../../../styles/fonts';

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

const StateBlock = styled.div`
  display: flex;
`;

const LockImg = styled.img.attrs({
  alt: '자물쇠',
})`
  width: 3.2rem;
`;

type RoomHeaderProps = {
  roomName: string;
  currentPlayers: number;
  limitPlayers: number;
  isPublic: boolean;
  isHost: boolean;
};

export default function RoomHeader({
  roomName,
  currentPlayers,
  limitPlayers,
  isPublic,
  isHost,
}: RoomHeaderProps) {
  return (
    <HeaderBlock>
      <TextBlock>
        {!isHost && (
          <StateBlock>
            {/* 이미지 */}
            <SmallText>{`방장이 게임을 시작하기를 기다리는 중입니다 ...`}</SmallText>
          </StateBlock>
        )}
        <RegularText>{`현재 방 이름 : ${roomName}`}</RegularText>
        <RegularText>{`참가자 수 : ${currentPlayers} / ${limitPlayers} 명`}</RegularText>
      </TextBlock>
      <LockImg
        src={
          isPublic
            ? require('../../../assets/img-unlock.png')
            : require('../../../assets/img-lock.png')
        }
      />
    </HeaderBlock>
  );
}
