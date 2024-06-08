import { ReportPageStatus } from './report-page-status.enum';

export class ReportPage {
  reportId!: number;
  reportHeading: string;
  reportMessage: string;

  constructor(
    reportHeading: string,
    reportMessage: string,
  ) {
    this.reportHeading = reportHeading;
    this.reportMessage = reportMessage;
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

  get status(): ReportPageStatus {
    return this.status;
  }
}
