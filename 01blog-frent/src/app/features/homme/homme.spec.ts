import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Homme } from './homme';

describe('Homme', () => {
  let component: Homme;
  let fixture: ComponentFixture<Homme>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Homme]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Homme);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
