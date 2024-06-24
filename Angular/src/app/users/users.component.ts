import { Component } from '@angular/core';
import { UserDTO } from '../model/userDTO';
import { AuthService } from '../service';
import { Router } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { GroupService } from '../service/group.service';
import { GroupRequestDTO } from '../model/groupRequestDTO';
import { FriendRequestDTO } from '../model/friendRequestDTO';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css'],
})
export class UsersComponent {
  public users!: UserDTO[];
  public logedInUser!: UserDTO;
  public searchTerm: string = '';
  public requests!: FriendRequestDTO[];

  constructor(private router: Router, private userService: AuthService) {
    this.logedInUser = userService.logedUser;
  }

  ngOnInit() {
    this.logedInUser = this.userService.logedUser;
    this.allUsers();
    this.allSentRequests();
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
  public allSentRequests() {
    // all sent requests for logged user and one that he cant send again (approved ones and null ones that are not approved still)
    this.userService.getAllSent().subscribe(
      (response: FriendRequestDTO[]) => {
        this.requests = response;
        console.log('All that cant be sent again: ', this.requests);
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  public IsButtonSeen(id: number): boolean {
    return this.requests.some((request) => request.user_id === id);
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
        this.ngOnInit();
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  get IsUserAdmin(): boolean {
    // console.log(this.userService.isLogedUserNameAdmin());
    return this.userService.isLogedUserNameAdmin();
  }
}
