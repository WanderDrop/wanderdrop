import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

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

  constructor(private modalService: NgbModal) {}

  onDelete() {
    console.log('Attraction deleted (not really).');
  }

  onCancel() {
    this.modalService.dismissAll();
  }
}
