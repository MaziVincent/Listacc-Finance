import { FinanceDashboardRoutingModule } from './finance-dashboard-routing.module';
import { FinanceSharedModule } from './../finance-shared/finance-shared.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FinanceDashboardComponent } from './components/finance-dashboard/finance-dashboard.component';

@NgModule({
  imports: [
    FinanceSharedModule,
    FinanceDashboardRoutingModule
  ],
  declarations: [
    FinanceDashboardComponent
  ]
})
export class FinanceDashboardModule { }
