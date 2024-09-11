import { FC, InputHTMLAttributes } from "react";

interface PromiseInputProps extends InputHTMLAttributes<HTMLInputElement> {}

const PromiseInput: FC<PromiseInputProps> = ({ ...props }) => {
  return (
    <input
      className="appearance-none border-none outline-none flex-grow min-w-0 text-18 font-semibold placeholder-coolGray300 placeholder:text-18 placeholder:font-semibold text-right"
      placeholder="0"
      {...props}
    />
  );
};

export default PromiseInput;
