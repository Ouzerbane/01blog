import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { autherGuard } from './auther-guard';

describe('autherGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => autherGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
