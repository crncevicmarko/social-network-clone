import { Component } from '@angular/core';
import { UserDTO } from '../model/userDTO';
import { AuthService } from '../service';
import { Router } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css'],
})
export class UsersComponent {
  public users!: UserDTO[];
  public logedInUser!: UserDTO;
  public searchTerm: string = '';

  constructor(private router: Router, private userService: AuthService) {
    this.logedInUser = userService.logedUser;
  }

  ngOnInit() {
    this.logedInUser = this.userService.logedUser;
    this.allUsers();
  }

  onSearch() {
    // Filter the users based on the searchTerm
    if (this.searchTerm) {
      this.userService.search(this.searchTerm).subscribe(
        (response: UserDTO[]) => {
          this.users = response;
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
      );
    } else {
      this.allUsers();
    }
  }

  public allUsers() {
    this.userService.getAllUsers().subscribe(
      (response: UserDTO[]) => {
        this.users = response;
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  blockUser(id: number) {
    alert('Korisnik je blokiran');
    this.userService.blockUser(id).subscribe(
      (response: void) => {
        alert(response);
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  unblockUser(id: number) {
    alert('Korisnik je odblokiran');
    this.userService.unBlockUser(id).subscribe(
      (response: void) => {
        alert(response);
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  sendFriendRequest(id: number) {
    this.userService.sendFriendRequest(id).subscribe(
      (response: void) => {
        alert('Uspesno ste poslali zahtev');
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  get IsUserAdmin(): boolean {
    console.log(this.userService.isLogedUserNameAdmin());
    return this.userService.isLogedUserNameAdmin();
  }
}
