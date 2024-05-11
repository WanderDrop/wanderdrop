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
import { ActivatedRoute, Router } from '@angular/router';

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
  attraction!: Attraction | undefined;
  comments!: Comment[];
  attractionName: string = '';
  description: string = '';
  selectedAttractionId!: number;
  @ViewChild('addCommentContent') addCommentContent!: TemplateRef<any>;

  constructor(
    private modalService: NgbModal,
    private attractionService: AttractionService,
    private commentService: CommentService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.attraction = this.attractionService.getAttractionById(+id);
      if (this.attraction) {
        this.selectedAttractionId = this.attraction.id;
        this.comments = this.commentService.getComments(this.attraction.id);
        this.commentService
          .getCommentsUpdated()
          .subscribe((comments: Comment[]) => {
            this.comments = comments;
          });
      }
    }
  }

  onNavigateHome() {
    this.router.navigate(['/home']);
  }

  onAddComment() {
    const modalRef = this.modalService.open(AddCommentComponent);
    if (this.attraction) {
      modalRef.componentInstance.attractionId = this.attraction.id;
    }
  }

  openModify(content: any) {
    if (this.attraction) {
      this.attractionName = this.attraction.name;
      this.description = this.attraction.description;
      this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' });
    }
  }

  openDelete(content: any) {
    const modalRef = this.modalService.open(DeleteConfirmationComponent);

    modalRef.result
      .then((result) => {
        if (result === 'delete' && this.attraction) {
          this.attractionService.deleteAttraction(this.attraction.id);
          this.router.navigate(['/home']);
        }
      })
      .catch((reason) => {
        console.log('Modal dismissed due to: ', reason);
      });
  }

  onDataChanged(event: { attractionName: string; description: string }) {
    if (this.attraction) {
      this.attraction.name = event.attractionName;
      this.attraction.description = event.description;
    }
  }

  onAttractionSelected(attraction: Attraction) {
    this.selectedAttractionId = attraction.id;
  }
}
