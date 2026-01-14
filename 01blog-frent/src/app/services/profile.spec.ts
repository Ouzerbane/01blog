import { TestBed } from '@angular/core/testing';

import { profileService } from './profileService';

describe('Profile', () => {
  let service: profileService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(profileService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
