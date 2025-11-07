import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Regester } from './regester';

describe('Regester', () => {
  let component: Regester;
  let fixture: ComponentFixture<Regester>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Regester]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Regester);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
