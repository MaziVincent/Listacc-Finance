import { NgModule } from '@angular/core';
import { SharedModule } from '../shared/shared.module';
import { DepartmentsRoutingModule } from './departments-routing.module';
import { DepartmentsListComponent } from './components/departments-list/departments-list.component';

@NgModule({
  imports: [
      SharedModule,
      DepartmentsRoutingModule
  ],
  declarations: [
      DepartmentsListComponent
  ],
  entryComponents: [],
  providers: []
})
export class DepartmentsModule { }
