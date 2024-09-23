"use client";

import dynamic from "next/dynamic";
import "@toast-ui/editor/dist/toastui-editor.css";
import Badge from "@/components/badge/Badge";
import Chips from "@/components/chips/Chips";

const Viewer = dynamic(
  () => import("@toast-ui/react-editor").then((mod) => mod.Viewer),
  { ssr: false },
);

export default function Boarddetail() {
  const board = {
    boardId: "1",
    status: "게시중",
    title: "test title",
    writer: "test writer",
    createdAt: "2024-09-11T17:45:15Z",
    category: "업무",
    time: 2,
    content: "test content",
  };

  return (
    <>
      <div className="text-40 font-bold text-teal900">SSAFY의 티끌</div>
      <div className="flex flex-1 flex-col gap-12 self-stretch rounded-12 border border-warmGray200 px-40 py-36">
        {/* 제목 영역 */}
        <div className="flex items-center gap-20">
          <Badge size="l" color="teal">
            {board.status}
          </Badge>
          <div className="flex flex-1 text-28 font-bold">{board.title}</div>
          <div>
            <span className="material-symbols-outlined">more_horiz</span>
          </div>
        </div>

        {/* 작성자, 작성일 */}
        <div className="flex items-center gap-20 pb-10">
          <div className="font-semibold text-warmGray500">{board.writer}</div>
          <div className="text-warmGray400">
            {new Date(board.createdAt).toLocaleString()}
          </div>
        </div>

        {/* 카테고리, 예상 시간 */}
        <div className="flex items-center gap-20">
          <Chips size="l" variant="primary" design="fill">
            {board.category}
          </Chips>
          <div className="w-1 self-stretch bg-warmGray200"></div>
          <div className="flex items-center justify-center gap-10">
            <div>예상 시간</div>
            <div className="font-semibold text-teal-500">{board.time}</div>
            <div>시간</div>
          </div>
        </div>

        {/* 내용 */}
        <div className="flex flex-1 self-stretch px-10 py-20 text-17 leading-7">
          <Viewer width="100%" initialValue={board.content} />
        </div>
      </div>
    </>
  );
}
