import styled from 'styled-components';
import { RegularText } from '../styles/fonts';

import boxImg from '../assets/img-box-gray.png';

const Box = styled.div`
  width: 28rem;
  height: 6.8rem;
  padding-bottom: 0.8rem;
  background-origin: border-box;
  background-image: url(${boxImg});
  background-position: center;
  background-repeat: no-repeat;
  display: flex;
  justify-content: center;
  align-items: center;
`;

type GrayBoxProps = {
  text: string;
};

export default function GrayBox({ text }: GrayBoxProps) {
  return (
    <Box>
      <RegularText color='black'>{text}</RegularText>
    </Box>
  );
}
