import styled, { css } from 'styled-components';

const FixedLayout = css`
  position: fixed;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
  padding: 4rem;
`;

export const ImgBackground = styled.div<{ $img: string }>`
  ${FixedLayout}
  background-image: url(${(props) => props.$img});
  background-size: cover;
  background-position: center;
`;

export const DarkBackground = styled.div<{ $opaque?: number }>`
  ${FixedLayout}
  background: ${(props) => `rgba(0, 0, 0, ${props.$opaque || 0.6})`};
`;

export const GridLayout = styled.div<{
  $col?: number;
  gap?: string;
}>`
  display: grid;
  ${(props) =>
    props.$col
      ? css`
          grid-template-columns: repeat(${props.$col}, minmax(0, 1fr));
        `
      : css`
          grid-template-columns: repeat(3, minmax(0, 1fr));
        `}

  gap: ${(props) => props.gap || '2rem'};
`;

export const FlexLayout = styled.div<{
  $isCol?: boolean;
  gap?: string;
}>`
  display: flex;
  justify-content: center;
  align-items: center;
  gap: ${(props) => props.gap || '2rem'};
  flex-direction: ${(props) => (props.$isCol ? 'column' : 'row')};
`;
