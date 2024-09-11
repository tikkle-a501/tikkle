import Badge from "@/components/badge/Badge";
import BoardCard from "@/components/card/BoardCard";
import Button from "@/components/button/Button";
import Dropbox from "@/components/drop-down/Dropbox";
import TitleInput from "@/components/input/TitleInput";
import SearchInput from "@/components/input/SearchInput";
import ChatList from "@/components/chat/ChatList";
import MessageList from "@/components/chat/MessageList";
import ReviewBadge from "@/components/badge/review/ReviewBadge";

export default function Landing() {
  const testCards = [
    {
      title: "Announcement",
      status: "Active",
      writer: "John Doe",
      createdAt: "2024.09.10 10:00",
      time: "10",
      context:
        "This is an important announcement regarding the upcoming event. Please read carefully. This is an important announcement regarding the upcoming event. Please read carefully.",
    },
    {
      title: "Maintenance Update",
      status: "Scheduled",
      writer: "Jane Smith",
      createdAt: "2024.09.09 13:00",
      time: "2.5",
      context:
        "There will be a scheduled maintenance on our servers next week. Expect some downtime.",
    },
    {
      title: "New Policy",
      status: "Draft",
      writer: "Admin",
      createdAt: "2024.09.08 17:30",
      time: "11",
      context:
        "A new policy document has been drafted and is open for review. Please provide your feedback.",
    },
  ];

  const testChats = [
    {
      content: "Hello, this is a test message from user12!",
      createdAt: "2024-09-11T12:34:56Z",
      writerId: "user12",
    },
    {
      content:
        "Hi, this is another message from a different user! Hi, this is another message from a different user! Hi, this is another message from a different user! Hi, this is another message from a different user!",
      createdAt: "2024-09-11T13:45:30Z",
      writerId: "user34",
    },
  ];

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
          title={card.title}
          status={card.status}
          writer={card.writer}
          createdAt={card.createdAt}
          time={card.time}
          context={card.context}
        />
      ))}

      <Dropbox items={["Option 1", "Option 2", "Option 3"]} />

      <div>
        <TitleInput width="300px" placeholder="제목을 입력하세요." />
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

      {testChats.map((chat, index) => (
        <ChatList
          key={index}
          content={chat.content}
          createdAt={chat.createdAt}
          writerId={chat.writerId}
        />
      ))}

      <ReviewBadge type="time" />
    </div>
  );
}
