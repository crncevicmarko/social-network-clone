import { Component } from '@angular/core';
import { AuthService } from '../service';
import { Router } from '@angular/router';
import { UserDTO } from '../model/userDTO';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
})
export class HeaderComponent {
  public logedInUser!: UserDTO;
  constructor(private logOutService: AuthService, private router: Router) {}
  ngOnInit() {
    this.logedInUser = this.logOutService.logedUser;
  }

  public logout() {
    this.logOutService.logout();
    this.router.navigate(['/login']);
  }

  get isLoggedIn(): boolean {
    return this.logOutService.tokenIsPresent();
  }

  get IsUserAdmin(): boolean {
    // console.log(this.logOutService.isLogedUserNameAdmin());
    return this.logOutService.isLogedUserNameAdmin();
  }
}
