import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({
    providedIn: 'root'
})
export class AuthGuard implements CanActivate {
    constructor(private router: Router, private authService: AuthService) {}

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
        if (this.authService.isUserLoggedIn()) {
            return true;
        }

        // not logged in so redirect to login page with return url
        this.authService.logout();
        this.router.navigate(['login'], { queryParams: { returnUrl: state.url }});
        return false;
    }
}