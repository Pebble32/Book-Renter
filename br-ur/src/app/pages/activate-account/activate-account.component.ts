import {Component} from '@angular/core';
import {AuthenticationService} from "../../services/services/authentication.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-activate-account',
  templateUrl: './activate-account.component.html',
  styleUrls: ['./activate-account.component.scss']
})
export class ActivateAccountComponent {

  message: string = '';
  isOkay: boolean = true;
  submitted: boolean = false;

  constructor(
    private authenticationService: AuthenticationService,
    private router: Router,
  ) {
  }


  onCodeCompleted(token: string) {
    this.accountConfirmation(token);
  }

  redirectToLogin() {
    this.router.navigate(['login']);
  }

  private accountConfirmation(token: string) {
    this.authenticationService.confirm({
      token
    }).subscribe({
      next: () => {
        this.message = 'You account has been successfully activated.';
        this.submitted = true;
        this.isOkay = true;
      },
      error: () => {
        this.message = 'Activation code is invalid or it has expired.';
        this.submitted = true;
        this.isOkay = false;
      }
    });
  }
}
