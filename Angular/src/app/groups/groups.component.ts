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
  public groups: GroupDTO[] = [];
  public group!: GroupDTO;
  public desription!: string;
  public activeGroups!: GroupDTO[];
  public otherGroups!: any[];
  public logedInUser!: UserDTO;
  public selectedFileName: string = '';

  constructor(
    private groupService: GroupService,
    private router: Router,
    private userService: AuthService
  ) {
    this.activeGroups = [];
    this.logedInUser = userService.logedUser;
  }

  groupFIle: any;

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.groupFIle = input;
    if (input.files && input.files.length > 0) {
      // console.log('File: ', input.files[0]);
      this.selectedFileName = input.files[0].name;
    }
  }

  reloadPage() {
    this.router.navigate([this.router.url]);
  }
  public addGroup(group: NgForm) {
    console.log('Form Data:', this.groupFIle.files[0]);
    console.log('Group: ', group.value);
    this.groupService
      .createGroup(group.value, this.groupFIle.files[0])
      .subscribe(
        (response: GroupDTO) => {
          alert('Uspesno ste kreirali grupu');
          this.group = response;
          this.reloadPage;
          group.resetForm();
        },
        (error: HttpErrorResponse) => {
          if (error.status === 406) {
            alert('Form cant be empty');
          } else {
            alert(error.message);
            group.resetForm();
          }
        }
      );
  }

  public allGroups() {
    this.groups = [];
    this.activeGroups = [];
    this.otherGroups = [];
    this.groupService.getAllSent().subscribe(
      (response: GroupDTO[]) => {
        console.log('User Requests : ', response);
        for (var val of response) {
          console.log('Groupe Request: ', val);
          this.otherGroups.push(val);
        }
        console.log('Users : ', this.otherGroups);
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
    this.groupService.getGroups().subscribe(
      (response: GroupDTO[]) => {
        for (var val of response) {
          console.log('IsSuspended:', val.suspended);
          if (val.suspended == false) {
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

  searchGroups(searchData: any) {
    this.groups = [];
    console.log('Search Data:', searchData);
    this.groupService.searchGroups(searchData).subscribe(
      (response: any) => {
        for (var val of response) {
          console.log('Value: ', val);
          this.groupService
            .getGroupById(val.groupId)
            .subscribe((group: GroupDTO) => {
              console.log('Group: ', group);
              this.groups.push(group);
            });
        }
        this.groupService.getAllSent().subscribe(
          (response: GroupDTO[]) => {
            console.log('User Requests : ', response);
            for (var val of response) {
              console.log('Groupe Request: ', val);
              this.otherGroups.push(val);
            }
            console.log('Users : ', this.otherGroups);
          },
          (error: HttpErrorResponse) => {
            alert(error.message);
          }
        );
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  public isActiveGroup(id: number): boolean {
    return this.activeGroups.some((group) => group.id === id);
  }

  public joinableGroup(groupId: number): boolean {
    const groupExistsInActiveGroups = this.activeGroups.some(
      (group) => group.id === groupId
    );
    const groupExistsInOtherGroups = this.otherGroups.some(
      (group) => group.group_id === groupId
    );

    if (!groupExistsInActiveGroups) {
      return true; // ID doesn't exist in active groups, button should be disabled
    }

    if (groupExistsInOtherGroups) {
      return true; // ID exists in other groups, button should be disabled
    }

    return false; // ID exists in active groups and doesn't exist in other groups, button should be enabled
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

  get IsUserAdmin(): boolean {
    // console.log(this.userService.isLogedUserNameAdmin());
    return this.userService.isLogedUserNameAdmin();
  }
}
