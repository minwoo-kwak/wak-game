import Button from '../../../components/Button';

type NewRoomButtonProps = {
  handleClick: () => void;
};

export default function NewRoomButton({ handleClick }: NewRoomButtonProps) {
  return <Button isBigger label={'방 만들기'} onClick={handleClick} />;
}
