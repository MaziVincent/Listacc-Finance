import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { LayoutComponent } from '../../../finance-shared/components/layout/layout.component';

@Component({
  selector: 'app-finance-dashboard',
  templateUrl: './finance-dashboard.component.html',
  styleUrls: ['./finance-dashboard.component.css']
})
export class FinanceDashboardComponent implements OnInit {

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
