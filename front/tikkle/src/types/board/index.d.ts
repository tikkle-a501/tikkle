import { Board } from "./index.d";
export interface Board {
  boardId: string;
  memberId: string;
  title: string;
  content: string;
  time: number;
  status: string;
  category: string;
  createdAt: Date;
}

export interface BoardDetailResponse {
  board: Board;
}

export interface BoardListResponses {
  data: Board[];
}

export interface BoardRequest {
  title: string;
  content: string;
  time: number;
  status: string;
  category: string;
}
