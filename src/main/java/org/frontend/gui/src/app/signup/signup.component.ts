import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {NgClass, NgIf} from '@angular/common';

@Component({
  selector: 'app-signup',
  imports: [
    ReactiveFormsModule,
    NgIf,
    NgClass
  ],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.css'
})
export class SignupComponent implements OnInit {
  signupForm: FormGroup;
  submitted = false;
  userData: any = null;

  constructor(private formBuilder: FormBuilder) {
    console.log('Component constructed'); // Debug constructor
    this.signupForm = this.formBuilder.group({
      firstName: ['', Validators.required,Validators.minLength(3)],
      surName: ['', Validators.required, Validators.minLength(3)],
      gender: ['', Validators.required],
      dateOfBirth: ['', Validators.required],
      phoneNumber: ['', [Validators.required, Validators.pattern(/^\+?\d{1,3}[- ]?\(?\d\)?[- ]?\d{1,13}$/)]],
      email: ['', [Validators.required, Validators.email]],
      province: ['', Validators.required],
      city: ['', Validators.required],
      street: ['', Validators.required],
      address: ['', Validators.required],
      zipCode: ['', Validators.required]
    });
  }

  ngOnInit() {
    console.log('Component initialized'); // Debug initialization

    // Add form value changes subscription
    this.signupForm.valueChanges.subscribe(values => {
      console.log('Form values changed:', values);
    });
  }

  onSubmit() {
    this.submitted = true;
    console.log('Submit button clicked'); // Debug button click

    if (this.signupForm.valid) {
      console.log('Form validation passed');
      this.userData = {
        firstName: this.signupForm.get('firstName')?.value,
        surName: this.signupForm.get('surName')?.value,
        gender: this.signupForm.get('gender')?.value,
        dateOfBirth: this.signupForm.get('dateOfBirth')?.value,
        email: this.signupForm.get('email')?.value,
        phoneNumber: this.signupForm.get('phoneNumber')?.value,
        province: this.signupForm.get('province')?.value,
        city: this.signupForm.get('city')?.value,
        street: this.signupForm.get('street')?.value,
        address: this.signupForm.get('address')?.value,
        zipCode: this.signupForm.get('zipCode')?.value,
      };
      console.log('UserData updated:', this.userData);
    } else {
      console.log('Form validation failed');
      console.log('Form errors:', this.signupForm.errors);
      console.log('Username errors:', this.signupForm.get('username')?.errors);
      console.log('Password errors:', this.signupForm.get('password')?.errors);
    }
  }

  onReset() {
    this.submitted = false;
    this.userData = null;
    this.signupForm.reset();
  }
}
