import {Component} from '@angular/core';
import {RegistrationRequest} from '../../services/models/registration-request'
import {Router} from "@angular/router";
import {AuthenticationService} from "../../services/services/authentication.service";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  registrationRequest: RegistrationRequest = {email: '', password: '', firstName: '', lastName: ''};
  errorMessage: Array<string> = [];

  constructor(
    private router: Router,
    private authenticationService: AuthenticationService,
  ) {
  }

  register() {
    this.errorMessage = [];
    this.authenticationService.register({
      body: this.registrationRequest
    }).subscribe({
      next: (res) => {
        this.router.navigate(['activate-account']);
      },
      error: (err) => {
        this.errorMessage = err.error.validationErrors;
      }
    })

  }

  toLogin() {
    this.router.navigate(['login']);
  }
}
