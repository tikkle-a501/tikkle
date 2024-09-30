const InfoBoxLoading: React.FC = () => {
  return (
    <div className="flex h-[793px] w-[298px] flex-col items-center gap-10 rounded-12 border border-warmGray200 px-20 py-40 text-20">
      <div className="h-[150px] w-[150px] rounded-round bg-warmGray200"></div>
      <div className="flex flex-col items-center justify-center gap-10 py-10">
        <div className="h-[30px] w-[150px] rounded-12 bg-warmGray100"></div>
        <div className="h-[30px] w-[213.250px] rounded-12 bg-warmGray100"></div>
      </div>

      <div className="flex items-center justify-center gap-10 p-10">
        <div className="h-[30px] w-[150px] rounded-12 bg-warmGray100"></div>
      </div>
      <div className="border-warmGray-200 flex w-full border-t"></div>
      <div className="flex w-full flex-col items-center gap-20 p-20">
        <div className="h-[30px] w-[150px] rounded-12 bg-warmGray100"></div>
        <div className="h-[30px] w-[150px] rounded-12 bg-warmGray100"></div>
        <div className="h-[30px] w-[150px] rounded-12 bg-warmGray100"></div>
      </div>
    </div>
  );
};

export default InfoBoxLoading;
