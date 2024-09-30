import {
  BoardDetailResponse,
  BoardListResponses,
  BoardRequest,
  Board,
} from "@/types/board";
import { handleApiRequest } from "../client";

//전체 공고 조회
export const fetchBoardList = async () => {
  return handleApiRequest<Board[], "get">("/board", "get");
};

//공고 디테일 조회
export const fetchBoardDetail = async (boardId: string) => {
  return handleApiRequest<Board, "get">(`/board/${boardId}`, "get");
};

//공고 작성
export const createBoard = async (data: BoardRequest) => {
  return handleApiRequest<void, "post", BoardRequest>("/board", "post", data);
};

//공고 업데이트
export const updateBoard = async (boardId: string, data: BoardRequest) => {
  return handleApiRequest<void, "put", BoardRequest>(
    `/board/${boardId}`,
    "put",
    data,
  );
};

//공고 삭제
export const deleteBoard = async (boardId: string) => {
  return handleApiRequest<void, "delete">(`/board/${boardId}`, "delete");
};
