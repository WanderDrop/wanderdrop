import { Component } from '@angular/core';
import { CommentItemComponent } from './comment-item/comment-item.component';

@Component({
  selector: 'app-comment-list',
  standalone: true,
  templateUrl: './comment-list.component.html',
  styleUrl: './comment-list.component.css',
  imports: [CommentItemComponent],
})
export class CommentListComponent {
  comments: Comment[] = [];
}
