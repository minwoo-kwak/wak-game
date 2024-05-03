import { useState } from 'react';

import styled from 'styled-components';
import { FlexLayout } from '../styles/layout';
import { textStyles } from '../styles/fonts';

import WhiteBox from './WhiteBox';
import GrayBox from './GrayBox';
import Input from './Input';
import Button from './Button';

const ChatText = styled.div`
  width: 32rem;
  height: 45.2rem;
  ${textStyles}
  display: flex;
  flex-direction: column;
  gap: 1rem;
  overflow-y: auto;
`;

type ChatBoxProps = {
  text: string;
};

export default function ChatBox({ text }: ChatBoxProps) {
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
    <WhiteBox mode='TALL' width='32rem'>
      <GrayBox text={text} />
      <ChatText>
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
