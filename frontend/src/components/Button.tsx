import styled from 'styled-components';
import { SmallText, textStyles } from '../styles/fonts';

const StyledButton = styled.button`
  width: fit-content;
  height: fit-content;
  padding: 0.6rem 1.2rem;
  border-style: outset;
  border-width: 0.4rem;
  border-color: #f2f2f2;
  background-color: #bababa;
  &:hover {
    border-style: inset;
    background-color: #b0b0b0;
  }
  ${textStyles}
`;

type ButtonProps = {
  isBigger?: boolean;
  label: string;
  onClick?: () => void;
};

export default function Button({ isBigger, label, ...props }: ButtonProps) {
  return (
    <StyledButton color='black' {...props}>
      {isBigger ? <SmallText color='black'>{label}</SmallText> : label}
    </StyledButton>
  );
}
