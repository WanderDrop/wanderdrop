import { Component, OnDestroy, OnInit } from '@angular/core';
import { CommentItemComponent } from './comment-item/comment-item.component';
import { CommentService } from '../comment.service';
import { Comment } from '../comment.model';
import { CommonModule } from '@angular/common';
import { AttractionService } from '../../attraction/attraction.service';
import { switchMap, map, Subscription } from 'rxjs';

@Component({
  selector: 'app-comment-list',
  standalone: true,
  templateUrl: './comment-list.component.html',
  styleUrl: './comment-list.component.css',
  imports: [CommentItemComponent, CommonModule],
})
export class CommentListComponent implements OnInit, OnDestroy {
  comments: Comment[] = [];
  private subscriptions: Subscription[] = [];

  constructor(
    private commentService: CommentService,
    private attractionService: AttractionService
  ) {}

  ngOnInit(): void {
    const attractionIdObservableSub = this.attractionService
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
          this.comments = comments.filter(
            (comment) => comment.attractionId === attractionId
          );
        }
      });
    this.subscriptions.push(attractionIdObservableSub);
  }

  ngOnDestroy() {
    this.subscriptions.forEach((sub) => sub.unsubscribe());
  }
}
