import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ConfigService } from './config.service';
import { Observable } from 'rxjs';
import { GroupDTO } from '../model/groupDTO';
import { GroupRequestDTO } from '../model/groupRequestDTO';

@Injectable({
  providedIn: 'root',
})
export class GroupService {
  constructor(private http: HttpClient, private config: ConfigService) {}

  public getGroups(): Observable<GroupDTO[]> {
    return this.http.get<GroupDTO[]>(this.config.groups_ulr);
  }

  public getGroupRequests(): Observable<GroupRequestDTO[]> {
    return this.http.get<GroupRequestDTO[]>(this.config.get_group_requests);
  }

  public getGroupsByUserId(): Observable<GroupDTO[]> {
    return this.http.get<GroupDTO[]>(`${this.config.get_groups_by_user_id}`);
  }
  public updateGroupRequests(id: number, value: boolean): Observable<void> {
    return this.http.put<void>(
      `${this.config.update_group_request}/${id}?value=${value}`,
      {}
    );
  }

  public createGroup(group: GroupDTO): Observable<GroupDTO> {
    return this.http.post<GroupDTO>(this.config.groups_ulr, group);
  }

  public update(id: number, group: GroupDTO): Observable<void> {
    return this.http.put<void>(`${this.config.updateGroup_url}/${id}`, group);
  }

  public delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.config.delete_group}/${id}`);
  }

  public joinGroup(id: number): Observable<void> {
    return this.http.post<void>(`${this.config.group_request}/${id}`, {});
  }
}
