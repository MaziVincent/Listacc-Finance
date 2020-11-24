import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { FinanceAuthService } from '../services/finance-auth.service';
import { Observable } from 'rxjs';

@Injectable()
export class UserTokenInterceptor implements HttpInterceptor {
    constructor(private auth: FinanceAuthService) { }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        request = request.clone({
            setHeaders: {
                Authorization: `Bearer ${this.auth.getToken()}`
            }
        });
        return next.handle(request);
    }
}
