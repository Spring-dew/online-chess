import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {debounceTime} from 'rxjs';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import {retypePasswordValidator} from '../directives/two-password-match.directive';
import {LoginService} from '../services/login.service';
import {environment} from '../../environments/environment';
import {auth} from '../globals';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  loginForm: boolean = true;
  logoutRequest: boolean = false;
  loginFormGroup: FormGroup = new FormGroup({
    userName: new FormControl('', [Validators.required, Validators.minLength(4)]),
    password: new FormControl('', [Validators.required])
  });
  signupFormGroup: FormGroup = new FormGroup({
    userName: new FormControl('', [Validators.required, Validators.minLength(4)]),
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required, Validators.minLength(6)]),
    passwordConfirmation: new FormControl('', [Validators.required, Validators.minLength(6)])
  }, {validators: retypePasswordValidator});
  signedUpSuccess: boolean = false;
  signedUpFailed: boolean = false;

  constructor(private route: ActivatedRoute, private loginService: LoginService, private router: Router) {
    this.logoutRequest = this.route.snapshot.queryParams['logout'];
  }

  get userName() {
    if (this.loginForm) return this.loginFormGroup.get('userName');
    return this.signupFormGroup.get('userName');
  }

  get password() {
    if (this.loginForm) return this.loginFormGroup.get('password');
    return this.signupFormGroup.get('password');
  }

  get email() {
    return this.signupFormGroup.get('email');
  }

  get passwordConfirmation() {
    return this.signupFormGroup.get('passwordConfirmation');
  }

  togglePassword() {
    let passwordButton: HTMLInputElement | null = document.getElementById('floatingPassword') as HTMLInputElement;
    if (passwordButton != null) {
      if (passwordButton.type == 'password') {
        passwordButton.type = 'text';
      } else {
        passwordButton.type = 'password';
      }
    }
    let passwordConfirmationButton: HTMLInputElement | null = document.getElementById('floatingPasswordConfirmation') as HTMLInputElement;
    if (passwordConfirmationButton != null) {
      if (passwordConfirmationButton.type == 'password') {
        passwordConfirmationButton.type = 'text';
      } else {
        passwordConfirmationButton.type = 'password';
      }
    }
  }

  toggleSignUp() {
    this.loginForm = !this.loginForm;
  }


  onSubmitLogin() {
    if (this.loginFormGroup?.invalid) {
    } else {
      this.loginService.login(this.loginFormGroup.value).subscribe(response => {
          auth.token = response.body.token;
          auth.userID = this.loginFormGroup.value.userName;
          this.router.navigate(['/mode-select'])
        }
      );
    }
  }

  onSubmitSignup() {
    if (this.signupFormGroup?.invalid) {
    } else {
      this.loginService.signup(this.signupFormGroup.value).subscribe({
          next: (response: any) => {
            this.signedUpSuccess = true;
            this.signedUpFailed = false;
            this.signupFormGroup.reset();
            this.loginFormGroup.reset();
            this.loginForm = true;
          }
          ,
          error: (error) => {
            this.signedUpFailed = true;
          }
        }
      );
    }
  }
}
