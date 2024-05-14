export type RoomListTypes = {
  roomId: number;
  roomName: string;
  mode: string;
  currentPlayers: number;
  limitPlayers: number;
  isStart: boolean;
  isPublic: boolean;
};

export type RoomInfoTypes = {
  roomId: number;
  roomName: string;
  mode: string;
  limitPlayers: number;
  isPublic: boolean;
  userId: number;
  isHost: boolean;
};

export type PlayerTypes = {
  userId: number;
  nickname: string;
  color: string;
  team: string;
  isHost: boolean;
};

export type RoomPlayTypes = {
  roomId: number;
  hostId: number;
  currentPlayers: number;
  start: boolean;
  users: PlayerTypes[];
};
