import { FlexLayout } from '../../../styles/layout';

import KillLog from '../components/KillLog';
import SpeechBubble from '../components/SpeechBubble';
import BattleFieldWait from './BattleFieldWait';

export default function GameWait() {
  return (
    <FlexLayout $isCol gap='1rem'>
      <FlexLayout gap='2rem'>
        <KillLog isWaiting />
        <SpeechBubble />
      </FlexLayout>
      <BattleFieldWait />
    </FlexLayout>
  );
}
