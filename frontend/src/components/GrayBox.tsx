import styled, { css } from 'styled-components';
import { FlexLayout } from '../styles/layout';

const StyledBox = styled(FlexLayout)<{ $width: string; $height: string }>`
  width: ${(props) => props.$width};
  height: ${(props) => props.$height};
  border-style: solid;
  border-left-width: 0rem;
  border-right-width: 0rem;
  border-top-width: 0.4rem;
  border-bottom-width: 0.4rem;
  background-color: #dadada;
`;

const BorderX = styled.img.attrs({
  alt: '',
})<{ $right?: boolean }>`
  ${(props) =>
    props.$right &&
    css`
      transform: rotate(0.5turn);
    `};
`;

type GrayBoxProps = {
  mode: 'SHORT' | 'MEDIUM' | 'TALL';
  width: string;
  children?: React.ReactNode;
};

export default function GrayBox({ mode, width, children }: GrayBoxProps) {
  let img;
  let height;

  switch (mode) {
    case 'SHORT':
      img = require('../assets/borderImg/img-border-black-h196.png');
      height = '18.8rem';
      break;
    case 'MEDIUM':
      img = require('../assets/borderImg/img-border-black-h400.png');
      height = '39.2rem';
      break;
    case 'TALL':
    default:
      img = require('../assets/borderImg/img-border-black-h480.png');
      height = '47.2rem';
  }

  return (
    <FlexLayout>
      <BorderX src={img} />
      <StyledBox $isCol $width={width} $height={height}>
        {children}
      </StyledBox>
      <BorderX src={img} $right />
    </FlexLayout>
  );
}
