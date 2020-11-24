import { Component, OnInit, AfterViewInit } from '@angular/core';
import { MyUser } from 'src/app/our-apps/finance/models/user';
import { FinanceAuthService } from 'src/app/our-apps/finance/services/finance-auth.service';
import { ActivatedRoute, Router, NavigationEnd } from '@angular/router';
import * as $ from 'jquery';

@Component({
    selector: 'app-layout',
    templateUrl: './layout.component.html',
    styleUrls: ['./layout.component.css']
})
export class LayoutComponent implements OnInit, AfterViewInit {

    user: MyUser;
    // timeStamp: number;

    ActivePage: string;

    PageHeading: string;
    PageSubHeading: string;

    ShowBackButton: boolean;
    isProductMenuCollapsed = true;

    ParentUrl: string;
    HomeUrl: string;

    constructor(private authService: FinanceAuthService,
                private route: ActivatedRoute,
                private router: Router) { }

    ngOnInit() {
        // get logged in user
        this.user = this.authService.getUser();

        // get values to route in order to configure the layout
        this.processRoutingData();
    }

    ngAfterViewInit() {
        this.router.events.subscribe(
            (event) => {
                if (event instanceof NavigationEnd) {
                    this.processRoutingData();
                }
            }
        );
    }


    processRoutingData() {
        const routeData = this.route.snapshot.data;
        const firstChildData = this.route.snapshot.firstChild.data;
        this.ShowBackButton = firstChildData.showBackButton !== null ? firstChildData.showBackButton : false;
        this.ParentUrl = this.ShowBackButton ? firstChildData.parentUrl : null;
        this.HomeUrl = routeData.home !== undefined ? routeData.home : null;
        this.ActivePage = routeData.activePage || firstChildData.activePage;
        /*if (this.ActivePage === 'products' || this.ActivePage === 'product_categories'
            || this.ActivePage === 'product_extra_attributes') {
            this.isProductMenuCollapsed = false;
        }*/
    }

    logOut() {
        this.authService.logout();
        this.router.navigate(['apps/finance/login']);
    }


    // UI Operations
    toggleSidebar() {
        $('body').hasClass('mini-sidebar') ?
        (
            $('body').trigger('resize'),
            $('.scroll-sidebar, .slimScrollDiv').css('overflow', 'hidden').parent().css('overflow', 'visible'),
            $('body').removeClass('mini-sidebar'),
            $('.navbar-brand span').show()
        )
        :
        (
            $('body').trigger('resize'),
            $('.scroll-sidebar, .slimScrollDiv').css('overflow-x', 'visible').parent().css('overflow', 'visible'),
            $('body').addClass('mini-sidebar'),
            $('.navbar-brand span').hide()
        );
    }

    toggleHiddenSidebar() {
        $('body').toggleClass('show-sidebar'), $('.nav-toggler i').toggleClass('mdi mdi-menu'),
        $('.nav-toggler i').addClass('mdi mdi-close');
    }


    // User Info
    getUserProfileImage() {
        return null;
    }

    refreshUserInfo() {
        this.authService.clearCurrentUser();
        // this.timeStamp = (new Date()).getTime();
        this.user = this.authService.getUser();
    }
}