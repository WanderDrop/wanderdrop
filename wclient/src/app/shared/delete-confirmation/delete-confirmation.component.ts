import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

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

  reasons: string[] = [
    'Violation of Community Guidelines',
    'Inappropriate Content',
    'Irrelevant or Off-topic',
    'Duplicate Content',
    'Trolling or Disruptive Behaviour',
    'Commercial or Promotional Content',
    'Political or Religious Sensitivity',
  ];

  constructor(private activeModal: NgbActiveModal) {}

  onDelete() {
    if (window.confirm('Are you sure? This action cannot be undone.')) {
      console.log('Deleted (not really).');
      console.log(
        this.selectedReason !== 'other' ? this.selectedReason : this.otherReason
      );
      this.activeModal.close('delete'); // Close this modal and resolve the Promise with 'delete'
    } else {
      this.activeModal.dismiss('cancel'); // Dismiss this modal and reject the Promise with 'cancel'
    }
  }

  onCancel() {
    this.activeModal.dismiss('cancel');
  }
}
