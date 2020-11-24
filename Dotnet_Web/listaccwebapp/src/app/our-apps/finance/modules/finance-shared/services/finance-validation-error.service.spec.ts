/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { FinanceValidationErrorService } from './finance-validation-error.service';

describe('Service: FinanceValidationError', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [FinanceValidationErrorService]
    });
  });

  it('should ...', inject([FinanceValidationErrorService], (service: FinanceValidationErrorService) => {
    expect(service).toBeTruthy();
  }));
});
