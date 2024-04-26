import styled from 'styled-components';
import { SmallText } from '../styles/fonts';

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
`;

type ButtonProps = {
  label: string;
  onClick?: () => void;
};

export default function Button({ label, ...props }: ButtonProps) {
  return (
    <StyledButton {...props}>
      <SmallText color='black'>{label}</SmallText>
    </StyledButton>
  );
}
