import Badge from "@/components/badge/Badge";
import BoardCard from "@/components/card/BoardCard";
import Button from "@/components/button/Button";
import LoginButton from "@/components/button/LoginButton";

import Dropbox from "@/components/drop-down/Dropbox";
import TitleInput from "@/components/input/TitleInput";
import SearchInput from "@/components/input/SearchInput";
import ReviewBadge from "@/components/badge/review/ReviewBadge";
import TodoList from "@/components/list/TodoList";
import HistoryList from "@/components/list/HistoryList";

export default function Landing() {
  const testCards = [
    {
      title: "Announcement",
      status: "Active",
      writer: "John Doe",
      createdAt: "2024.09.10 10:00",
      time: 10,
      context:
        "This is an important announcement regarding the upcoming event. Please read carefully. This is an important announcement regarding the upcoming event. Please read carefully.",
    },
    {
      title: "Maintenance Update",
      status: "Scheduled",
      writer: "Jane Smith",
      createdAt: "2024.09.09 13:00",
      time: 11,
      context:
        "There will be a scheduled maintenance on our servers next week. Expect some downtime.",
    },
    {
      title: "New Policy",
      status: "Draft",
      writer: "Admin",
      createdAt: "2024.09.08 17:30",
      time: 11,
      context:
        "A new policy document has been drafted and is open for review. Please provide your feedback.",
    },
  ];

  const testHistoryList = {
    profileImg: "/profile.png",
    nickname: "JohnDoe",
    status: "진행전",
    title: "미팅 일정",
    appointmentTime: "2024-09-15 14:00",
    boardId: "1234",
    time: 2,
    buttonText: "거래 완료하기",
  };

  return (
    <div className="space-y-4 p-8">
      <Badge size="l" color="teal">
        Teal Large
      </Badge>
      <Badge size="m" color="red">
        Red Medium
      </Badge>
      <Badge size="s" color="yellow">
        Yellow Small
      </Badge>
      <Badge size="l" color="gray">
        Gray Large
      </Badge>
      <LoginButton title="Mattermost"></LoginButton>
      <LoginButton title="Slack"></LoginButton>

      <Button
        size="l"
        variant="primary"
        design="fill"
        left="Left Text"
        main="Main Text"
        right="Right Text"
      >
        <span className="material-symbols-outlined">search</span>
      </Button>

      <Button size="m" variant="primary" design="outline" main="Main Only" />

      <Button
        size="s"
        variant="secondary"
        design="fill"
        main="Secondary Button"
      />

      <Button
        size="l"
        variant="tertiary"
        design="fill"
        main="Tertiary Button"
      />

      <Button
        size="m"
        variant="primary"
        design="fill"
        left="Left Text Only"
        main="Main"
      />

      <Button
        size="s"
        variant="secondary"
        design="fill"
        main="Main"
        right="Right Text Only"
      />

      <Button
        size="m"
        variant="primary"
        design="fill"
        main="Disabled Button"
        disabled
      />

      {testCards.map((card, index) => (
        <BoardCard
          key={index}
          boardId={"1"}
          title={card.title}
          status={card.status}
          writer={card.writer}
          createdAt={card.createdAt}
          time={card.time}
          content={card.context}
        />
      ))}

      <div>
        <TitleInput placeholder="제목을 입력하세요." />
      </div>

      <div>
        <SearchInput
          width="500px"
          placeholder="placeholder"
          label="label"
          leftIcon
          rightIcon
          warningMessage="그건 아니지"
        />
      </div>

      <ReviewBadge type="time" />

      <div className="flex flex-col p-4">
        <TodoList
          status="완료"
          appointmentTime="2024-09-11T12:00:00Z"
          nickname="John Doe"
          title="회의 준비"
          chatId="123"
        />
        <TodoList
          status="대기 중"
          appointmentTime="2024-09-12T15:30:00Z"
          nickname="Jane Smith"
          title="보고서 제출"
          chatId="456"
        />
        <TodoList
          status="취소됨"
          appointmentTime="2024-09-13T18:00:00Z"
          nickname="Alice Brown"
          title="프로젝트 검토"
          chatId="789"
        />
      </div>

      <HistoryList {...testHistoryList} />
    </div>
  );
}
