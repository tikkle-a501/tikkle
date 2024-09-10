import Badge from "@/components/badge/Badge";

export default function Landing() {
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
    </div>
  );
}
