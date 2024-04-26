import { FlexLayout } from '../../styles/layout';

import Background from '../../components/Background';
import StartTitle from './components/StartTitle';
import NameForm from './components/NameForm';
import StartFooter from './components/StartFooter';

export default function StartPage() {
  return (
    <Background isCol opaque={0.32}>
      <FlexLayout $isCol gap='10rem'>
        <StartTitle />
        <NameForm />
      </FlexLayout>
      <StartFooter />
    </Background>
  );
}
