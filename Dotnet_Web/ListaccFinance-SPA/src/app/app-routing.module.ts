import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginGuard } from './guards/login.guard';
import { AuthGuard } from './guards/auth.guard';


const routes: Routes = [
    {
        path: 'login',
        canActivate: [LoginGuard],
        loadChildren: () => import ('./modules/login/login.module')
            .then(mod => mod.LoginModule)
    },
    {
        path: 'users',
        canActivate: [AuthGuard],
        loadChildren: () => import ('./modules/users/users.module')
            .then(mod => mod.UsersModule)
    },
    {
        path: '',
        canActivate: [AuthGuard],
        loadChildren: () => import ('./modules/dashboard/dashboard.module')
            .then(mod => mod.DashboardModule)
    }
];

@NgModule({
    imports: [RouterModule.forRoot(routes, {
        scrollPositionRestoration: 'top'
    })],
    exports: [RouterModule]
})
export class AppRoutingModule { }
