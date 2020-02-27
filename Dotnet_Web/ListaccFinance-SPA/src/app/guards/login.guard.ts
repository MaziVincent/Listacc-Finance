import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Injectable } from '@angular/core';
import { AuthService } from '../services/auth.service';

@Injectable({
    providedIn: 'root'
})
export class LoginGuard implements CanActivate{

    constructor(private router: Router, private authService: AuthService) {}

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
        if (!this.authService.isUserLoggedIn()) {
            return true;
        }

        // logged in, so redirect to home page
        this.router.navigate(['']);
        return false;
    }
}