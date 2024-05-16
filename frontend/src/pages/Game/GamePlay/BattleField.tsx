import { useEffect, useState } from 'react';
import useGameStore from '../../../store/gameStore';
import { isOverlap } from '../../../utils/isOverlap';

import styled from 'styled-components';
import GrayBox from '../../../components/GrayBox';
import PlayerNickname from '../../../components/PlayerNickname';

const BattleFieldLayout = styled.div`
  position: relative;
  width: 100%;
  height: 100%;
`;

const Dot = styled.div<{ $top: string; $left: string }>`
  position: absolute;
  top: ${(props) => props.$top};
  left: ${(props) => props.$left};
`;

type DotPosition = {
  top: string;
  left: string;
};

// type BattleFieldProps = {};

export default function BattleField() {
  const { gameData } = useGameStore();
  const [dots, setDots] = useState<DotPosition[]>([]);

  useEffect(() => {
    const generatedDots: DotPosition[] = [];
    const generateRandomPosition = () => ({
      top: `${Math.random() * 92}%`,
      left: `${Math.random() * 92}%`,
    });

    while (generatedDots.length < gameData.players.length) {
      const newPosition = generateRandomPosition();
      if (!isOverlap(newPosition, generatedDots)) {
        generatedDots.push(newPosition);
      }
    }

    setDots(generatedDots);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return (
    <GrayBox mode={'MEDIUM'} width={'79.2rem'}>
      <BattleFieldLayout>
        {dots.map((dot, index) => (
          <Dot key={index} $top={dot.top} $left={dot.left}>
            <PlayerNickname
              isCol
              nickname={gameData.players[index].nickname}
              color={gameData.players[index].color}
            />
          </Dot>
        ))}
      </BattleFieldLayout>
    </GrayBox>
  );
}
