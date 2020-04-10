import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { DepartmentViewModel } from 'src/app/models/department';
import { Observable } from 'rxjs';
import { PaginatedResult } from 'src/app/models/pagination';
import { map } from 'rxjs/operators';

@Injectable()
export class DepartmentsService {

    private departmentsBase = environment.Url + 'dept';

    constructor(private httpClient: HttpClient) { }

    // Get list of departments
    getDepartmentList(pageNumber?: number, itemsPerPage?: number, searchTerm?: string)
    : Observable<PaginatedResult<DepartmentViewModel[]>> {
        const paginatedResult = new PaginatedResult<DepartmentViewModel[]>();
        let params = new HttpParams();
        if (pageNumber != null) {
            params = params.append('pageNumber', '' + pageNumber);
        }
        if (itemsPerPage != null) {
            params = params.append('pageSize', '' + itemsPerPage);
        }
        if (searchTerm != null) {
            params = params.append('searchString', searchTerm);
        }

        return this.httpClient.get<PaginatedResult<DepartmentViewModel[]>>(this.departmentsBase + '/search', {params})
        .pipe(
            map((response: any) => {
                paginatedResult.pagination = response.metaData;
                paginatedResult.result = response.retDept;
                return paginatedResult;
            })
        );
    }


    // Create Department
    createDepartment(dept: DepartmentViewModel): Observable<any> {
        return this.httpClient.post(this.departmentsBase + '/create/' + dept.name, null);
    }

    // Edit Department
    editDepartment(dept: DepartmentViewModel): Observable<any> {
        return this.httpClient.put(this.departmentsBase + '/edit/' + dept.id + '/' + dept.name, null);
    }
}
