import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { PageNotFoundComponent } from '../finance-shared/components/page-not-found/page-not-found.component';
import { LayoutComponent } from '../finance-shared/components/layout/layout.component';
import { FinanceDashboardComponent } from './components/finance-dashboard/finance-dashboard.component';

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
              component: FinanceDashboardComponent,
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
export class FinanceDashboardRoutingModule { }
