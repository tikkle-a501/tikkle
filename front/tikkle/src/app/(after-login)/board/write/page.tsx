"use client";
import dynamic from "next/dynamic";

// dynamic import로 WriteEditor를 동적으로 불러오기 (서버 사이드 렌더링 하지 않도록 설정)
const WriteEditor = dynamic(() => import("@/components/input/WriteEditor"), {
  ssr: false, // 서버 사이드 렌더링 비활성화
});

export default function Write() {
  return (
    <>
      <div className="text-40 font-bold text-teal900">게시글 작성</div>
      <WriteEditor />
    </>
  );
}
