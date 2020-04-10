import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { ToastrModule } from 'ngx-toastr';
import { Ng2TelInputModule } from 'ng2-tel-input';
import { Ng7BootstrapBreadcrumbModule } from 'ng7-bootstrap-breadcrumb';
import { NgbCollapseModule, NgbPaginationModule, NgbModalModule, NgbTooltipModule } from '@ng-bootstrap/ng-bootstrap';
import { LayoutComponent } from './components/layout/layout.component';
import { BsDropdownModule } from 'ngx-bootstrap';
import { MyPaginationComponent } from './components/my-pagination/my-pagination.component';
import { ConfirmExitComponent } from './components/confirm-exit/confirm-exit.component';
import { ConfirmDeleteComponent } from './components/confirm-delete/confirm-delete.component';
import { NgSelectizeModule } from 'ng-selectize';
import { InterceptorProviders } from 'src/app/interceptors/interceptor-providers';
import { DepartmentsAddComponent } from '../departments/components/departments-add/departments-add.component';
import { DepartmentsService } from '../departments/departments.service';

@NgModule({
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    HttpClientModule,
    ToastrModule.forRoot({
      maxOpened: 2,
      positionClass: 'toast-top-right',
      timeOut: 4000,
      closeButton: true,
      preventDuplicates: true,
      enableHtml: true
    }),
    Ng7BootstrapBreadcrumbModule,
    Ng2TelInputModule,
    NgbCollapseModule,
    NgbPaginationModule,
    NgbModalModule,
    NgbTooltipModule,
    NgSelectizeModule,
    BsDropdownModule.forRoot(),
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
    BsDropdownModule,
    MyPaginationComponent
  ],
  declarations: [
    PageNotFoundComponent,
    LayoutComponent,
    MyPaginationComponent,
    ConfirmExitComponent,
    ConfirmDeleteComponent,
    DepartmentsAddComponent
  ],
  entryComponents: [
    ConfirmExitComponent,
    ConfirmDeleteComponent,
    DepartmentsAddComponent
  ],
  providers: [
    InterceptorProviders,
    DepartmentsService
  ]
})
export class SharedModule { }
