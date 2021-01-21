import { NgbModalModule } from '@ng-bootstrap/ng-bootstrap';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LayoutComponent } from './layout/layout.component';
import { HomeComponent } from './home/home.component';
import { AboutComponent } from './about/about.component';
import { TeamComponent } from './team/team.component';
import { TestimonialsComponent } from './testimonials/testimonials.component';
import { ServicesComponent } from './services/services.component';
import { SolutionsComponent } from './solutions/solutions.component';
import { AcademyComponent } from './academy/academy.component';
import { ContactsComponent } from './contacts/contacts.component';
import { BlogComponent } from './blog/blog.component';
import { LoginComponent } from './login/login.component';
import { PortalComponent } from './portal/portal.component';
import { ReactiveFormsModule } from '@angular/forms';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { ContactService } from './scriptservice/contact.service';
import { FinanceRootComponent } from './finance-root/finance-root.component';
import { ToastrModule } from 'ngx-toastr';
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { BasicRoutesLayoutComponent } from './basic-routes-layout/basic-routes-layout.component';


@NgModule({
   declarations: [
      AppComponent,
      LayoutComponent,
      HomeComponent,
      AboutComponent,
      TeamComponent,
      TestimonialsComponent,
      ServicesComponent,
      SolutionsComponent,
      AcademyComponent,
      ContactsComponent,
      BlogComponent,
      LoginComponent,
      PortalComponent,
      FinanceRootComponent,
      BasicRoutesLayoutComponent
   ],
   imports: [
      ToastrModule.forRoot({
        maxOpened: 2,
        positionClass: 'toast-top-right',
        timeOut: 4000,
        closeButton: true,
        preventDuplicates: true,
        enableHtml: true
      }),
      BsDropdownModule.forRoot(),
      BrowserModule,
      BrowserAnimationsModule,
      AppRoutingModule,
      ReactiveFormsModule,
      HttpClientModule,
      FormsModule,
      NgbModalModule
   ],
   exports: [
      BsDropdownModule
   ],
   providers: [
      ContactService
   ],
   bootstrap: [
      AppComponent
   ]
})
export class AppModule { }
