/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { FinanceLoginService } from './finance-login.service';

describe('Service: FinanceLogin', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [FinanceLoginService]
    });
  });

  it('should ...', inject([FinanceLoginService], (service: FinanceLoginService) => {
    expect(service).toBeTruthy();
  }));
});
