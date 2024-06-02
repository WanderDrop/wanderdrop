import { Component, OnInit } from '@angular/core';
import { ReportPage } from '../../report-page/report-page.model';
import { ReportPageService } from '../../report-page/report-page.service';
import { Attraction } from '../../attraction/attraction.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-reports',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './reports.component.html',
  styleUrl: './reports.component.css'
})
export class ReportsComponent {}


