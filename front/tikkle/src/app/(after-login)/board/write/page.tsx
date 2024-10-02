"use client";
import dynamic from "next/dynamic";
import { useSearchParams } from "next/navigation"; // 쿼리 파라미터 사용

// dynamic import로 WriteEditor를 동적으로 불러오기 (서버 사이드 렌더링 하지 않도록 설정)
const WriteEditor = dynamic(() => import("@/components/input/WriteEditor"), {
  ssr: false, // 서버 사이드 렌더링 비활성화
});

export default function Write() {
  const searchParams = useSearchParams(); // 쿼리 파라미터 사용
  const id = searchParams.get("id") || undefined; // URL 쿼리에서 id 추출

  return (
    <>
      <div className="text-40 font-bold text-teal900">게시글 작성</div>
      <WriteEditor boardId={id} /> {/* boardId로 id 전달 */}
    </>
  );
}
