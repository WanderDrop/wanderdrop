import { Injectable } from '@angular/core';
import { Comment } from './comment.model';
import { BehaviorSubject, Observable, catchError, map, of } from 'rxjs';
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
    this.currentAttractionId = attractionId;
    this.getComments(attractionId).subscribe({
      next: (comments) => {
        this.comments[attractionId] = comments;
        this.commentsUpdated.next([...this.comments[attractionId]]);
      },
      error: (error) => {
        console.error('Error fetching comments:', error);
      },
    });
  }

  fetchUserComments(): Observable<Comment[]> {
    const token = StorageService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http
      .get<Comment[]>(`${this.baseUrl}/comments/user`, { headers })
      .pipe(
        catchError((error) => {
          console.error('Error fetching user comments:', error);
          return of([]);
        })
      );
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

  deleteComment(commentId: number, reasonId: number): Observable<void> {
    const token = StorageService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http
      .put<void>(
        `${this.baseUrl}/comments/${commentId}/${reasonId}`,
        {},
        { headers }
      )
      .pipe(
        map(() => {
          this.updateCommentsAfterDeletion(commentId);
        })
      );
  }

  private updateCommentsAfterDeletion(commentId: number) {
    const currentComments = this.comments[this.currentAttractionId!] || [];
    const updatedComments = currentComments.filter(
      (comment) => comment.commentId !== commentId
    );
    this.comments[this.currentAttractionId!] = updatedComments;
    this.commentsUpdated.next([...updatedComments]);

    if (this.currentAttractionId !== null) {
      this.fetchComments(this.currentAttractionId);
    }
  }

  clearComments(attractionId: number): void {
    this.comments[attractionId] = [];
    this.commentsUpdated.next([...this.comments[attractionId]]);
  }
}
