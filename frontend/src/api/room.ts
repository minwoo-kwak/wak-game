import { axiosInstance } from './baseInstance';

export const createRoom = async (
  room_name: string,
  limit_players: number,
  room_password: string,
  mode: string
) => {
  const response = await axiosInstance.post(
    `/rooms`,
    {
      room_name: room_name,
      room_password: room_password,
      limit_players: limit_players,
      mode: mode,
    },
    {
      headers: {
        'Content-Type': 'application/json',
      },
    }
  );
  return response.data;
};
