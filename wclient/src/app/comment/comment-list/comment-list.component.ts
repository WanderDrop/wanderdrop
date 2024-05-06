import { Component, OnInit } from '@angular/core';
import { CommentItemComponent } from './comment-item/comment-item.component';
import { CommentService } from '../comment.service';
import { Comment } from '../comment.model';
import { CommonModule } from '@angular/common';
import { AttractionService } from '../../attraction/attraction.service';
import { switchMap, map } from 'rxjs';

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
    this.attractionService
      .getAttractionIdObservable()
      .pipe(
        switchMap((attractionId: number | null) => {
          if (attractionId !== null) {
            this.commentService.fetchComments(attractionId);
          }
          return this.commentService
            .getCommentsUpdated()
            .pipe(map((comments: Comment[]) => ({ attractionId, comments })));
        })
      )
      .subscribe(({ attractionId, comments }) => {
        if (attractionId !== null) {
          console.log('Fetching comments for attractionId:', attractionId);
          this.comments = comments.filter(
            (comment) => comment.attractionId === attractionId
          );
        }
      });
  }
}
