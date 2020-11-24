import { Injectable } from '@angular/core';
import { MyUser, TokenUser } from '../models/user';
import { JwtHelperService } from '@auth0/angular-jwt';

@Injectable({
    providedIn: 'root'
})
export class FinanceAuthService {

    private jwtHelper: JwtHelperService;
    private currentUser: MyUser;

    constructor() {
        this.jwtHelper = new JwtHelperService();
    }

    // Token
    setToken(tokenString: string) {
        localStorage.setItem('user', tokenString);
    }

    getToken(): string {
        return this.getUserAndToken() ? this.getUserAndToken().tokenString : '';
    }


    // User
    getUser(): MyUser {
        if (!this.currentUser) {
            const userAndToken = this.getUserAndToken();
            this.currentUser = new MyUser(userAndToken);
        }
        return this.currentUser;
    }

    clearCurrentUser() {
        this.currentUser = null;
    }

    isUserLoggedIn() {
        const token = this.getUserAndToken() ? (this.getUserAndToken().tokenString ? this.getUserAndToken().tokenString : null) : null;
        if (token) {
            if (!this.jwtHelper.isTokenExpired(token)) {
                const decodedToken = this.jwtHelper.decodeToken(token);
                if (decodedToken != null) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    private getUserAndToken(): TokenUser {
        return JSON.parse(localStorage.getItem('user')) as TokenUser;
    }

    logout() {
        // remove user from local storage to log user out
        localStorage.removeItem('user');
        this.clearCurrentUser();
    }

}
