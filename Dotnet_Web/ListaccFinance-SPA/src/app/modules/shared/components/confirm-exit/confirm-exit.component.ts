import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-confirm-exit',
  templateUrl: './confirm-exit.component.html',
  styleUrls: ['./confirm-exit.component.css']
})
export class ConfirmExitComponent implements OnInit {

  @Output() closeEditModal = new EventEmitter<any>();

  constructor(public activeModal: NgbActiveModal) { }

  ngOnInit() {
  }

  discardChanges() {
    this.closeEditModal.emit();
  }

}
