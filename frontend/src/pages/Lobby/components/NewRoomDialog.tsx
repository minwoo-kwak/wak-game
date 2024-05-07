import styled from 'styled-components';
import { FlexLayout } from '../../../styles/layout';
import { SmallText } from '../../../styles/fonts';

import Dialog from '../../../components/Dialog';
import Input from '../../../components/Input';
import CheckboxInput from '../../../components/CheckboxInput';
import RoundButton from '../../../components/RoundButton';

const InputBlock = styled(FlexLayout)`
  align-items: start;
`;

const ContentBlock = styled(FlexLayout)`
  align-items: start;
`;

const ButtonBlock = styled.div`
  margin-top: 4.8rem;
`;

type NewRoomDialogProps = {
  closeDialog: () => void;
};

export default function NewRoomDialog({ closeDialog }: NewRoomDialogProps) {
  const inputColumn = (text: string, inputName: string) => {
    return (
      <InputBlock $isCol gap='1rem'>
        <SmallText color='black'>{text}</SmallText>
        <Input name={inputName} width='64rem' />
      </InputBlock>
    );
  };

  const inputRow = (text: string, isCheckbox: boolean, inputName?: string) => {
    return (
      <FlexLayout gap='1rem'>
        <SmallText color='black'>{text}</SmallText>
        {isCheckbox ? (
          <CheckboxInput />
        ) : (
          <Input name={inputName || ''} width='34.2rem' />
        )}
      </FlexLayout>
    );
  };

  return (
    <Dialog isOpen onClose={closeDialog}>
      <ContentBlock $isCol gap='2.8rem'>
        {inputColumn('방 제목', 'title')}
        {inputColumn('인원', 'people')}
        <FlexLayout gap='4rem'>
          {inputRow('개인전', true)}
          {inputRow('팀전', true)}
        </FlexLayout>
        <FlexLayout gap='4rem'>
          {inputRow('비밀방', true)}
          {inputRow('비밀번호', false, 'password')}
        </FlexLayout>
      </ContentBlock>
      <ButtonBlock>
        <RoundButton label={`방 만들기`} />
      </ButtonBlock>
    </Dialog>
  );
}
