import { AcademyService } from './../services/academy.service';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve } from '@angular/router';
import { NotificationService } from '../services/notification.service';
import { forkJoin, Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

@Injectable()
export class AcademyResolver implements Resolve<any>{

    constructor(private academyService: AcademyService,
                private notify: NotificationService) { }

    resolve(_route: ActivatedRouteSnapshot): Observable<any>{
        return forkJoin([
            this.academyService.getUpcomingProject()
        ]).pipe(
            map(result => {
                return {
                    upcomingProjects: result[0]
                    // pathTopics: null // result[1]
                };
            }),
            catchError((error) => {
                // this.notify.error('Problem loading topics!');
                return of(
                    {
                        upcomingProjects : null
                        // pathTopics: null
                    });
            })
        );
    }
}
