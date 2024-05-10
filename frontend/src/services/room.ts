import { axiosInstance } from './base';

export const getRoomlist = async () => {
  const response = await axiosInstance.get(`/rooms/topic/lobby`, {
    headers: {
      'Content-Type': 'application/json',
    },
  });
  return response.data;
};

export const getRoomInfo = async (roomId: number) => {
  const response = await axiosInstance.get(`/rooms/topic/${roomId}`, {
    headers: {
      'Content-Type': 'application/json',
    },
  });
  return response.data;
};

export const createRoom = async (
  roomName: string,
  limitPlayers: number,
  roomPassword: string,
  mode: string
) => {
  const response = await axiosInstance.post(
    `/rooms`,
    {
      roomName: roomName,
      roomPassword: roomPassword,
      limitPlayers: limitPlayers,
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

export const enterRoom = async (roomId: number, roomPassword: string) => {
  const response = await axiosInstance.post(
    `/rooms/${roomId}`,
    {
      roomPassword: roomPassword,
    },
    {
      headers: {
        'Content-Type': 'application/json',
      },
    }
  );
  return response.data;
};
