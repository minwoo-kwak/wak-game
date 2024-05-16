import { create } from 'zustand';
import { ResultTypes } from '../types/GameTypes';

interface Store {
  resultData: ResultTypes;
  setResultData: (userData: ResultTypes) => void;
}

const useResultStore = create<Store>((set) => ({
  resultData: { isFinished: true, roundNumber: 0, rank: 0 } as ResultTypes,
  setResultData: (resultData: ResultTypes) =>
    set(() => ({ resultData: { ...resultData } })),
}));

export default useResultStore;
