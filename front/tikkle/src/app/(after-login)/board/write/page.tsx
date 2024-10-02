"use client"; // 클라이언트 컴포넌트로 지정

import dynamic from "next/dynamic";
import { useSearchParams } from "next/navigation";
import { Suspense } from "react";

// dynamic import로 WriteEditor를 동적으로 불러오기 (서버 사이드 렌더링 하지 않도록 설정)
const WriteEditor = dynamic(() => import("@/components/input/WriteEditor"), {
  ssr: false, // 서버 사이드 렌더링 비활성화
});

export default function WritePage() {
  const searchParams = useSearchParams(); // 쿼리 파라미터 사용
  const id = searchParams.get("id") || undefined; // id 파라미터 추출

  return (
    <Suspense fallback={<div>Loading...</div>}>
      <div className="text-40 font-bold text-teal900">게시글 작성</div>
      <WriteEditor boardId={id} /> {/* boardId로 id 전달 */}
    </Suspense>
  );
}
