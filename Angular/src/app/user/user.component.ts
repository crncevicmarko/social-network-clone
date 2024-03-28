import { Component } from '@angular/core';
import { FormGroup, NgForm } from '@angular/forms';
import { AuthService } from '../service';
import { Router } from '@angular/router';
import { UserDTO } from '../model/userDTO';
import { HttpErrorResponse } from '@angular/common/http';
import { LoginComponent } from '../login/login.component';
import { GroupService } from '../service/group.service';
import { GroupDTO } from '../model/groupDTO';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css'],
})
export class UserComponent {
  public logedUser!: UserDTO;
  public groups!: GroupDTO[];

  constructor(
    private userService: AuthService,
    private gropService: GroupService
  ) {
    this.getUserById();
    this.getAllGroups();
  }

  getUserById() {
    this.userService.getUsetById(0).subscribe((response: UserDTO) => {
      this.logedUser = response;
      console.log(this.logedUser.id);
      if (this.userService.access_token === '') {
        alert('UserService acces_token is " "');
      } else {
        console.log(this.userService.access_token);
      }
    });
  }
  signInForm(submit: NgForm) {
    this.userService.update(submit.value).subscribe(
      (response: UserDTO) => {
        this.logedUser = response;
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  getAllGroups() {
    this.gropService.getGroupsByUserId().subscribe(
      (response: GroupDTO[]) => {
        this.groups = response;
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }
}
