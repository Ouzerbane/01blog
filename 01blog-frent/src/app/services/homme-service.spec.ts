import { TestBed } from '@angular/core/testing';

import { HommeService } from './homme-service';

describe('HommeService', () => {
  let service: HommeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HommeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
