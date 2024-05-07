import { FlexLayout } from '../../../styles/layout';
import { RegularText, SmallText } from '../../../styles/fonts';

import GrayBox from '../../../components/GrayBox';
import Input from '../../../components/Input';

type GameResultProps = {
  isWinner?: boolean;
  round: number;
};

export default function GameResult({ isWinner, round }: GameResultProps) {
  const ResultText = ({
    title,
    results,
    timeText,
  }: {
    title: string;
    results: string[][];
    timeText: string;
  }) => {
    const RESULTS_LENGTH = 5;
    return (
      <FlexLayout $isCol gap={isWinner && round < 3 ? '4rem' : '6rem'}>
        <RegularText color='black'>{title}</RegularText>
        <FlexLayout $isCol gap='2.4rem'>
          {results.slice(0, RESULTS_LENGTH - 1).map((value, index) => {
            return (
              <SmallText
                key={index}
                color='black'
              >{`${value[0]} : ${value[1]}`}</SmallText>
            );
          })}
          {isWinner && round < 3 ? (
            <FlexLayout $isCol gap='1rem'>
              <SmallText color='black'>{`${
                round + 1
              } 라운드 도발 멘트 입력`}</SmallText>
              <Input name='winnerComment' width='60rem' />
            </FlexLayout>
          ) : (
            <FlexLayout gap='1.2rem'>
              <SmallText color='black'>{`${
                results[RESULTS_LENGTH - 1][0]
              } : `}</SmallText>
              <SmallText color='#725bff'>
                {results[RESULTS_LENGTH - 1][1]}
              </SmallText>
            </FlexLayout>
          )}
        </FlexLayout>
        <SmallText color='black'>{timeText}</SmallText>
      </FlexLayout>
    );
  };

  const roundData = {
    title: `<< ${round} 라운드 결과 >>`,
    results: [
      ['게임 시간', '10.03초'],
      ['나의 생존 시간', '5.29초'],
      ['나의 킬 수', '3킬'],
      ['나의 랭킹', '9위'],
      ['나를 죽인 사람', '김싸피'],
    ],
    timeText: `${round + 1} 라운드 시작까지 ...30초`,
  };

  const finalData = {
    title: `<< 최종 결과 >>`,
    results: [
      ['총 게임 시간', '50.35초'],
      ['나의 총 생존 시간', '14.78초'],
      ['나의 총 킬 수', '11킬'],
      ['나의 최종 랭킹', '6위'],
      ['최종 1위', '김싸피'],
    ],
    timeText: `게임 종료까지 ...30초`,
  };

  return (
    <FlexLayout $isCol gap='1rem'>
      <GrayBox mode={'TALL'} width={'79.2rem'}>
        {round < 3 ? ResultText(roundData) : ResultText(finalData)}
      </GrayBox>
    </FlexLayout>
  );
}
