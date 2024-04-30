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

// type RoomHeaderProps = {};

export default function RoomHeader() {
  return (
    <HeaderBlock>
      <TextBlock>
        <StateBlock>
          {/* 이미지 */}
          <SmallText>{`방장이 게임을 시작하기를 기다리는 중입니다 ...`}</SmallText>
        </StateBlock>
        <RegularText>{`현재 방 이름 : 덤벼`}</RegularText>
        <RegularText>{`참가자 수 : 22 / 40 명`}</RegularText>
      </TextBlock>
      <SmallText>아이콘</SmallText>
    </HeaderBlock>
  );
}
