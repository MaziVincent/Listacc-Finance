import { FinanceDepartmentsRoutingModule } from './finance-departments-routing.module';
import { FinanceSharedModule } from './../finance-shared/finance-shared.module';
import { NgModule } from '@angular/core';
import { FinanceDepartmentsListComponent } from './components/finance-departments-list/finance-departments-list.component';

@NgModule({
  imports: [
    FinanceSharedModule,
    FinanceDepartmentsRoutingModule
  ],
  declarations: [
    FinanceDepartmentsListComponent
  ]
})
export class FinanceDepartmentsModule { }
