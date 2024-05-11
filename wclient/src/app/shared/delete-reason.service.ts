import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class DeleteReasonService {
  constructor() {}

  private _reasons = new BehaviorSubject<string[]>([
    'Violation of Community Guidelines',
    'Inappropriate Content',
    'Irrelevant or Off-topic',
    'Duplicate Content',
    'Trolling or Disruptive Behaviour',
    'Commercial or Promotional Content',
    'Political or Religious Sensitivity',
  ]);

  get reasons() {
    return this._reasons.asObservable();
  }

  saveReasonToDatabase(reason: string) {
    // Logic to save the "other" reason into the database here.
  }
}
