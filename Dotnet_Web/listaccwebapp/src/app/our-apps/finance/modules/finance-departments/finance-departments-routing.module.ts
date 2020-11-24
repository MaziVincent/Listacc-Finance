import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LayoutComponent } from '../finance-shared/components/layout/layout.component';
import { PageNotFoundComponent } from '../finance-shared/components/page-not-found/page-not-found.component';
import { FinanceDepartmentsListComponent } from './components/finance-departments-list/finance-departments-list.component';

const routes: Routes = [
  {
      path: '',
      component: LayoutComponent,
      data: {
          home: '/',
          activePage: 'departments'
      },
      children: [
          {
              path: '',
              component: FinanceDepartmentsListComponent,
              data: {
                  breadcrumb: [{label: 'Departments', url: ''}],
                  showBackButton: false
              }
          },
          {
              path: '**',
              component: PageNotFoundComponent,
              data: {
                  breadcrumb: [
                      { label: 'Departments', url: '/departments'},
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
export class FinanceDepartmentsRoutingModule { }
