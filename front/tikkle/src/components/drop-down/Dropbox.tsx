import DropboxBtn from "./DropboxBtn";

interface DropdownProps {
  items: string[]; // 드롭다운에 표시될 항목들
}

const Dropbox: React.FC<DropdownProps> = ({ items }) => {
  return (
    <div className="inline-flex flex-col p-6 gap-4 rounded-4 bg-white shadow-s">
      {items.map((item, index) => (
        <DropboxBtn key={index}>{item}</DropboxBtn>
      ))}
    </div>
  );
};

export default Dropbox;
