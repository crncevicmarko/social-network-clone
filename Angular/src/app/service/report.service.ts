import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ConfigService } from './config.service';
import { Observable } from 'rxjs';
import { CommentDTO } from '../model/commentDTO';
import { PostDTO } from '../model/postDTO';
import { ReportDTO } from '../model/reportDTO';

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
export class ReportService {
  headers = new HttpHeaders({
    Accept: 'application/json',
    'Content-Type': 'application/json',
  });

  constructor(private http: HttpClient, private config: ConfigService) {}

  public getAll(): Observable<CommentDTO[]> {
    return this.http.get<CommentDTO[]>(
      `${this.config.get_comments}`
    ); /*ne znam za sta mi je nemoj brisati jos*/
  }

  public getAllReports(): Observable<ReportDTO[]> {
    return this.http.get<ReportDTO[]>(`${this.config.report_url}`);
  }

  public add(report: ReportDTO): Observable<ReportDTO> {
    if (report.comment_id == 0) {
      return this.http.post<ReportDTO>(
        `${this.config.report_url}/${0}/${report.post_id}`,
        report
      );
    } else {
      return this.http.post<ReportDTO>(
        `${this.config.report_url}/${report.comment_id}/${report.post_id}`,
        report
      );
    }
  }

  public update(text: string, commentId: number): Observable<CommentDTO> {
    return this.http.patch<CommentDTO>(
      `${this.config.updateComment}/${commentId}`,
      {
        text: text,
        // parentId: commentId /*!!!!!!!!!!!!!!!!!*/,
      }
    );
  }

  public delete(commentId: number): Observable<{}> {
    return this.http.delete(`${this.config.deleteComment}/${commentId}`);
  }
}
