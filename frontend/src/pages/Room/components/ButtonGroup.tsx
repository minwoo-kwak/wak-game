import { useNavigate } from 'react-router-dom';

import styled from 'styled-components';
import { FlexLayout } from '../../../styles/layout';

import RoundButton from '../../../components/RoundButton';

const Layout = styled(FlexLayout)`
  place-self: end;
`;

type ButtonGroupProps = {
  isHost: boolean;
};

export default function ButtonGroup({ isHost }: ButtonGroupProps) {
  const navigate = useNavigate();

  const handleStart = () => {
    navigate(`/game`);
  };

  const hanndleBack = () => {
    navigate(`/lobby`, { replace: true });
  };

  return (
    <Layout gap='2rem'>
      <RoundButton color='purple' label={`게임 방법`} />
      {isHost && (
        <RoundButton color='blue' label={`게임 시작`} onClick={handleStart} />
      )}
      <RoundButton color='red' label={`나가기`} onClick={hanndleBack} />
    </Layout>
  );
}
