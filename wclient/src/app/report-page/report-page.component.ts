import { Component, OnInit } from '@angular/core';
import { DeleteConfirmationComponent } from "../shared/delete-confirmation/delete-confirmation.component";
import { ReportPage } from './report-page.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ReportPageService } from './report-page.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
    selector: 'app-report-page',
    standalone: true,
    templateUrl: './report-page.component.html',
    styleUrl: './report-page.component.css',
    imports: [DeleteConfirmationComponent]
})
export class ReportPageComponent {
        reportPage!: ReportPage | undefined;
        reportHeading: string = '';
        reportText: string = '';
        selectedReportPageId!: number;
      
      
        constructor(
          private modalService: NgbModal,
          private reportPageService: ReportPageService,
          private router: Router,
          private route: ActivatedRoute
        ) {}
      
        onNavigateHome() {
          this.router.navigate(['/home']);
        }
       
        openModify(content: any) {
          if (this.reportPage) {
            this.reportHeading = this.reportPage.reportHeading;
            this.reportText = this.reportPage.reportText;
            this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' });
          }
        }
      
        openDelete(content: any) {
          const modalRef = this.modalService.open(DeleteConfirmationComponent);
      
          modalRef.result
            .then((result) => {
              if (result === 'delete') {
                console.log('DELETED');
              }
            })
            .catch((reason) => {
              console.log('Modal dismissed due to: ', reason);
            });
        }
      
        onDataReportPageChanged(event: { reportPageName: string; description: string }) {
          if (this.reportPage) {
            this.reportPage.reportHeading = event.reportPageName;
            this.reportPage.reportText = event.description;
          }
        }
      
        onReportPageSelected(reportPage: ReportPage) {
          this.selectedReportPageId = reportPage.reportId;
        }
      }
    
