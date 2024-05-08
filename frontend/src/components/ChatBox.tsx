import { useState } from 'react';
import useUserStore from '../store/store';

import styled from 'styled-components';
import { FlexLayout } from '../styles/layout';
import { textStyles } from '../styles/fonts';

import WhiteBox from './WhiteBox';
import GrayTitleBox from './GrayTitleBox';
import Input from './Input';
import Button from './Button';

const ChatBlock = styled.div<{ height: string }>`
  width: 32rem;
  height: ${(props) => props.height};
  ${textStyles}
  display: flex;
  flex-direction: column;
  gap: 1rem;
  overflow-y: auto;
`;

const ChatLine = styled.div`
  display: flex;
  gap: 0.4rem;
`;

const ChatText = styled.div`
  ${textStyles}
`;

type ChatBoxProps = {
  isShort?: boolean;
  text: string;
};

export default function ChatBox({ isShort, text }: ChatBoxProps) {
  const [userChatting, setUserChatting] = useState<string[][]>([]);
  const [chatting, setChatting] = useState('');
  const { userData } = useUserStore();

  const handleChange = (e: { target: { value: string } }) => {
    setChatting(e.target.value);
  };

  const handleClick = () => {
    setUserChatting([...userChatting, [userData.nickname, chatting]]);
  };

  return (
    <WhiteBox mode={isShort ? 'MEDIUM' : 'TALL'} width='32rem'>
      <GrayTitleBox text={text} />
      <ChatBlock height={isShort ? '15.2rem' : '45.2rem'}>
        {userChatting.map((value, index) => {
          return (
            <ChatLine key={index}>
              <ChatText color={userData.color}>{`${value[0]}`}</ChatText>
              <ChatText>{`: ${value[1]}`}</ChatText>
            </ChatLine>
          );
        })}
      </ChatBlock>
      <FlexLayout gap='1rem'>
        <Input name='chatting' onChange={handleChange} />
        <Button label={`전송`} onClick={handleClick} />
      </FlexLayout>
    </WhiteBox>
  );
}
