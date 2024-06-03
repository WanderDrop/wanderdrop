import { Component, OnInit } from '@angular/core';
import { Attraction } from '../../attraction/attraction.model';
import { Comment } from '../../comment/comment.model';
import { AttractionService } from '../../attraction/attraction.service';
import { CommentService } from '../../comment/comment.service';
import { CommonModule } from '@angular/common';
import { switchMap, map, forkJoin, Observable } from 'rxjs';

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
    this.attractionService
      .fetchUserAttractions()
      .pipe(
        switchMap((attractions: Attraction[]) => {
          this.attractions = attractions;
          const commentsObservables: Observable<{
            attractionId: number;
            comments: Comment[];
          }>[] = attractions.map((attraction: Attraction) =>
            this.commentService.getComments(attraction.id).pipe(
              map((comments: Comment[]) => ({
                attractionId: attraction.id,
                comments,
              }))
            )
          );
          return forkJoin(commentsObservables);
        })
      )
      .subscribe(
        (commentGroups: { attractionId: number; comments: Comment[] }[]) => {
          this.comments = commentGroups.flatMap(
            (group: { attractionId: number; comments: Comment[] }) =>
              group.comments
          );
        }
      );
  }
}
