import { Injectable } from '@angular/core';
import { Comment } from './comment.model';

@Injectable({
  providedIn: 'root',
})
export class CommentService {
  comments: Comment[] = [
    new Comment(1, 'Nice place', 'I fell in love with this place.'),
    new Comment(1, 'Very nice', 'Loved the incredible view.'),
  ];

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
