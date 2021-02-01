import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { AcademyProject, RegistrationInfo } from '../models/academy';

@Injectable({
  providedIn: 'root'
})
export class AcademyService {
    private academyBase = environment.AcademyUrl + 'auth';


    constructor(private http: HttpClient) { }

    // Get Upcoming Project
    getUpcomingProject(): Observable<AcademyProject[]> {
      return this.http.get<AcademyProject[]>(this.academyBase + '/projects' );
    }

    // Register for academy training
    registerComingSoon(info: RegistrationInfo): Observable<any> {
        return this.http.post(this.academyBase + '/register', info);
    }

}
