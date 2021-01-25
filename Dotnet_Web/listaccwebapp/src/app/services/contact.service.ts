import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, catchError } from 'rxjs/operators';



@Injectable({
  providedIn: 'root'
})
export class ContactService {

  private api = 'https://mailthis.to/georgevincent35'

  constructor(private http:HttpClient) { }

  postMessage(input:any){
    return this.http.post(this.api, input,{responseType:'text'}).pipe(
      map(
        (response) => {
        if (response) {
          return response;
        }
      },
        (error: any) => {
          return error;

        })
    )
  }


}
