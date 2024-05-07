import styled from 'styled-components';
import { SmallText, textStyles } from '../styles/fonts';
import { FlexLayout } from '../styles/layout';

const NicknameBox = styled.div`
  width: 1rem;
  height: 1rem;
  background-color: ${(props) => props.color};
`;

const TinyText = styled.div`
  ${textStyles}
  font-size: 1.2rem;
`;

type PlayerNicknameProps = {
  isCol?: boolean;
  nickname: string;
  color: string;
};

export default function PlayerNickname({
  isCol,
  nickname,
  color,
}: PlayerNicknameProps) {
  return (
    <FlexLayout $isCol={isCol} gap={isCol ? '0.2rem' : '1rem'}>
      <NicknameBox color={color} />
      {isCol ? (
        <TinyText color={color}>{nickname}</TinyText>
      ) : (
        <SmallText color={color}>{nickname}</SmallText>
      )}
    </FlexLayout>
  );
}
