import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { ToastrModule } from 'ngx-toastr';
import { Ng2TelInputModule } from 'ng2-tel-input';
import { Ng7BootstrapBreadcrumbModule } from 'ng7-bootstrap-breadcrumb';
import { NgbCollapseModule } from '@ng-bootstrap/ng-bootstrap';

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
    NgbCollapseModule
  ],
  exports: [
    CommonModule,
    FormsModule,
    Ng7BootstrapBreadcrumbModule,
    Ng2TelInputModule,
    NgbCollapseModule
  ],
  declarations: [
    PageNotFoundComponent
  ]
})
export class SharedModule { }
