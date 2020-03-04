import { NgModule } from '@angular/core';
import { SharedModule } from '../shared/shared.module';
import { UsersRoutingModule } from './users-routing.module';
import { UsersListComponent } from './components/users-list/users-list.component';
import { UserService } from './user.service';
import { UsersAddComponent } from './components/users-add/users-add.component';

@NgModule({
    imports: [
        SharedModule,
        UsersRoutingModule
    ],
    declarations: [
        UsersListComponent,
        UsersAddComponent
    ],
    entryComponents: [
        UsersAddComponent
    ],
    providers: [
        UserService
    ]
})
export class UsersModule { }
