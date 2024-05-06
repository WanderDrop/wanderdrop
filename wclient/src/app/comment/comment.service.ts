import { Injectable } from '@angular/core';
import { Comment } from './comment.model';
import { BehaviorSubject, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CommentService {
  comments: { [attractionId: number]: Comment[] } = {};
  private commentsUpdated = new BehaviorSubject<Comment[]>([]);

  constructor() {}

  getCommentsUpdated() {
    return this.commentsUpdated.asObservable();
  }

  getComments(attractionId: number) {
    return this.comments[attractionId] || [];
  }

  addComment(comment: Comment) {
    console.log('adding comment: ' + comment.attractionId);
    if (!this.comments[comment.attractionId]) {
      this.comments[comment.attractionId] = [];
    }
    this.comments[comment.attractionId].push(comment);
    this.commentsUpdated.next(this.comments[comment.attractionId]);
  }

  deleteComment(commentId: number, attractionId: number) {
    if (this.comments[attractionId]) {
      this.comments[attractionId] = this.comments[attractionId].filter(
        (comment) => comment.commentId !== commentId
      );
      this.commentsUpdated.next(this.comments[attractionId]);
    }
  }
}
