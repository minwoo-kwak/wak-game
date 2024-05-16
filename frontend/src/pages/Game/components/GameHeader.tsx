import { useEffect, useState } from 'react';
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
  clientRef: React.MutableRefObject<CompatClient | null>;
};

export default function GameHeader({ clientRef }: GameHeaderProps) {
  const ACCESS_TOKEN = getAccessToken();
  const header = {
    Authorization: `Bearer ${ACCESS_TOKEN}`,
    'Content-Type': 'application/json',
  };
  const { gameData } = useGameStore();
  const [info, setInfo] = useState({
    roundNumber: 1,
    totalCount: gameData.players.length,
    aliveCount: gameData.players.length,
  });
  const [isSubscribed, setIsSubscribed] = useState(false);

  useEffect(() => {
    const subscribeToTopic = () => {
      clientRef.current?.subscribe(
        `/topic/games/${gameData.roundId}/dashboard`,
        (message) => {
          setInfo(JSON.parse(message.body));
        },
        header
      );
    };

    const connectCallback = () => {
      if (clientRef.current) {
        subscribeToTopic();
        setIsSubscribed(true);
      }
    };

    if (clientRef.current && clientRef.current.connected) {
      subscribeToTopic();
      setIsSubscribed(true);
    } else {
      setIsSubscribed(false);
    }

    if (clientRef.current) {
      clientRef.current.onConnect = connectCallback;
    }

    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [clientRef, gameData.roundId, isSubscribed]);

  return (
    <HeaderBlock>
      <TextBlock>
        <SmallText>{`현재 방 이름 : ${gameData.roomName} ( ${info.roundNumber} 라운드 )`}</SmallText>
        <SmallText>{`생존자 수 : ${info.totalCount} / ${info.aliveCount} 명`}</SmallText>
        <SmallText>{`내 상태 : 생존 !`}</SmallText>
      </TextBlock>
    </HeaderBlock>
  );
}
