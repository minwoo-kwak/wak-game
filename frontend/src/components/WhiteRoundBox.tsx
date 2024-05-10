import styled, { css } from 'styled-components';
import { FlexLayout } from '../styles/layout';

const StyledBox = styled(FlexLayout)<{ width: string }>`
  width: ${(props) => props.width};
  height: 11.2rem;
  border-style: solid;
  border-color: white;
  border-left-width: 0rem;
  border-right-width: 0rem;
  border-top-width: 0.4rem;
  border-bottom-width: 0.4rem;
  background-color: rgba(255, 255, 255, 0.1);
  text-align: center;
`;

const BorderX = styled.img.attrs({
  src: require('../assets/borderImg/img-border-white-h120.png'),
  alt: '',
})<{ $right?: boolean }>`
  display: inline;
  ${(props) =>
    props.$right &&
    css`
      transform: rotate(0.5turn);
    `};
`;

const BorderXHover = styled.img.attrs({
  src: require('../assets/borderImg/img-border-white-h120-hover.png'),
  alt: '',
})<{ $right?: boolean }>`
  display: none;
  ${(props) =>
    props.$right &&
    css`
      transform: rotate(0.5turn);
    `};
`;

const BoxBlock = styled.div<{ $clickable?: boolean }>`
  display: flex;
  align-items: center;
  ${(props) =>
    props.$clickable &&
    css`
      &:hover ${BorderX} {
        display: none;
      }
      &:hover ${BorderXHover} {
        display: inline;
      }
      &:hover ${StyledBox} {
        background-color: rgba(255, 255, 255, 0.3);
      }
    `}
`;

type WhiteRoundBoxProps = {
  width: string;
  clickable?: boolean;
  children?: React.ReactNode;
  onClick?: () => void;
};

export default function WhiteRoundBox({
  width,
  clickable,
  children,
  onClick,
}: WhiteRoundBoxProps) {
  return (
    <BoxBlock $clickable={clickable} onClick={onClick}>
      <BorderX />
      <BorderXHover />
      <StyledBox $isCol width={width}>
        {children}
      </StyledBox>
      <BorderX $right />
      <BorderXHover $right />
    </BoxBlock>
  );
}
