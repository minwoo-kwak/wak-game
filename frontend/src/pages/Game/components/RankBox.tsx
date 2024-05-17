import { useEffect, useRef, useState } from 'react';
import { CompatClient } from '@stomp/stompjs';
import { getAccessToken } from '../../../constants/api';
import useGameStore from '../../../store/gameStore';

import styled from 'styled-components';
import { GridLayout } from '../../../styles/layout';
import { textStyles } from '../../../styles/fonts';
import WhiteBox from '../../../components/WhiteBox';
import GrayTitleBox from '../../../components/GrayTitleBox';

const RankLayout = styled(GridLayout)`
  width: 28rem;
  padding-left: 1.2rem;
  place-self: start;
  ${textStyles}
`;

const GridSpan = styled.div<{ $col: number; $align: string }>`
  grid-column: span ${(props) => props.$col} / span ${(props) => props.$col};
  text-align: ${(props) => props.$align};
  ${textStyles}
`;

const HorizontalLine = styled.div`
  width: 96%;
  height: 0.1rem;
  background-color: white;
`;

const RankingBlock = styled.div`
  width: 100%;
  height: 60%;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 1rem;
`;

type RankPlayersTypes = {
  userId: number;
  nickname: string;
  color: string;
  killCnt: number;
};

type RankBoxProps = { client: CompatClient };

export default function RankBox({ client }: RankBoxProps) {
  const ACCESS_TOKEN = getAccessToken();
  const header = {
    Authorization: `Bearer ${ACCESS_TOKEN}`,
    'Content-Type': 'application/json',
  };
  const { gameData } = useGameStore();
  const [ranks, setRanks] = useState<RankPlayersTypes[]>([]);
  const subscribedRef = useRef(false);

  const subscribeToTopic = () => {
    if (!subscribedRef.current) {
      client.subscribe(
        `/topic/games/${gameData.roundId}/rank`,
        (message) => {
          console.log(JSON.parse(message.body).ranks);
          setRanks([...JSON.parse(message.body).ranks]);
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

  const RankText = (
    first: string,
    second: string,
    third: string,
    color?: string,
    key?: number
  ) => {
    return (
      <RankLayout key={key} $col={4} gap='0.4rem'>
        <GridSpan $col={1} $align={`start`}>
          {first}
        </GridSpan>
        <GridSpan color={color} $col={2} $align={`center`}>
          {second}
        </GridSpan>
        <GridSpan $col={1} $align={`end`}>
          {third}
        </GridSpan>
      </RankLayout>
    );
  };

  return (
    <WhiteBox mode='MEDIUM' width='32rem'>
      <GrayTitleBox text={`랭킹`} />
      {RankText('등수', '닉네임', '킬수')}
      <HorizontalLine />
      <RankingBlock>
        {ranks.map((value, index) => {
          return RankText(
            `${index + 1}등`,
            value.nickname,
            `${value.killCnt}킬`,
            value.color,
            index
          );
        })}
      </RankingBlock>
    </WhiteBox>
  );
}
