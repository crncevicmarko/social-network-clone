export interface FriendRequestDTO {
  id: number;
  approved: boolean;
  createdAt: string;
  requestAcceptedOrDeniedAt: string;
  friend_id: number;
  user_id: number;
}
