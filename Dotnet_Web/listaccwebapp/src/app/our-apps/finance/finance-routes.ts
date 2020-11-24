import { FinanceRootComponent } from './../../finance-root/finance-root.component';
import { Routes } from '@angular/router';
import { FinanceLoginGuard } from './guards/finance-login.guard';

export const FinanceRoutes: Routes = [
    {
        path: 'apps',
        // canActivate: [FinanceLoginGuard],
        component: FinanceRootComponent
        /*loadChildren: () => import ('./modules/finance-login/finance-login.module')
            .then(mod => mod.FinanceLoginModule)*/
    }
];
