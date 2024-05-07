import styled from 'styled-components';
import { SmallText } from '../../../styles/fonts';

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

// type GameHeaderProps = {};

export default function GameHeader() {
  return (
    <HeaderBlock>
      <TextBlock>
        <SmallText>{`현재 방 이름 : 덤벼 ( 1 라운드 )`}</SmallText>
        <SmallText>{`생존자 수 : 40 / 40 명`}</SmallText>
        <SmallText>{`내 상태 : 생존 !`}</SmallText>
      </TextBlock>
    </HeaderBlock>
  );
}
