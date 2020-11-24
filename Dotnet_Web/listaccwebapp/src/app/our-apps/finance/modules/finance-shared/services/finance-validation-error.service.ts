import { FinanceNotificationService } from './finance-notification.service';
import { MyValidationErrors } from '../../../models/my-validation-errors';
import { Injectable } from '@angular/core';

@Injectable()
export class FinanceValidationErrorService {

    allErrors: MyValidationErrors;

    constructor(private notificationService: FinanceNotificationService) {
        this.allErrors = new MyValidationErrors();
    }

    showValidationErrors(errors: any, isDelete?: boolean, genErrorMsg?: boolean): MyValidationErrors {
        this.allErrors.validationErrors = [];
        this.allErrors.fieldErrors = {};

        errors = errors.hasOwnProperty('error') && errors.error.hasOwnProperty('errors') ? errors.error.errors : errors;

        if (errors) {
            let count = 0;
            for (let key in errors) {
                if (errors.hasOwnProperty(key)) {
                    count++;
                    let value = errors[key].toString();
                    key = key.toString();
                    if (key === '') {
                        this.allErrors.validationErrors.push(value);
                    } else {
                        // is in the form "object.property"
                        /*if (key.indexOf('.') !== -1) {
                            key = key.substr(key.indexOf('.') + 1);
                        } */

                        // contains array
                        const leftSqrBrIn = key.indexOf('[');
                        const rightSqrBrIn = key.indexOf(']');
                        if (leftSqrBrIn !== -1 && rightSqrBrIn !== -1 && leftSqrBrIn < rightSqrBrIn) {
                            const innerKey = key.substr(rightSqrBrIn + 2);
                            const arrIndex: number = parseInt(key.substr(leftSqrBrIn + 1, rightSqrBrIn - leftSqrBrIn - 1), 10);
                            const newKey = key.substr(0, leftSqrBrIn);
                            if (!this.allErrors.fieldErrors.hasOwnProperty(newKey)) {
                                this.allErrors.fieldErrors[newKey] = [];
                            }
                            if (this.allErrors.fieldErrors[newKey][arrIndex] === undefined) {
                                this.allErrors.fieldErrors[newKey][arrIndex] = {};
                            }
                            this.allErrors.fieldErrors[newKey][arrIndex][innerKey] = value;
                        } else if (value.indexOf('.,') === -1) {
                            // check if value is an object of a known format
                            if (errors[key].code) {
                                this.allErrors.fieldErrors[errors[key].code] = errors[key].description;
                            } else {
                                this.allErrors.fieldErrors[key] = value;
                            }
                        } else {
                            const arr = value.split('.,');
                            value = '';
                            for (const x in arr) {
                                if (arr.hasOwnProperty(x)) {
                                    value += arr[x] + '\n';
                                }
                            }
                            this.allErrors.fieldErrors[key] = value;
                        }
                    }
                }
            }
            if (count > 0) {
                if (isDelete) {
                    this.notificationService.error('Problem deleting record');
                } else if (genErrorMsg) {
                    this.notificationService.error('Problem processing request');
                } else {
                    this.notificationService.error('Please fill the form correctly');
                }
            }
        } else {
            this.allErrors.validationErrors.push('Problem occurred');
        }

        return this.allErrors;
    }

}
