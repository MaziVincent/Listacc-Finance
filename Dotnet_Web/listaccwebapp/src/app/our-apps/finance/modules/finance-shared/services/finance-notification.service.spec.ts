/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { FinanceNotificationService } from './finance-notification.service';

describe('Service: FinanceNotification', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [FinanceNotificationService]
    });
  });

  it('should ...', inject([FinanceNotificationService], (service: FinanceNotificationService) => {
    expect(service).toBeTruthy();
  }));
});
