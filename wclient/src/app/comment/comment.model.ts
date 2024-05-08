import { CommentStatus } from './comment-status.enum';

export class Comment {
  private _commentId!: number;
  private _commentHeading: string;
  private _commentText: string;
  private _status: CommentStatus;
  private _attractionId: number;
  private _date: Date;
  private _author: number;
  private _deletionReasonId!: number;
  public static lastId = 0;

  constructor(
    attractionId: number,
    commentHeading: string,
    commentText: string,
    author: number
  ) {
    Comment.lastId++;
    this._commentId = Comment.lastId;
    this._commentHeading = commentHeading;
    this._commentText = commentText;
    this._status = CommentStatus.Active;
    this._attractionId = attractionId;
    this._date = new Date();
    this._author = author;
  }

  get commentId(): number {
    return this._commentId;
  }

  get commentHeading(): string {
    return this._commentHeading;
  }

  set commentHeading(value: string) {
    this._commentHeading = value;
  }

  get commentText(): string {
    return this._commentText;
  }

  set commentText(value: string) {
    this._commentText = value;
  }

  get status(): CommentStatus {
    return this._status;
  }

  set status(value: CommentStatus) {
    this._status = value;
  }

  get attractionId(): number {
    return this._attractionId;
  }

  get date(): Date {
    return this._date;
  }

  get author() {
    return this._author;
  }

  set author(value: number) {
    this._author = value;
  }

  get deletionReasonId(): number {
    return this._deletionReasonId;
  }

  set deletionReasonId(value: number) {
    this._deletionReasonId = value;
  }
}
