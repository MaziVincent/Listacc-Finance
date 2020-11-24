import { ToastrService } from 'ngx-toastr';
import { Injectable } from '@angular/core';

@Injectable()
export class FinanceNotificationService {

    constructor(private toastr: ToastrService) { }

    error(message: string, title?: string) {
        this.toastr.error(message, title);
    }

    success(message: string, title?: string) {
        this.toastr.success(message, title);
    }

    info(message: string, title?: string) {
        this.toastr.info(message, title);
    }

    clearAll() {
        this.toastr.clear();
    }

}
