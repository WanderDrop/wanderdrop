import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AttractionService } from '../../attraction/attraction.service';

@Component({
  selector: 'app-add-comment',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './add-comment.component.html',
  styleUrl: './add-comment.component.css',
})
export class AddCommentComponent {
  constructor(
    private modalService: NgbModal,
    private attractionService: AttractionService
  ) {}

  onSubmit() {
    // const attractionId = this.attractionService.getAttractionId();
  }

  onCancel() {
    this.modalService.dismissAll();
  }
}
