import React from "react";

interface ReviewBadgeProps {
  type: "time" | "accuracy" | "workload" | "kindness" | "fastReply";
}

const ReviewBadge: React.FC<ReviewBadgeProps> = ({ type }) => {
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

  return (
    <div className="inline-flex items-center bg-baseWhite border border-coolGray200 rounded-round px-6 py-4 m-2 text-12 font-medium text-gray-700">
      <span>{emoji}</span>
      <span className="ml-2">{text}</span>
    </div>
  );
};

export default ReviewBadge;
