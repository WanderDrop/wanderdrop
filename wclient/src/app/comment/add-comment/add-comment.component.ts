import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AttractionService } from '../../attraction/attraction.service';
import { Comment } from '../comment.model';
import { CommentService } from '../comment.service';
import { UserService } from '../../user/user.service';

@Component({
  selector: 'app-add-comment',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './add-comment.component.html',
  styleUrl: './add-comment.component.css',
})
export class AddCommentComponent {
  @Input() attractionId!: number;
  @Output() commentAdded = new EventEmitter<{
    commentHeading: string;
    commentText: string;
  }>();
  commentForm: FormGroup;

  constructor(private fb: FormBuilder, private modalService: NgbModal) {
    this.commentForm = this.fb.group({
      commentHeading: ['', Validators.required],
      commentText: ['', Validators.required],
    });
  }

  onSubmit() {
    if (this.commentForm.valid) {
      const { commentHeading, commentText } = this.commentForm.value;
      this.commentAdded.emit({ commentHeading, commentText });
      this.modalService.dismissAll();
    }
  }

  onCancel() {
    this.modalService.dismissAll();
  }
}
