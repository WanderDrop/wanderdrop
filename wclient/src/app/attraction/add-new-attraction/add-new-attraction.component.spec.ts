import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddNewAttractionComponent } from './add-new-attraction.component';

describe('AddNewAttractionComponent', () => {
  let component: AddNewAttractionComponent;
  let fixture: ComponentFixture<AddNewAttractionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddNewAttractionComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddNewAttractionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
