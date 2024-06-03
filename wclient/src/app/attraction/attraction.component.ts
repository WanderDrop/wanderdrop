import {
  Component,
  OnDestroy,
  OnInit,
  TemplateRef,
  ViewChild,
} from '@angular/core';
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
import { ReportPage } from '../report-page/report-page.model';
import { AddNewReportPageComponent } from '../report-page/add-new-report-page/add-new-report-page.component';
import { DeleteReasonService } from '../shared/delete-reason.service';
import { Subscription, combineLatest } from 'rxjs';
import { ReportPageService } from '../report-page/report-page.service';
import { StorageService } from '../user/storage/storage.service';
import { AuthStatusService } from '../user/auth/auth-status.service';
import { AuthService } from '../user/auth/auth.service';

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
    AddNewReportPageComponent,
  ],
})
export class AttractionComponent implements OnInit, OnDestroy {
  attraction!: Attraction | undefined;
  reports!: ReportPage[];
  comments!: Comment[];
  attractionName: string = '';
  description: string = '';
  id: any = '';
  selectedAttractionId!: number;

  deletionReasons: { id: number; reasonMessage: string }[] = [];
  selectedReasonId: number | null = null;

  isAdmin = false;
  isAuthorized = false;
  private authSubscription!: Subscription;

  @ViewChild('addCommentContent') addCommentContent!: TemplateRef<any>;
  @ViewChild('addReportPageContent') addReportPageContent!: TemplateRef<any>;

  private subscriptions: Subscription[] = [];

  constructor(
    private modalService: NgbModal,
    private attractionService: AttractionService,
    private commentService: CommentService,
    private reportService: ReportPageService,
    private router: Router,
    private route: ActivatedRoute,
    private deleteReasonService: DeleteReasonService,
    private authStatusService: AuthStatusService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      const attractionSub = this.attractionService
        .fetchAttractionById(+id)
        .subscribe((attraction) => {
          this.attraction = attraction;
          if (this.attraction) {
            this.selectedAttractionId = this.attraction.id;
            this.commentService
              .getComments(this.attraction.id)
              .subscribe((comments) => {
                this.comments = comments;
              });
            const commentsSub = this.commentService
              .getCommentsUpdated()
              .subscribe((comments: Comment[]) => {
                this.comments = comments;
              });
            this.subscriptions.push(commentsSub);
          }
        });
      this.subscriptions.push(attractionSub);

      const reasonsSub = this.deleteReasonService.reasons.subscribe(
        (reasons) => {
          this.deletionReasons = reasons;
        }
      );
      this.subscriptions.push(reasonsSub);
    }

    this.isAdmin = StorageService.isAdminLoggedIn();
    this.isAuthorized =
      StorageService.isAdminLoggedIn() || StorageService.isUserLoggedIn();

    console.log('Initial admin status:', this.isAdmin);
    console.log('Initial authorized status:', this.isAuthorized);

    this.authSubscription = combineLatest([
      this.authStatusService.loggedInStatus$,
      this.authStatusService.userRoleStatus$,
    ]).subscribe(([isLoggedIn, userRole]) => {
      this.isAuthorized = isLoggedIn;
      this.isAdmin = isLoggedIn && userRole === 'ADMIN';

      console.log('Updated admin status:', this.isAdmin);
      console.log('Updated authorized status:', this.isAuthorized);

      if (this.isAdmin) {
        this.deleteReasonService.forceFetchReasons();
      } else {
        this.deleteReasonService.clearReasons();
      }
    });
  }

  onNavigateHome() {
    this.router.navigate(['/home']);
  }

  onAddComment() {
    const modalRef = this.modalService.open(AddCommentComponent);
    if (this.attraction) {
      modalRef.componentInstance.attractionId = this.attraction.id;
      modalRef.componentInstance.commentAdded.subscribe(
        (commentData: { commentHeading: string; commentText: string }) => {
          this.commentService
            .addComment(this.attraction!.id, commentData)
            .subscribe((newComment) => {
              this.comments.push(newComment);
            });
        }
      );
    }
  }

  onAddNewReportPage() {
    const modalRef = this.modalService.open(AddNewReportPageComponent);
    if (this.attraction) {
      modalRef.componentInstance.attractionId = this.attraction.id;
      modalRef.componentInstance.dataChanged.subscribe(
        (reportData: {
          reportHeading: AttractionComponent['attractionName'];
          reportMessage: string;
        }) => {
          this.reportService
            .addReport(this.attraction!.id, reportData)
            .subscribe((newReport) => {
              this.reports.push(newReport);
            });
        }
      );
    }
  }

  openModify(content: any) {
    if (this.attraction) {
      this.id = this.attraction.id;
      this.attractionName = this.attraction.name;
      this.description = this.attraction.description;
      this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' });
    }
  }

  openDelete(content: any) {
    const modalRef = this.modalService.open(DeleteConfirmationComponent);

    modalRef.result
      .then((result) => {
        if (
          result.action === 'delete' &&
          result.reasonId !== null &&
          this.attraction
        ) {
          this.selectedReasonId = result.reasonId;
          this.attractionService
            .deleteAttraction(this.attraction.id, this.selectedReasonId!)
            .subscribe(
              () => {
                this.attractionService.removeAttractionFromList(
                  this.attraction!.id
                );
                this.router.navigate(['/home']);
              },
              (error) => {
                console.error('Error deleting attraction:', error);
              }
            );
        }
      })
      .catch((reason) => {
        console.log('Modal dismissed due to: ', reason);
      });
  }

  onDataChanged(event: { attractionName: string; description: string }) {
    if (this.attraction) {
      const updatedAttraction = new Attraction(
        event.attractionName,
        event.description,
        this.attraction.latitude,
        this.attraction.longitude,
        this.attraction.createdBy
      );
      updatedAttraction.id = this.attraction.id;
      updatedAttraction.updatedBy = this.attraction.updatedBy;
      updatedAttraction.createdAt = this.attraction.createdAt!;
      updatedAttraction.status = this.attraction.status;
      updatedAttraction.deletionReason = this.attraction.deletionReason;

      this.attractionService
        .updateAttraction(this.attraction.id, updatedAttraction)
        .subscribe((updatedAttraction) => {
          this.attraction = updatedAttraction;
          this.modalService.dismissAll();
        });
    }
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((sub) => sub.unsubscribe());
  }
}
