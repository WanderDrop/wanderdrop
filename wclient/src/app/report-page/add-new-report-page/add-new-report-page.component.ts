import { Component, EventEmitter, Input, NgZone, OnDestroy, OnInit, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../../user/user.service';
import { ReportPage } from '../report-page.model';
import { ReportPageService } from '../report-page.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AttractionComponent } from '../../attraction/attraction.component';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-add-new-report-page',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './add-new-report-page.component.html',
  styleUrl: './add-new-report-page.component.css',
})
export class AddNewReportPageComponent implements OnDestroy {
  @Input() reportMessage: string = '';
  @Input() attractionId!: number;
  @Input() attractionName: string = '';
  @Output() dataChanged = new EventEmitter<{
    attractionId: AttractionComponent["id"];
    reportHeading: AttractionComponent["attractionName"];
    reportMessage: string;
  }>();
  private subscriptions: Subscription[] = [];

  constructor(
    private router: Router,
    private ngZone: NgZone,
    private modalService: NgbModal,
    private reportPageService: ReportPageService,
    private userService: UserService
  ) { }

  ngOnDestroy(): void {
    this.subscriptions.forEach((sub) => sub.unsubscribe());
  }

  onClose() {
    this.ngZone.run(() => {
      this.router.navigate(['/home']);
    });
  }

  closeModal() {
    this.modalService.dismissAll();
  }

  onSubmit() {
    const reportPage = new ReportPage(
      this.attractionId,
      this.attractionName,
      this.reportMessage,
      this.userService.getDummyUser().UserId
    );
    this.reportPageService.addReport(reportPage);
    this.modalService.dismissAll();
  }

  onCancel() {
    this.modalService.dismissAll();
  }
  onAddReportPage() {
    const userId = this.userService.getDummyUser().UserId;

    const newReport = new ReportPage(
      this.attractionId,
      this.attractionName,
      this.reportMessage,
      userId
    );
    const addReportPageSub = this.reportPageService
      .addReport(newReport)
      .subscribe({
        next: (response) => {
          this.reportPageService.reports.push(response);
        },
        error: (error) => {
          console.error("Error adding new report", error);
        },
      })
    this.subscriptions.push(addReportPageSub);
  }
}
