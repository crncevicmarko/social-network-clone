import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ConfigService } from './config.service';
import { Observable, Subject } from 'rxjs';
import { CommentDTO } from '../model/commentDTO';

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
export class CommentsService {
  headers = new HttpHeaders({
    Accept: 'application/json',
    'Content-Type': 'application/json',
  });

  constructor(private http: HttpClient, private config: ConfigService) {}

  public getAllComments(): Observable<CommentDTO[]> {
    return this.http.get<CommentDTO[]>(`${this.config.get_comments}`);
  }

  public addComment(
    text: string,
    postId: string,
    parentId: null | string
  ): Observable<CommentDTO> {
    console.log('ID: ' + postId);
    return this.http.post<CommentDTO>(`${this.config.addComment}`, {
      text: text,
      parent_Id: parentId,
      post_id: parseInt(postId),
    });
  }

  public updateComment(
    text: string,
    commentId: number
  ): Observable<CommentDTO> {
    return this.http.patch<CommentDTO>(
      `${this.config.updateComment}/${commentId}`,
      {
        text: text,
        // parentId: commentId /*!!!!!!!!!!!!!!!!!*/,
      }
    );
  }

  public deleteComment(commentId: number): Observable<{}> {
    return this.http.delete(`${this.config.deleteComment}/${commentId}`);
  }

  public sortComments(
    turn: number,
    datumOd: string,
    datumDo: string
  ): Observable<CommentDTO[]> {
    return this.http.get<CommentDTO[]>(
      `${this.config.sort_comments}/${turn}/${datumOd}/${datumDo}`
    );
  }

  private sortCommentsSource = new Subject<{
    order: number;
    start_date: string;
    end_date: string;
  }>();
  sortComments$ = this.sortCommentsSource.asObservable();

  sortComments1(order: number, start_date: string, end_date: string) {
    this.sortCommentsSource.next({ order, start_date, end_date });
  }
}
