import { FC, InputHTMLAttributes } from "react";

interface PromiseInputProps extends InputHTMLAttributes<HTMLInputElement> {}

const PromiseInput: FC<PromiseInputProps> = ({ ...props }) => {
  return (
    <input
      className="min-w-0 flex-grow border-none text-right text-17 font-semibold placeholder-coolGray300 outline-none placeholder:text-18 placeholder:font-semibold"
      placeholder="0"
      type="number"
      {...props}
    />
  );
};

export default PromiseInput;
