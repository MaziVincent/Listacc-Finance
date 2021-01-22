import { AcademyRegistrationComponent } from './components/academy-registration/academy-registration.component';
import { Component, OnInit, TemplateRef, ViewChild, AfterViewInit } from '@angular/core';
import { ModalDismissReasons, NgbModal, NgbModalOptions } from '@ng-bootstrap/ng-bootstrap';

@Component({
    selector: 'app-academy',
    templateUrl: './academy.component.html',
    styleUrls: ['./academy.component.css']
})
export class AcademyComponent implements OnInit, AfterViewInit {

    @ViewChild('content') templateRef: TemplateRef<any>;

    closeResult = '';

    modalConfig: NgbModalOptions = {
        size: 'lg',
        centered: true,
        keyboard: true,
        backdrop: 'static'
    };

    constructor(private modalService: NgbModal) {}

    ngOnInit(): void {}

    ngAfterViewInit(): void {

        // open the modal popup when the page becomes visible
        setTimeout(() => {
            this.open();
        });
    }

    open() {
        this.modalService.open(AcademyRegistrationComponent, this.modalConfig)
            /*.result.then(
                (result) => {
                    this.closeResult = `Closed with: ${result}`;
                },
                (reason) => {
                    this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
                }
            );*/
    }

    private getDismissReason(reason: any): string {
        if (reason === ModalDismissReasons.ESC) {
            return 'by pressing ESC';
        } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
            return 'by clicking on a backdrop';
        } else {
            return `with: ${reason}`;
        }
    }

}
