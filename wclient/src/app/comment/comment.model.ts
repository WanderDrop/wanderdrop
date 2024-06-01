import { CommentStatus } from './comment-status.enum';

export class Comment {
  commentId!: number;
  commentHeading: string;
  commentText: string;
  status: string;
  attractionId: number;
  createdAt: Date;
  author: string;
  deletionReasonId?: number;

  constructor(
    commentHeading: string,
    commentText: string,
    author: string,
    attractionId: number,
    commentId?: number,
    status?: string,
    createdAt?: Date,
    deletionReasonId?: number
  ) {
    this.commentHeading = commentHeading;
    this.commentText = commentText;
    this.author = author;
    this.attractionId = attractionId;
    this.commentId = commentId ?? 0;
    this.status = status ?? 'ACTIVE';
    this.createdAt = createdAt ?? new Date();
    this.deletionReasonId = deletionReasonId;
  }

  get _commentId(): number {
    return this._commentId;
  }

  get _commentHeading(): string {
    return this._commentHeading;
  }

  set _commentHeading(value: string) {
    this._commentHeading = value;
  }

  get _commentText(): string {
    return this._commentText;
  }

  set _commentText(value: string) {
    this._commentText = value;
  }

  get _status(): CommentStatus {
    return this._status;
  }

  set _status(value: CommentStatus) {
    this._status = value;
  }

  get _attractionId(): number {
    return this._attractionId;
  }

  get _date(): Date {
    return this._date;
  }

  get _author() {
    return this._author;
  }

  set _author(value: number) {
    this._author = value;
  }

  get _deletionReasonId(): number {
    return this._deletionReasonId;
  }

  set _deletionReasonId(value: number) {
    this._deletionReasonId = value;
  }
}
