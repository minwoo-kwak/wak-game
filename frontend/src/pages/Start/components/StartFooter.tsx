import styled from 'styled-components';
import { SmallText } from '../../../styles/fonts';

const Copyright = styled(SmallText)`
  position: fixed;
  bottom: 2.4rem;
`;

export default function StartFooter() {
  return <Copyright color='#bababa'>{`© 2024 GongGongChilPal`}</Copyright>;
}
