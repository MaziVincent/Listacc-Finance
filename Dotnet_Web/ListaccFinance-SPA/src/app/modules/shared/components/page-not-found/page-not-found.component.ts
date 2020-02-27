import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
    selector: 'app-page-not-found',
    templateUrl: './page-not-found.component.html',
    styleUrls: ['./page-not-found.component.css']
})
export class PageNotFoundComponent implements OnInit {

    HomeURL: string;

    constructor(private route: ActivatedRoute) { }

    ngOnInit() {
        const home = this.route.snapshot.parent.data.home || this.route.snapshot.parent.parent.data.home;
        if (home) {
            this.HomeURL = home;
        }
    }

}
