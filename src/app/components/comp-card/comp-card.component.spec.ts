import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CompCardComponent } from './comp-card.component';

describe('CompCardComponent', () => {
  let component: CompCardComponent;
  let fixture: ComponentFixture<CompCardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CompCardComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CompCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
