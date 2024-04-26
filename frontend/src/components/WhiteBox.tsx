import styled from 'styled-components';

const BoxBlock = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
`;

const StyledBox = styled.div<{ $width?: string; $height: string }>`
  width: ${(props) => props.$width || '36rem'};
  height: ${(props) => props.$height};
  border-style: solid;
  border-color: white;
  border-left-width: 0.4rem;
  border-right-width: 0.4rem;
  border-top-width: 0rem;
  border-bottom-width: 0rem;
  text-align: center;
`;

const BorderY = styled.img.attrs({
  src: require('../assets/img-border-white-w360.png'),
  alt: '',
})<{ $width?: string }>`
  width: ${(props) => props.$width || '36rem'};
`;

type WhiteBoxProps = {
  height: string;
  children?: React.ReactNode;
};

export default function WhiteBox({ height, children }: WhiteBoxProps) {
  return (
    <BoxBlock>
      <BorderY />
      <StyledBox $height={height}>{children}</StyledBox>
      <BorderY />
    </BoxBlock>
  );
}
