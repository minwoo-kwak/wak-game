import { useState } from 'react';

import styled from 'styled-components';
import { FlexLayout } from '../../../styles/layout';
import { SmallText, textStyles } from '../../../styles/fonts';

import SpeechImg from '../../../assets/img-speech-bubble.png';

const SpeechBubbleBlock = styled(FlexLayout)`
  width: 38.2rem;
  height: 11.6rem;
  padding-left: 2rem;
  padding-right: 2rem;
  padding-bottom: 1.8rem;
  background-image: url(${SpeechImg});
  background-repeat: no-repeat;
  align-items: start;
`;

const Text = styled.div`
  ${textStyles}
  line-height: 1.2;
`;

// type SpeechBubbleProps = {};

export default function SpeechBubble() {
  const [mention, setMention] = useState({
    sender: '',
    content: '',
  });

  return (
    <SpeechBubbleBlock $isCol gap='1rem'>
      <SmallText color='black'>{`도발 멘트!`}</SmallText>
      <FlexLayout>
        <Text color='#725bff'>{mention.sender}</Text>
        <Text color='black'>
          {mention.content === ''
            ? `(멘트를 입력하지 않았습니다)`
            : mention.content}
        </Text>
      </FlexLayout>
    </SpeechBubbleBlock>
  );
}
