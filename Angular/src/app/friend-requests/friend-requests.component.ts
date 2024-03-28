import { Component } from '@angular/core';
import { AuthService } from '../service';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { UserDTO } from '../model/userDTO';
import { FriendRequestDTO } from '../model/friendRequestDTO';

@Component({
  selector: 'app-friend-requests',
  templateUrl: './friend-requests.component.html',
  styleUrls: ['./friend-requests.component.css'],
})
export class FriendRequestsComponent {
  public users!: FriendRequestDTO[];
  public friends!: FriendRequestDTO[];

  constructor(
    private router: Router,
    private friendRequestService: AuthService
  ) {}

  ngOnInit() {
    this.allFriedRequests();
    this.findAllFriends();
  }

  reloadPage() {
    this.router.navigate([this.router.url]).then(() => {
      location.reload();
    });
  }

  public allFriedRequests() {
    this.friendRequestService.findAllFriendRequests().subscribe(
      (response: FriendRequestDTO[]) => {
        this.users = response;
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  public findAllFriends() {
    this.friendRequestService.findAllFriend().subscribe(
      (response: FriendRequestDTO[]) => {
        this.friends = response;
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  button1(id: number) {
    this.friendRequestService.acceptFriendRequest(id, true).subscribe(
      (response: void) => {
        alert('Uspesno ste prihvatili prijateljstvo');
        this.ngOnInit();
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  decline(id: number) {
    this.friendRequestService.declineFriendRequest(id, false).subscribe(
      (response: void) => {
        alert('Uspesno ste odbili prijateljstvo');
        this.ngOnInit();
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }
}
