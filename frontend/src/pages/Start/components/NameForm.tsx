import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

import styled, { keyframes } from 'styled-components';
import { SmallText } from '../../../styles/fonts';
import { FlexLayout } from '../../../styles/layout';

import Input from '../../../components/Input';
import Button from '../../../components/Button';

const BlinkSmallText = styled(SmallText)`
  animation: ${keyframes`
    0% { opacity: 0; }
    100% { opacity: 1; }
  `} 1s steps(2, jump-none) infinite;
`;

export default function NicknameForm() {
  const navigate = useNavigate();
  const [nickname, setNickname] = useState('');

  const onChangeInput = (e: { target: { value: string } }) => {
    setNickname(e.target.value);
  };

  const handleClick = () => {
    console.log(nickname);
    navigate('/lobby');
  };

  return (
    <FlexLayout $isCol gap='1rem'>
      <BlinkSmallText>{`시작하려면 닉네임을 입력하세요`}</BlinkSmallText>
      <FlexLayout gap='1rem'>
        <Input name='nickname' width='36rem' onChange={onChangeInput} />
        <Button label={`GO!`} onClick={handleClick} />
      </FlexLayout>
    </FlexLayout>
  );
}
