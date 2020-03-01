import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SummaryDiagComponent } from './summary-diag.component';

describe('SummaryDiagComponent', () => {
  let component: SummaryDiagComponent;
  let fixture: ComponentFixture<SummaryDiagComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SummaryDiagComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SummaryDiagComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
