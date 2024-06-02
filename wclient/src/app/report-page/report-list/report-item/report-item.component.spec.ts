import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportItemComponent } from './report-item.component';

describe('ReportItemComponent', () => {
  let component: ReportItemComponent;
  let fixture: ComponentFixture<ReportItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReportItemComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ReportItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
