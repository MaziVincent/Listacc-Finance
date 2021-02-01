import { Component, EventEmitter, Input, OnInit, Output, OnDestroy } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Subscription, timer } from 'rxjs';
import { AcademyProject, RegistrationInfo } from 'src/app/models/academy';
import { MyValidationErrors } from 'src/app/our-apps/finance/models/my-validation-errors';
import { NotificationService } from 'src/app/services/notification.service';
import { ValidationErrorService } from 'src/app/services/validation-error.service';
import { AcademyService } from '../../../../services/academy.service';

@Component({
  selector: 'app-academy-registration',
  templateUrl: './academy-registration.component.html',
  styleUrls: ['./academy-registration.component.scss']
})
export class AcademyRegistrationComponent implements OnInit, OnDestroy {

    @Input() initialState: any;
    @Output() registrationComplete = new EventEmitter<any>();

    CountdownEndDate = 'January 25, 2021';
    Units = 'Days | Hours | Minutes | Seconds';

    Project: AcademyProject;
    Past: boolean;
    Registration: RegistrationInfo;

    SecondsPast: string;
    MinutesPast: string;
    HoursPast: string;
    DaysPast: string;
    subscription: Subscription;

    fieldErrors: any = {};
    processing!: boolean;

    constructor(private activeModal: NgbActiveModal,
                private academyService: AcademyService,
                private validationErrorService: ValidationErrorService,
                private notify: NotificationService) {
        this.Registration = new RegistrationInfo();
    }

    ngOnInit() {
        if (this.initialState && this.initialState.Project) {
            this.Project = this.initialState.Project;
            this.Past = this.initialState.Past;
            if (this.Past) {
                const dt = new Date(this.Project.startDate);
                const source = timer(1000, 1000);
                this.subscription = source.subscribe(() => {
                    const now = new Date();
                    const timeDiff = now.getTime() - dt.getTime();
                    const days = Math.floor(timeDiff / (1000 * 3600 * 24));
                    this.DaysPast = days >= 10 ? days + '' : '0' + days;
                    const hours = Math.floor((timeDiff % (1000 * 3600 * 24)) / (1000 * 3600));
                    this.HoursPast = hours >= 10 ? hours + '' : '0' + hours;
                    const minutes = Math.floor((timeDiff % (1000 * 3600)) / (1000 * 60));
                    this.MinutesPast = minutes >= 10 ? minutes + '' : '0' + minutes;
                    const seconds = Math.floor((timeDiff % (1000 * 60)) / 1000);
                    this.SecondsPast = seconds >= 10 ? seconds + '' : '0' + seconds;
                });
            } else {
                this.CountdownEndDate = this.Project.altStartDate;
            }
        }
    }

    ngOnDestroy(): void {
        this.subscription.unsubscribe();
    }

    close(): void{
        this.activeModal.dismiss();
    }

    register(): void{
        this.fieldErrors = {};
        this.processing = true;
        let error = false;

        // check for error
        if (!this.Registration.lastName || this.Registration.lastName.trim() === '') {
            this.fieldErrors.LastName = 'Enter your last name/surname';
            error = true;
        }
        if (!this.Registration.firstName || this.Registration.firstName.trim() === '') {
            this.fieldErrors.FirstName = 'Enter your first name';
            error = true;
        }
        if (!this.Registration.phoneNumber || this.Registration.phoneNumber.trim() === '') {
            this.fieldErrors.PhoneNumber = 'Enter your phone number';
            error = true;
        } else if (!this.validPhoneNumber(this.Registration.phoneNumber)) {
            this.fieldErrors.PhoneNumber = 'Enter a valid phone number';
        }
        if (!this.Registration.email || this.Registration.email.trim() === '') {
            this.fieldErrors.Email = 'Enter your email address';
            error = true;
        } else if (!this.validEmail(this.Registration.email)) {
            this.fieldErrors.Email = 'Enter a valid email address';
        }
        if (!this.Registration.gender || this.Registration.gender.trim() === '') {
            this.fieldErrors.Gender = 'Select your gender';
            error = true;
        }
        if (!this.Registration.programId || this.Registration.programId === 0) {
            this.fieldErrors.SelectedProgram = 'Select your training path';
            error = true;
        }

        if (!error){
            this.academyService.registerComingSoon(this.Registration)
            .subscribe(

                // success
                (response) => {
                    this.registrationComplete.emit();
                },

                // error
                (errors) => {
                    const allErrors: MyValidationErrors = this.validationErrorService.showValidationErrors(errors);
                    this.fieldErrors = allErrors.fieldErrors;
                    if (this.fieldErrors.error && this.fieldErrors.error.indexOf('email') !== - 1) {
                        this.fieldErrors.Email = this.fieldErrors.error;
                    }
                    if (this.fieldErrors.error && this.fieldErrors.error.indexOf('phone') !== - 1) {
                        this.fieldErrors.PhoneNumber = this.fieldErrors.error;
                    }
                    if (this.fieldErrors.error && this.fieldErrors.error.indexOf('program selected') !== - 1) {
                        this.fieldErrors.SelectedProgram = this.fieldErrors.error;
                    }
                    if (this.fieldErrors.error && this.fieldErrors.error.indexOf('Sorry') !== - 1) {
                        this.notify.clearAll();
                        this.notify.error('You have already been registered for another course!');
                    }
                    this.processing = false;
                }
            );
        } else {
            this.processing = false;
        }
    }

    validEmail(email: string): boolean {
        const re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        return re.test(String(email).toLowerCase());
    }

    validPhoneNumber(phone: string): boolean {
        const re = /(^[0]\d{10}$)|(^[\+]?[234]\d{12}$)/;
        return re.test(phone);
    }

}
