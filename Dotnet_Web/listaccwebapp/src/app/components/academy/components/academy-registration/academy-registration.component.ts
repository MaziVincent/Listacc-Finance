import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { MyValidationErrors } from 'src/app/our-apps/finance/models/my-validation-errors';
import { ValidationErrorService } from 'src/app/services/validation-error.service';
import { AcademyService } from '../../../../services/academy.service';

@Component({
  selector: 'app-academy-registration',
  templateUrl: './academy-registration.component.html',
  styleUrls: ['./academy-registration.component.scss']
})
export class AcademyRegistrationComponent implements OnInit {


    @Output() registrationComplete = new EventEmitter<any>();

    CountdownEndDate = 'January 25, 2021';
    Units = 'Days | Hours | Minutes | Seconds';

    Registration: RegistrationInfo;

    fieldErrors: any = {};
    processing!: boolean;

    constructor(private activeModal: NgbActiveModal,
                private academyService: AcademyService,
                private validationErrorService: ValidationErrorService) {
        this.Registration = new RegistrationInfo();
    }

    ngOnInit() {
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
        if (!this.Registration.phone || this.Registration.phone.trim() === '') {
            this.fieldErrors.Phone = 'Enter your phone number';
            error = true;
        } else if (!this.validPhoneNumber(this.Registration.phone)) {
            this.fieldErrors.Phone = 'Enter a valid phone number';
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
        if (!this.Registration.selectedProgram || this.Registration.selectedProgram === 0) {
            this.fieldErrors.selectedProgram = 'Select your training path';
            error = true;
        }

        // TODO: send to server
        if (!error){
            this.academyService.registerComingSoon(this.Registration)
            .subscribe(

                // success
                (response) => {
                    // TODO: if success, close modal, show success message
                    this.registrationComplete.emit();
                },

                // error
                (errors) => {
                    // TODO: if error, show error
                    const allErrors: MyValidationErrors = this.validationErrorService.showValidationErrors(errors);
                    this.fieldErrors = allErrors.fieldErrors;
                    // if (this.fieldErrors.error && this.fieldErrors.error.indexOf('name') !== - 1) {
                        // this.fieldErrors.Name = this.fieldErrors.error;
                    // }
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

export class RegistrationInfo {
    lastName!: string;
    firstName!: string;
    phone!: string;
    email!: string;
    gender!: string;
    selectedProgram!: number;
}
