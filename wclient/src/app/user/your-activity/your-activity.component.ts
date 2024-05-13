import { Component, OnInit } from '@angular/core';
import { Attraction } from '../../attraction/attraction.model';
import { Comment } from '../../comment/comment.model';
import { AttractionService } from '../../attraction/attraction.service';
import { CommentService } from '../../comment/comment.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-your-activity',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './your-activity.component.html',
  styleUrl: './your-activity.component.css',
})
export class YourActivityComponent implements OnInit {
  attractions: Attraction[] = [];
  comments: Comment[] = [];

  constructor(
    private attractionService: AttractionService,
    private commentService: CommentService
  ) {}

  ngOnInit(): void {
    this.attractions = this.attractionService.attractions;
    this.attractions.forEach((attraction) => {
      const attractionComments = this.commentService.getComments(attraction.id);
      this.comments = [...this.comments, ...attractionComments];
    });
  }
}
