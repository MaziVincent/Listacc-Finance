import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { FinanceAuthService } from '../services/finance-auth.service';

@Injectable({
    providedIn: 'root'
})
export class FinanceLoginGuard implements CanActivate{

    constructor(private router: Router, private authService: FinanceAuthService) {}

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
        if (!this.authService.isUserLoggedIn()) {
            return true;
        }

        // logged in, so redirect to home page
        this.router.navigate(['/apps/finance']);
        return false;
    }
}