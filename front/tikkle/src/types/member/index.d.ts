// 마이페이지
export interface MypageMemberResponse {
  id: string;
  name: string;
  nickname: string;
  email: string;
  image: string;
}

export interface MypageMemberResponses {
  data: MypageMemberResponse[];
}
