export interface GroupDTO {
  id: number;
  name: string;
  description: string;
  suspended: boolean;
  suspendedReason: string;
  user_id: number;
}
