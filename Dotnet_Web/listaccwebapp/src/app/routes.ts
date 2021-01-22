import { Routes, Route } from '@angular/router';
import { FinanceRoutes } from './our-apps/finance/finance-routes';
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

export const AppRoutes: Routes = [];

const BasicRoutes: Routes = [
    { path: '', component: HomeComponent },
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
