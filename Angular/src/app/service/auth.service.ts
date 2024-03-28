import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { UserService } from './user.service';
import { ConfigService } from './config.service';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { LogedUserDTO } from '../model/logedUser';
import { UserDTO } from '../model/userDTO';
import { FriendRequestDTO } from '../model/friendRequestDTO';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(
    private apiService: ApiService,
    private userService: UserService,
    private config: ConfigService,
    private http: HttpClient,
    private router: Router
  ) {}

  public access_token = '';
  public logedUser!: UserDTO;
  public role = '';

  public login(logedUser: UserDTO): Observable<LogedUserDTO> {
    return this.http.post<LogedUserDTO>(this.config.login_url, logedUser);
  }

  public signUp(logedUser: UserDTO): Observable<UserDTO> {
    return this.http.post<UserDTO>(this.config.signup_url, logedUser);
  }

  public getAllUsers(): Observable<UserDTO[]> {
    return this.http.get<UserDTO[]>(`${this.config.users_url}`);
  }

  public blockUser(id: number): Observable<void> {
    return this.http.post<void>(`${this.config.block_user_url}/${id}`, {});
  }

  public unBlockUser(id: number): Observable<void> {
    return this.http.post<void>(`${this.config.unBlock_user_url}/${id}`, {});
  }

  public getUsetById(id: number): Observable<UserDTO> {
    return this.http.get<UserDTO>(`${this.config.user_url}/${id}`);
  }

  public search(username: string): Observable<UserDTO[]> {
    return this.http.get<UserDTO[]>(
      `${this.config.search}?username=${username}`
    );
  }

  public getUsetByUserName(userName: string): Observable<UserDTO> {
    return this.http.get<UserDTO>(`${this.config.user_name_url}/${userName}`);
  }

  public update(user: UserDTO): Observable<UserDTO> {
    console.log(user.id);
    return this.http.patch<UserDTO>(
      `${this.config.update_user}/${user.id}`,
      user
    );
  }

  public findAllFriendRequests(): Observable<FriendRequestDTO[]> {
    return this.http.get<FriendRequestDTO[]>(
      `${this.config.send_friend_request}`
    );
  }

  public findAllFriend(): Observable<FriendRequestDTO[]> {
    return this.http.get<FriendRequestDTO[]>(
      `${this.config.find_friend_requests}`
    );
  }

  public sendFriendRequest(id: number): Observable<void> {
    return this.http.post<void>(`${this.config.send_friend_request}/${id}`, {});
  }

  public acceptFriendRequest(id: number, approved: boolean): Observable<void> {
    return this.http.put<void>(
      `${this.config.send_friend_request}/${id}/${approved}`,
      {}
    );
  }

  public declineFriendRequest(id: number, approved: boolean): Observable<void> {
    return this.http.put<void>(
      `${this.config.send_friend_request}/${id}/${approved}`,
      {}
    );
  }

  private isAdmin: boolean = false; // Default value, change based on your logic

  setUserAdminStatus(isAdmin: boolean) {
    this.isAdmin = isAdmin;
  }

  isAdminUser(): boolean {
    return this.isAdmin;
  }

  // login(user: any) {
  //   const loginHeaders = new HttpHeaders({
  //     Accept: 'application/json',
  //     'Content-Type': 'application/json',
  //   });
  //   // const body = `username=${user.username}&password=${user.password}`;
  //   const body = {
  //     username: user.username,
  //     password: user.password,
  //   };
  //   return this.apiService
  //     .post(this.config.login_url, JSON.stringify(body), loginHeaders)
  //     .pipe(
  //       map((res) => {
  //         console.log('Login success');
  //         this.access_token = res.accessToken;
  //         localStorage.setItem('jwt', res.accessToken);
  //       })
  //     );
  // }

  // signup(user: any) {
  //   const signupHeaders = new HttpHeaders({
  //     Accept: 'application/json',
  //     'Content-Type': 'application/json',
  //   });
  //   return this.apiService
  //     .post(this.config.signup_url, JSON.stringify(user), signupHeaders)
  //     .pipe(
  //       map(() => {
  //         console.log('Sign up success');
  //       })
  //     );
  // }

  public logout() {
    this.userService.currentUser = null;
    this.access_token = '';
    this.router.navigate(['/login']);
  }

  // tokenIsPresent() {
  //   return this.access_token != undefined && this.access_token != null;
  // }

  tokenIsPresent() {
    return this.access_token != '';
  }

  getToken() {
    return this.access_token;
  }

  isLogedUserNameAdmin(): boolean {
    if (this.role === 'ADMIN') {
      return true;
    } else {
      return false;
    }
  }
}
