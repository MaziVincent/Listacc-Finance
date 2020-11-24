import { Component, OnInit } from '@angular/core';
import { LoginUser } from 'src/app/our-apps/finance/models/user';
import { Title } from '@angular/platform-browser';
import { Router, ActivatedRoute } from '@angular/router';
import { FinanceValidationErrorService } from '../../../finance-shared/services/finance-validation-error.service';
import { FinanceNotificationService } from '../../../finance-shared/services/finance-notification.service';
import { MyValidationErrors } from 'src/app/our-apps/finance/models/my-validation-errors';
import { FinanceLoginService } from '../../finance-login.service';

@Component({
    selector: 'app-finance-login',
    templateUrl: './finance-login.component.html',
    styleUrls: ['./finance-login.component.css']
})
export class FinanceLoginComponent implements OnInit {

    User: LoginUser;
    validationErrors: any[] = [];
    fieldErrors: any = {};
    returnUrl: string;
    processing = false;

    constructor(private loginService: FinanceLoginService,
                private titleService: Title,
                private validationService: FinanceValidationErrorService,
                private router: Router,
                private route: ActivatedRoute,
                private notificationService: FinanceNotificationService) {
        this.titleService.setTitle('Login | Listacc Finance');
        this.User = new LoginUser();
    }

    ngOnInit() {
        // get return url from the route parameters or default to '/'
        this.returnUrl = this.route.snapshot.queryParams.returnUrl || '/apps/finance';
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
