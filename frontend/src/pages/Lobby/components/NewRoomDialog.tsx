import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { createRoom } from '../../../services/room';

import styled from 'styled-components';
import { FlexLayout } from '../../../styles/layout';
import { RegularText, SmallText, textStyles } from '../../../styles/fonts';
import Dialog from '../../../components/Dialog';
import Input from '../../../components/Input';
import CheckboxInput from '../../../components/CheckboxInput';
import RoundButton from '../../../components/RoundButton';

const DialogTitle = styled(RegularText)`
  margin-top: 1rem;
  margin-bottom: 4rem;
`;

const ContentBlock = styled(FlexLayout)`
  align-items: start;
`;

const InputBlock = styled(FlexLayout)`
  position: relative;
`;

const WarningText = styled.div`
  position: absolute;
  top: 4.8rem;
  right: 0;
  ${textStyles}
`;

const TinyText = styled.div`
  ${textStyles}
`;

const ButtonBlock = styled.div`
  margin-top: 4rem;
  margin-bottom: 0.4rem;
`;

type NewRoomDialogProps = {
  closeDialog: () => void;
};

export default function NewRoomDialog({ closeDialog }: NewRoomDialogProps) {
  const navigate = useNavigate();
  const [info, setInfo] = useState({
    title: '',
    players: 0,
    isSecret: false,
    password: '',
    mode: 'SOLO',
  });
  const [warn, setWarn] = useState({
    warnTitle: false,
    warnPlayers: false,
    warnPassword: false,
  });

  const handleChange = (e: { target: { name: string; value: string } }) => {
    const { name, value } = e.target;
    setInfo({ ...info, [name]: value });
  };

  const handleClick = async () => {
    const updatedWarn = { ...warn };

    if (info.title.length < 2 || 12 < info.title.length) {
      updatedWarn.warnTitle = true;
    } else {
      updatedWarn.warnTitle = false;
    }
    if (info.players < 2 || 100 < info.players) {
      updatedWarn.warnPlayers = true;
    } else {
      updatedWarn.warnPlayers = false;
    }
    if (info.isSecret && info.password.length !== 4) {
      updatedWarn.warnPassword = true;
    } else {
      updatedWarn.warnPassword = false;
    }

    setWarn(updatedWarn);

    if (
      updatedWarn.warnTitle === false &&
      updatedWarn.warnPlayers === false &&
      updatedWarn.warnPassword === false
    ) {
      try {
        const { title, players, password, mode } = info;
        const fetchedData = await createRoom(title, players, password, mode);
        navigate(`/room/${fetchedData.data.roomId}`);
      } catch (error: any) {
        console.error('방 만들기 에러', error);
        navigate(`/error`);
      }
    }
  };

  return (
    <Dialog mode='TALL' isOpen onClose={closeDialog}>
      <DialogTitle color='black'>{`₊‧.°.⋆ 새로운 방 •˚₊‧⋆.`}</DialogTitle>
      <ContentBlock $isCol gap='4.8rem'>
        <InputBlock gap='1rem'>
          <SmallText color='black'>{`방 제목 :`}</SmallText>
          <Input name={`title`} width='52rem' onChange={handleChange} />
          <WarningText
            color={warn.warnTitle ? '#e84b4b' : '#7c7c7c'}
          >{`2자에서 12자 입력`}</WarningText>
        </InputBlock>
        <InputBlock gap='1rem'>
          <SmallText color='black'>{`인원 :`}</SmallText>
          <FlexLayout gap='1rem'>
            <Input name={`players`} width='39.8rem' onChange={handleChange} />
            <SmallText color='black'>{`명 / 100 명`}</SmallText>
          </FlexLayout>
          <WarningText
            color={warn.warnPlayers ? '#e84b4b' : '#7c7c7c'}
          >{`2 이상 100 이하의 숫자 입력`}</WarningText>
        </InputBlock>

        <ContentBlock $isCol gap='2.8rem'>
          <FlexLayout gap='4rem'>
            <FlexLayout gap='1rem'>
              <SmallText color='black'>{`비밀방`}</SmallText>
              <CheckboxInput
                onClick={() => setInfo({ ...info, isSecret: !info.isSecret })}
              />
            </FlexLayout>
            <InputBlock gap='1rem'>
              <SmallText color='black'>{`비밀번호`}</SmallText>
              <Input
                name={`password`}
                width='35.8rem'
                disabled={!info.isSecret}
                onChange={handleChange}
              />
              <WarningText
                color={warn.warnPassword ? '#e84b4b' : '#7c7c7c'}
              >{`4자리 숫자 입력`}</WarningText>
            </InputBlock>
          </FlexLayout>
          <FlexLayout gap='4rem'>
            <FlexLayout gap='1rem'>
              <SmallText color='black'>{`개인전`}</SmallText>
              <CheckboxInput checked />
            </FlexLayout>
            <FlexLayout gap='1rem'>
              <SmallText color='black'>{`팀전`}</SmallText>
              <CheckboxInput disabled />
              <TinyText color='#7c7c7c'>{`(준비 중이에요._.)`}</TinyText>
            </FlexLayout>
          </FlexLayout>
        </ContentBlock>
      </ContentBlock>
      <ButtonBlock>
        <RoundButton label={`방 만들기`} onClick={handleClick} />
      </ButtonBlock>
    </Dialog>
  );
}
