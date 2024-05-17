import { CompatClient } from '@stomp/stompjs';

import { FlexLayout } from '../../../styles/layout';
import KillLog from '../components/KillLog';
import SpeechBubble from '../components/SpeechBubble';
import BattleField from './BattleField';

type GamePlayProps = {
  client: CompatClient;
};

export default function GamePlay({ client }: GamePlayProps) {
  return (
    <FlexLayout $isCol gap='1rem'>
      <FlexLayout gap='2rem'>
        <KillLog client={client} />
        <SpeechBubble />
      </FlexLayout>
      <BattleField client={client} />
    </FlexLayout>
  );
}
