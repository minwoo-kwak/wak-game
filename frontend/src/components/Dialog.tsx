import { useRef } from 'react';

import styled, { css } from 'styled-components';
import { DarkBackground, FlexLayout } from '../styles/layout';
import { motion } from 'framer-motion';

const DialogLayout = styled(motion.div)`
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
`;

const DialogBlock = styled(FlexLayout)<{
  mode: 'TALL' | 'SHORT';
  width?: string;
}>`
  width: ${(props) => props.width || '78rem'};
  height: ${(props) => (props.mode === 'TALL' ? '53.2rem' : '15.2rem')};
  border-style: solid;
  border-left-width: 0rem;
  border-right-width: 0rem;
  border-top-width: 0.4rem;
  border-bottom-width: 0.4rem;
  background-color: #dadada;
`;

const BorderX = styled.img.attrs({
  alt: '모달',
})<{ $right?: boolean }>`
  ${(props) =>
    props.$right &&
    css`
      transform: rotate(0.5turn);
    `};
`;

type DialogProps = {
  mode: 'TALL' | 'SHORT';
  width?: string;
  isOpen: boolean;
  children?: React.ReactNode;
  onClose: () => void;
};

export default function Dialog({
  mode,
  width,
  isOpen,
  children,
  onClose,
}: DialogProps) {
  const outside = useRef<HTMLDivElement>(null);
  const container = {
    show: { y: 0, opacity: 1 },
    hidden: { y: '100%', opacity: 0 },
  };
  const img =
    mode === 'TALL'
      ? require('../assets/borderImg/img-border-black-h540.png')
      : require('../assets/borderImg/img-border-black-h160.png');

  return (
    <DarkBackground>
      <DialogLayout
        variants={container}
        initial='hidden'
        animate={isOpen ? 'show' : 'hidden'}
        transition={{
          type: 'spring',
          mass: 0.5,
          damping: 40,
          stiffness: 400,
        }}
        ref={outside}
        onClick={(e) => {
          if (e.target === outside.current) {
            onClose?.();
          }
        }}
      >
        <BorderX src={img} />
        <DialogBlock $isCol mode={mode} width={width}>
          {children}
        </DialogBlock>
        <BorderX src={img} $right />
      </DialogLayout>
    </DarkBackground>
  );
}
