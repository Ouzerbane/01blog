import { TestBed } from '@angular/core/testing';

import { CreatPostService } from './creat-post-service';

describe('CreatPostService', () => {
  let service: CreatPostService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CreatPostService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
