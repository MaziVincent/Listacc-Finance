import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-academy-registration',
  templateUrl: './academy-registration.component.html',
  styleUrls: ['./academy-registration.component.scss']
})
export class AcademyRegistrationComponent implements OnInit {

    CountdownEndDate = 'January 25, 2021';
    Units = 'Days | Hours | Minutes | Seconds';

    Registration: RegistrationInfo;

    fieldErrors: any = {};
    processing!: boolean;

    constructor(private activeModal: NgbActiveModal) {
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
        // TODO: Make button spin
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
        } else if (!this.validPhoneNumber(this.Registration.phone)) { // TODO: Validate phone number
            this.fieldErrors.Email = 'Enter a valid phone number';
        }
        if (!this.Registration.email || this.Registration.email.trim() === '') {
            this.fieldErrors.Email = 'Enter your email address';
            error = true;
        } else if (!this.validEmail(this.Registration.email)) {
            this.fieldErrors.Email = 'Enter a valid email address';
        }

        // TODO: send to server
        if (!error){
            // TODO: if error, show error
            // TODO: if success, close modal, show success message
        } else {
            this.processing = false;
        }
    }

    validEmail(email: string): boolean {
        const re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        return re.test(String(email).toLowerCase());
    }

    validPhoneNumber(phone: string): boolean {
        const re = /^[0]\d{10}$/;///(^[0]\d{10}$)|(^[\+]?[234]\d{12}$)/;
        return re.test(phone);
    }

}

export class RegistrationInfo {
    lastName!: string;
    firstName!: string;
    phone!: string;
    email!: string;
}
