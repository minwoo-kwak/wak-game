import styled, { css } from 'styled-components';

const BoxBlock = styled.div`
  display: flex;
  align-items: center;
`;

const StyledBox = styled.div<{ $width: string }>`
  width: ${(props) => props.$width};
  height: 11.2rem;
  border-style: solid;
  border-color: white;
  border-left-width: 0rem;
  border-right-width: 0rem;
  border-top-width: 0.4rem;
  border-bottom-width: 0.4rem;
  background-color: transparent;
  &:hover {
    background-color: rgba(255, 255, 355, 0.2);
  }
  text-align: center;
`;

const BorderX = styled.img.attrs({
  src: require('../assets/borderImg/img-border-white-h120.png'),
  alt: '',
})<{ $right?: boolean }>`
  ${(props) =>
    props.$right &&
    css`
      transform: rotate(0.5turn);
    `};
`;

type WhiteRoundBoxProps = {
  width: string;
  children?: React.ReactNode;
};

export default function WhiteRoundBox({ width, children }: WhiteRoundBoxProps) {
  return (
    <BoxBlock>
      <BorderX />
      <StyledBox $width={width}>{children}</StyledBox>
      <BorderX $right />
    </BoxBlock>
  );
}
