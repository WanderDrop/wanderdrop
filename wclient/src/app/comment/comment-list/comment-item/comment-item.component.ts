import { Component, Input, OnInit } from '@angular/core';
import { Comment } from '../../comment.model';
import { CommentService } from '../../comment.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { DeleteConfirmationComponent } from '../../../shared/delete-confirmation/delete-confirmation.component';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-comment-item',
  standalone: true,
  imports: [],
  templateUrl: './comment-item.component.html',
  styleUrl: './comment-item.component.css',
})
export class CommentItemComponent {
  @Input() comment!: Comment;
  private datePipe = new DatePipe('en-US');

  constructor(
    private commentService: CommentService,
    private modalService: NgbModal
  ) {}

  onDeleteComment(commentId: number) {
    const modalRef = this.modalService.open(DeleteConfirmationComponent);

    modalRef.result
      .then((result) => {
        if (result.action === 'delete' && result.reasonId !== null) {
          this.commentService
            .deleteComment(commentId, result.reasonId)
            .subscribe(() => {
              console.log(
                `Comment ${commentId} deleted with reason ${result.reasonId}`
              );
            });
        }
      })
      .catch((reason) => {
        console.log('Modal dismissed due to: ', reason);
      });
  }

  formatDate(date: string | Date): string {
    return this.datePipe.transform(date, 'yyyy-MM-dd') || '';
  }
}
