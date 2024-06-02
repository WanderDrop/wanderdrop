import { ReportPageStatus } from './report-page-status.enum';

export class ReportPage {
  reportId!: number;
  reportHeading: string;
  reportMessage: string;
  status: string;
  attractionId!: number;
  createdBy!: number;
  createdAt: Date;


  constructor(
    reportHeading: string,
    reportMessage: string,
    attractionId: number,
    createdBy: number,
    status?: string,
    reportId?: number,
    createdAt?: Date
  ) {
    this.reportHeading = reportHeading;
    this.reportMessage = reportMessage;
    this.createdBy = createdBy;
    this.attractionId = attractionId;
    this.createdAt =  createdAt ?? new Date();
    this.status = status ?? 'active';
  }

  get _reportId(): number {
    return this._reportId;
  }

  set _reportId(value: number) {
    this._reportId = value;
  }

  get _reportHeading(): string {
    return this._reportHeading;
  }

  set _reportHeading(value: string) {
    this._reportHeading = value;

  }

  get _reportMessage(): string {
    return this._reportMessage;
  }

  set _reportMessage(value: string) {
    this._reportMessage = value;
  }

  get _status(): ReportPageStatus {
    return this._status;
  }

  set _status(value: ReportPageStatus) {
    this._status = value;
  }

  get _createdBy(): number {
    return this._createdBy;
  }

  set _createdBy(value: number) {
    this._createdBy = value;
  }

  set _createdAt(value: Date) {
    this.createdAt = value
  }

  get _createdAt(): Date {
    return this._createdAt;
  }

  get _attractionId(): number {
    return this._attractionId;
  }
}
