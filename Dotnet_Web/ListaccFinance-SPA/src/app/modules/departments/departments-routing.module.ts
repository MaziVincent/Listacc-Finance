import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LayoutComponent } from '../shared/components/layout/layout.component';
import { PageNotFoundComponent } from '../shared/components/page-not-found/page-not-found.component';
import { DepartmentsListComponent } from './components/departments-list/departments-list.component';

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
              component: DepartmentsListComponent,
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
export class DepartmentsRoutingModule { }
