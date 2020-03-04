import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { AuthService } from 'src/app/services/auth.service';
import { LoginUser } from 'src/app/models/user';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

@Injectable()
export class LoginService {
    private authBase = environment.Url + 'auth';

    constructor(private http: HttpClient,
                private authService: AuthService) { }

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
