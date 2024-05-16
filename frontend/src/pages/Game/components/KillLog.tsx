import { CompatClient } from '@stomp/stompjs';

import styled, { css } from 'styled-components';
import { FlexLayout } from '../../../styles/layout';
import { textStyles } from '../../../styles/fonts';

import WhiteRoundBox from '../../../components/WhiteRoundBox';
import { useEffect, useState } from 'react';
import useGameStore from '../../../store/gameStore';
import { getAccessToken } from '../../../constants/api';

const KillLogBlock = styled.div<{ $isWaiting?: boolean }>`
  width: 100%;
  height: 90%;
  overflow-y: auto;
  ${(props) =>
    props.$isWaiting &&
    css`
      display: flex;
      flex-direction: column;
      justify-content: center;
    `}
`;

const TextBlock = styled(FlexLayout)`
  justify-content: space-evenly;
  margin-bottom: 0.4rem;
`;

const Text = styled.div`
  ${textStyles}
`;

type KillLogPlayersTypes = {
  roundId: number;
  userNickname: string;
  color: string;
  victimNickName: string;
  victimColor: string;
};

type KillLogProps = {
  isWaiting?: boolean;
  clientRef: React.MutableRefObject<CompatClient | null>;
};

export default function KillLog({ isWaiting, clientRef }: KillLogProps) {
  const ACCESS_TOKEN = getAccessToken();
  const header = {
    Authorization: `Bearer ${ACCESS_TOKEN}`,
    'Content-Type': 'application/json',
  };
  const { gameData } = useGameStore();
  const [logs, setLogs] = useState<KillLogPlayersTypes[]>([]);
  const [isSubscribed, setIsSubscribed] = useState(false);

  useEffect(() => {
    const subscribeToTopic = () => {
      clientRef.current?.subscribe(
        `/topic/games/${gameData.roundId}/kill-log`,
        (message) => {
          setLogs((prevLogs) => {
            const newLogs = [JSON.parse(message.body), ...prevLogs];
            return newLogs;
          });
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
    <WhiteRoundBox width='32rem'>
      <KillLogBlock $isWaiting={isWaiting}>
        {isWaiting ? (
          <TextBlock>
            <Text>{`Kill Log`}</Text>
          </TextBlock>
        ) : (
          logs.map((value, index) => {
            return (
              <TextBlock key={index}>
                <Text color={value.color}>{value.userNickname}</Text>
                <Text>{`> > >`}</Text>
                <Text color={value.victimColor}>{value.victimNickName}</Text>
                <Text>{`X`}</Text>
              </TextBlock>
            );
          })
        )}
      </KillLogBlock>
    </WhiteRoundBox>
  );
}
