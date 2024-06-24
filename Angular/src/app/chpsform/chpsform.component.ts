import { Component } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  Validators,
  AbstractControl,
} from '@angular/forms';
import { AuthService } from '../service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-chpsform',
  templateUrl: './chpsform.component.html',
  styleUrls: ['./chpsform.component.css'],
})
export class ChpsformComponent {
  passwordForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private userService: AuthService
  ) {
    this.passwordForm = this.formBuilder.group(
      {
        oldPassword: ['', Validators.required],
        newPassword: [
          '',
          [
            Validators.required,
            Validators.minLength(8),
            Validators.pattern(
              /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/
            ),
          ],
        ],
        confirmPassword: ['', Validators.required],
      },
      { validators: this.matchPasswords }
    );
  }

  onSubmit() {
    if (this.passwordForm.valid) {
      this.userService
        .changePassword(
          this.passwordForm.value.oldPassword,
          this.passwordForm.value.newPassword,
          this.passwordForm.value.confirmPassword
        )
        .subscribe(
          (result: any) => {},
          (error: HttpErrorResponse) => {
            if (error.status === 400) {
              alert('Stara lozinka je pogresna');
            } else if (error.status === 500) {
              alert('Ne mozemo da sacuvamo lozinku');
            } else if (error.status === 200) {
              alert('Ok');
            }
          }
        );
    } else {
      alert('Somthing went wrong');
    }
  }

  hasError(controlName: string, errorName: string) {
    return this.passwordForm.get(controlName)?.hasError(errorName);
  }

  matchPasswords(control: AbstractControl): { [key: string]: boolean } | null {
    const newPassword = control.get('newPassword')?.value;
    const confirmPassword = control.get('confirmPassword')?.value;
    return newPassword === confirmPassword ? null : { mismatch: true };
  }
}
