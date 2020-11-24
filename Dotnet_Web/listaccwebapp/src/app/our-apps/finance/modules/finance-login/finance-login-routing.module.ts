import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { PageNotFoundComponent } from '../finance-shared/components/page-not-found/page-not-found.component';
import { FinanceLoginLayoutComponent } from './components/finance-login-layout/finance-login-layout.component';
import { FinanceLoginComponent } from './components/finance-login/finance-login.component';

const routes: Routes = [
  {
      path: '',
      component: FinanceLoginLayoutComponent,
      data: {home: '/login'},
      children: [
          {
              path: '',
              component: FinanceLoginComponent
          },
          {
              path: '**',
              component: PageNotFoundComponent
          }
      ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class FinanceLoginRoutingModule { }
