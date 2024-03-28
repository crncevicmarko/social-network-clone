import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ConfigService } from './config.service';
import { Observable } from 'rxjs';
import { CommentDTO } from '../model/commentDTO';
import { PostDTO } from '../model/postDTO';
import { ReportDTO } from '../model/reportDTO';
import { ReactionDTO } from '../model/reactionDTO';

export enum RequestMethod {
  Get = 'GET',
  Head = 'HEAD',
  Post = 'POST',
  Put = 'PUT',
  Delete = 'DELETE',
  Options = 'OPTIONS',
  Patch = 'PATCH',
}

@Injectable({
  providedIn: 'root',
})
export class ReactionService {
  headers = new HttpHeaders({
    Accept: 'application/json',
    'Content-Type': 'application/json',
  });

  constructor(private http: HttpClient, private config: ConfigService) {}
  private baseUrl = 'http://localhost:8080/reactions';
  private baseUrl1 = 'http://localhost:8080/reactions/post';
  private baseUrl2 = 'http://localhost:8080/reactions/user';
  public save(
    commentId: number,
    postId: number,
    reaction: ReactionDTO
  ): Observable<ReactionDTO> {
    return this.http.put<ReactionDTO>(
      `${this.baseUrl}/${commentId}/${postId}`,
      reaction
    );
  }

  public getByUserId(userId: number): Observable<ReactionDTO[]> {
    return this.http.get<ReactionDTO[]>(`${this.baseUrl2}/${userId}`);
  }

  public getByPostId(postId: number): Observable<ReactionDTO[]> {
    return this.http.get<ReactionDTO[]>(`${this.baseUrl1}/${postId}`);
  }
}
