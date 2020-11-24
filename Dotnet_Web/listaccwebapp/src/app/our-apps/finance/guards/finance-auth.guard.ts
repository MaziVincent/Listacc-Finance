import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { FinanceAuthService } from '../services/finance-auth.service';
import { Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class FinanceAuthGuard implements CanActivate {
    constructor(private router: Router, private authService: FinanceAuthService) {}

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
        if (this.authService.isUserLoggedIn()) {
            return true;
        }

        // not logged in so redirect to login page with return url
        this.authService.logout();
        this.router.navigate(['apps/finance/login'], { queryParams: { returnUrl: state.url }});
        return false;
    }
}
