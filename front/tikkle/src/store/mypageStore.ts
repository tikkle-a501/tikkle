import { create } from "zustand";

export interface MypageMemberResponse {
  id: string;
  name: string;
  nickname: string;
  email: string;
}

interface MypageState {
  member: MypageMemberResponse | null;
  setMember: (member: MypageMemberResponse) => void;
  clearMember: () => void;
}

export const useMypageStore = create<MypageState>((set) => ({
  member: null,
  setMember: (member: MypageMemberResponse) => set({ member }),
  clearMember: () => set({ member: null }),
}));
