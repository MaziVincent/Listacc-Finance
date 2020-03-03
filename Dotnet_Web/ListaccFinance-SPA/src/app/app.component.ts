import { Component, AfterViewInit } from '@angular/core';
import { NavigationCancel, Router, NavigationStart, NavigationEnd } from '@angular/router';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss']
})
export class AppComponent implements AfterViewInit {
    title = 'Listacc Finance';

    loadingView: boolean;

    constructor(private router: Router) {
        this.loadingView = true;
    }

    ngAfterViewInit() {
        this.router.events
            .subscribe((event) => {
                if (event instanceof NavigationStart) {
                    this.loadingView = true;
                } else if (event instanceof NavigationEnd || event instanceof NavigationCancel) {
                    this.loadingView = false;
                }
            });
    }
}
