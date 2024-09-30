"use client";

import { useState, useEffect, useRef } from "react";
import "@toast-ui/editor/dist/toastui-editor.css";
import { Editor } from "@toast-ui/react-editor";
import Button from "@/components/button/Button";
import Chips from "@/components/chips/Chips";

const WriteEditor = () => {
  // 글 작성 중인지 확인용 변수
  const [isDirty, setIsDirty] = useState(false);

  const handleFormChange = () => {
    setIsDirty(true);
  };

  // 제목, 카테고리, 내용
  const [title, setTitle] = useState("");
  const [selectedCategory, setSelectedCategory] = useState<"업무" | "비업무">(
    "업무",
  );
  const editorRef = useRef<Editor>(null);

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

    const postData = {
      title,
      content,
      selectedCategory,
    };
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

  // 에디터의 초기값을 명시적으로 설정
  useEffect(() => {
    if (editorRef.current) {
      editorRef.current.getInstance().setMarkdown(""); // 명시적으로 빈 값을 설정
    }
  }, []);

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
            main="등록하기"
          ></Button>
        </div>
      </div>

      <div className="flex items-center gap-20">
        <div className="flex gap-14">
          <Chips
            size="l"
            variant="primary"
            design={selectedCategory === "업무" ? "fill" : "outline"}
            onClick={() => setSelectedCategory("업무")}
          >
            업무
          </Chips>
          <Chips
            size="l"
            variant="primary"
            design={selectedCategory === "비업무" ? "fill" : "outline"}
            onClick={() => setSelectedCategory("비업무")}
          >
            비업무
          </Chips>
        </div>
        <div className="w-1 self-stretch bg-warmGray200"></div>
        <div className="flex items-center justify-center gap-10">
          <div>예상 시간</div>
          <div className="max-w-20">
            <input
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
          initialValue=""
          placeholder="내용을 입력해주세요."
          previewStyle="vertical"
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
