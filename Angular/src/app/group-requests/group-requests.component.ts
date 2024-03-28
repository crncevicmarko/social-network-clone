import { Component } from '@angular/core';
import { AuthService } from '../service';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { GroupService } from '../service/group.service';
import { GroupRequestDTO } from '../model/groupRequestDTO';

@Component({
  selector: 'app-group-requests',
  templateUrl: './group-requests.component.html',
  styleUrls: ['./group-requests.component.css'],
})
export class GroupRequestsComponent {
  public users!: GroupRequestDTO[];
  public friends!: GroupRequestDTO[];

  constructor(
    private router: Router,
    private groupRequestService: GroupService
  ) {}

  ngOnInit() {
    this.getAllGroupRequests();
  }

  getAllGroupRequests() {
    this.groupRequestService.getGroupRequests().subscribe(
      (respnse: GroupRequestDTO[]) => {
        this.users = respnse;
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }
  button1(id: number) {
    this.groupRequestService.updateGroupRequests(id, true).subscribe(
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
    this.groupRequestService.updateGroupRequests(id, false).subscribe(
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
