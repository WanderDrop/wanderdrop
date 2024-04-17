import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { Attraction } from './attraction.model';
import { CommentItemComponent } from '../comment/comment-list/comment-item/comment-item.component';
import { CommentListComponent } from '../comment/comment-list/comment-list.component';
import { CommentsComponent } from '../comment/comment.component';
import { AddCommentComponent } from '../comment/add-comment/add-comment.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AttractionService } from './attraction.service';
import { CommentService } from '../comment/comment.service';
import { Comment } from '../comment/comment.model';
import { CommonModule } from '@angular/common';
import { ModifyAttractionComponent } from './modify-attraction/modify-attraction.component';
import { DeleteConfirmationComponent } from '../shared/delete-confirmation/delete-confirmation.component';

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
    CommonModule,
    ModifyAttractionComponent,
    DeleteConfirmationComponent,
  ],
})
export class AttractionComponent implements OnInit {
  attraction!: Attraction;
  comments!: Comment[];
  attractionName: string = '';
  description: string = '';
  @ViewChild('addCommentContent') addCommentContent!: TemplateRef<any>;

  constructor(
    private modalService: NgbModal,
    private attractionService: AttractionService,
    private commentService: CommentService
  ) {}

  ngOnInit(): void {
    this.attraction = this.attractionService.getAttraction();
    this.comments = this.commentService.getComments(this.attraction.id);
    this.commentService
      .getCommentsUpdated()
      .subscribe((comments: Comment[]) => {
        this.comments = comments;
      });
  }

  onAddComment() {
    this.modalService.open(this.addCommentContent, {
      ariaLabelledBy: 'modal-basic-title',
    });
  }

  open(content: any) {
    this.attractionName = this.attraction.name;
    this.description = this.attraction.description;
    this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' });
  }

  onDataChanged(event: { attractionName: string; description: string }) {
    this.attraction.name = event.attractionName;
    this.attraction.description = event.description;
  }
}
