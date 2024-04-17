import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-delete-confirmation',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './delete-confirmation.component.html',
  styleUrl: './delete-confirmation.component.css',
})
export class DeleteConfirmationComponent {
  reasons: string[] = [];
  onDelete() {}
  onCancel() {}
}
