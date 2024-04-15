import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { Attraction } from './attraction.model';
import { CommentItemComponent } from '../comment/comment-list/comment-item/comment-item.component';
import { CommentListComponent } from '../comment/comment-list/comment-list.component';
import { CommentsComponent } from '../comment/comment.component';
import { AddCommentComponent } from '../comment/add-comment/add-comment.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AttractionService } from './attraction.service';

@Component({
  selector: 'app-attraction',
  standalone: true,
  templateUrl: './attraction.component.html',
  styleUrl: './attraction.component.css',
  imports: [
    CommentItemComponent,
    CommentListComponent,
    CommentsComponent,
    AddCommentComponent,
  ],
})
export class AttractionComponent implements OnInit {
  attraction!: Attraction;
  @ViewChild('addCommentContent') addCommentContent!: TemplateRef<any>;

  constructor(
    public modalService: NgbModal,
    private attractionService: AttractionService
  ) {}

  ngOnInit(): void {
    this.attraction = this.attractionService.getAttraction();
  }

  onAddComment() {
    this.modalService.open(this.addCommentContent, {
      ariaLabelledBy: 'modal-basic-title',
    });
  }
}
