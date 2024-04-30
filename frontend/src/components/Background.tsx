import styled from 'styled-components';
import { FlexLayout, ImgBackground, DarkBackground } from '../styles/layout';

import mainImg1 from '../assets/img-main1.png';

const BackgroundLayout = styled(FlexLayout)`
  width: 100%;
  height: 100%;
`;

type BackgroundProps = {
  isCol?: boolean;
  gap?: string;
  opaque?: number;
  children: React.ReactNode;
};

export default function Background({
  isCol,
  gap,
  opaque,
  children,
}: BackgroundProps) {
  return (
    <ImgBackground $img={mainImg1}>
      <DarkBackground $opaque={opaque}>
        <BackgroundLayout $isCol={isCol} gap={gap}>
          {children}
        </BackgroundLayout>
      </DarkBackground>
    </ImgBackground>
  );
}
