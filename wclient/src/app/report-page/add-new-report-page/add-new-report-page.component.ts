import { Component, EventEmitter, Input, NgZone, Output } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../../user/user.service';
import { ReportPage } from '../report-page.model';
import { ReportPageService } from '../report-page.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AttractionComponent } from '../../attraction/attraction.component';
import { CommonModule } from '@angular/common';
import { Subscription } from 'rxjs';


@Component({
  selector: 'app-add-new-report-page',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './add-new-report-page.component.html',
  styleUrl: './add-new-report-page.component.css',
})
export class AddNewReportPageComponent {
  @Input() reportMessage: string = '';
  @Input() attractionId!: number;
  @Input() attractionName: string = '';
  @Output() dataChanged = new EventEmitter<{
    reportHeading: AttractionComponent["attractionName"];
    reportMessage: string;
  }>();
  private subscriptions: Subscription[] = [];
  constructor(
    private router: Router,
    private reportService: ReportPageService,
    private ngZone: NgZone,
    private userService: UserService,
    private modalService: NgbModal
  ){}
  onClose() {
    this.ngZone.run(() => {
      this.router.navigate(['/home']);
    });
  }
  onAddReport() {
    const userId = this.userService.getDummyUser().UserId;

    const newReport = new ReportPage(
      this.attractionName,
      this.reportMessage,
    );

    const addReportSub = this.reportService
      .addReport(this.attractionId, newReport)
      .subscribe({
        next: (response)=>{
          this.reportService.reports.push(response);
        },
          error: (error) => { console.error('Error adding new report:', error);
        },
      });
      this.subscriptions.push(addReportSub);
      console.log("Report done");
      this.modalService.dismissAll();
  }
  onCancel() {
    this.modalService.dismissAll();
  }
}
