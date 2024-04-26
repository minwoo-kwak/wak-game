import { createGlobalStyle } from 'styled-components';

const GlobalStyle = createGlobalStyle`
  @font-face {
    font-family: 'DungGeunMo';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_six@1.2/DungGeunMo.woff') format('woff');
  }
  @layer {
    html {
      font-family: 'DungGeunMo';
      font-size: 62.5%;
    }
    body{
      margin: 0;
    }
  }
`;

export default GlobalStyle;
