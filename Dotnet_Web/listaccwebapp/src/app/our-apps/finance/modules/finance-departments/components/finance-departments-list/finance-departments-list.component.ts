import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { Pagination } from 'src/app/our-apps/finance/models/pagination';
import { DepartmentViewModel } from 'src/app/our-apps/finance/models/department';
import { Subject } from 'rxjs';
import { NgbModalRef, NgbModalOptions, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Title } from '@angular/platform-browser';
import { LayoutComponent } from '../../../finance-shared/components/layout/layout.component';
import { FinanceNotificationService } from '../../../finance-shared/services/finance-notification.service';
import { FinanceDepartmentsService } from '../../finance-departments.service';
import { debounceTime, distinctUntilChanged, switchMap } from 'rxjs/operators';
import { FinanceDepartmentsAddComponent } from '../finance-departments-add/finance-departments-add.component';

@Component({
  selector: 'app-finance-departments-list',
  templateUrl: './finance-departments-list.component.html',
  styleUrls: ['./finance-departments-list.component.css']
})
export class FinanceDepartmentsListComponent implements OnInit {

    contentLoading = false;
    pagination: Pagination;

    Departments: DepartmentViewModel[] = [];

    @ViewChild('searchText', {static: false}) searchText: ElementRef;
    lastSearchTerm: string = null;
    private searchTerms = new Subject<string>();

    modalRef: NgbModalRef;
    private modalConfig: NgbModalOptions = {
        // size: 'md',
        centered: true,
        keyboard: false,
        backdrop: 'static'
    };

    constructor(private titleService: Title,
                parentComponent: LayoutComponent,
                private notify: FinanceNotificationService,
                private userService: FinanceDepartmentsService,
                private modalService: NgbModal) {

        // set page title
        this.titleService.setTitle('Departments | Listacc Finance');

        // set page heading
        parentComponent.PageHeading = 'Departments';
        parentComponent.PageSubHeading = '';

        // initialize pagination parameters
        this.pagination = {
            currentPage: 1,
            itemsPerPage: 20,
            totalCount: 0,
            maxSize: 5,
            rotate: true
        };
    }

    ngOnInit() {
        // this.createDummyData();
        this.loadDepartments();
        this.setupSearch();
    }

    setupSearch() {
        this.searchTerms.pipe(
            // wait 500ms after each keystroke before considering the term
            debounceTime(400),

            // ignore new term if same as previous term
            distinctUntilChanged(),

            // switch to new search observable each time the term changes
            switchMap((term: string) => this.userService.getDepartmentList(1, this.pagination.itemsPerPage, term))
        ).subscribe(response => {
            this.pagination.currentPage = response.pagination.currentPage;
            this.pagination.totalCount = response.pagination.totalCount;

            this.Departments = response.result;

            this.contentLoading = false;
        });
    }

    createDummyData() {
        /*this.Users = [
        {
            firstName : 'Ebuka',
            lastName : 'Olisaemeka',
            gender : 'Male',
            email : 'olisaemekaebuka@gmail.com',
            phone : '+2347039466051',
            role : 'Rider'
        },
        {
            firstName : 'Vincent',
            lastName : 'Mazi',
            gender : 'Male',
            email : 'vincentgeorge@gmail.com',
            phone : '+2347039466051',
            role : 'Administrator'
        },
        {
            firstName : 'Nneka',
            lastName : 'Ogbonna',
            gender : 'Female',
            email : 'ogbonnaneka@gmail.com',
            phone : '+2347039466051',
            role : 'Administrator'
        },
        {
            firstName : 'Uchechi',
            lastName : 'Okah',
            gender : 'Female',
            email : 'okahuchechi@gmail.com',
            phone : '+2347039466051',
            role : 'Front Desk Operator'
        },
        {
            firstName : 'Agozie',
            lastName : 'Okechukwu',
            gender : 'Male',
            email : 'victoragome@gmail.com',
            phone : '+2347039466051',
            role : 'Rider'
        }
        ];
        this.pagination.totalCount = this.Users.length;
        this.DisplayedUsers = this.Users.slice(0, this.pagination.itemsPerPage);*/
    }

    // Content Loading Operations
    loadDepartments(pageNumber?: number) {
        this.contentLoading = true;

        pageNumber = pageNumber || this.pagination.currentPage;
        // ensure this uses search and filter
        this.userService.getDepartmentList(pageNumber, this.pagination.itemsPerPage, this.lastSearchTerm)
            .subscribe(
                // success
                response => {
                    this.pagination.currentPage = response.pagination.currentPage;
                    this.pagination.totalCount = response.pagination.totalCount;

                    this.Departments = response.result;
                    this.contentLoading = false;
                },

                // error
                error => {
                    this.notify.error('Problem loading departments list, please reload page.');
                    this.contentLoading = false;
                }
            );
    }

    reloadPage() {
        this.searchText.nativeElement.value = '';
        this.setupSearch();
        this.lastSearchTerm = null;
        this.loadDepartments(1);
    }

    pageChanged(newPageNumber: number): void {
        /*const startItem = (newPageNumber - 1) * this.pagination.itemsPerPage;
        const endItem = newPageNumber * this.pagination.itemsPerPage;
        this.DisplayedStaff = this.Staff.slice(startItem, endItem);*/

        this.pagination.currentPage = newPageNumber;
        this.loadDepartments();
        window.scrollTo(0, 0);
    }


    // Search Operations
    search(term: string): void {
        this.contentLoading = true;
        this.searchTerms.next(term);
        this.lastSearchTerm = term;
    }


    // Create & Edit Operations
    openNewDepartmentModal() {
        const initialState = {
            title: 'Add Department'
        };
        this.modalRef = this.modalService.open(FinanceDepartmentsAddComponent, this.modalConfig);
        this.modalRef.componentInstance.initialState = initialState;
        this.modalRef.componentInstance.departmentCreated.subscribe(
            () => {
                this.modalRef.close();
                this.notify.success('Department was created successfully!');
                this.reloadPage();
            }
        );
    }

    openDepartmentEditModal(selectedDepartment: DepartmentViewModel) {
        const initialState = {
            title: 'Edit Department',
            Department: selectedDepartment
        };
        this.modalRef = this.modalService.open(FinanceDepartmentsAddComponent, this.modalConfig);
        this.modalRef.componentInstance.initialState = initialState;
        this.modalRef.componentInstance.departmentEdited.subscribe(
            () => {
                this.modalRef.close();
                this.notify.success('Department was updated successfully!');
                this.reloadPage();
            }
        );
        this.modalRef.componentInstance.departmentDeleted.subscribe(
            () => {
                this.modalRef.close();
                this.notify.success('Department was deleted successfully!');
                this.reloadPage();
            }
        );
    }

}
