import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AttractionService } from '../../attraction/attraction.service';
import { Comment } from '../comment.model';
import { CommentService } from '../comment.service';

@Component({
  selector: 'app-add-comment',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './add-comment.component.html',
  styleUrl: './add-comment.component.css',
})
export class AddCommentComponent {
  commentHeading = '';
  commentText = '';
  @Input() attractionId!: number;

  constructor(
    private modalService: NgbModal,
    private attractionService: AttractionService,
    private commentService: CommentService
  ) {}

  onSubmit() {
    const comment = new Comment(
      this.attractionId,
      this.commentHeading,
      this.commentText
    );
    this.commentService.addComment(comment);
    this.modalService.dismissAll();
  }

  onCancel() {
    this.modalService.dismissAll();
  }
}
