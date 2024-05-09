import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs';
import { DeleteReasonService } from '../delete-reason.service';

@Component({
  selector: 'app-delete-confirmation',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './delete-confirmation.component.html',
  styleUrl: './delete-confirmation.component.css',
})
export class DeleteConfirmationComponent {
  selectedReason: string = '';
  otherReason: string = '';
  reasons!: Observable<string[]>;

  constructor(
    private activeModal: NgbActiveModal,
    private deleteReasonService: DeleteReasonService
  ) {
    this.reasons = this.deleteReasonService.reasons;
  }

  onDelete() {
    if (window.confirm('Are you sure? This action cannot be undone.')) {
      const reason =
        this.selectedReason !== 'other'
          ? this.selectedReason
          : this.otherReason;
      console.log(reason);
      this.deleteReasonService.saveReasonToDatabase(reason);
      this.activeModal.close('delete');
    } else {
      this.activeModal.dismiss('cancel');
    }
  }

  onCancel() {
    this.activeModal.dismiss('cancel');
  }
}
