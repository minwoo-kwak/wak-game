import { useEffect, useState } from 'react';
import { isOverlap } from '../../../util/isOverlap';

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

export default function BattleField() {
  const [dots, setDots] = useState<DotPosition[]>([]);

  useEffect(() => {
    const generatedDots: DotPosition[] = [];
    const generateRandomPosition = () => ({
      top: `${Math.random() * 92}%`,
      left: `${Math.random() * 92}%`,
    });

    while (generatedDots.length < 100) {
      const newPosition = generateRandomPosition();
      if (!isOverlap(newPosition, generatedDots)) {
        generatedDots.push(newPosition);
      }
    }

    setDots(generatedDots);
  }, []);

  return (
    <GrayBox mode={'MEDIUM'} width={'79.2rem'}>
      <BattleFieldLayout>
        {dots.map((dot, index) => (
          <Dot key={index} $top={dot.top} $left={dot.left}>
            <PlayerNickname isCol nickname={`김라쿤`} color={`white`} />
          </Dot>
        ))}
      </BattleFieldLayout>
    </GrayBox>
  );
}
