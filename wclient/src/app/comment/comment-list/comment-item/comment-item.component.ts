import { Component, OnInit } from '@angular/core';
import { Attraction } from '../../../attraction/attraction.model';
import { Comment } from '../../comment.model';

@Component({
  selector: 'app-comment-item',
  standalone: true,
  imports: [],
  templateUrl: './comment-item.component.html',
  styleUrl: './comment-item.component.css',
})
export class CommentItemComponent implements OnInit {
  comment: Comment | undefined;

  ngOnInit(): void {
    this.comment = new Comment(
      1,
      'Nice place',
      'I fell in love with this place.'
    );
  }
}
