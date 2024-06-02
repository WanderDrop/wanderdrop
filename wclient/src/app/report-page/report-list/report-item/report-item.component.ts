import { Component, Input } from '@angular/core';
import { ReportPage } from '../../report-page.model';
import { DatePipe } from '@angular/common';
import { ReportPageService } from '../../report-page.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { DeleteConfirmationComponent } from '../../../shared/delete-confirmation/delete-confirmation.component';

@Component({
  selector: 'app-report-item',
  standalone: true,
  imports: [],
  templateUrl: './report-item.component.html',
  styleUrl: './report-item.component.css'
})
export class ReportItemComponent {
  @Input() report!: ReportPage;
  private datePipe = new DatePipe('en-US');
  constructor(
    private reportPageService: ReportPageService,
    private modalService: NgbModal
  ){}

  onDeleteReport(reportId: number){
    const modalRef = this.modalService.open(DeleteConfirmationComponent);
    modalRef.result
    .then((result) => {
      if(result.action === 'delete'){
        this.reportPageService
        .deleteReport(reportId)
        .subscribe(() => {
          console.log(`Report${reportId} deleted`);
        });
      }
      })
    .catch((reason) => {
      console.log('Modal dismissed due to: ', reason);
    })
  }
  formatDate(date: string | Date): string {
    return this.datePipe.transform(date, 'yyyy-MM-dd') || '';
  }
}