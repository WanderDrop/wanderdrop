import { Component, Input, OnDestroy, OnInit } from '@angular/core';
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
  @Input() comments: Comment[] = [];
  private subscriptions: Subscription[] = [];

  constructor(
    private commentService: CommentService,
    private attractionService: AttractionService
  ) {}

  ngOnInit(): void {
    const attractionIdObservableSub = this.attractionService
      .getAttractionIdObservable()
      .subscribe((attractionId: number | null) => {
        if (attractionId !== null) {
          this.commentService.clearComments(attractionId);
          this.commentService.fetchComments(attractionId);
        }
      });

    const commentsUpdatedSub = this.commentService
      .getCommentsUpdated()
      .subscribe((comments: Comment[]) => {
        console.log('Updated comments received:', comments);
        this.comments = comments;
      });

    this.subscriptions.push(attractionIdObservableSub, commentsUpdatedSub);
  }

  ngOnDestroy() {
    this.subscriptions.forEach((sub) => sub.unsubscribe());
  }
}
