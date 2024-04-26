import styled from 'styled-components';
import { RegularText } from '../../../styles/fonts';

const TextBlock = styled.div`
  display: flex;
  flex-direction: column;
  gap: 2rem;
`;

export default function LobbyText() {
  return (
    <TextBlock>
      <RegularText>{`내 이름 : 김싸피`}</RegularText>
      <RegularText>{`참여할 수 있는 게임`}</RegularText>
    </TextBlock>
  );
}
