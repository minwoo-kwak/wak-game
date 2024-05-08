import { create } from 'zustand';
import { persist, createJSONStorage } from 'zustand/middleware';
import { UserDataType } from '../types/UserTypes.ts';

interface Store {
  userData: UserDataType;
  setUserData: (userData: UserDataType) => void;
}

const useUserStore = create(
  persist<Store>(
    (set) => ({
      userData: { nickname: '', color: '', token: null } as UserDataType,
      setUserData: (userData: UserDataType) => set(() => ({ userData })),
    }),
    {
      name: 'userStore',
      storage: createJSONStorage(() => sessionStorage),
    }
  )
);

export default useUserStore;
