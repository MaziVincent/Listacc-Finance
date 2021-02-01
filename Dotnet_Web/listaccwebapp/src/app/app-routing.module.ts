import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { AboutComponent } from './components/about/about.component';
import { TeamComponent } from './components/team/team.component';
import { TestimonialsComponent } from './components/testimonials/testimonials.component';
import { ServicesComponent } from './components/services/services.component';
import { SolutionsComponent } from './components/solutions/solutions.component';
import { ContactsComponent } from './components/contacts/contacts.component';
import { BlogComponent } from './components/blog/blog.component';
import { AcademyComponent } from './components/academy/academy.component';
import { LoginComponent } from './components/login/login.component';
import { PortalComponent } from './components/portal/portal.component';
import { FinanceLoginGuard } from './our-apps/finance/guards/finance-login.guard';
import { FinanceAuthGuard } from './our-apps/finance/guards/finance-auth.guard';
import { FinanceRootComponent } from './components/finance-root/finance-root.component';
import { BasicRoutesLayoutComponent } from './components/basic-routes-layout/basic-routes-layout.component';
import { AcademyResolver } from './resolvers/academy.resolver';


const routes: Routes = [
    {
        path: 'apps/finance',
        component: FinanceRootComponent,
        children : [
            {
                path: 'login',
                canActivate: [FinanceLoginGuard],
                // loadChildren: './our-apps/finance/modules/finance-login/finance-login.module#FinanceLoginModule'
                loadChildren: () => import ('./our-apps/finance/modules/finance-login/finance-login.module')
                    .then(mod => mod.FinanceLoginModule)
            },
            {
                path: 'departments',
                canActivate: [FinanceAuthGuard],
                // loadChildren: './our-apps/finance/modules/finance-departments/finance-departments.module#FinanceDepartmentsModule'
                loadChildren: () => import ('./our-apps/finance/modules/finance-departments/finance-departments.module')
                    .then(mod => mod.FinanceDepartmentsModule)
            },
            {
                path: 'users',
                canActivate: [FinanceAuthGuard],
                // loadChildren: './our-apps/finance/modules/finance-users/finance-users.module#FinanceUsersModule'
                loadChildren: () => import ('./our-apps/finance/modules/finance-users/finance-users.module')
                    .then(mod => mod.FinanceUsersModule)
            },
            {
                path: '',
                canActivate: [FinanceAuthGuard],
                // loadChildren: './our-apps/finance/modules/finance-dashboard/finance-dashboard.module#FinanceDashboardModule'
                loadChildren: () => import ('./our-apps/finance/modules/finance-dashboard/finance-dashboard.module')
                    .then(mod => mod.FinanceDashboardModule)
            }
        ]
    },
    {
        path: '',
        component: BasicRoutesLayoutComponent,
        children: [
            { path: '', component: HomeComponent },
            { path: 'about', component: AboutComponent },
            { path: 'team', component: TeamComponent },
            { path: 'testimonials', component: TestimonialsComponent },
            { path: 'services', component: ServicesComponent },
            { path: 'solutions', component: SolutionsComponent },
            { path: 'contacts', component: ContactsComponent },
            { path: 'blog', component: BlogComponent },
            {
                path: 'academy', component: AcademyComponent,
                resolve: {
                    academyInfo: AcademyResolver
                }
            },
            { path: 'login', component: LoginComponent },
            { path: 'portal', component: PortalComponent }
        ]
    }
];

@NgModule({
    imports: [RouterModule.forRoot(routes, {scrollPositionRestoration: 'enabled'})],
    exports: [RouterModule]
})
export class AppRoutingModule { }
