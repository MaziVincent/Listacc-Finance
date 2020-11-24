import { Routes, Route } from '@angular/router';
import { FinanceRoutes } from './our-apps/finance/finance-routes';
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

export const AppRoutes: Routes = [];

const BasicRoutes: Routes = [
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
];


// add basic routes
Array.from(BasicRoutes.values()).forEach((value: Route) => {
    AppRoutes.push(value);
});

// add finance routes
Array.from(FinanceRoutes.values()).forEach((value: Route) => {
    AppRoutes.push(value);
});
