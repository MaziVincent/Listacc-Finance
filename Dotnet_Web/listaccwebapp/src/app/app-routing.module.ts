import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AppRoutes } from './routes';
import { HomeComponent } from './home/home.component';
import { LayoutComponent } from './layout/layout.component';
import { AboutComponent } from './about/about.component';
import { TeamComponent } from './team/team.component';
import { TestimonialsComponent } from './testimonials/testimonials.component';
import { ServicesComponent } from './services/services.component';
import { SolutionsComponent } from './solutions/solutions.component';
import { ContactsComponent } from './contacts/contacts.component';
import { BlogComponent } from './blog/blog.component';
import { AcademyComponent } from './academy/academy.component';
import { LoginComponent } from './login/login.component';
import { PortalComponent } from './portal/portal.component';
import { FinanceLoginGuard } from './our-apps/finance/guards/finance-login.guard';
import { BasicRoutesLayoutComponent } from './basic-routes-layout/basic-routes-layout.component';
import { FinanceAuthGuard } from './our-apps/finance/guards/finance-auth.guard';
import { FinanceRootComponent } from './finance-root/finance-root.component';


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
            { path: 'layout', component: LayoutComponent },
            { path: 'about', component: AboutComponent },
            { path: 'team', component: TeamComponent },
            { path: 'testimonials', component: TestimonialsComponent },
            { path: 'services', component: ServicesComponent },
            { path: 'solutions', component: SolutionsComponent },
            { path: 'contacts', component: ContactsComponent },
            { path: 'blog', component: BlogComponent },
            { path: 'academy', component: AcademyComponent },
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
