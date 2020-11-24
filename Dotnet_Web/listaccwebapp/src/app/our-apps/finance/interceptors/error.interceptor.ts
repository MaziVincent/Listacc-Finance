import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { FinanceAuthService } from '../services/finance-auth.service';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
    constructor(private auth: FinanceAuthService) {}

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>>  {
        return next.handle(request).pipe(
            catchError(err => {
                if (err instanceof HttpErrorResponse) {
                    if (err.status === 401) {
                        // redirect to the login route
                        this.auth.logout();
                        location.reload();
                    }
                    const error = err.error.errors || err.error || err.message || err.statusText;
                    return throwError(error);
                }
            })
        );
    }
}
