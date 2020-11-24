import { Component, OnInit } from '@angular/core';

import { FormBuilder, FormGroup, FormControl, Validators } from "@angular/forms";
import { ContactService } from '../scriptservice/contact.service';

@Component({
  selector: 'app-contacts',
  templateUrl: './contacts.component.html',
  styleUrls: ['./contacts.component.css']
})
export class ContactsComponent implements OnInit {

  FormData:FormGroup;

  

  constructor( private builder:FormBuilder, private contact:ContactService) { }

  ngOnInit() {

    this.FormData = this.builder.group({
      Fullname: new FormControl('',[Validators.required]),
      Email: new FormControl('',[Validators.compose([Validators.required,Validators.email])]),
      Phonenumber: new FormControl('',[Validators.required]),
      Message: new FormControl('',[Validators.required]),
      
    });
  }

    onSubmit(FormData){
      console.log(FormData)
      this.contact.postMessage(FormData)
      .subscribe(response =>{
        location.href = 'https://mailthis.to/confirm'
        console.log(response)
      }, error =>{
        console.warn(error.responseText)
        console.log({error})

      })

    }




  }

    


