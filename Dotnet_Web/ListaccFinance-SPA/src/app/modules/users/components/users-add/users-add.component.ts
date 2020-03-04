import { Component, OnInit, ViewChild, Output, EventEmitter, Input } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Ng2TelInput } from 'ng2-tel-input';
import { UserViewModel, StatusOption } from 'src/app/models/user';
import { NgbModalOptions, NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { UserService } from '../../user.service';
import { ValidationErrorService } from 'src/app/services/validation-error.service';
import { SharedService } from 'src/app/services/shared.service';
import { ConfirmExitComponent } from 'src/app/modules/shared/components/confirm-exit/confirm-exit.component';
import { MyValidationErrors } from 'src/app/models/my-validation-errors';
import { ConfirmDeleteComponent } from 'src/app/modules/shared/components/confirm-delete/confirm-delete.component';

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

    User: UserViewModel;
    OriginalUser: string;
    TempPhone: string;
    States: string[];
    fieldErrors: any = {};
    processing: boolean;
    deleting: boolean;
    editMode: boolean;

    profilePictureConfig: any;

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

    constructor(private activeModal: NgbActiveModal,
                private modalService: NgbModal,
                shared: SharedService,
                private userService: UserService,
                private validationErrorService: ValidationErrorService) {
        this.User = new UserViewModel();

        this.StatusOptions = shared.StatusOptions;
    }

    ngOnInit() {
        // if in edit mode
        if (this.initialState.User !== undefined) {

            // clone original object so that changes do not reflect on the list
            this.User = JSON.parse(JSON.stringify(this.initialState.User));

            // set deactivated status
            this.User.deactivated = this.User.status ? 'true' : 'false';
            /*if (this.User.addresses.length === 0) {
                this.User.addresses = [{ id: 0, street: '', city: '', state: '', country: 'Nigeria'}];
            }*/
            this.editMode = true;
            this.TempPhone = this.User.phoneNumber;
        } else {
            // set default values
            this.User.deactivated = 'false';
            this.User.gender = 'female';
            this.User.role = 'Admin';
        }
        this.OriginalUser = JSON.stringify(this.User);

        // set parameters for profile picture component
        /*this.profilePictureConfig = {
            cssClass: 'centered',
            photoUrl: this.editMode && this.Staff.photoUrl && this.Staff.photoUrl !== '' ? this.Staff.photoUrl : null
        };*/
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
        this.User.phoneNumber = obj;
        if (this.telInputDirectiveRef.isInputValid()) {
            this.fieldErrors.Phone = null;
        }
    }


    // Address Methods
    /*addNewAddress() {
        this.User.addresses.push({ id: 0, street: '', city: '', state: '', country: 'Nigeria'});
    }

    removeAddress(index: number) {
        const addr = this.User.addresses[index];
        if (addr.id !== 0) {
            addr.deleted = true;
        } else {
            this.User.addresses.splice(index, 1);
        }
    }

    getActiveAddresses() {
        return this.User.addresses.filter(x => x.deleted === false || x.deleted === null || x.deleted === undefined);
    }

    trackAddressByIndex(index: number, obj: any): any {
        return index;
    }*/


    // Staff (Create, Edit & Delete)
    save() {
        this.processing = true;
        this.fieldErrors = {};

        // create new object to contain staff information
        const userObj: UserViewModel = JSON.parse(JSON.stringify(this.User));

        // configure address
        // this.configureAddresses(userObj);

        // configure deactivation status
        if (userObj.deactivated === 'false') {
            userObj.status = false;
        } else {
            userObj.status = true;
        }

        if (!this.editMode) {
            userObj.status = true;
            this.createNewUser(userObj);
        } else {
            this.editUser(userObj);
        }
    }

    /*configureAddresses(staffObj: StaffViewModel) {
        for (let index = staffObj.addresses.length - 1; index >= 0; index --) {
            const add = staffObj.addresses[index];
            if ((add.street === undefined || add.street.trim() === '') && (add.city === undefined || add.city.trim() === '') &&
                (add.state === undefined || add.state.trim() === '')) {
                staffObj.addresses.splice(index, 1);
            }
        }
    }*/

    createNewUser(userObj: UserViewModel) {
        this.userService.createUser(userObj)
        .subscribe(

            // success
            (response) => {
                //if (this.profilePictureComponent.Picture === null) {
                    // tell parent component to reload staff list
                    this.userCreated.emit();
                    this.processing = false;
                /*} else {
                    this.uploadStaffPhoto(response.id, false);
                }*/
            },

            // error
            error => {
                const allErrors: MyValidationErrors = this.validationErrorService.showValidationErrors(error);
                this.fieldErrors = allErrors.fieldErrors;
                if (this.fieldErrors.DuplicateUserName) {
                    this.fieldErrors.Email = 'This email address is already taken';
                }
                this.processing = false;
            }
        );
    }

    editUser(userObj: UserViewModel) {

        // check if a new picture was selected
        // let newPictureSelected = this.profilePictureComponent.Picture !== null;
        // newPictureSelected = newPictureSelected ?
            // (this.Staff.photoUrl && this.Staff.photoUrl !== '' ? this.profilePictureComponent.newPictureSelected : true) : false;

        // if changes were made to staff information then update staff
        if (this.OriginalUser !== JSON.stringify(this.User)) {
            const myUser = userObj;
            // myUser.removePhoto = this.Staff.photoUrl !== null && this.Staff.photoUrl !== '' ? this.profilePictureComponent.pictureRemoved
               // : false;

            this.userService.editUser(myUser)
            .subscribe(

                // success
                () => {

                    /*if (newPictureSelected) {
                        this.uploadStaffPhoto(myStaff.id, true);
                    } else {*/
                        // tell parent component to reload staff list
                        this.userEdited.emit();
                        this.processing = false;
                    //}
                },

                // error
                error => {
                    const allErrors: MyValidationErrors = this.validationErrorService.showValidationErrors(error);
                    this.fieldErrors = allErrors.fieldErrors;
                    if (this.fieldErrors.DuplicateUserName) {
                        this.fieldErrors.Email = 'This email address is already taken';
                    }
                    this.processing = false;
                }
            );
        } /*else if (newPictureSelected) { // if picture was just changed
            this.uploadStaffPhoto(staffObj.id, true);
        } else if (this.Staff.photoUrl !== null && this.profilePictureComponent.pictureRemoved) { // if picture was just removed
            this.deleteStaffPhoto(staffObj.id);
        }*/
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


    // Picture Upload & Delete
    /*uploadStaffPhoto(staffId: number, update) {
        this.staffService.uploadStaffPhoto(staffId, this.profilePictureComponent.Picture)
        .subscribe(

            // success
            () => {
                // tell parent component to reload staff list
                if (!update) {
                    this.staffCreated.emit();
                } else {
                    this.staffEdited.emit();
                }
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

    deleteStaffPhoto(staffId: number) {
        this.staffService.deleteStaffPhoto(staffId)
        .subscribe(

            // success
            () => {
                // tell parent component to reload staff list
                this.staffEdited.emit();
                this.processing = false;
            },

            // error
            error => {
                const allErrors: MyValidationErrors = this.validationErrorService.showValidationErrors(error);
                this.fieldErrors = allErrors.fieldErrors;
                this.processing = false;
            }
        );
    }*/


    // Changes
    changesMade() {
        /*if (!this.editMode) {
            return false;
        }*/

        const staffInfoChanged = this.OriginalUser !== JSON.stringify(this.User);
        // const pictureRemoved = this.Staff.photoUrl && this.profilePictureComponent && this.profilePictureComponent.pictureRemoved;
        // let newPictureSelected = this.profilePictureComponent && this.profilePictureComponent.Picture;
        // newPictureSelected = newPictureSelected ?
            // (this.Staff.photoUrl && this.Staff.photoUrl !== '' ? this.profilePictureComponent.newPictureSelected : true) : false;
        return staffInfoChanged; // || pictureRemoved || newPictureSelected;
    }

}
