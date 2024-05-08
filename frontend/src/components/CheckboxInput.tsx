import { useState } from 'react';
import styled, { css } from 'styled-components';
import { LargeText } from '../styles/fonts';

const InputBlock = styled.div`
  display: flex;
  align-items: center;
`;

const StyledCheckbox = styled.div<{ disabled?: boolean }>`
  width: 3.4rem;
  height: 3.4rem;
  border-style: solid;
  border-left-width: 0rem;
  border-right-width: 0rem;
  border-top-width: 0.4rem;
  border-bottom-width: 0.4rem;
  background-color: ${(props) => (props.disabled ? '#bababa' : 'white')};
  &:hover {
    background-color: #bababa;
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

type CheckboxInputProps = {
  disabled?: boolean;
  checked?: boolean;
  onClick?: () => void;
};

export default function CheckboxInput({
  disabled,
  checked,
  onClick,
}: CheckboxInputProps) {
  const [isChecked, setIsChecked] = useState(checked ? true : false);

  return (
    <InputBlock>
      <BorderX />
      <StyledCheckbox
        disabled={disabled}
        onClick={() => {
          if (!disabled && !checked) {
            onClick && onClick();
            setIsChecked(!isChecked);
          }
        }}
      >
        {isChecked && <LargeText color='#725bff'>X</LargeText>}
      </StyledCheckbox>
      <BorderX $right />
    </InputBlock>
  );
}
