import { Injectable } from '@angular/core';
import { Comment } from './comment.model';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CommentService {
  comments: Comment[] = [
    new Comment(1, 'Nice place', 'I fell in love with this place.'),
    new Comment(1, 'Very nice', 'Loved the incredible view.'),
  ];
  private commentsUpdated = new Subject<Comment[]>();

  constructor() {}

  getCommentsUpdated() {
    return this.commentsUpdated.asObservable();
  }

  getComments(attractionId: number) {
    return this.comments.filter(
      (comment) => comment.attractionId === attractionId
    );
  }

  addComment(comment: Comment) {
    this.comments.push(comment);
    this.commentsUpdated.next([...this.comments]);
  }

  deleteComment(commentId: number) {
    this.comments = this.comments.filter(
      (comment) => comment.commentId !== commentId
    );
    this.commentsUpdated.next([...this.comments]);
  }
}
