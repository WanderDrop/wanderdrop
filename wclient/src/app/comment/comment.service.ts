import { Injectable } from '@angular/core';
import { Comment } from './comment.model';
import { BehaviorSubject, Observable, map } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { StorageService } from '../user/storage/storage.service';

@Injectable({
  providedIn: 'root',
})
export class CommentService {
  comments: { [attractionId: number]: Comment[] } = {};
  private commentsUpdated = new BehaviorSubject<Comment[]>([]);
  private baseUrl = `http://localhost:8080/api`;
  private currentAttractionId: number | null = null;

  constructor(private http: HttpClient) {}

  getCommentsUpdated() {
    return this.commentsUpdated.asObservable();
  }

  getComments(attractionId: number): Observable<Comment[]> {
    return this.http.get<Comment[]>(
      `${this.baseUrl}/comments/attraction/${attractionId}`
    );
  }

  fetchComments(attractionId: number): void {
    if (this.currentAttractionId !== attractionId) {
      this.currentAttractionId = attractionId;
      this.getComments(attractionId).subscribe((comments) => {
        this.comments[attractionId] = comments;
        this.commentsUpdated.next([...this.comments[attractionId]]);
      });
    }
  }

  addComment(
    attractionId: number,
    commentData: { commentHeading: string; commentText: string }
  ): Observable<Comment> {
    const token = StorageService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.post<Comment>(
      `${this.baseUrl}/comments/${attractionId}`,
      commentData,
      { headers }
    );
  }

  deleteComment(commentId: number, attractionId: number) {
    if (this.comments[attractionId]) {
      this.comments[attractionId] = this.comments[attractionId].filter(
        (comment) => comment.commentId !== commentId
      );
      this.commentsUpdated.next(this.comments[attractionId]);
    }
  }

  clearComments(attractionId: number): void {
    this.comments[attractionId] = [];
    this.commentsUpdated.next([...this.comments[attractionId]]);
  }
}
