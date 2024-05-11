import { TestBed } from '@angular/core/testing';

import { ReportPageService } from './report-page.service';

describe('ReportPageService', () => {
  let service: ReportPageService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReportPageService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
