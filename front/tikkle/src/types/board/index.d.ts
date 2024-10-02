import { Board } from "./index.d";
export interface Board {
  boardId: string;
  memberId: string;
  title: string;
  content: string;
  time: number;
  status: string;
  category: string;
  createdAt: string;
}

export interface BoardDetailResponse {
  board: Board;
}

export type BoardListResponses = Board[];

export interface BoardRequest {
  title: string;
  content: string;
  time: number;
  status: string;
  category: string;
}
