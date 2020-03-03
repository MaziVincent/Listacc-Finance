import { Injectable } from '@angular/core';
import { StatusOption } from '../models/user';

@Injectable({
  providedIn: 'root'
})
export class SharedService {

    StatusOptions: StatusOption[] = [
        { name: 'Active', value: 'true', iconClass: 'active'},
        { name: 'Inactive', value: 'false', iconClass: ''}
    ];

    constructor() { }

}
