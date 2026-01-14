import { TestBed } from '@angular/core/testing';

import { PostActions } from './post-actions';

describe('PostActions', () => {
  let service: PostActions;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PostActions);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
