import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NotFund } from './not-fund';

describe('NotFund', () => {
  let component: NotFund;
  let fixture: ComponentFixture<NotFund>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NotFund]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NotFund);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
