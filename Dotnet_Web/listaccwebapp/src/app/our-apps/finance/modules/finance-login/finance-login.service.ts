import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { FinanceAuthService } from '../../services/finance-auth.service';
import { LoginUser } from '../../models/user';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

@Injectable()
export class FinanceLoginService {

    private authBase = environment.FinanceUrl + 'auth';

    constructor(private http: HttpClient,
                private authService: FinanceAuthService) { }

    signIn(user: LoginUser): Observable<LoginUser> {
        return this.http.post<LoginUser>(this.authBase + '/userlogin', user)
        .pipe(
            tap((response: any) => {
                if (response) {
                    this.authService.setToken(JSON.stringify(response));
                }
            })
        );
    }
}
