import { Component, OnInit } from '@angular/core';
import { LoginUser } from 'src/app/models/user';
import { Title } from '@angular/platform-browser';
import { Router, ActivatedRoute } from '@angular/router';
import { LoginService } from '../../login.service';
import { NotificationService } from 'src/app/services/notification.service';
import { ValidationErrorService } from 'src/app/services/validation-error.service';
import { MyValidationErrors } from 'src/app/models/my-validation-errors';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

    User: LoginUser;
    validationErrors: any[] = [];
    fieldErrors: any = {};
    returnUrl: string;
    processing = false;

    constructor(private loginService: LoginService,
                private titleService: Title,
                private validationService: ValidationErrorService,
                private router: Router,
                private route: ActivatedRoute,
                private notificationService: NotificationService) {
        this.titleService.setTitle('Login | Listacc Finance');
        this.User = new LoginUser();
    }

    ngOnInit() {
        // get return url from the route parameters or default to '/'
        this.returnUrl = this.route.snapshot.queryParams.returnUrl || '/';
    }

    signIn() {
        this.processing = true;
        this.validationErrors = [];
        this.fieldErrors = {};

        this.loginService.signIn(this.User)
            .subscribe(

                // success
                () => {
                    this.notificationService.clearAll();
                    this.router.navigate([this.returnUrl]);
                    this.processing = false;
                },

                // error
                error => {
                    const allErrors: MyValidationErrors = this.validationService.showValidationErrors(error);
                    this.validationErrors = allErrors.validationErrors;
                    this.fieldErrors = allErrors.fieldErrors;
                    if (this.fieldErrors.EmailAddress === undefined && this.fieldErrors.Password === undefined) {
                        this.validationErrors.push('Invalid account details');
                    }
                    this.processing = false;
                }
            );
    }

}
