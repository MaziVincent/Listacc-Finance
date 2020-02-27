import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { LayoutComponent } from 'src/app/modules/shared/components/layout/layout.component';

@Component({
    selector: 'app-dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

    constructor(private title: Title,
                parentComponent: LayoutComponent) {
        // set page title
        this.title.setTitle('Dashboard | Listacc Finance');

        // set page heading
        parentComponent.PageHeading = 'Dashboard';
    }

    ngOnInit() {
    }

}
