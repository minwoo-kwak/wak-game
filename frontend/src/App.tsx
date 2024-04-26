import styled from 'styled-components';
import Main from './components/Main';

const AppBlock = styled.div`
  width: 100dvw;
  height: 100dvh;
`;

function App() {
  return (
    <AppBlock className='App'>
      <Main />
    </AppBlock>
  );
}

export default App;
