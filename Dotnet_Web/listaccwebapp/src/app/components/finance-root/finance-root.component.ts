import { Component, Renderer2, OnDestroy } from '@angular/core';
import { Router, NavigationStart, NavigationEnd, NavigationCancel } from '@angular/router';

@Component({
  selector: 'app-finance-root',
  templateUrl: './finance-root.component.html',
  styleUrls: ['./finance-root.component.css']
})
export class FinanceRootComponent implements OnDestroy {
    title = 'Listacc Finance';

    loadingView: boolean;

    navigationSubscription;

    constructor(private router: Router,
                private renderer: Renderer2) {
        // add css class to body
        this.renderer.addClass(document.body, 'finance-app-layout');

        // show loading animation
        this.loadingView = true;

        this.navigationSubscription = this.router.events.subscribe((event) => {
            if (event instanceof NavigationStart) {
                this.loadingView = true;
            } else if (event instanceof NavigationEnd || event instanceof NavigationCancel) {
                this.loadingView = false;
            }
        });
    }

    ngOnDestroy() {
        // remove css from body
        this.renderer.removeClass(document.body, 'finance-app-layout');

        if (this.navigationSubscription) {
            this.navigationSubscription.unsubscribe();
        }
    }
}
