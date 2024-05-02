import styled from 'styled-components';
import { FlexLayout } from '../../../styles/layout';

import RoundButton from '../../../components/RoundButton';

const Layout = styled(FlexLayout)`
  place-self: end;
`;

export default function ButtonGroup() {
  return (
    <Layout>
      <RoundButton color='purple' label={`게임 방법`} />
      {/* <RoundButton color='blue' label={`게임 시작`} /> */}
      <RoundButton color='red' label={`나가기`} />
    </Layout>
  );
}
