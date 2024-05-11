import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddNewReportPageComponent } from './add-new-report-page.component';

describe('AddNewReportPageComponent', () => {
  let component: AddNewReportPageComponent;
  let fixture: ComponentFixture<AddNewReportPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddNewReportPageComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddNewReportPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
