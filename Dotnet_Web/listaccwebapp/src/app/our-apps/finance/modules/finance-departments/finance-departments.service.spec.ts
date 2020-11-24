/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { FinanceDepartmentsService } from './finance-departments.service';

describe('Service: FinanceDepartments', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [FinanceDepartmentsService]
    });
  });

  it('should ...', inject([FinanceDepartmentsService], (service: FinanceDepartmentsService) => {
    expect(service).toBeTruthy();
  }));
});
