import { Component, OnInit } from '@angular/core';
import { PostDTO } from './model/postDTO';
import {
  HttpClient,
  HttpClientModule,
  HttpErrorResponse,
  HttpResponse,
} from '@angular/common/http';
import { ApiService, AuthService } from './service';
import { GroupService } from './service/group.service';
import { GroupDTO } from './model/groupDTO';
import { NgForm } from '@angular/forms';
import { LogedUserDTO } from './model/logedUser';
import { UserDTO } from './model/userDTO';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent {
  public logedInUser!: UserDTO;
  constructor(private logOutService: AuthService, private router: Router) {}
  title = 'Angular';

  ngOnInit() {
    this.logedInUser = this.logOutService.logedUser;
  }
  public logout() {
    this.logOutService.logout();
    this.router.navigate(['/posts']);
  }

  get isLoggedIn(): boolean {
    return this.logOutService.tokenIsPresent();
  }
  get IsUserAdmin(): boolean {
    console.log(this.logOutService.isLogedUserNameAdmin());
    return this.logOutService.isLogedUserNameAdmin();
  }
  // userDto!: UserDTO;

  // constructor(private auteantificationService: AuthService) {}

  // onSignUpPost(signupform: NgForm) {
  //   this.autentificationService.signUp(signupform.value).subscribe(
  //     (response: UserDTO) => {
  //       this.userDto = response;
  //     },
  //     (error: HttpErrorResponse) => {
  //       alert(error.message);
  //     }
  //   );
  // }
}
