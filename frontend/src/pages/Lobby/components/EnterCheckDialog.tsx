import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { enterRoom } from '../../../services/room';

import styled from 'styled-components';
import { textStyles, SmallText } from '../../../styles/fonts';
import { FlexLayout } from '../../../styles/layout';
import Dialog from '../../../components/Dialog';
import Input from '../../../components/Input';
import Button from '../../../components/Button';

const ContentBlock = styled(FlexLayout)`
  align-items: start;
`;

const WarnText = styled.div`
  ${textStyles}
`;

type EnterCheckDialogProps = {
  clickedRoom: { roomId: number; isPublic: boolean };
  closeDialog: () => void;
};

export default function EnterCheckDialog({
  clickedRoom,
  closeDialog,
}: EnterCheckDialogProps) {
  const navigate = useNavigate();
  const { roomId, isPublic } = clickedRoom;
  const [password, setPassword] = useState('');
  const [showMessage, setShowMessage] = useState(false);

  const handleChange = (e: { target: { value: string } }) => {
    setPassword(e.target.value);
  };
  const handleClick = async () => {
    try {
      const fetchedData = await enterRoom(roomId, password);
      fetchedData.success && navigate(`/room/${roomId}`);
    } catch (error: any) {
      const { message } = error.response.data.error.apierror;
      if (message === 'ROOM PASSWORD IS WRONG') {
        setShowMessage(true);
      } else {
        console.error('방 입장하기 에러', error);
        navigate(`/error`);
      }
    }
  };

  return (
    <Dialog mode={'SHORT'} width='40rem' isOpen onClose={closeDialog}>
      {isPublic ? (
        <FlexLayout $isCol gap='2rem'>
          <SmallText color='black'>{`게임에 입장하시겠습니까?`}</SmallText>
          <Button isBigger label={`YES`} onClick={handleClick} />
        </FlexLayout>
      ) : (
        <ContentBlock $isCol gap={showMessage ? '1.4rem' : '2rem'}>
          <ContentBlock $isCol gap='0.6rem'>
            <SmallText color='black'>{`비밀번호 입력`}</SmallText>
            {showMessage && (
              <WarnText color='#e84b4b'>{`비밀번호를 다시 확인해 주세요 ( ᵕ‧̯ᵕ̥̥ )`}</WarnText>
            )}
          </ContentBlock>
          <FlexLayout gap='1rem'>
            <Input name={`password`} width='26rem' onChange={handleChange} />
            <Button label={`입장`} onClick={handleClick} />
          </FlexLayout>
        </ContentBlock>
      )}
    </Dialog>
  );
}
