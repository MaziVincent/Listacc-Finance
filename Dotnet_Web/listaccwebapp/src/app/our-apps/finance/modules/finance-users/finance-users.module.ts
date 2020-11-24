import { FinanceUsersService } from './finance-users.service';
import { FinanceUsersAddComponent } from './components/finance-users-add/finance-users-add.component';
import { NgModule } from '@angular/core';
import { FinanceSharedModule } from '../finance-shared/finance-shared.module';
import { FinanceUsersRoutingModule } from './finance-users-routing.module';
import { FinanceUsersListComponent } from './components/finance-users-list/finance-users-list.component';

@NgModule({
  imports: [
    FinanceSharedModule,
    FinanceUsersRoutingModule
  ],
  declarations: [
    FinanceUsersListComponent,
    FinanceUsersAddComponent
  ],
  entryComponents: [
    FinanceUsersAddComponent
  ],
  providers: [
    FinanceUsersService
  ]
})
export class FinanceUsersModule { }
