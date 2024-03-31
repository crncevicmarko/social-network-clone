import { Component } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  Validators,
} from '@angular/forms';

@Component({
  selector: 'app-chpsform',
  templateUrl: './chpsform.component.html',
  styleUrls: ['./chpsform.component.css'],
})
export class ChpsformComponent {
  passwordForm: FormGroup;

  constructor(private formBuilder: FormBuilder) {
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
      // Handle form submission
      // Call your API to change the password
      alert('Uspesno ste izmenili lozinku');
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
  control: AbstractControl
): { [key: string]: boolean } | null {
  const newPassword = control.get('newPassword');
  const confirmPassword = control.get('confirmPassword');

  if (
    newPassword &&
    confirmPassword &&
    newPassword.value !== confirmPassword.value
  ) {
    return { passwordMismatch: true };
  }

  return null;
}
