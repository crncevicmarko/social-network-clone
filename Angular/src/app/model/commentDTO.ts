export interface CommentDTO {
  id: number;
  text: string;
  timeStamp: string;
  isDeleted: boolean;
  user_id: number;
  post_id: number;
  parentId: string;
  userName: string;
}
