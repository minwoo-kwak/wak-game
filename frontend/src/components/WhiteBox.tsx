import styled, { css } from 'styled-components';
import { FlexLayout } from '../styles/layout';

const BoxBlock = styled.div`
  display: flex;
  align-items: center;
`;

const Layout = styled(FlexLayout)`
  justify-content: space-between;
`;

const StyledBox = styled(Layout)<{ $width: string; $height: string }>`
  width: ${(props) => props.$width};
  height: ${(props) => props.$height};
  padding: 1.2rem 1rem;
  border-style: solid;
  border-color: white;
  border-left-width: 0rem;
  border-right-width: 0rem;
  border-top-width: 0.4rem;
  border-bottom-width: 0.4rem;
  background-color: rgba(255, 255, 255, 0.1);
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

type WhiteBoxProps = {
  mode: 'TALL' | 'MEDIUM' | 'SHORT';
  width: string;
  children?: React.ReactNode;
};

export default function WhiteBox({ mode, width, children }: WhiteBoxProps) {
  let img;
  let height;

  switch (mode) {
    case 'TALL':
      img = require('../assets/borderImg/img-border-white-h640.png');
      height = '60.8rem';
      break;
    case 'MEDIUM':
      img = require('../assets/borderImg/img-border-white-h400.png');
      height = '36.8rem';
      break;
    case 'SHORT':
    default:
      img = require('../assets/borderImg/img-border-white-h320.png');
      height = '28.8rem';
  }

  return (
    <BoxBlock>
      <BorderX src={img} />
      <StyledBox $isCol $width={width} $height={height} gap='1rem'>
        {children}
      </StyledBox>
      <BorderX src={img} $right />
    </BoxBlock>
  );
}
