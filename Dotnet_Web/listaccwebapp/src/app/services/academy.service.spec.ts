/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { AcademyService } from './academy.service';

describe('Service: Academy', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AcademyService]
    });
  });

  it('should ...', inject([AcademyService], (service: AcademyService) => {
    expect(service).toBeTruthy();
  }));
});
