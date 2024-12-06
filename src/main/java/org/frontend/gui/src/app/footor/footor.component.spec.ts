import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FootorComponent } from './footor.component';

describe('FootorComponent', () => {
  let component: FootorComponent;
  let fixture: ComponentFixture<FootorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FootorComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FootorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
