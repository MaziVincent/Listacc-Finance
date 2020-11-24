import { NgModule } from '@angular/core';
import { FinanceSharedModule } from '../finance-shared/finance-shared.module';
import { FinanceLoginRoutingModule } from './finance-login-routing.module';
import { FinanceLoginService } from './finance-login.service';
import { FinanceLoginLayoutComponent } from './components/finance-login-layout/finance-login-layout.component';
import { FinanceLoginComponent } from './components/finance-login/finance-login.component';

@NgModule({
  imports: [
      FinanceSharedModule,
      FinanceLoginRoutingModule
  ],
  declarations: [
      FinanceLoginLayoutComponent,
      FinanceLoginComponent
  ],
  providers: [
      FinanceLoginService
  ]
})
export class FinanceLoginModule { }
