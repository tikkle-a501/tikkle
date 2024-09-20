interface TitleInputProps {
  value?: string;
  placeholder?: string;
  onChange?: () => void;
}

const TitleInput: React.FC<TitleInputProps> = ({
  value,
  placeholder,
  onChange,
}) => {
  return (
    <input
      type="text"
      value={value}
      placeholder={placeholder}
      className="inline-flex flex-1 appearance-none items-center justify-center border-b border-warmGray200 pb-5 text-28 font-bold placeholder-warmGray300 focus:outline-none"
    />
  );
};

export default TitleInput;
