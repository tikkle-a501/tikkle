"use client";

import { useState, useEffect, useRef } from "react";
import "@toast-ui/editor/dist/toastui-editor.css";
import { Editor } from "@toast-ui/react-editor";
import Button from "@/components/button/Button";
import Chips from "@/components/chips/Chips";
import {
  useCreateBoard,
  useFetchBoardDetail,
  useUpdateBoard,
} from "@/hooks/board";
import { useRouter, useSearchParams } from "next/navigation";
export interface BoardRequest {
  title: string;
  content: string;
  time: number;
  status: string;
  category: string;
}
const WriteEditor = () => {
  // useRouter 훅 사용
  const router = useRouter();
  // 글 작성 중인지 확인용 변수
  const [isDirty, setIsDirty] = useState(false);
  //
  const searchParams = useSearchParams(); // 쿼리 파라미터 사용
  const boardId = searchParams.get("id") || undefined; // URL 쿼리에서 id 추출
  const handleFormChange = () => {
    setIsDirty(true);
  };
  // 제목, 카테고리, 시간, 내용
  const [title, setTitle] = useState("");
  const [category, setCategory] = useState<"업무" | "비업무">("업무");
  const [time, setTime] = useState(0);
  const editorRef = useRef<Editor>(null);

  // useCreateBoard 및 useUpdateBoard 훅을 호출
  const { mutate: createBoard } = useCreateBoard();
  const { mutate: updateBoard } = useUpdateBoard();

  // boardId가 있는 경우에만 데이터를 가져오도록 조건부 처리
  const {
    data: board,
    isLoading,
    error,
  } = boardId
    ? useFetchBoardDetail(boardId) // boardId가 있을 때만 훅 호출
    : { data: null, isLoading: false, error: null }; // boardId가 없을 때 기본값

  useEffect(() => {
    if (boardId && board) {
      setTitle(board.title);
      setCategory(
        board.category === "업무" || board.category === "비업무"
          ? board.category
          : "업무", // 기본값으로 "업무" 설정
      );

      setTime(board.time || 0);
      // Toast UI Editor에 내용 설정
      if (editorRef.current) {
        editorRef.current.getInstance().setMarkdown(board.content || ""); // 에디터 내용 설정
      }
    } else {
      // 에디터의 초기값을 명시적으로 설정
      if (editorRef.current) {
        editorRef.current.getInstance().setMarkdown(""); // 명시적으로 빈 값을 설정
      }
    }
  }, [boardId, board]);
  // 발행 버튼
  const handleFormSubmit = async () => {
    const content = editorRef.current?.getInstance().getMarkdown(); // 에디터에서 markdown 텍스트를 가져옴

    // 제목과 내용을 확인 (공백 제외하고)
    if (!title.trim()) {
      alert("제목을 입력해주세요.");
      return;
    }

    if (!content.trim()) {
      alert("내용을 입력해주세요.");
      return;
    }

    const boardData: BoardRequest = {
      title,
      content,
      time,
      status: "진행전",
      category,
    };
    // boardId가 있으면 수정, 없으면 생성
    if (boardId) {
      // 업데이트 요청 시 boardId와 boardData를 전달
      updateBoard(
        { boardId, boardData },
        {
          onSuccess: () => {
            console.log("게시글이 성공적으로 수정되었습니다.");
            router.push("/board");
          },
          onError: (error) => {
            console.error("게시글 수정 중 오류 발생:", error);
          },
        },
      );
    } else {
      // 생성 요청
      createBoard(boardData, {
        onSuccess: () => {
          console.log("게시글이 성공적으로 생성되었습니다.");
          router.push("/board");
        },
        onError: (error) => {
          console.error("게시글 생성 중 오류 발생:", error);
        },
      });
    }
  };
  // 글 작성 중 새로고침 방지
  useEffect(() => {
    const handleBeforeUnload = (e: BeforeUnloadEvent) => {
      if (isDirty) {
        e.preventDefault();
      }
    };

    window.addEventListener("beforeunload", handleBeforeUnload);

    return () => {
      window.removeEventListener("beforeunload", handleBeforeUnload);
    };
  }, [isDirty]);

  // 글 작성 중 뒤로가기 방지, 페이지 이동 방지 필요

  return (
    <div className="flex flex-1 flex-col items-start gap-12 self-stretch rounded-12 border border-warmGray200 px-40 py-36">
      <div className="flex items-center gap-40 self-stretch">
        <div className="flex flex-1">
          <input
            type="text"
            value={title}
            placeholder="제목을 입력해주세요."
            className="inline-flex flex-1 appearance-none items-center justify-center border-b border-warmGray200 pb-5 text-28 font-bold placeholder-warmGray300 focus:outline-none"
            onChange={(e) => {
              setTitle(e.target.value);
              handleFormChange();
            }}
          />
        </div>
        <div>
          <Button
            size="l"
            variant="primary"
            design="fill"
            main={boardId ? "수정하기" : "등록하기"} // 버튼 텍스트 변경
            onClick={handleFormSubmit}
          ></Button>
        </div>
      </div>

      <div className="flex items-center gap-20">
        <div className="flex gap-14">
          <Chips
            size="l"
            variant="primary"
            design={category === "업무" ? "fill" : "outline"}
            onClick={() => setCategory("업무")}
          >
            업무
          </Chips>
          <Chips
            size="l"
            variant="primary"
            design={category === "비업무" ? "fill" : "outline"}
            onClick={() => setCategory("비업무")}
          >
            비업무
          </Chips>
        </div>
        <div className="w-1 self-stretch bg-warmGray200"></div>
        <div className="flex items-center justify-center gap-10">
          <div>예상 시간</div>
          <div className="max-w-20">
            <input
              type="number"
              value={time}
              onChange={(e) => {
                setTime(Number(e.target.value));
                handleFormChange();
              }}
              className="min-w-0 max-w-20 flex-grow appearance-none border-none text-right text-18 font-semibold placeholder-coolGray300 outline-none placeholder:text-18 placeholder:font-semibold"
              placeholder="0"
            />
          </div>
          <div>시간</div>
        </div>
      </div>

      <div className="w-full flex-grow px-10 py-20">
        <Editor
          ref={editorRef}
          height="500px"
          placeholder="내용을 입력해주세요."
          previewStyle={window.innerWidth > 1000 ? "vertical" : "tab"}
          initialEditType="markdown"
          hideModeSwitch={false}
          toolbarItems={[
            ["heading", "bold", "italic", "strike"],
            ["hr", "quote"],
            ["table", "image", "link"],
            ["code", "codeblock"],
          ]}
          usageStatistics={false}
          onChange={handleFormChange} // 내용이 변경될 때 isDirty 설정
        />
      </div>
    </div>
  );
};

export default WriteEditor;
