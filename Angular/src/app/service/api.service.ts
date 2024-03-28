import { Injectable } from '@angular/core';
import {
  HttpClient,
  HttpHeaders,
  HttpRequest,
  HttpResponse,
  HttpParams,
} from '@angular/common/http';
import { Observable, catchError, filter, map } from 'rxjs';
import { ConfigService } from './config.service';
import { PostDTO } from '../model/postDTO';
import { LogedUserDTO } from '../model/logedUser';
import { UserDTO } from '../model/userDTO';

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
export class ApiService {
  headers = new HttpHeaders({
    Accept: 'application/json',
    'Content-Type': 'application/json',
  });

  constructor(private http: HttpClient, private config: ConfigService) {}

  public getPosts(): Observable<PostDTO[]> {
    return this.http.get<PostDTO[]>(`${this.config.posts_url}`);
  }

  public addPost(groupId: number, post: PostDTO): Observable<PostDTO> {
    return this.http.post<PostDTO>(`${this.config.posts_url}/${groupId}`, post);
  }

  public update(id: number, updatedPost: PostDTO): Observable<PostDTO> {
    return this.http.put<PostDTO>(
      `${this.config.updatePost_url}/${id}`,
      updatedPost
    );
  }

  public deletePost(id: number): Observable<void> {
    return this.http.delete<void>(`${this.config.delete_post}/${id}`);
  }

  public getPostsByDate(date: string): Observable<PostDTO[]> {
    return this.http.get<PostDTO[]>(
      `${this.config.find_posts_by_date}/${date}`
    );
  }

  public getPostsByNumberOfLikes(numberOfLikes: number): Observable<PostDTO[]> {
    return this.http.get<PostDTO[]>(
      `${this.config.find_posts_by_number_of_likes}/${numberOfLikes}`
    );
  }

  public getPostById(number: number): Observable<PostDTO> {
    return this.http.get<PostDTO>(`${this.config.posts_url}/${number}`);
  }

  // get(path: string, args?: any): Observable<any> {
  //   const options = {
  //     headers: this.headers,
  //   };

  //   if (args) {
  //     options['params'] = this.serialize(args);
  //   }

  //   return this.http
  //     .get(path, options)
  //     .pipe(catchError(this.checkError.bind(this)));
  // }

  // post(path: string, body: any, customHeaders?: HttpHeaders): Observable<any> {
  //   return this.request(path, body, RequestMethod.Post, customHeaders);
  // }

  // put(path: string, body: any): Observable<any> {
  //   return this.request(path, body, RequestMethod.Put);
  // }

  // delete(path: string, body?: any): Observable<any> {
  //   return this.request(path, body, RequestMethod.Delete);
  // }

  // private request(
  //   path: string,
  //   body: any,
  //   method = RequestMethod.Post,
  //   custemHeaders?: HttpHeaders
  // ): Observable<any> {
  //   const req = new HttpRequest(method, path, body, {
  //     headers: custemHeaders || this.headers,
  //   });

  //   return this.http
  //     .request(req)
  //     .pipe(filter((response) => response instanceof HttpResponse))
  //     .pipe(map((response) => response.body))
  //     .pipe(catchError((error) => this.checkError(error)));
  // }

  // private checkError(error: any): any {
  //   throw error;
  // }

  // private serialize(obj: any): HttpParams {
  //   let params = new HttpParams();

  //   for (const key in obj) {
  //     if (obj.hasOwnProperty(key) && !this.looseInvalid(obj[key])) {
  //       params = params.set(key, obj[key]);
  //     }
  //   }

  //   return params;
  // }

  // private looseInvalid(a: string | number): boolean {
  //   return a === '' || a === null || a === undefined;
  // }
}
