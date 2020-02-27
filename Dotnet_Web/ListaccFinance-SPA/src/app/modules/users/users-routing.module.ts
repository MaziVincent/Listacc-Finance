import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LayoutComponent } from '../shared/components/layout/layout.component';
import { PageNotFoundComponent } from '../shared/components/page-not-found/page-not-found.component';
import { UsersListComponent } from './components/users-list/users-list.component';

const routes: Routes = [
    {
        path: '',
        component: LayoutComponent,
        data: {
            home: '/',
            activePage: 'users'
        },
        children: [
            {
                path: '',
                component: UsersListComponent,
                data: {
                    breadcrumb: [{label: 'Users', url: ''}],
                    showBackButton: false
                }
            },
            {
                path: '**',
                component: PageNotFoundComponent,
                data: {
                    breadcrumb: [
                        { label: 'Users', url: '/users'},
                        { label: 'Page Not Found', url: ''}
                    ],
                    showBackButton: false
                }
            }
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class UsersRoutingModule { }
