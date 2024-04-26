import styled, { css } from 'styled-components';

export const textStyles = css<{ color?: string }>`
  font-family: 'DungGeunMo';
  font-size: 2rem;
  color: ${(props) => props.color || 'white'};
`;

export const SmallText = styled.div<{ color?: string }>`
  ${textStyles};
  font-size: 2.8rem;
`;

export const RegularText = styled.div<{ color?: string }>`
  ${textStyles};
  font-size: 3.6rem;
`;

export const LargeText = styled.div<{ color?: string }>`
  ${textStyles};
  font-size: 4.4rem;
`;
