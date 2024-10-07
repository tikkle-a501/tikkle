"use client";

import BoardCard from "@/components/card/BoardCard";
import { useFetchActivity } from "@/hooks";
import { ActivityGetResponses } from "@/types";
import { useEffect, useState } from "react";

export default function Activity() {
  const { data, isLoading, error } = useFetchActivity();
  const [activity, setActivity] = useState<ActivityGetResponses | null>(null);

  useEffect(() => {
    if (data) {
      setActivity(data);
      console.log("Activity:", data);
    }
  }, [data]);

  if (isLoading || !activity) return <div>Loading...</div>;
  if (error) return <div>Error occurred: {error.message}</div>;

  const postedPosts = activity.postedPosts || [];
  const activePosts = activity.activePosts || [];
  const donePosts = activity.donePosts || [];

  console.log(data);

  return (
    <div className="flex w-full flex-col gap-10">
      <span className="py-6 text-28 font-bold leading-34 text-teal-900">
        나의 게시글
      </span>
      <div className="flex flex-row gap-10 px-10">
        <div className="flex flex-1 flex-col gap-10">
          <span className="text-22 font-bold leading-28 text-teal-900">
            게시 중인 글
          </span>
          <div className="scrollbar-hidden flex h-[44rem] flex-col gap-10 overflow-y-auto">
            {postedPosts.length > 0 ? (
              postedPosts.map((post) => (
                <BoardCard
                  key={post.boardId}
                  boardId={post.boardId}
                  title={post.title}
                  status={post.status}
                  createdAt={post.createdAt}
                  time={post.time}
                  content={post.content}
                />
              ))
            ) : (
              <span className="text-15 text-warmGray300">
                게시 중인 글이 없습니다.
              </span>
            )}
          </div>
        </div>

        <div className="flex flex-1 flex-col gap-10">
          <span className="text-22 font-bold leading-28 text-teal-900">
            진행 중인 글
          </span>
          <div className="scrollbar-hidden 0 flex h-[44rem] flex-col gap-10 overflow-y-auto">
            {activePosts.length > 0 ? (
              activePosts.map((post) => (
                <BoardCard
                  key={post.boardId}
                  boardId={post.boardId}
                  title={post.title}
                  status={post.status}
                  createdAt={post.createdAt}
                  time={post.time}
                  content={post.content}
                />
              ))
            ) : (
              <span className="text-15 text-warmGray300">
                진행 중인 글이 없습니다.
              </span>
            )}
          </div>
        </div>

        <div className="flex flex-1 flex-col gap-10">
          <span className="text-22 font-bold leading-28 text-teal-900">
            완료된 글
          </span>
          <div className="scrollbar-hidden flex h-[44rem] flex-col gap-10 overflow-y-auto">
            {donePosts.length > 0 ? (
              donePosts.map((post) => (
                <BoardCard
                  key={post.boardId}
                  boardId={post.boardId}
                  title={post.title}
                  status={post.status}
                  createdAt={post.createdAt}
                  time={post.time}
                  content={post.content}
                />
              ))
            ) : (
              <span className="text-15 text-warmGray300">
                완료된 글이 없습니다.
              </span>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
