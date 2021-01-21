import { Component, OnInit, TemplateRef, ViewChild, AfterViewInit } from '@angular/core';
import { ModalDismissReasons, NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
    selector: 'app-academy',
    templateUrl: './academy.component.html',
    styleUrls: ['./academy.component.css']
})
export class AcademyComponent implements OnInit, AfterViewInit {

    @ViewChild('content') templateRef: TemplateRef<any>;

    closeResult = '';

    constructor(private modalService: NgbModal) { }

    ngOnInit(): void {}

    ngAfterViewInit(): void {
        const user = {
            id: 10
        };
        // this.modalRef =
        setTimeout(() => {
            this.modalService.open(this.templateRef, {ariaLabelledBy: 'modal-basic-title'})
                .result.then(
                    (result) => {
                        this.closeResult = `Closed with: ${result}`;
                    },
                    (reason) => {
                        this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
                    }
                );
        });
    }

    open(content) {
        this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'}).result.then(
            (result) => {
                this.closeResult = `Closed with: ${result}`;
            },
            (reason) => {
                this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
            });
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
