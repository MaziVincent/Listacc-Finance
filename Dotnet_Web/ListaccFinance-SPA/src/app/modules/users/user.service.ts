import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PaginatedResult } from 'src/app/models/pagination';
import { UserViewModel } from 'src/app/models/user';
import { map } from 'rxjs/operators';

@Injectable()
export class UserService {
    private usersBase = environment.Url + 'user';

    constructor(private httpClient: HttpClient) { }

    // Get list of users
    getUserList(pageNumber?: number, itemsPerPage?: number, searchTerm?: string, role?: string[], deactivated?: string)
    : Observable<PaginatedResult<UserViewModel[]>> {
        const paginatedResult = new PaginatedResult<UserViewModel[]>();
        let params = new HttpParams();
        if (role.length === 0){
            role = ['Admin', 'Member'];
        }
        params = new HttpParams({ fromObject: {role}});
        if (deactivated != null) {
            params = params.append('status', deactivated);
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
                paginatedResult.pagination = response.pagination;
                paginatedResult.result = response.users;
                return paginatedResult;
            })
        );
    }

    // Create User
    createUser(user: UserViewModel): Observable<any> {
        return this.httpClient.post(this.usersBase + '/create', user);
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
}
