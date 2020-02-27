import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { PageNotFoundComponent } from '../shared/components/page-not-found/page-not-found.component';
import { LayoutComponent } from '../shared/components/layout/layout.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';

const routes: Routes = [
    {
        path: '',
        component: LayoutComponent,
        data: {
            home: '/',
            activePage: 'dashboard'
        },
        children: [
            {
                path: '',
                component: DashboardComponent,
                data: {
                    breadcrumb: [{label: 'Dashboard', url: ''}],
                    showBackButton: false
                },
                resolve: {}
            },
            {
                path: '**',
                component: PageNotFoundComponent,
                data: {
                    breadcrumb: [
                        { label: 'Dashboard', url: '/' },
                        { label: 'Page Not Found', url: '' }
                    ]
                }
            }
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class DashboardRoutingModule { }
