import { Component, Input, OnInit } from '@angular/core';
import { Attraction } from '../../../attraction/attraction.model';
import { Comment } from '../../comment.model';

@Component({
  selector: 'app-comment-item',
  standalone: true,
  imports: [],
  templateUrl: './comment-item.component.html',
  styleUrl: './comment-item.component.css',
})
export class CommentItemComponent {
  @Input() comment!: Comment;

}
