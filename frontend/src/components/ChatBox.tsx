import { useState } from 'react';

import styled from 'styled-components';
import { FlexLayout } from '../styles/layout';
import { textStyles } from '../styles/fonts';

import WhiteBox from './WhiteBox';
import GrayTitleBox from './GrayTitleBox';
import Input from './Input';
import Button from './Button';

const ChatText = styled.div<{ height: string }>`
  width: 32rem;
  height: ${(props) => props.height};
  ${textStyles}
  display: flex;
  flex-direction: column;
  gap: 1rem;
  overflow-y: auto;
`;

type ChatBoxProps = {
  isShort?: boolean;
  text: string;
};

export default function ChatBox({ isShort, text }: ChatBoxProps) {
  const [userChatting, setUserChatting] = useState<string[][]>([]);
  const [chatting, setChatting] = useState('');

  const handleChange = (e: { target: { value: string } }) => {
    setChatting(e.target.value);
  };

  const handleClick = () => {
    console.log(chatting);
    setUserChatting([...userChatting, ['김싸피', chatting]]);
  };

  return (
    <WhiteBox mode={isShort ? 'MEDIUM' : 'TALL'} width='32rem'>
      <GrayTitleBox text={text} />
      <ChatText height={isShort ? '15.2rem' : '45.2rem'}>
        {userChatting.map((value, index) => {
          return <div key={index}>{`${value[0]} : ${value[1]}`}</div>;
        })}
      </ChatText>
      <FlexLayout gap='1rem'>
        <Input name='chatting' onChange={handleChange} />
        <Button label={`전송`} onClick={handleClick} />
      </FlexLayout>
    </WhiteBox>
  );
}
