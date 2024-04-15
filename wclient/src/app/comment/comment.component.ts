import { Component } from '@angular/core';
import { CommentListComponent } from './comment-list/comment-list.component';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-comments',
  standalone: true,
  templateUrl: './comment.component.html',
  styleUrl: './comment.component.css',
  imports: [CommentListComponent, RouterModule],
})
export class CommentsComponent {}
