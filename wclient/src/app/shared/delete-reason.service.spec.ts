import { TestBed } from '@angular/core/testing';

import { DeleteReasonService } from './delete-reason.service';

describe('DeleteReasonService', () => {
  let service: DeleteReasonService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DeleteReasonService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
