import { useEffect, useRef, useState } from 'react';
import { CompatClient } from '@stomp/stompjs';

import { getAccessToken } from '../../../constants/api';
import useGameStore from '../../../store/gameStore';

import styled from 'styled-components';
import { SmallText } from '../../../styles/fonts';

const HeaderBlock = styled.div`
  place-self: stretch;
  display: flex;
  justify-content: space-between;
  align-items: end;
`;

const TextBlock = styled.div`
  display: flex;
  flex-direction: column;
  gap: 2rem;
`;

type GameHeaderProps = {
  client: CompatClient;
};

export default function GameHeader({ client }: GameHeaderProps) {
  const ACCESS_TOKEN = getAccessToken();
  const header = {
    Authorization: `Bearer ${ACCESS_TOKEN}`,
    'Content-Type': 'application/json',
  };
  const { gameData } = useGameStore();
  const [info, setInfo] = useState({
    roundNumber: 1,
    totalCount: gameData.playersNumber,
    aliveCount: gameData.playersNumber,
  });
  const subscribedRef = useRef(false);

  const subscribeToTopic = () => {
    if (!subscribedRef.current) {
      client.subscribe(
        `/topic/games/${gameData.roundId}/dashboard`,
        (message) => {
          setInfo(JSON.parse(message.body));
        },
        header
      );
      subscribedRef.current = true;
    }
  };

  useEffect(() => {
    if (client && client.connected) {
      subscribeToTopic();
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [client]);

  return (
    <HeaderBlock>
      <TextBlock>
        <SmallText>{`현재 방 이름 : ${gameData.roomName} ( ${info.roundNumber} 라운드 )`}</SmallText>
        <SmallText>{`생존자 수 : ${info.totalCount} / ${info.aliveCount} 명`}</SmallText>
        <SmallText>{`내 상태 : ${
          gameData.isAlive ? `생존!` : `죽음`
        }`}</SmallText>
      </TextBlock>
    </HeaderBlock>
  );
}
