import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';
import { TokenUser, MyUser } from '../models/user';

@Injectable({
    providedIn: 'root'
})
export class AuthService {

    private jwtHelper: JwtHelperService;
    private currentUser: MyUser;

    constructor() {
        this.jwtHelper = new JwtHelperService();
    }

    // Token
    setToken(tokenString: string) {
        localStorage.setItem('user', tokenString);
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
        const token = this.getUserAndToken() ? (this.getUserAndToken().token ? this.getUserAndToken().token : null) : null;
        if (token) {
            if (!this.jwtHelper.isTokenExpired(token)) {
                const decodedToken = this.jwtHelper.decodeToken(token);
                if (decodedToken != null) return true;
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
    }
}
