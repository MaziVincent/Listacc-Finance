import { Component, OnInit, ViewChild, Output, EventEmitter, Input } from '@angular/core';
import { NgForm } from '@angular/forms';
import { DepartmentViewModel } from 'src/app/our-apps/finance/models/department';
import { NgbModalOptions, NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ConfirmExitComponent } from '../../../finance-shared/components/confirm-exit/confirm-exit.component';
import { MyValidationErrors } from 'src/app/our-apps/finance/models/my-validation-errors';
import { FinanceValidationErrorService } from '../../../finance-shared/services/finance-validation-error.service';
import { FinanceDepartmentsService } from '../../finance-departments.service';

@Component({
    selector: 'app-finance-departments-add',
    templateUrl: './finance-departments-add.component.html',
    styleUrls: ['./finance-departments-add.component.css']
})
export class FinanceDepartmentsAddComponent implements OnInit {

    @ViewChild('myForm', {static: false}) private myForm: NgForm;

    @Output() departmentCreated = new EventEmitter<any>();
    @Output() departmentEdited = new EventEmitter<any>();
    @Output() departmentDeleted = new EventEmitter<any>();

    @Input() initialState: any;

    title: string;

    processing: boolean;
    deleting: boolean;
    editMode: boolean;

    Department: DepartmentViewModel;
    OriginalDepartment: string;
    fieldErrors: any = {};

    modalConfig: NgbModalOptions = {
        centered: true,
        keyboard: true,
        backdrop: 'static'
    };

    constructor(private activeModal: NgbActiveModal,
                private modalService: NgbModal,
                private deptService: FinanceDepartmentsService,
                private validationErrorService: FinanceValidationErrorService) {
        this.Department = new DepartmentViewModel();
    }

    ngOnInit() {
        // if in edit mode
        if (this.initialState.Department !== undefined) {

            // clone original object so that changes do not reflect on the list
            this.Department = JSON.parse(JSON.stringify(this.initialState.Department));

            this.editMode = true;
        } else {
            this.editMode = false;
        }
        this.OriginalDepartment = JSON.stringify(this.Department);
    }

    close() {
        if (!this.changesMade()) {
            this.activeModal.dismiss();
        } else {
            // show another modal asking to discard changes
            const modalRef = this.modalService.open(ConfirmExitComponent, this.modalConfig);
            modalRef.componentInstance.closeEditModal.subscribe(
                () => {
                    modalRef.close();
                    this.activeModal.close();
                }
            );
        }
    }



    // Department (Create, Edit)
    save() {
        this.processing = true;
        this.fieldErrors = {};

        if (!this.editMode) {
            this.createNewDepartment();
        } else {
            this.editDepartment();
        }
    }

    createNewDepartment() {
        this.deptService.createDepartment(this.Department)
        .subscribe(

            // success
            (response) => {
                // tell parent component to reload department list
                this.departmentCreated.emit();
                this.processing = false;
            },

            // error
            error => {
                const allErrors: MyValidationErrors = this.validationErrorService.showValidationErrors(error);
                this.fieldErrors = allErrors.fieldErrors;
                this.processing = false;
            }
        );
    }

    editDepartment() {
        this.deptService.editDepartment(this.Department)
        .subscribe(

            // success
            () => {
                // tell parent component to reload department list
                this.departmentEdited.emit();
                this.processing = false;
            },

            // error
            error => {
                const allErrors: MyValidationErrors = this.validationErrorService.showValidationErrors(error);
                this.fieldErrors = allErrors.fieldErrors;
                /*if (this.fieldErrors.DuplicateUserName) {
                    this.fieldErrors.EmailAddress = 'This email address is already taken';
                }
                if (this.fieldErrors.departmentId) {
                    this.fieldErrors.Department = 'Select a department';
                }*/
                this.processing = false;
            }
        );
    }



    // Changes
    changesMade() {
        /*if (!this.editMode) {
            return false;
        }*/
        const staffInfoChanged = this.OriginalDepartment !== JSON.stringify(this.Department);
        return staffInfoChanged;
    }

    nameEmpty() {
        const nameEmpty = !this.Department.name || this.Department.name.trim().length === 0;
        return nameEmpty;
    }

}
