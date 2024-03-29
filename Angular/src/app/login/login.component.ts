import { Component, EventEmitter, Output } from '@angular/core';
import { LogedUserDTO } from '../model/logedUser';
import { AuthService } from '../service';
import { NgForm } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { UserDTO } from '../model/userDTO';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  public token = '';
  public logedUserName = '';
  loggedUser!: LogedUserDTO;
  // @Output() logedUser = new EventEmitter<UserDTO>();
  public logedUser!: UserDTO;

  constructor(
    private autentificationService: AuthService,
    private router: Router
  ) {}

  public login(loginFrom: NgForm) {
    this.autentificationService.login(loginFrom.value).subscribe(
      (response: LogedUserDTO) => {
        console.log(response.role);
        this.loggedUser = response;
        this.token = this.loggedUser.accessToken;
        this.autentificationService.access_token = this.token;
        this.autentificationService.role = response.role;
        this.getUserByUserName(loginFrom.value.username);
        alert('Uspesno ste se ulogovali');
        this.router.navigate(['/posts']);
      },
      (error: HttpErrorResponse) => {
        if (error.error instanceof ProgressEvent) {
          // Handle network errors
          alert('Network error occurred. Please try again later.');
        } else if (error.status === 403) {
          // Handle 403 (Forbidden) response from server
          alert('User is blocked. Please wait to be unblocked.');
        } else {
          // Handle other errors (e.g., 500 Internal Server Error)
          alert('An error occurred. Please try again later.');
        }
      }
    );
  }

  public getUserByUserName(userName: string) {
    this.autentificationService.getUsetByUserName(userName).subscribe(
      (respnse: UserDTO) => {
        this.logedUser = respnse;
        this.autentificationService.logedUser = this.logedUser;
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }
}
