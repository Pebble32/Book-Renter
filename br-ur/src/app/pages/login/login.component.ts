import {Component} from '@angular/core';
import {AuthenticationRequest} from "../../services/models/authentication-request";
import {AuthenticationService} from "../../services/services/authentication.service";
import {Router} from "@angular/router";
import {HttpResponse} from "@angular/common/http";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {

  authRequest: AuthenticationRequest = {email: '', password: ''};
  errorMessage: Array<string> = [];

  constructor(
    private authenticationService: AuthenticationService,
    private router: Router,
    // todo impl service
  ) {
  }

  login() {
    this.errorMessage = [];
    this.authenticationService.authenticate({
      body: this.authRequest,
    }).subscribe({
      next: (res) => {
        // todo save token
        this.router.navigate(['books']);
      },
      error: (err) => {
        console.log(err);
      }
    })
  }

  toRegistration() {
    this.router.navigate(['register']);
  }
}
