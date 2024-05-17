import { CompatClient } from '@stomp/stompjs';

import { FlexLayout } from '../../../styles/layout';
import KillLog from '../components/KillLog';
import SpeechBubble from '../components/SpeechBubble';
import BattleFieldWait from './BattleFieldWait';

type GameWaitProps = {
  countdown: number;
  client: CompatClient;
};

export default function GameWait({ countdown, client }: GameWaitProps) {
  return (
    <FlexLayout $isCol gap='1rem'>
      <FlexLayout gap='2rem'>
        <KillLog isWaiting client={client} />
        <SpeechBubble isWaiting />
      </FlexLayout>
      <BattleFieldWait countdown={countdown} />
    </FlexLayout>
  );
}
