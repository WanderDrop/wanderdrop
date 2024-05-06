import { Component, OnInit } from '@angular/core';
import { CommentItemComponent } from './comment-item/comment-item.component';
import { CommentService } from '../comment.service';
import { Comment } from '../comment.model';
import { CommonModule } from '@angular/common';
import { AttractionService } from '../../attraction/attraction.service';

@Component({
  selector: 'app-comment-list',
  standalone: true,
  templateUrl: './comment-list.component.html',
  styleUrl: './comment-list.component.css',
  imports: [CommentItemComponent, CommonModule],
})
export class CommentListComponent implements OnInit {
  comments: Comment[] = [];

  constructor(
    private commentService: CommentService,
    private attractionService: AttractionService
  ) {}

  ngOnInit(): void {
    this.commentService
      .getCommentsUpdated()
      .subscribe((comments: Comment[]) => {
        const attractionId = this.attractionService.getAttractionId();
        console.log('Fetching comments for attractionId:', attractionId);
        this.comments = comments.filter(
          (comment) => comment.attractionId === attractionId
        );
      });
  }
}
