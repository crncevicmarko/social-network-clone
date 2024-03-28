export interface GroupRequestDTO {
  id: number;
  approved: boolean;
  createdAt: string;
  requestAcceptedOrDeniedAt: string;
  user_id: number;
  group_id: number;
}
