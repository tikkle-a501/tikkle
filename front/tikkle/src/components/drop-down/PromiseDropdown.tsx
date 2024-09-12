import Button from "../button/Button";
import PromiseInput from "../input/PromiseInput";

const PromiseDropdown: React.FC = () => {
  return (
    <div className="flex h-[260px] w-[230px] flex-col justify-center gap-10 rounded-12 bg-white p-20 text-15 text-teal900 shadow-s">
      {/* 타이틀 */}
      <div className="flex text-24 font-bold text-teal900">약속 잡기</div>

      {/* 시간(재화) 입력 */}
      <div className="flex min-w-0 items-center justify-end gap-10 self-stretch px-0 py-10">
        <PromiseInput autoFocus />
        <div className="whitespace-nowrap">시간</div>
      </div>

      {/* 날짜 입력 */}
      <div className="flex appearance-none items-center justify-end gap-8 self-stretch border-none px-0 py-10 outline-none">
        <PromiseInput />
        <div>월</div>
        <PromiseInput />
        <div>일</div>
        <PromiseInput />
        <div>시</div>
        <PromiseInput />
        <div>분</div>
      </div>

      {/* 버튼 */}
      <Button size="s" variant="primary" design="fill" main="확인"></Button>
    </div>
  );
};

export default PromiseDropdown;
