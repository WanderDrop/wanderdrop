import { Component, } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ReportListComponent } from "./report-list/report-list.component";


@Component({
    selector: 'app-report-page',
    standalone: true,
    templateUrl: './report-page.component.html',
    styleUrl: './report-page.component.css',
    imports: [ReportListComponent, RouterModule]
})
export class ReportPageComponent {}

