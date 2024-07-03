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

  public searchGroups(searchData: any): Observable<any> {
    console.log('Search Data: ', searchData);
    const formData = new FormData();
    formData.append('name', searchData.name);
    formData.append('description', searchData.description);
    formData.append('rules', searchData.rules);
    if (
      searchData.likeRandeLower == '' ||
      searchData.likeRandeUpper == '' ||
      searchData.likeRandeLower == null ||
      searchData.likeRandeUpper == null
    ) {
      formData.append('likeRange', '');
    } else {
      formData.append(
        'likeRange',
        searchData.likeRandeLower + ':' + searchData.likeRandeUpper
      );
    }
    if (
      searchData.postRangeLower == '' ||
      searchData.postRangeUpper == '' ||
      searchData.postRangeLower == null ||
      searchData.postRangeUpper == null
    ) {
      formData.append('postRange', '');
    } else {
      formData.append(
        'postRange',
        searchData.postRangeLower + ':' + searchData.postRangeUpper
      );
    }
    formData.append('operation', searchData.operation);
    return this.http.post<any>(
      `${this.config.groups_ulr}/search/advanced`,
      formData
    );
  }

  public getGroupById(id: number): Observable<GroupDTO> {
    return this.http.get<GroupDTO>(`${this.config.groups_ulr}/${id}`);
  }

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

  public createGroup(group: any, file: File): Observable<GroupDTO> {
    const formData: FormData = new FormData();
    formData.append('group', JSON.stringify(group)); // Convert group object to JSON string
    formData.append('file', file, file.name);
    console.log('Form: ', formData);
    return this.http.post<GroupDTO>(this.config.groups_ulr, formData);
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

  public getAllSent(): Observable<any> {
    return this.http.get<any>(
      `${'http://localhost:8080/groupRequests/allSent'}`,
      {}
    );
  }
}
