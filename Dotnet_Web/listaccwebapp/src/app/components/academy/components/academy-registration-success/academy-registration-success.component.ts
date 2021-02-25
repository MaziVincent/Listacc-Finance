import { Component, Input, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { AcademyProject } from 'src/app/models/academy';

@Component({
    selector: 'app-academy-registration-success',
    templateUrl: './academy-registration-success.component.html',
    styleUrls: ['./academy-registration-success.component.scss']
})
export class AcademyRegistrationSuccessComponent implements OnInit {

    @Input() initialState: any;

    Project: AcademyProject;
    Past: boolean;

    constructor(private activeModal: NgbActiveModal) {}

    ngOnInit() {
        if (this.initialState && this.initialState.Project) {
            this.Project = this.initialState.Project;
            this.Past = this.initialState.Past;
            if (this.Past) {
                // const dt = new Date(this.Project.startDate);
            } else {
                // this.CountdownEndDate = this.Project.altStartDate;
            }
        }
    }

    close(): void{
        this.activeModal.dismiss();
    }

}
