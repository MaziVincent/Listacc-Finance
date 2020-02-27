import { NgModule } from '@angular/core';
import { SharedModule } from '../shared/shared.module';
import { DashboardRoutingModule } from './dashboard-routing.module';
import { LayoutComponent } from '../shared/components/layout/layout.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';

@NgModule({
    imports: [
        SharedModule,
        DashboardRoutingModule
    ],
    declarations: [
        LayoutComponent,
        DashboardComponent
    ]
})
export class DashboardModule { }
