import { Component, OnInit, Output, EventEmitter, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
    selector: 'app-confirm-delete',
    templateUrl: './confirm-delete.component.html',
    styleUrls: ['./confirm-delete.component.css']
})
export class ConfirmDeleteComponent implements OnInit {

    @Output() completeDelete = new EventEmitter<any>();
    @Input() ModalContent: any;

    Item: string;
    ExtraMessage: string;
    Action: string;
    processing: boolean;

    constructor(public activeModal: NgbActiveModal) { }

    ngOnInit() {
        if (this.ModalContent.item) {
            this.Item = this.ModalContent.item;
        }

        if (this.ModalContent.extraMessage) {
            this.ExtraMessage = this.ModalContent.extraMessage;
        }

        if (this.ModalContent.action) {
            this.Action = this.ModalContent.action;
        } else {
            this.Action = 'Delete';
        }
    }

    delete() {
        this.completeDelete.emit();
    }

}
