import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { LayoutComponent } from 'src/app/modules/shared/components/layout/layout.component';
import { NotificationService } from 'src/app/services/notification.service';
import { Pagination } from 'src/app/models/pagination';
import { Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged, switchMap } from 'rxjs/operators';
import { UserViewModel } from 'src/app/models/user';
import { UserService } from '../../user.service';

@Component({
  selector: 'app-users-list',
  templateUrl: './users-list.component.html',
  styleUrls: ['./users-list.component.css']
})
export class UsersListComponent implements OnInit {

    contentLoading = false;
    pagination: Pagination;

    Users: UserViewModel[] = [];
    // DisplayeUsers: UserListViewModel[] = [];

    @ViewChild('searchText', {static: false}) searchText: ElementRef;
    lastSearchTerm: string = null;
    private searchTerms = new Subject<string>();

    Filter: {role: string[], deactivated: string} = { role : [], deactivated: 'false'};
    SelectedFilter: {role: string[], deactivated: string} = { role : [], deactivated: 'false'};
    filtered: boolean;
    FilteredElements: any[] = [];
    RoleFullNames: any;

    constructor(private titleService: Title,
                parentComponent: LayoutComponent,
                private notify: NotificationService,
                private userService: UserService) {

        // set page title
        this.titleService.setTitle('Users | Listacc Finance');

        // set page heading
        parentComponent.PageHeading = 'users';
        parentComponent.PageSubHeading = '';

        // initialize pagination parameters
        this.pagination = {
            currentPage: 1,
            itemsPerPage: 20,
            totalCount: 0,
            maxSize: 5,
            rotate: true
        };

        this.RoleFullNames = {
            Admin : 'Administrator',
            Member : 'Member'
        };
    }

    ngOnInit() {
        // this.createDummyData();
        this.loadUsers();
        this.setupSearch();
    }

    setupSearch() {
        this.searchTerms.pipe(
            // wait 500ms after each keystroke before considering the term
            debounceTime(400),

            // ignore new term if same as previous term
            distinctUntilChanged(),

            // switch to new search observable each time the term changes
            switchMap((term: string) => this.userService.getUserList(1, this.pagination.itemsPerPage, term,
                this.Filter.role, this.Filter.deactivated))
        ).subscribe(response => {
            this.pagination.currentPage = response.pagination.currentPage;
            this.pagination.totalCount = response.pagination.totalCount;

            this.Users = response.result;

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
    loadUsers(pageNumber?: number) {
        this.contentLoading = true;

        pageNumber = pageNumber || this.pagination.currentPage;
        // ensure this uses search and filter
        this.userService.getUserList(pageNumber, this.pagination.itemsPerPage, this.lastSearchTerm,
            this.Filter.role, this.Filter.deactivated)
        .subscribe(
            // success
            response => {
                this.pagination.currentPage = response.pagination.currentPage;
                this.pagination.totalCount = response.pagination.totalCount;

                this.Users = response.result;
                this.SelectedFilter.deactivated = this.Filter.deactivated;
                this.SelectedFilter.role = [];
                this.Filter.role.forEach((value: string) => {
                    this.SelectedFilter.role.push(value);
                });

                // show filters
                this.filtered = false;
                this.FilteredElements = [];
                for (const key in this.Filter) {
                    if (this.Filter.hasOwnProperty(key)) {
                        const value = this.Filter[key];
                        if (Array.isArray(value)) {
                            if (value.length > 0) {
                                this.filtered = true;
                                for (const index in value) {
                                    if (value.hasOwnProperty(index)) {
                                        this.FilteredElements.push({key, value: this.RoleFullNames[value[index]], index });
                                    }
                                }
                            }
                        } else if (value !== null) {
                            this.filtered = true;
                            this.FilteredElements.push({key: key === 'deactivated' ? 'Status' : key,
                                 value: (key === 'deactivated' ? (value === 'true' ? 'Inactive' : 'Active') : value)});
                        }
                    }
                }

                this.contentLoading = false;
            },

            // error
            error => {
                this.notify.error('Problem loading user list, please reload page.');
                this.contentLoading = false;
            }
        );
    }


    // Filter Opeations
    clearFilterForReload() {
        this.Filter.role = [];
        // this.Filter.deactivated = 'false';
    }

    clearFilter() {
        this.Filter.role = [];
        this.Filter.deactivated = 'false';
        if (this.filtered) {
            this.loadUsers(1);
        }
    }

    handleFilterOpen() {
        // return to previous settings
        this.Filter.deactivated = this.SelectedFilter.deactivated;
        this.Filter.role = [];
        this.SelectedFilter.role.forEach((value: string) => {
            this.Filter.role.push(value);
        });
    }

    toggleFilterRole(role: string) {
        const index = this.Filter.role.indexOf(role);
        if (index === -1) {
            this.Filter.role.push(role);
        } else {
            this.Filter.role.splice(index, 1);
        }
    }

    removeFilter(key: string, index?: number) {
        if (index === null) {
        this.Filter[key] = null;
        } else if (Array.isArray(this.Filter[key])) {
        this.Filter[key].splice(index, 1);
        }
        this.loadUsers(1);
    }

    filter() {
        this.loadUsers(1);
    }
}
