import { Component, OnInit, AfterViewInit, Renderer2, OnDestroy } from '@angular/core';
import { NavigationCancel, NavigationEnd, NavigationStart, Router } from '@angular/router';

@Component({
  selector: 'app-basic-routes-layout',
  templateUrl: './basic-routes-layout.component.html',
  styleUrls: ['./basic-routes-layout.component.css']
})
export class BasicRoutesLayoutComponent implements AfterViewInit, OnDestroy {

    title = 'listaccwebapp';
    loadAPI: Promise<any>;
    previousUrl = '';
    logoPath = '../assets/images/logoTrans2.png';
    loadingView: boolean;

    navigationSubscription;

    constructor(private router: Router,
                private renderer: Renderer2) {
        // add css class to body
        this.renderer.addClass(document.body, 'basic-app-layout');
        this.loadingView = true;

        this.navigationSubscription = this.router.events.subscribe((event) => {
          if (event instanceof NavigationEnd){
            if (this.previousUrl !== ''){
                this.loadAPI = new Promise(resolve => {
                    this.loadScripts();
                    resolve(true);
                });
            }
            this.previousUrl = event.url;
            this.loadingView = false;
          }
          else if (event instanceof NavigationStart) {
            this.loadingView = true;
            // this.notificationService.clearAllToasts();
          } else if (event instanceof NavigationCancel) {
              this.loadingView = false;
          }
      });
    }

    ngAfterViewInit() {}

    ngOnDestroy() {
        // remove css from body
        this.renderer.removeClass(document.body, 'basic-app-layout');

        if (this.navigationSubscription) {
            this.navigationSubscription.unsubscribe();
        }
    }

    /*ngOnInit() {
      // Called after the constructor, initializing input properties, and the first call to ngOnChanges.
      // Add 'implements OnInit' to the class.
      window.addEventListener('scroll', this.scroll, true);
    }*/

    scroll = () => {
        const scrollPos = document.documentElement.scrollTop;
        // change '50' according to when you want to change the image
        if (scrollPos > 50){
            console.log('new image');
            this.logoPath = '../assets/images/logoTrans.png';
        }
        else{
            console.log('old image');
            this.logoPath = '../assets/images/logoTrans2.png';
        }
    }

    loadScripts(){
      const externalScriptArray = [
        '../assets/js/main.js'
      ];
      for (const script of externalScriptArray){
        // remove script tag if already there
        const scriptList = document.getElementsByTagName('script');
        for (let index = 0; index < scriptList.length; ++index){
          if (scriptList[index].getAttribute('src') != null && scriptList[index].getAttribute('src').includes('main.js')){
            scriptList[index].remove();
          }
        }
        const scriptTag = document.createElement('script');
        scriptTag.src = script;
        scriptTag.type = 'text/javascript';
        scriptTag.async = false;
        scriptTag.charset = 'utf-8';
        document.getElementsByTagName('head')[0].appendChild(scriptTag);
      }
    }
  }
