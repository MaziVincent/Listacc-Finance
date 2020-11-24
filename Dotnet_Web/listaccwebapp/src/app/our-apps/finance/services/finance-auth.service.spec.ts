/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { FinanceAuthService } from './finance-auth.service';

describe('Service: FinanceAuth', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [FinanceAuthService]
    });
  });

  it('should ...', inject([FinanceAuthService], (service: FinanceAuthService) => {
    expect(service).toBeTruthy();
  }));
});
