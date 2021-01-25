import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { RegistrationInfo } from '../components/academy/components/academy-registration/academy-registration.component';

@Injectable({
  providedIn: 'root'
})
export class AcademyService {
    private academyBase = environment.AcademyUrl + 'admin/learningpath';


    constructor(private http: HttpClient) { }


    // Register for academy training
    registerComingSoon(path: RegistrationInfo): Observable<any> {
        return this.http.post(this.academyBase + '/create', path);
    }

}
