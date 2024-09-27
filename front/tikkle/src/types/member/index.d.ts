// 마이페이지
export interface MypageMemberResponse {
  id: string;
  name: string;
  nickname: string;
  email: string;
}

export interface MypageMemberResponses {
  datat: MypageMemberResponse[];
}
