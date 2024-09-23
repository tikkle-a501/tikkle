import BoardCard from "@/components/card/BoardCard";

export default function Activity() {
  // mock data
  const boardData = [
    {
      title: "title",
      writer: "writer",
      status: "done",
      createdAt: "00000",
      time: "2",
      context: "Jorem ipsum dolor sit amet, consectetur adipiscing elit.",
    },
    {
      title: "title",
      writer: "writer",
      status: "done",
      createdAt: "00000",
      time: "2",
      context: "Jorem ipsum dolor sit amet, consectetur adipiscing elit.",
    },
    {
      title: "title",
      writer: "writer",
      status: "done",
      createdAt: "00000",
      time: "2",
      context: "Jorem ipsum dolor sit amet, consectetur adipiscing elit.",
    },
    {
      title: "title",
      writer: "writer",
      status: "done",
      createdAt: "00000",
      time: "2",
      context: "djklfajdfljadlfajdflj",
    },
  ];

  return (
    <>
      <div className="flex flex-col gap-10">
        <span className="py-6 text-28 font-bold leading-34 text-teal-900">
          나의 게시글
        </span>
        <div className="flex flex-row gap-10 px-10">
          {/* 게시중인 글 */}
          <div className="flex flex-col gap-10">
            <span className="text-22 font-bold leading-28 text-teal-900">
              게시 중인 글
            </span>
            <div className="scrollbar-hidden flex h-[44rem] flex-col gap-10 overflow-y-auto p-10">
              {boardData.map((item, index) => (
                <BoardCard
                  key={index}
                  title={item.title}
                  writer={item.writer}
                  status={item.status}
                  createdAt={item.createdAt}
                  time={item.time}
                  context={item.context}
                />
              ))}
            </div>
          </div>

          {/* 진행 중인 글 */}
          <div className="flex flex-col gap-10">
            <span className="text-22 font-bold leading-28 text-teal-900">
              진행 중인 글
            </span>
            <div className="scrollbar-hidden flex h-[44rem] flex-col gap-10 overflow-y-auto p-10">
              {boardData.map((item, index) => (
                <BoardCard
                  key={index}
                  title={item.title}
                  writer={item.writer}
                  status={item.status}
                  createdAt={item.createdAt}
                  time={item.time}
                  context={item.context}
                />
              ))}
            </div>
          </div>

          {/* 완료된 글 */}
          <div className="flex flex-col gap-10">
            <span className="text-22 font-bold leading-28 text-teal-900">
              완료된 글
            </span>
            <div className="scrollbar-hidden flex h-[44rem] flex-col gap-10 overflow-y-auto p-10">
              {boardData.map((item, index) => (
                <BoardCard
                  key={index}
                  title={item.title}
                  writer={item.writer}
                  status={item.status}
                  createdAt={item.createdAt}
                  time={item.time}
                  context={item.context}
                />
              ))}
            </div>
          </div>
        </div>
      </div>
    </>
  );
}
