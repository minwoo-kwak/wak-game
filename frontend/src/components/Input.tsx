import { useState } from 'react';

import styled, { css } from 'styled-components';
import { textStyles } from '../styles/fonts';

const InputBlock = styled.div`
  display: flex;
  align-items: center;
`;

const StyledInput = styled.input`
  width: ${(props) => props.width};
  height: 4.8rem;
  padding: 0rem;
  border-style: solid;
  border-color: black;
  border-left-width: 0rem;
  border-right-width: 0rem;
  border-top-width: 0.4rem;
  border-bottom-width: 0.4rem;
  background-color: rgba(255, 255, 355, 0.6);
  &:focus {
    outline-width: 0rem;
  }
  ${(props) => props.color && textStyles}
  text-align: center;
`;

const BorderX = styled.img.attrs({
  src: require('../assets/img-border-black-h56.png'),
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
  onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void;
};

export default function Input({ name, width, onChange }: InputProps) {
  const [inputValue, SetInputValue] = useState('');

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newValue = e.target.value;
    onChange && onChange(e);
    SetInputValue(newValue);
  };

  return (
    <InputBlock>
      <BorderX />
      <StyledInput
        name={name}
        value={inputValue}
        width={width || '20rem'}
        color='black'
        onChange={handleChange}
      />
      <BorderX $right />
    </InputBlock>
  );
}
