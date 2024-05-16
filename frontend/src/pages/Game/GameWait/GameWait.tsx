import { CompatClient } from '@stomp/stompjs';

import { FlexLayout } from '../../../styles/layout';

import KillLog from '../components/KillLog';
import SpeechBubble from '../components/SpeechBubble';
import BattleFieldWait from './BattleFieldWait';

type GameWaitProps = {
  countdown: number;
  clientRef: React.MutableRefObject<CompatClient | null>;
};

export default function GameWait({ countdown, clientRef }: GameWaitProps) {
  return (
    <FlexLayout $isCol gap='1rem'>
      <FlexLayout gap='2rem'>
        <KillLog isWaiting clientRef={clientRef} />
        <SpeechBubble isWaiting />
      </FlexLayout>
      <BattleFieldWait countdown={countdown} />
    </FlexLayout>
  );
}
