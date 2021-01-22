import { ValidationErrorService } from 'src/app/services/validation-error.service';
import { AcademyRegistrationComponent } from './components/academy/components/academy-registration/academy-registration.component';
import { NgbModalModule } from '@ng-bootstrap/ng-bootstrap';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './components/home/home.component';
import { AboutComponent } from './components/about/about.component';
import { TeamComponent } from './components/team/team.component';
import { TestimonialsComponent } from './components/testimonials/testimonials.component';
import { ServicesComponent } from './components/services/services.component';
import { SolutionsComponent } from './components/solutions/solutions.component';
import { AcademyComponent } from './components/academy/academy.component';
import { ContactsComponent } from './components/contacts/contacts.component';
import { BlogComponent } from './components/blog/blog.component';
import { LoginComponent } from './components/login/login.component';
import { PortalComponent } from './components/portal/portal.component';
import { ReactiveFormsModule } from '@angular/forms';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { ContactService } from './services/contact.service';
import { FinanceRootComponent } from './components/finance-root/finance-root.component';
import { ToastrModule } from 'ngx-toastr';
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { CountdownModule } from 'ngx-date-countdown';
import { AcademyService } from './services/academy.service';
import { BasicRoutesLayoutComponent } from './components/basic-routes-layout/basic-routes-layout.component';


@NgModule({
   declarations: [
      AppComponent,
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
      BasicRoutesLayoutComponent,
      AcademyRegistrationComponent
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
      NgbModalModule,
      CountdownModule
   ],
   entryComponents: [
      AcademyRegistrationComponent
   ],
   exports: [
      BsDropdownModule
   ],
   providers: [
      ContactService,
      AcademyService,
      ValidationErrorService
   ],
   bootstrap: [
      AppComponent
   ]
})
export class AppModule { }
