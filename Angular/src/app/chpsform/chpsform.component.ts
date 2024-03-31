import { Component } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  Validators,
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
      {
        validator: passwordMatchValidator,
      }
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
      alert(
        'Nova sifra mora da bude minimum 8 karaktera, makar jedno veliko slovo, broj i karakter'
      );
    }
  }

  hasError(controlName: string, errorName: string) {
    return this.passwordForm.get(controlName)?.hasError(errorName);
  }
}

function passwordMatchValidator(
  formGroup: FormGroup
): { [key: string]: boolean } | null {
  const newPassword = formGroup.get('newPassword');
  const confirmPassword = formGroup.get('confirmPassword');

  if (
    newPassword &&
    confirmPassword &&
    newPassword.value !== confirmPassword.value
  ) {
    return { passwordMismatch: true };
  }

  return null;
}
