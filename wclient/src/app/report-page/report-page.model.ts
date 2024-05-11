import { ReportPageStatus } from './report-page-status.enum';

export class ReportPage {
  private _reportId: number;
  private _reportHeading: string;
  private _reportText: string;
  private _attractionId: number;
  private _createdBy!: number;
  private _updatedBy?: number;
  private _createdAt: Date;
  private _updatedAt?: Date;
  private _status: ReportPageStatus;
  private _deletionReason?: number;
  private static lastId = 0;


  constructor(
    attractionId:number,
    reportHeading: string,
    reportText: string,
    createdBy: number
  ) {
    this._reportId = ReportPage.lastId++;
    this._reportHeading = reportHeading;
    this._reportText = reportText;
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

  get reportHeading(): string {
    return this._reportHeading;
  }

  set reportHeading(value: string) {
    this._reportHeading = value;
  }

  get reportText(): string {
    return this._reportText;
  }

  set reportText(value: string) {
    this._reportText = value;
  }

  get createdBy(): number {
    return this._createdBy;
  }

  set createdBy(value: number) {
    this._createdBy = value;
  }

  get updatedBy(): number | undefined {
    return this._updatedBy;
  }

  set createdAt(value:Date){
    this.createdAt = value
  }

  get createdAt():Date{
    return this._createdAt;
  }



  set updatedBy(value: number | undefined) {
    this._updatedBy = value;
  }

  get updatedAt(): Date | undefined {
    return this._updatedAt;
  }

  set updatedAt(value: Date | undefined) {
    this._updatedAt = value;
  }

  get attractionId(): number {
    return this._attractionId;
  }

  get status(): ReportPageStatus {
    return this._status;
  }

  set status(value: ReportPageStatus) {
    this._status = value;
  }

  get deletionReason(): number | undefined {
    return this._deletionReason;
  }

  set deletionReason(value: number | undefined) {
    this._deletionReason = value;
  }
}
