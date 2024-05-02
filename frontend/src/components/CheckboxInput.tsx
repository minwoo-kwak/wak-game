import { useState } from 'react';
import styled, { css } from 'styled-components';
import { LargeText } from '../styles/fonts';

const InputBlock = styled.div`
  display: flex;
  align-items: center;
`;

const StyledCheckbox = styled.div`
  width: 3.4rem;
  height: 3.4rem;
  border-style: solid;
  border-left-width: 0rem;
  border-right-width: 0rem;
  border-top-width: 0.4rem;
  border-bottom-width: 0.4rem;
  background-color: white;
  &:hover {
    background-color: #00000020;
  }
  text-align: center;
  line-height: 3.2rem;
`;

const BorderX = styled.img.attrs({
  src: require('../assets/borderImg/img-border-black-h42.png'),
  alt: '입력',
})<{ $right?: boolean }>`
  ${(props) =>
    props.$right &&
    css`
      transform: rotate(0.5turn);
    `};
`;

export default function CheckboxInput() {
  const [isChecked, setIsChecked] = useState(false);

  return (
    <InputBlock>
      <BorderX />
      <StyledCheckbox onClick={() => setIsChecked(!isChecked)}>
        {isChecked && <LargeText color='#725bff'>X</LargeText>}
      </StyledCheckbox>
      <BorderX $right />
    </InputBlock>
  );
}
