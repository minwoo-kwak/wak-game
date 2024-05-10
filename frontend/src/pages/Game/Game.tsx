import { useState } from 'react';
import { FlexLayout } from '../../styles/layout';

import Background from '../../components/Background';
import GameHeader from './components/GameHeader';
import ChatBox from '../../components/ChatBox';
import RankBox from './components/RankBox';

import GameWait from './GameWait/GameWait';
import GamePlay from './GamePlay/GamePlay';
import GameResult from './GameResult/GameResult';

export default function GamePage() {
  const [state, setState] = useState<'WAIT' | 'PLAY' | 'RESULT'>('WAIT');

  return (
    <Background>
      <FlexLayout gap='4rem'>
        <FlexLayout $isCol gap='2rem'>
          <GameHeader />
          {state === 'WAIT' ? (
            <GameWait />
          ) : state === 'PLAY' ? (
            <GamePlay />
          ) : (
            <GameResult isWinner round={3}/>
          )}
        </FlexLayout>
        <FlexLayout $isCol gap='1.2rem'>
          <ChatBox isShort text={`방 채팅`} />
          <RankBox />
        </FlexLayout>
      </FlexLayout>
    </Background>
  );
}
