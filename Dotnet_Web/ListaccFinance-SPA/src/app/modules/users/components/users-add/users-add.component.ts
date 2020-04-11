import { Component, OnInit, ViewChild, Output, EventEmitter, Input } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Ng2TelInput } from 'ng2-tel-input';
import { UserViewModel, StatusOption } from 'src/app/models/user';
import { NgbModalOptions, NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { UserService } from '../../user.service';
import { ValidationErrorService } from 'src/app/services/validation-error.service';
import { SharedService } from 'src/app/services/shared.service';
import { ConfirmExitComponent } from 'src/app/modules/shared/components/confirm-exit/confirm-exit.component';
import { MyValidationErrors } from 'src/app/models/my-validation-errors';
import { ConfirmDeleteComponent } from 'src/app/modules/shared/components/confirm-delete/confirm-delete.component';
import { DepartmentViewModel } from 'src/app/models/department';
import { NotificationService } from 'src/app/services/notification.service';
import { DepartmentsAddComponent } from 'src/app/modules/departments/components/departments-add/departments-add.component';

@Component({
  selector: 'app-users-add',
  templateUrl: './users-add.component.html',
  styleUrls: ['./users-add.component.css']
})
export class UsersAddComponent implements OnInit {

    // @ViewChild(ProfilePicComponent, {static: false})
    // private profilePictureComponent: ProfilePicComponent;

    @ViewChild('myForm', {static: false}) private myForm: NgForm;

    @ViewChild(Ng2TelInput, {static: false}) telInputDirectiveRef: Ng2TelInput;

    @Output() userCreated = new EventEmitter<any>();
    @Output() userEdited = new EventEmitter<any>();
    @Output() userDeleted = new EventEmitter<any>();

    @Input() initialState: any;

    title: string;

    processing: boolean;
    deleting: boolean;
    editMode: boolean;

    departmentsReady = false;
    Departments: DepartmentViewModel[];

    editUserReady = true;
    User: UserViewModel;
    OriginalUser: string;
    TempPhone: string;
    fieldErrors: any = {};

    profilePictureConfig: any;

    modalRef: NgbModalRef;
    modalConfig: NgbModalOptions = {
        centered: true,
        keyboard: true,
        backdrop: 'static'
    };

    StatusOptions: StatusOption[] = [];
    statusSelectizeConfig = {
        labelField: 'name',
        valueField: 'value',
        searchField: 'name',
        maxItems: 1,
        highlight: false,
        create: false,
        closeAfterSelect: true,
        render: {
            item(item, escape) { // selection
                const buildString = '<div><span class="font-14"><span class="dot ' + escape(item.iconClass) + '"></span>'
                    + escape(item.name) + '</span></div>';
                return buildString;
            },
            option(item, escape) {
                const buildString = '<div><span class="font-14"><span class="dot ' + escape(item.iconClass) + '"></span>'
                    + escape(item.name) + '</span></div>';
                return buildString;
            }
        },
        // dropdownParent: 'body',
        // placeholder: 'Select Parent Category'
        // items: [] -> initial selected values
    };

    selectizeConfig = {
        labelField: 'name',
        valueField: 'id',
        searchField: 'name',
        maxItems: 1,
        highlight: true,
        create: false,
        closeAfterSelect: true,
        render: {
            item(item, escape) {
                return '<div><span class="font-14">' + escape(item.name) + '</span></div>';
            },
            option(item, escape) {
                return '<div><span class="font-14">' + escape(item.name) + '</span></div>';
            }
        }
    };

    constructor(private activeModal: NgbActiveModal,
                private modalService: NgbModal,
                shared: SharedService,
                private userService: UserService,
                private validationErrorService: ValidationErrorService,
                private notify: NotificationService) {
        this.User = new UserViewModel();

        this.StatusOptions = shared.StatusOptions;
    }

    ngOnInit() {
        // load departments
        this.loadDepartments();

        // if in edit mode
        if (this.initialState.User !== undefined) {

            // clone original object so that changes do not reflect on the list
            this.User = JSON.parse(JSON.stringify(this.initialState.User));

            this.TempPhone = this.User.phone;

            // get more info from server
            this.getUserForEdit();

            this.editMode = true;
        } else {
            // set default values
            this.User.status = 'true';
            this.User.gender = 'female';
            this.User.role = 'Admin';
            this.OriginalUser = JSON.stringify(this.User);
        }
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



    // Department
    loadDepartments() {
        this.departmentsReady = false;

        if (!this.userService.DepartmentsList) {
           this.loadFreshDepartmentsList(false);
        } else {
            this.completeLoad(this.userService.DepartmentsList);
        }
    }

    loadFreshDepartmentsList(newDeptCreated: boolean) {
        this.userService.getDepartments()
        .subscribe(
            // success
            response => {
                if (newDeptCreated) {
                    this.userService.DepartmentsList = response;

                    // find latest entry
                    let dept = null;
                    for (const d of this.userService.DepartmentsList) {
                        if (dept === null || dept.id < d.id) {
                            dept = d;
                        }
                    }

                    // add new item
                    this.Departments.push({id: dept.id, name: dept.name});

                    this.User.departmentId = null;
                    this.User.departmentId = dept.id + '';
                } else  {
                    this.completeLoad(response);
                }
            },

            // error
            error => {
                this.notify.error('Problem loading departments, please reload the page.');
            }
        );
    }

    completeLoad(list: DepartmentViewModel[]) {
        this.Departments = list;
        this.userService.DepartmentsList = this.Departments;
        this.departmentsReady = true;

        // Get department
        if (this.editMode && this.editUserReady && this.User.department) {
            this.User.departmentId = this.User.departmentId.toString();

            // get copy of user to determine if change has been made
            this.OriginalUser = JSON.stringify(this.User);
        }
    }



    // Phone operations
    checkPhoneError() {
        if (this.TempPhone !== '') {
            if (this.telInputDirectiveRef.isInputValid()) {
                this.fieldErrors.Phone = null;
            } else {
                this.fieldErrors.Phone = 'Enter a valid phone number';
            }
        } else {
            this.fieldErrors.Phone = null;
        }
    }

    getNumber(obj: string) {
        this.User.phone = obj;
        if (this.telInputDirectiveRef.isInputValid()) {
            this.fieldErrors.Phone = null;
        }
    }


    // Create Department
    openNewDepartmentModal() {
        const initialState = {
            title: 'Add Department'
        };
        this.modalRef = this.modalService.open(DepartmentsAddComponent, this.modalConfig);
        this.modalRef.componentInstance.initialState = initialState;
        this.modalRef.componentInstance.departmentCreated.subscribe(
            () => {
                this.modalRef.close();
                this.notify.success('Department was created successfully!');
                this.loadFreshDepartmentsList(true);
                // this.reloadPage();
            }
        );
    }



    // User (Retrieve, Create, Edit & Delete)
    getUserForEdit() {
        this.editUserReady = false;

        this.userService.getUser(this.User)
            .subscribe(
                // success
                response => {
                    // this.User = response;

                    // make modifications
                    this.User.gender = this.User.gender === 'Female' || this.User.gender === 'female' ? 'female' : 'male';
                    this.User.emailAddress = response.emailAddress;
                    this.User.departmentId = response.departmentId + '';
                    this.User.address = response.address;
                    this.User.status = response.status === 'Active' ? 'true' : 'false';

                    // Get copy of user to determine if change has been made
                    this.OriginalUser = JSON.stringify(this.User);

                    this.editUserReady = true;
                },

                // error
                error => {
                    this.notify.error('Problem loading user information, please reload page.');
                    this.editUserReady = false;
                }
            );
    }

    save() {
        this.processing = true;
        this.fieldErrors = {};

        // create new object to contain staff information
        const userObj: UserViewModel = JSON.parse(JSON.stringify(this.User));

        // configure department id
        userObj.departmentId = userObj.departmentId && (userObj.departmentId + '').length > 0 ?
            parseInt(userObj.departmentId + '', 10) : null;

        // adjust names
        userObj.lastName = userObj.lastName.trim();
        userObj.firstName = userObj.firstName.trim();

        // configure deactivation status
        /*if (userObj.status === 'false') {
            userObj.status = false;
        } else {
            userObj.status = true;
        }*/

        if (!this.editMode) {
            userObj.status = 'true';
            this.createNewUser(userObj);
        } else {
            this.editUser(userObj);
        }
    }

    createNewUser(userObj: UserViewModel) {
        this.userService.createUser(userObj)
        .subscribe(

            // success
            (response) => {
                // tell parent component to reload users list
                this.userCreated.emit();
                this.processing = false;
            },

            // error
            error => {
                const allErrors: MyValidationErrors = this.validationErrorService.showValidationErrors(error);
                this.fieldErrors = allErrors.fieldErrors;
                if (this.fieldErrors.DuplicateUserName) {
                    this.fieldErrors.EmailAddress = 'This email address is already taken';
                }
                if (this.fieldErrors.departmentId) {
                    this.fieldErrors.Department = 'Select a department';
                }
                this.processing = false;
            }
        );
    }

    editUser(userObj: UserViewModel) {
        // if changes were made to staff information then update staff
        if (this.OriginalUser !== JSON.stringify(this.User)) {
            const myUser = userObj;
            myUser.role = this.initialState.User.role;

            this.userService.editUser(myUser)
            .subscribe(

                // success
                () => {
                    // tell parent component to reload staff list
                    this.userEdited.emit();
                    this.processing = false;
                },

                // error
                error => {
                    const allErrors: MyValidationErrors = this.validationErrorService.showValidationErrors(error);
                    this.fieldErrors = allErrors.fieldErrors;
                    if (this.fieldErrors.DuplicateUserName) {
                        this.fieldErrors.EmailAddress = 'This email address is already taken';
                    }
                    if (this.fieldErrors.departmentId) {
                        this.fieldErrors.Department = 'Select a department';
                    }
                    this.processing = false;
                }
            );
        }
    }

    deactivate() {
        // show another modal asking to delete or not
        const modalRef = this.modalService.open(ConfirmDeleteComponent, this.modalConfig);
        modalRef.componentInstance.ModalContent = {
            item: 'User',
            action: 'Deactivate'
        };
        modalRef.componentInstance.completeDelete.subscribe(
            () => {
                // close modal
                modalRef.close();

                // deactivate
                this.completeDeactivate();
            }
        );
    }

    completeDeactivate() {
        this.deleting = true;
        this.userService.deactivateUser(this.User)
        .subscribe(

            // success
            (response) => {
                // tell parent component to reload staff list
                this.userDeleted.emit();
                this.deleting = false;
            },

            // error
            error => {
                const allErrors: MyValidationErrors = this.validationErrorService
                .showValidationErrors(error, true);
                this.fieldErrors = allErrors.fieldErrors;
                this.deleting = false;
            }
        );
    }



    // Changes
    changesMade() {
        /*if (!this.editMode) {
            return false;
        }*/
        const staffInfoChanged = this.OriginalUser !== JSON.stringify(this.User);
        return staffInfoChanged;
    }

    nameEmpty() {
        const lastNameEmpty = this.User.lastName === null || this.User.lastName.trim().length === 0;
        const firstNameEmpty = this.User.firstName === null || this.User.firstName.trim().length === 0;
        return lastNameEmpty || firstNameEmpty;
    }

}
