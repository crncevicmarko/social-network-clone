import { Component } from '@angular/core';
import { AuthService } from '../service/auth.service';
import { NgFor } from '@angular/common';
import { NgForm } from '@angular/forms';
import { UserDTO } from '../model/userDTO';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css'],
})
export class SignUpComponent {
  private newUser!: UserDTO;
  constructor(private signUpService: AuthService, private router: Router) {}
  reloadPage() {
    this.router.navigate([this.router.url]);
  }
  public signInForm(signIn: NgForm) {
    this.signUpService.signUp(signIn.value).subscribe(
      (result: UserDTO) => {
        this.newUser = result;
        this.router.navigate(['/login']);
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }
}
