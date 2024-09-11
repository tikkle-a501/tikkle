import Chips from "@/components/chips/Chips";

export default function Landing() {
  return (
    <div className="flex">
      <Chips variant="primary" design="fill" size="m">
        Chips
      </Chips>
      <Chips variant="primary" design="outline" size="m">
        Chips
      </Chips>
      <Chips variant="secondary" design="fill" size="m">
        Chips
      </Chips>
    </div>
  );
}
