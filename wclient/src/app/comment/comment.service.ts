import { Injectable } from '@angular/core';
import { Comment } from './comment.model';

@Injectable({
  providedIn: 'root',
})
export class CommentService {
  comments: Comment[] = [];

  constructor() {}

  getComments(attractionId: number) {
    return this.comments.filter(
      (comment) => comment.attractionId === attractionId
    );
  }

  addComment(comment: Comment) {
    this.comments.push(comment);
  }
}
