import { AddNewAttractionComponent } from '../attraction/add-new-attraction/add-new-attraction.component';
import { ReportPageStatus } from './report-page-status.enum';

export class ReportPage {
  private _reportId!: number;
  private _reportHeading: string;
  private _reportMessage: string;
  private _status: ReportPageStatus;
  private _attractionId!: number;
  private _createdBy!: number;
  private _createdAt: Date;
  static _reportId:any;


  constructor(
    attractionId:number,
    reportHeading: string,
    reportMessage: string,
    createdBy: number
  ) {
    this._reportHeading = reportHeading;
    this._reportMessage = reportMessage;
    this._createdBy = createdBy;
    this._attractionId = attractionId;
    this._createdAt = new Date();
    this._status = ReportPageStatus.Active;
  }

  get reportId(): number {
    return this._reportId;
  }

  set reportId(value: number) {
    this._reportId = value;
  }

  get reportHeading(): string{
    return this._reportHeading;
  }

  set reportHeading(value: string) {
    this._reportHeading = value;

  }

  get reportMessage(): string {
    return this._reportMessage;
  }

  set reportMessage(value: string) {
    this._reportMessage = value;
  }

  get status(): ReportPageStatus {
    return this._status;
  }

  set status(value: ReportPageStatus) {
    this._status = value;
  }

  get createdBy(): number {
    return this._createdBy;
  }

  set createdBy(value: number) {
    this._createdBy = value;
  }

  set createdAt(value:Date){
    this.createdAt = value
  }

  get createdAt():Date{
    return this._createdAt;
  }

  get attractionId(): number {
    return this._attractionId;
  }
  static fromResponse(response: any): ReportPage {
    const report = new ReportPage(
      response.attractionId,
      response.reportHeading,
      response.reportMessage,
      response.createdBy,
    );
    report._reportId = response.reportId;
    report.createdAt = new Date(response.createdAt);
    return report;
  }

  toRequestPayLoad(){
    return {
      attractionId: this.attractionId,
      reportHeading: this.reportHeading,
      reportMessage: this.reportMessage,
    }
  }
  
}
