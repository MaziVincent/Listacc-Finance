import { NgModule } from '@angular/core';
import { LoginLayoutComponent } from './components/login-layout/login-layout.component';
import { SharedModule } from '../shared/shared.module';
import { LoginService } from './login.service';
import { LoginRoutingModule } from './login-routing.module';
import { LoginComponent } from './components/login/login.component';

@NgModule({
    imports: [
        SharedModule,
        LoginRoutingModule
    ],
    declarations: [
        LoginLayoutComponent,
        LoginComponent
    ],
    providers: [
        LoginService
    ]
})
export class LoginModule { }
