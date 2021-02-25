import { AcademyProject } from './../../models/academy';
import { ActivatedRoute } from '@angular/router';
import { NotificationService } from 'src/app/services/notification.service';
import { Component, OnInit, TemplateRef, ViewChild, AfterViewInit, OnDestroy } from '@angular/core';
import { NgbModal, NgbModalOptions, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { AcademyRegistrationComponent } from './components/academy-registration/academy-registration.component';
import { Subscription, timer } from 'rxjs';
import { AcademyRegistrationSuccessComponent } from './components/academy-registration-success/academy-registration-success.component';

@Component({
    selector: 'app-academy',
    templateUrl: './academy.component.html',
    styleUrls: ['./academy.component.css']
})
export class AcademyComponent implements OnInit, AfterViewInit, OnDestroy {

    @ViewChild('content') templateRef: TemplateRef<any>;

    Projects: AcademyProject[] = [];
    UpcomingProject: AcademyProject;
    PastProject: AcademyProject;
    SecondsPast: string;
    MinutesPast: string;
    HoursPast: string;
    DaysPast: string;

    subscription: Subscription;

    months = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];
    Units = 'Days | Hours | Minutes | Seconds';
    CountdownEndDate = 'January 25, 2021';

    modalRef: NgbModalRef;
    modalConfig: NgbModalOptions = {
        size: 'lg',
        centered: true,
        keyboard: true,
        backdrop: 'static'
    };
    successModalConfig: NgbModalOptions = {
        centered: true,
        keyboard: true,
        backdrop: 'static'
    };

    constructor(private modalService: NgbModal,
                private notify: NotificationService,
                private route: ActivatedRoute) {}

    ngOnInit(): void {
        this.route.data.subscribe(data => {
            this.Projects = data.academyInfo.upcomingProjects;
            this.processProjects();
        });
    }

    ngAfterViewInit(): void {
        // open the modal popup when the page becomes visible
        setTimeout(() => {
            this.openRegistrationModal();
        });
    }

    ngOnDestroy(): void {
        if(this.subscription) {
            this.subscription.unsubscribe();
        }
        if (this.modalRef) {
            this.modalRef.close();
        }
    }

    openRegistrationModal() {
        if (this.UpcomingProject || this.PastProject) {
            this.modalRef = this.modalService.open(AcademyRegistrationComponent, this.modalConfig);
            this.modalRef.componentInstance.initialState = {
                Project: this.UpcomingProject ? this.UpcomingProject : this.PastProject,
                Past: new Date(this.Projects[0].startDate) < new Date()
            };
            this.modalRef.componentInstance.registrationComplete.subscribe(
                () => {
                    if (this.modalRef) {
                        this.modalRef.dismiss();
                    }
                    // this.notify.success('Registration was completed successfully!');
                    // show success info and next steps
                    this.openSuccessModal();
                }
            );
        }
    }

    processProjects() {
        // check if there are projects
        if (this.Projects && this.Projects.length > 0) {
            // check if project is past or future
            const dt = new Date(this.Projects[0].startDate);
            let now = new Date();
            if (dt < now) {
                // past
                this.PastProject = this.Projects[0];
                const source = timer(1000, 1000);
                this.subscription = source.subscribe(val => {
                    now = new Date();
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
                // future
                this.UpcomingProject = this.Projects[0];
                this.UpcomingProject.altStartDate = this.getAlternateDateFormat(this.UpcomingProject.startDate);
                this.CountdownEndDate = this.UpcomingProject.altStartDate;
            }
        }
    }

    // Date Operations
    getAlternateDateFormat(dateStr: string) {
        const dateArr = dateStr.split('T');
        const datePart = dateArr[0].split('-');
        const newDateStr = this.months[Number.parseInt(datePart[1], 10) - 1]  + ' ' + datePart[2] + ', ' + datePart[0] + ' ' + dateArr[1];
        return newDateStr;
    }

    openSuccessModal() {
        this.modalRef = this.modalService.open(AcademyRegistrationSuccessComponent, this.successModalConfig);
        this.modalRef.componentInstance.initialState = {
            Project: this.UpcomingProject ? this.UpcomingProject : this.PastProject,
            Past: new Date(this.Projects[0].startDate) < new Date()
        };
    }

}
