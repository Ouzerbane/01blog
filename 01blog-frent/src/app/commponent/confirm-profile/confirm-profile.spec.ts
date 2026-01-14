import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmProfile } from './confirm-profile';

describe('ConfirmProfile', () => {
  let component: ConfirmProfile;
  let fixture: ComponentFixture<ConfirmProfile>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConfirmProfile]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ConfirmProfile);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
