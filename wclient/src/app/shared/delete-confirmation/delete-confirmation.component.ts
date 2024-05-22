import { CommonModule } from '@angular/common';
import { Component, OnDestroy } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, Subscription } from 'rxjs';
import { DeleteReasonService } from '../delete-reason.service';

@Component({
  selector: 'app-delete-confirmation',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './delete-confirmation.component.html',
  styleUrl: './delete-confirmation.component.css',
})
export class DeleteConfirmationComponent implements OnDestroy {
  selectedReason: string = '';
  otherReason: string = '';
  reasons!: Observable<{ id: number; reasonMessage: string }[]>;
  private subscriptions: Subscription[] = [];

  constructor(
    private activeModal: NgbActiveModal,
    private deleteReasonService: DeleteReasonService
  ) {
    this.reasons = this.deleteReasonService.reasons;
  }

  onDelete() {
    if (this.selectedReason === 'other') {
      const postReasonSub = this.deleteReasonService
        .postReason(this.otherReason)
        .subscribe((data: { id: number; reasonMessage: string }) => {
          this.activeModal.close({ action: 'delete', reasonId: data.id });
        });
      this.subscriptions.push(postReasonSub);
    } else {
      const reasonsSub = this.reasons.subscribe((reasonsArray) => {
        const reasonIndex = reasonsArray.findIndex(
          (reasonObj) => reasonObj.reasonMessage === this.selectedReason
        );
        const reasonId = reasonsArray[reasonIndex].id;
        this.activeModal.close({ action: 'delete', reasonId });
      });
      this.subscriptions.push(reasonsSub);
    }
  }

  onCancel() {
    this.activeModal.dismiss('cancel');
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((sub) => sub.unsubscribe());
  }
}
