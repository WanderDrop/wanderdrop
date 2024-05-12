import { ComponentFixture, TestBed } from '@angular/core/testing';

import { YourActivityComponent } from './your-activity.component';

describe('YourActivityComponent', () => {
  let component: YourActivityComponent;
  let fixture: ComponentFixture<YourActivityComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [YourActivityComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(YourActivityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
