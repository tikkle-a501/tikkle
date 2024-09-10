import Button from "../button/Button";

const PromiseDropdown: React.FC = () => {
  return (
    <div className="flex flex-col w-[230px] h-[260px] p-20 justify-center gap-10 rounded-12 bg-white shadow-s text-teal900 text-15">
      {/* 타이틀 */}
      <div className="flex text-teal900 text-24 font-bold">약속 잡기</div>

      {/* 시간(재화) 입력 */}
      <div className="flex justify-end items-center gap-10 self-stretch px-0 py-10 min-w-0">
        <input
          className="flex-grow min-w-0 text-18 font-semibold placeholder-coolGray300 placeholder:text-18 placeholder:font-semibold text-right"
          placeholder="0"
          autoFocus
        />
        <div className="whitespace-nowrap">시간</div>
      </div>

      {/* 날짜 입력 */}
      <div className="flex justify-end items-center gap-8 self-stretch px-0 py-10">
        <input
          className="flex-grow min-w-0 text-18 font-semibold placeholder-coolGray300 placeholder:text-18 placeholder:font-semibold text-right"
          placeholder="0"
        />
        <div>월</div>
        <input
          className="flex-grow min-w-0 text-18 font-semibold placeholder-coolGray300 placeholder:text-18 placeholder:font-semibold text-right"
          placeholder="0"
        />
        <div>일</div>
        <input
          className="flex-grow min-w-0 text-18 font-semibold placeholder-coolGray300 placeholder:text-18 placeholder:font-semibold text-right"
          placeholder="0"
        />
        <div>시</div>
        <input
          className="flex-grow min-w-0 text-18 font-semibold placeholder-coolGray300 placeholder:text-18 placeholder:font-semibold text-right"
          placeholder="0"
        />
        <div>분</div>
      </div>

      {/* 버튼 */}
      <Button size="s" variant="primary" design="fill" main="확인"></Button>
    </div>
  );
};

export default PromiseDropdown;
