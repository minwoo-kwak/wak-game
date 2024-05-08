import { useState } from 'react';

import styled, { css } from 'styled-components';
import { textStyles } from '../styles/fonts';

const InputBlock = styled.div`
  display: flex;
  align-items: center;
`;

const StyledInput = styled.input<{ $isRound?: boolean }>`
  width: ${(props) => props.width};
  height: ${(props) => (props.$isRound ? '4.8rem' : '3.4rem')};
  padding: 0rem 1rem;
  border-style: solid;
  border-color: black;
  border-left-width: 0rem;
  border-right-width: 0rem;
  border-top-width: 0.4rem;
  border-bottom-width: 0.4rem;
  background-color: ${(props) =>
    props.$isRound
      ? css`rgba(255, 255, 255, 0.6);`
      : css`rgba(255, 255, 255, 0.8)`};
  &:focus {
    outline-width: 0rem;
  }
  &:disabled {
    background-color: #bababa;
  }
  ${textStyles}
  text-align: ${(props) => (props.$isRound ? 'center' : 'start')};
`;

const BorderX = styled.img.attrs({
  alt: '입력',
})<{ $right?: boolean }>`
  ${(props) =>
    props.$right &&
    css`
      transform: rotate(0.5turn);
    `};
`;

type InputProps = {
  name: string;
  width?: string;
  isRound?: boolean;
  disabled?: boolean;
  onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void;
};

export default function Input({
  name,
  width,
  isRound,
  disabled,
  onChange,
}: InputProps) {
  const [inputValue, SetInputValue] = useState('');

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newValue = e.target.value;
    onChange && onChange(e);
    SetInputValue(newValue);
  };

  const img = isRound
    ? require('../assets/borderImg/img-border-black-h56.png')
    : require('../assets/borderImg/img-border-black-h42.png');

  return (
    <InputBlock>
      <BorderX src={img} />
      <StyledInput
        $isRound={isRound}
        name={name}
        value={inputValue}
        width={width || '20rem'}
        color='black'
        disabled={disabled}
        onChange={handleChange}
      />
      <BorderX src={img} $right />
    </InputBlock>
  );
}
