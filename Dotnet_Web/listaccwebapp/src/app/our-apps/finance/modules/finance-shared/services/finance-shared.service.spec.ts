/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { FinanceSharedService } from './finance-shared.service';

describe('Service: FinanceShared', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [FinanceSharedService]
    });
  });

  it('should ...', inject([FinanceSharedService], (service: FinanceSharedService) => {
    expect(service).toBeTruthy();
  }));
});
