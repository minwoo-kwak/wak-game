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
  return (
    <Layout gap='2rem'>
      <RoundButton color='purple' label={`게임 방법`} />
      {isHost && <RoundButton color='blue' label={`게임 시작`} />}
      <RoundButton color='red' label={`나가기`} />
    </Layout>
  );
}
