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

export default function SpeechBubble() {
  return (
    <SpeechBubbleBlock $isCol gap='1rem'>
      <SmallText color='black'>{`방장의 한마디!`}</SmallText>
      <FlexLayout>
        <Text color='#725bff'>{`김라쿤`}</Text>
        <Text color='black'>{`: 바보들아 ~`}</Text>
      </FlexLayout>
    </SpeechBubbleBlock>
  );
}
