import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PaginatedResult } from 'src/app/models/pagination';
import { UserViewModel } from 'src/app/models/user';
import { map } from 'rxjs/operators';
import { DepartmentViewModel } from 'src/app/models/department';

@Injectable()
export class UserService {
    private usersBase = environment.Url + 'user';
    private departmentsBase = environment.Url + 'dept';

    DepartmentsList: DepartmentViewModel[];

    constructor(private httpClient: HttpClient) { }

    // USER
    // Get list of users
    getUserList(pageNumber?: number, itemsPerPage?: number, searchTerm?: string, role?: string[], status?: string)
    : Observable<PaginatedResult<UserViewModel[]>> {
        const paginatedResult = new PaginatedResult<UserViewModel[]>();
        let params = new HttpParams();
        if (role.length === 0) {
            role = ['Admin', 'Member'];
        }
        params = new HttpParams({ fromObject: {role}});
        if (status != null) {
            params = params.append('status', status);
        }
        if (pageNumber != null) {
            params = params.append('pageNumber', '' + pageNumber);
        }
        if (itemsPerPage != null) {
            params = params.append('pageSize', '' + itemsPerPage);
        }
        if (searchTerm != null) {
            params = params.append('searchString', searchTerm);
        }

        return this.httpClient.get<PaginatedResult<UserViewModel[]>>(this.usersBase, {params})
        .pipe(
            map((response: any) => {
                paginatedResult.pagination = response.data;
                paginatedResult.result = response.returnList;
                return paginatedResult;
            })
        );
    }

    // Get single user
    getUser(user: UserViewModel): Observable<UserViewModel> {
        return this.httpClient.get<UserViewModel>(this.usersBase + '/' + user.id);
    }

    // Create User
    createUser(user: UserViewModel): Observable<any> {
        if (user.role === 'Admin') {
            return this.httpClient.post(this.usersBase + '/createadmin', user);
        } else {
            return this.httpClient.post(this.usersBase + '/createmember', user);
        }
    }

    // Edit User
    editUser(user: UserViewModel): Observable<any> {
        return this.httpClient.put(this.usersBase + '/update', user);
    }

    // Deactivate user
    deactivateUser(user: UserViewModel): Observable<any> {
        return this.httpClient.put(this.usersBase + user.id + '/deactivate', null);
    }

    // Activate user
    activateUser(user: UserViewModel): Observable<any> {
        return this.httpClient.put(this.usersBase + user.id + '/activate', null);
    }


    // DEPARTMENT
    // Get list of departments
    getDepartments(): Observable<DepartmentViewModel[]> {
        return this.httpClient.get<DepartmentViewModel[]>(this.departmentsBase);
    }
}
