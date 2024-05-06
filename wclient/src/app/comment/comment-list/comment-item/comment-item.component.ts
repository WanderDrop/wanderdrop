import { Component, Input, OnInit } from '@angular/core';
import { Comment } from '../../comment.model';
import { CommentService } from '../../comment.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { DeleteConfirmationComponent } from '../../../shared/delete-confirmation/delete-confirmation.component';

@Component({
  selector: 'app-comment-item',
  standalone: true,
  imports: [],
  templateUrl: './comment-item.component.html',
  styleUrl: './comment-item.component.css',
})
export class CommentItemComponent {
  @Input() comment!: Comment;

  constructor(
    private commentService: CommentService,
    private modalService: NgbModal
  ) {}

  onDeleteComment(commentId: number) {
    const modalRef = this.modalService.open(DeleteConfirmationComponent);
    modalRef.componentInstance.selectedReason = '';
    modalRef.componentInstance.otherReason = '';

    console.log(modalRef.result);

    modalRef.result
      .then((result) => {
        if (result === 'delete') {
          this.commentService.deleteComment(
            commentId,
            this.comment.attractionId
          );
        }
      })
      .catch((reason) => {
        console.log('Modal dismissed due to: ', reason);
      });
  }
}
