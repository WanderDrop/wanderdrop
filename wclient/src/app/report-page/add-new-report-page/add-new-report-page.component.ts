import { Component, EventEmitter, Input, NgZone, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../../user/user.service';
import { ReportPage } from '../report-page.model';
import { ReportPageService } from '../report-page.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AttractionService } from '../../attraction/attraction.service';

@Component({
  selector: 'app-add-new-report-page',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './add-new-report-page.component.html',
  styleUrl: './add-new-report-page.component.css'
})
export class AddNewReportPageComponent {
  @Input() reportPageHeading: string = '';
  @Input() reportPageText: string = '';
  @Input() attractionId!: number;
  @Output() dataChanged = new EventEmitter<{
    reportPageHeading: string;
    reportPageText: string;
  }>();

  constructor(
    private router: Router,
    private ngZone: NgZone,
    private modalService: NgbModal,
    private reportPageService: ReportPageService,
    private attractionService:AttractionService,
    private userService : UserService
  ) {}
  
  onClose() {
    this.ngZone.run(() => {
      this.router.navigate(['/home']);
    });
  }

  closeModal() {
    this.modalService.dismissAll();
  }
  onSaveChanges() {
    if (window.confirm('Are you sure you want to send a report?')) {
      this.dataChanged.emit({
        reportPageHeading: this.reportPageHeading,
        reportPageText: this.reportPageText,
      });
    }
    this.modalService.dismissAll();
  }
 
  onSubmit() {
    const reportPage = new ReportPage(
      this.attractionId,
      this.reportPageHeading,
      this.reportPageText,
      this.userService.getDummyUser().userId
    );
    this.reportPageService.addReportPage(reportPage);
    this.modalService.dismissAll();
  }

  onCancel() {
    this.modalService.dismissAll();
  }

  onAddReportPage() {
    const reportPage = new ReportPage(
      this.attractionId,
      this.reportPageHeading,
      this.reportPageText,
      this.userService.getDummyUser().userId
    );
    const newReportPageId = reportPage.reportId;
}
}