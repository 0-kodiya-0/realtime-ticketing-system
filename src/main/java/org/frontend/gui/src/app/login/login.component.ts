import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {NgClass, NgIf} from '@angular/common';

@Component({
  selector: 'app-login',
  imports: [
    FormsModule,
    ReactiveFormsModule,
    NgClass,
    NgIf
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  submitted = false;
  userData: any = null;

  constructor(private formBuilder: FormBuilder) {
    console.log('Component constructed'); // Debug constructor
    this.loginForm = this.formBuilder.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  ngOnInit() {
    console.log('Component initialized'); // Debug initialization

    // Add form value changes subscription
    this.loginForm.valueChanges.subscribe(values => {
      console.log('Form values changed:', values);
    });
  }

  onSubmit() {
    this.submitted = true;
    console.log('Submit button clicked'); // Debug button click

    if (this.loginForm.valid) {
      console.log('Form validation passed');
      this.userData = {
        username: this.loginForm.get('username')?.value,
        password: this.loginForm.get('password')?.value
      };
      console.log('UserData updated:', this.userData);
    } else {
      console.log('Form validation failed');
      console.log('Form errors:', this.loginForm.errors);
      console.log('Username errors:', this.loginForm.get('username')?.errors);
      console.log('Password errors:', this.loginForm.get('password')?.errors);
    }
  }

  onReset() {
    this.submitted = false;
    this.userData = null;
    this.loginForm.reset();
  }
}
