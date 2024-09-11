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
      className="appearance-none inline-flex justify-center items-center pb-5 text-28 font-bold border-b border-warmGray200 placeholder-warmGray300 focus:outline-none"
    />
  );
};

export default TitleInput;
