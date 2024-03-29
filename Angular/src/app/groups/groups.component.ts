import { Component } from '@angular/core';
import { ApiService, AuthService } from '../service';
import { GroupService } from '../service/group.service';
import { GroupDTO } from '../model/groupDTO';
import { HttpErrorResponse } from '@angular/common/http';
import { NgForm } from '@angular/forms';
import { Route, Router } from '@angular/router';
import { UserDTO } from '../model/userDTO';
import { LoginComponent } from '../login/login.component';

@Component({
  selector: 'app-groups',
  templateUrl: './groups.component.html',
  styleUrls: ['./groups.component.css'],
})
export class GroupsComponent {
  public groups!: GroupDTO[];
  public group!: GroupDTO;
  public desription!: string;
  public activeGroups!: GroupDTO[];
  public logedInUser!: UserDTO;

  constructor(
    private groupService: GroupService,
    private router: Router,
    private userService: AuthService
  ) {
    this.activeGroups = [];
    this.logedInUser = userService.logedUser;
  }

  reloadPage() {
    this.router.navigate([this.router.url]);
  }
  public addGroup(group: NgForm) {
    this.groupService.createGroup(group.value).subscribe(
      (response: GroupDTO) => {
        alert('Uspesno ste kreirali grupu');
        this.group = response;
        this.reloadPage;
        group.resetForm();
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
        group.resetForm();
      }
    );
  }

  public allGroups() {
    this.activeGroups = [];
    this.groupService.getGroups().subscribe(
      (response: GroupDTO[]) => {
        for (var val of response) {
          console.log('IsSuspended:', val.suspended);
          if (val.suspended == true) {
            this.activeGroups.push(val);
            console.log('Active Groups: ', this.activeGroups);
          }
        }
        this.groups = response;
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  public isActiveGroup(id: number): boolean {
    return this.activeGroups.some((group) => group.id === id);
  }

  public updateGroup(id: number, group: GroupDTO) {
    // group.description = this.desription;
    this.groupService.update(id, group).subscribe(
      (response: void) => {
        alert('Update Group');
        this.allGroups();
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  public deleteGroup(id: number) {
    this.groupService.delete(id).subscribe(
      (response: void) => {
        console.log('Response: ', response);
        alert('Uspesno ste suspendovali grupu');
        this.allGroups();
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  public logedUser() {}

  public view(id: number) {
    this.router.navigate(['/group', id]);
  }

  public join(id: number) {
    this.groupService.joinGroup(id).subscribe(
      (response: void) => {
        alert('Uspešno ste se poslali zahtev');
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }
}
