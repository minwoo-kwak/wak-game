import { axiosInstance } from './base';

export const startGame = async (
  roomId: number,
  comment: string,
  showNickname: boolean
) => {
  const response = await axiosInstance.post(
    `/games/start/${roomId}`,
    {
      comment: comment,
      showNickname: showNickname,
    },
    {
      headers: {
        'Content-Type': 'application/json',
      },
    }
  );
  return response.data;
};

export const getBattleField = async (roundId: number) => {
  const response = await axiosInstance.get(`/games/${roundId}/battle-field`, {
    headers: {
      'Content-Type': 'application/json',
    },
  });
  return response.data;
};
