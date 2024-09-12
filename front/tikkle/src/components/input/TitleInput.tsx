interface TitleInputProps {
  width: string;
  placeholder?: string;
}

const TitleInput: React.FC<TitleInputProps> = ({ width, placeholder }) => {
  return (
    <input
      type="text"
      placeholder={placeholder}
      style={{ width }}
      className="inline-flex appearance-none items-center justify-center border-b border-warmGray200 pb-5 text-28 font-bold placeholder-warmGray300 focus:outline-none"
    />
  );
};

export default TitleInput;
