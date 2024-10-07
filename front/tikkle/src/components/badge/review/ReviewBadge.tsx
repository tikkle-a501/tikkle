import React, { useState } from "react";

interface ReviewBadgeProps {
  type: "time" | "accuracy" | "workload" | "kindness" | "fastReply";
  onClick: (type: string, content: string) => void; // type과 content를 부모로 전달
}

const ReviewBadge: React.FC<ReviewBadgeProps> = ({ type, onClick }) => {
  const [isSelected, setIsSelected] = useState(false); // 배지의 선택 상태 관리

  let emoji = "";
  let text = "";

  switch (type) {
    case "time":
      emoji = "⏰";
      text = "시간을 잘 지켜요";
      break;
    case "accuracy":
      emoji = "✏️";
      text = "일처리가 정확해요";
      break;
    case "workload":
      emoji = "👌";
      text = "업무량이 적절해요";
      break;
    case "kindness":
      emoji = "😊";
      text = "친절해요";
      break;
    case "fastReply":
      emoji = "✉️";
      text = "답장이 빨라요";
      break;
    default:
      emoji = "";
      text = "-";
  }

  const handleClick = () => {
    setIsSelected(!isSelected);
    onClick(type, text); // type과 해당 텍스트(content)를 함께 부모로 전달
  };

  return (
    <div
      onClick={handleClick}
      className={`rm-2 flex cursor-pointer items-center justify-center rounded-round border border-coolGray200 px-6 py-4 text-12 font-medium text-gray-700 ${
        isSelected ? "bg-warmGray100" : "bg-baseWhite"
      }`}
    >
      <span>{emoji}</span>
      <span className="ml-2">{text}</span>
    </div>
  );
};

export default ReviewBadge;
