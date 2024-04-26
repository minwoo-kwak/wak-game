import styled from 'styled-components';
import { SmallText } from '../styles/fonts';

import PurpleButton from '../assets/buttonImg/img-button-purple.png';
import BlueButton from '../assets/buttonImg/img-button-blue.png';
import RedButton from '../assets/buttonImg/img-button-red.png';
import DarkPurpleButton from '../assets/buttonImg/img-button-darkpurple.png';
import DarkBlueButton from '../assets/buttonImg/img-button-darkblue.png';
import DarkRedButton from '../assets/buttonImg/img-button-darkred.png';

const buttonColor = (color: string) => {
  switch (color) {
    case 'blue':
      return { bg: BlueButton, bgDark: DarkBlueButton };
    case 'red':
      return { bg: RedButton, bgDark: DarkRedButton };
    case 'purple':
    default:
      return { bg: PurpleButton, bgDark: DarkPurpleButton };
  }
};

const StyledButton = styled.button<{ color: string }>`
  width: 18rem;
  height: 5.6rem;
  padding: 0 0 0.6rem 0;
  border: 0;
  background-color: transparent;
  background-image: url(${(props) => buttonColor(props.color).bg}),
    url(${(props) => buttonColor(props.color).bgDark});
  background-position: top, bottom;
  background-repeat: no-repeat;
  &:hover {
    padding: 1rem 0 0 0;
    background-position: bottom, bottom;
  }
  text-align: center;
`;

type RoundButtonProps = {
  label: string;
  color?: 'purple' | 'blue' | 'red';
  onClick?: () => void;
};

export default function RoundButton({
  label,
  color,
  ...props
}: RoundButtonProps) {
  return (
    <StyledButton color={color || 'purple'} {...props}>
      <SmallText>{label}</SmallText>
    </StyledButton>
  );
}
