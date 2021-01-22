import { FinanceRootComponent } from '../../components/finance-root/finance-root.component';
import { Routes } from '@angular/router';

export const FinanceRoutes: Routes = [
    {
        path: 'apps',
        // canActivate: [FinanceLoginGuard],
        component: FinanceRootComponent
        /*loadChildren: () => import ('./modules/finance-login/finance-login.module')
            .then(mod => mod.FinanceLoginModule)*/
    }
];
