import { Component, OnDestroy, OnInit } from '@angular/core';
import { ReportPage } from './report-page.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ReportPageService } from './report-page.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';


@Component({
    selector: 'app-report-page',
    standalone: true,
    templateUrl: './report-page.component.html',
    styleUrl: './report-page.component.css',
    imports: []
})
export class ReportPageComponent implements OnInit, OnDestroy{
        reportPage!: ReportPage | undefined;
        reportHeading: string = '';
        reportMessage: string = '';
        reportId: any = '';
        selectedReportPageId!: number;
    
        private subscriptions: Subscription[] = [];

        constructor(
          private modalService: NgbModal,
          private reportPageService: ReportPageService,
          private router: Router,
          private route: ActivatedRoute
        ) {}

      ngOnInit(): void {
        if(this.reportId) {
          const reportSub = this.reportPageService
          .fetchReportById(+this.reportId)
          .subscribe((reportPage) => {this.reportPage = reportPage;     
            if(this.reportPage) {
              this.selectedReportPageId = this.reportPage.reportId;
            }});
            this.subscriptions.push(reportSub);
          }
        }
  
        onNavigateHome() {
          this.router.navigate(['/home']);
        }
          
    
        onReportPageSelected(reportPage: ReportPage) {
        this.selectedReportPageId = reportPage.reportId;
        }

        
        ngOnDestroy(): void {
          this.subscriptions.forEach((sub) => sub.unsubscribe());
        }
      }
    
