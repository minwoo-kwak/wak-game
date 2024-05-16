import { CompatClient } from '@stomp/stompjs';
import { FlexLayout } from '../../../styles/layout';

import KillLog from '../components/KillLog';
import SpeechBubble from '../components/SpeechBubble';
import BattleField from './BattleField';

type GamePlayProps = {
  clientRef: React.MutableRefObject<CompatClient | null>;
};

export default function GamePlay({ clientRef }: GamePlayProps) {
  return (
    <FlexLayout $isCol gap='1rem'>
      <FlexLayout gap='2rem'>
        <KillLog clientRef={clientRef} />
        <SpeechBubble />
      </FlexLayout>
      <BattleField />
    </FlexLayout>
  );
}
