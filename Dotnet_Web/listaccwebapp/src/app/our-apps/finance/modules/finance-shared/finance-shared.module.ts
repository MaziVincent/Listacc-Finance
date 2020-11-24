import { FinanceSharedService } from './services/finance-shared.service';
import { FinanceNotificationService } from './services/finance-notification.service';
import { FinanceValidationErrorService } from './services/finance-validation-error.service';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { Ng2TelInputModule } from 'ng2-tel-input';
import { Ng7BootstrapBreadcrumbModule } from 'ng7-bootstrap-breadcrumb';
import { NgbCollapseModule, NgbPaginationModule, NgbModalModule, NgbTooltipModule } from '@ng-bootstrap/ng-bootstrap';
import { NgSelectizeModule } from 'ng-selectize';
import { MyPaginationComponent } from './components/my-pagination/my-pagination.component';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';
import { LayoutComponent } from './components/layout/layout.component';
import { ConfirmExitComponent } from './components/confirm-exit/confirm-exit.component';
import { ConfirmDeleteComponent } from './components/confirm-delete/confirm-delete.component';
import { FinanceDepartmentsAddComponent } from '../finance-departments/components/finance-departments-add/finance-departments-add.component';
import { FinanceDepartmentsService } from '../finance-departments/finance-departments.service';
import { InterceptorProviders } from '../../interceptors/interceptor-providers';
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { PageWrapperDirective } from '../finance-users/directives/page-wrapper.directive';

@NgModule({
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    HttpClientModule,
    Ng7BootstrapBreadcrumbModule,
    Ng2TelInputModule,
    NgbCollapseModule,
    NgbPaginationModule,
    NgbModalModule,
    NgbTooltipModule,
    NgSelectizeModule,
    BsDropdownModule.forRoot()
  ],
  exports: [
    CommonModule,
    FormsModule,
    Ng7BootstrapBreadcrumbModule,
    Ng2TelInputModule,
    NgbCollapseModule,
    NgbPaginationModule,
    NgbModalModule,
    NgbTooltipModule,
    NgSelectizeModule,
    MyPaginationComponent,
    BsDropdownModule
  ],
  declarations: [
    PageNotFoundComponent,
    LayoutComponent,
    MyPaginationComponent,
    ConfirmExitComponent,
    ConfirmDeleteComponent,
    FinanceDepartmentsAddComponent,
    PageWrapperDirective
  ],
  entryComponents: [
    ConfirmExitComponent,
    ConfirmDeleteComponent,
    FinanceDepartmentsAddComponent
  ],
  providers: [
    InterceptorProviders,
    FinanceDepartmentsService,
    FinanceValidationErrorService,
    FinanceNotificationService,
    FinanceSharedService
  ]
})
export class FinanceSharedModule { }
