/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { FinanceUsersService } from './finance-users.service';

describe('Service: FinanceUsers', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [FinanceUsersService]
    });
  });

  it('should ...', inject([FinanceUsersService], (service: FinanceUsersService) => {
    expect(service).toBeTruthy();
  }));
});
