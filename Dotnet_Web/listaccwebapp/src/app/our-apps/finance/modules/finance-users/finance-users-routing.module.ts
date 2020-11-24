import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LayoutComponent } from '../finance-shared/components/layout/layout.component';
import { PageNotFoundComponent } from '../finance-shared/components/page-not-found/page-not-found.component';
import { FinanceUsersListComponent } from './components/finance-users-list/finance-users-list.component';

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
              component: FinanceUsersListComponent,
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
export class FinanceUsersRoutingModule { }
