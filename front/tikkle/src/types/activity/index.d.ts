export interface ActivityGetResponse {
  boardId: string;
  title: string;
  content: string;
  status: string;
  time: number;
  createdAt: string;
}

export interface ActivityGetResponses {
  postedPosts: ActivityGetResponse[];
  activePosts: ActivityGetResponse[];
  donePosts: ActivityGetResponse[];
}
