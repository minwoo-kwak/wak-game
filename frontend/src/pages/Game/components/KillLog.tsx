import { useEffect, useRef, useState } from 'react';
import { CompatClient } from '@stomp/stompjs';
import { getAccessToken } from '../../../constants/api';
import useGameStore from '../../../store/gameStore';

import styled, { css } from 'styled-components';
import { FlexLayout } from '../../../styles/layout';
import { textStyles } from '../../../styles/fonts';
import WhiteRoundBox from '../../../components/WhiteRoundBox';

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
  client: CompatClient;
};

export default function KillLog({ isWaiting, client }: KillLogProps) {
  const ACCESS_TOKEN = getAccessToken();
  const header = {
    Authorization: `Bearer ${ACCESS_TOKEN}`,
    'Content-Type': 'application/json',
  };
  const { gameData } = useGameStore();
  const [logs, setLogs] = useState<KillLogPlayersTypes[]>([]);
  const subscribedRef = useRef(false);

  const subscribeToTopic = () => {
    if (!subscribedRef.current) {
      client.subscribe(
        `/topic/games/${gameData.roundId}/kill-log`,
        (message) => {
          setLogs((prevLogs) => {
            const newLogs = [JSON.parse(message.body), ...prevLogs];
            return newLogs;
          });
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
