import BoardCard from "@/components/card/BoardCard";

export default function Activity() {
  return (
    <>
      {/* 게시중인 글 */}
      <div className="flex flex-col gap-10">
        <span className="text-22 font-bold leading-28 text-teal-900">
          게시 중인 글
        </span>
        <div className="scrollbar-hidden flex h-[50rem] flex-col gap-10 overflow-y-auto">
          <BoardCard
            title="title"
            writer="writer"
            status="done"
            createdAt="00000"
            time="2"
            context="Jorem ipsum dolor sit amet, consectetur adipiscing elit. NuncJorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc 
"
          />
          <BoardCard
            title="title"
            writer="writer"
            status="done"
            createdAt="00000"
            time="2"
            context="Jorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc Jorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc 
"
          />
          <BoardCard
            title="title"
            writer="writer"
            status="done"
            createdAt="00000"
            time="2"
            context="djklfajdfljadlfajdflj"
          />
          <BoardCard
            title="title"
            writer="writer"
            status="done"
            createdAt="00000"
            time="2"
            context="djklfajdfljadlfajdflj"
          />
          <BoardCard
            title="title"
            writer="writer"
            status="done"
            createdAt="00000"
            time="2"
            context="djklfajdfljadlfajdflj"
          />
        </div>
      </div>

      <div className="flex flex-col gap-10">
        <span className="text-22 font-bold leading-28 text-teal-900">
          진행 중인 글
        </span>
        <div className="scrollbar-hidden flex h-[50rem] flex-col gap-10 overflow-y-auto">
          <BoardCard
            title="title"
            writer="writer"
            status="done"
            createdAt="00000"
            time="2"
            context="Jorem ipsum dolor sit amet, consectetur adipiscing elit. NuncJorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc 
"
          />
          <BoardCard
            title="title"
            writer="writer"
            status="done"
            createdAt="00000"
            time="2"
            context="Jorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc Jorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc 
"
          />
          <BoardCard
            title="title"
            writer="writer"
            status="done"
            createdAt="00000"
            time="2"
            context="djklfajdfljadlfajdflj"
          />
          <BoardCard
            title="title"
            writer="writer"
            status="done"
            createdAt="00000"
            time="2"
            context="djklfajdfljadlfajdflj"
          />
          <BoardCard
            title="title"
            writer="writer"
            status="done"
            createdAt="00000"
            time="2"
            context="djklfajdfljadlfajdflj"
          />
        </div>
      </div>

      <div className="flex flex-col gap-10">
        <span className="text-22 font-bold leading-28 text-teal-900">
          완료된 글
        </span>
        <div className="scrollbar-hidden flex h-[50rem] flex-col gap-10 overflow-y-auto">
          <BoardCard
            title="title"
            writer="writer"
            status="done"
            createdAt="00000"
            time="2"
            context="Jorem ipsum dolor sit amet, consectetur adipiscing elit. NuncJorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc 
"
          />
          <BoardCard
            title="title"
            writer="writer"
            status="done"
            createdAt="00000"
            time="2"
            context="Jorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc Jorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc 
"
          />
          <BoardCard
            title="title"
            writer="writer"
            status="done"
            createdAt="00000"
            time="2"
            context="djklfajdfljadlfajdflj"
          />
          <BoardCard
            title="title"
            writer="writer"
            status="done"
            createdAt="00000"
            time="2"
            context="djklfajdfljadlfajdflj"
          />
          <BoardCard
            title="title"
            writer="writer"
            status="done"
            createdAt="00000"
            time="2"
            context="djklfajdfljadlfajdflj"
          />
        </div>
      </div>
    </>
  );
}
