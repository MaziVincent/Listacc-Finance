import { NotificationService } from 'src/app/services/notification.service';
import { AcademyRegistrationComponent } from './components/academy-registration/academy-registration.component';
import { Component, OnInit, TemplateRef, ViewChild, AfterViewInit } from '@angular/core';
import { ModalDismissReasons, NgbModal, NgbModalOptions, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';

@Component({
    selector: 'app-academy',
    templateUrl: './academy.component.html',
    styleUrls: ['./academy.component.css']
})
export class AcademyComponent implements OnInit, AfterViewInit {

    @ViewChild('content') templateRef: TemplateRef<any>;

    modalRef: NgbModalRef;

    modalConfig: NgbModalOptions = {
        size: 'lg',
        centered: true,
        keyboard: true,
        backdrop: 'static'
    };

    constructor(private modalService: NgbModal,
                private notify: NotificationService) {}

    ngOnInit(): void {}

    ngAfterViewInit(): void {

        // open the modal popup when the page becomes visible
        setTimeout(() => {
            this.open();
        });
    }

    open() {
        this.modalRef = this.modalService.open(AcademyRegistrationComponent, this.modalConfig);
        this.modalRef.componentInstance.registrationComplete.subscribe(
            () => {
                if (this.modalRef) {
                    this.modalRef.dismiss();
                }
                this.notify.success('Registration was completed successfully!');
            }
        );
    }

}
