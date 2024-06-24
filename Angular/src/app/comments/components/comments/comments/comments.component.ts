import { Component, Input, OnInit } from '@angular/core';
import { CommentDTO } from 'src/app/model/commentDTO';
import { ActiveCommentType } from 'src/app/model/activeCommentDTO';
import { CommentsService } from 'src/app/service/comment.service';
import { ApiService, AuthService } from 'src/app/service';
import { UserDTO } from 'src/app/model/userDTO';
import { HttpErrorResponse } from '@angular/common/http';
import { Subscription, forkJoin, map } from 'rxjs';

@Component({
  selector: 'app-comments',
  templateUrl: './comments.component.html',
  styleUrls: ['./comments.component.css'],
})
export class CommentsComponent implements OnInit {
  private subscription!: Subscription;
  @Input() curentUserId!: string;
  @Input() postId!: string;

  public logedInUser!: UserDTO;
  comments: CommentDTO[] = [];

  activeComment: ActiveCommentType | null = null;

  constructor(
    private commentsService: CommentsService,
    private userService: AuthService,
    private commentService: CommentsService
  ) {
    this.subscription = this.commentService.sortComments$.subscribe(
      ({ order, start_date, end_date }) => {
        // sort comments based on the received values
        this.sortComments(order, start_date, end_date);
      }
    );
  }

  sortComments(order: number, start_date: string, end_date: string) {
    this.commentService
      .sortComments(order, start_date, end_date)
      .subscribe((response: CommentDTO[]) => {
        this.comments = response.filter(
          (comment) => comment.post_id.toString() === this.postId,
          console.log(this.postId)
        );
      });
  }
  ngOnInit(): void {
    // this.commentsService.getAllComments().subscribe(
    //   (response: CommentDTO[]) => {
    //     this.comments = response;
    //     alert('Komentari' + this.comments);

    //     for (var val of this.comments) {
    //       console.log(val.user_id);
    //       this.userService.getUsetById(val.user_id).subscribe(
    //         (response: UserDTO) => {
    //           val.userName = response.username;
    //           console.log(val.user_id);
    //         },
    //         (error: HttpErrorResponse) => {
    //           alert('Ne postoji korisnik' + error);
    //         }
    //       );
    //     }
    //   },
    //   (error: HttpErrorResponse) => {
    //     alert('Komentari ne postoje' + ' ' + error.message);
    //   }
    // );
    this.logedInUser = this.userService.logedUser;
    this.commentsService
      .getAllComments()
      .subscribe((response: CommentDTO[]) => {
        this.comments = response.filter(
          (comment) => comment.post_id.toString() === this.postId,
          console.log(this.postId)
        );
        const userRequests = this.comments.map((comment) =>
          this.userService.getUsetById(comment.user_id).pipe(
            map((user: UserDTO) => {
              comment.userName = user.username;
              console.log('Map1: ' + comment.text);
              return comment;
            })
          )
        );
        forkJoin(userRequests).subscribe((comments) => {
          console.log(comments);
        });
      });
  }

  updateComment({ text, commentId }: { text: string; commentId: number }) {
    this.commentsService
      .updateComment(text, commentId)
      .subscribe((response: CommentDTO) => {
        this.comments = this.comments.map((comment) => {
          if (comment.id === commentId) {
            return response;
          }
          return comment;
        });
        this.activeComment = null;
      });
  }

  deleteComment(commentId: number): void {
    this.commentsService.deleteComment(commentId).subscribe(() => {
      this.comments = this.comments.filter(
        (comment) => comment.id !== commentId
      );
    });
  }

  addComment({
    text,
    postId,
    parentId,
  }: {
    text: string;
    postId: string;
    parentId: null | string;
  }): void {
    console.log('addComment', text, postId, parentId);
    this.commentsService.addComment(text, postId, parentId).subscribe(
      (createComment: CommentDTO) => {
        this.comments = [...this.comments, createComment];
        this.activeComment = null;
      },
      (error: HttpErrorResponse) => {
        alert('Add komentar: ' + error.message);
      }
    );
  }

  getReplies(commentId: number): CommentDTO[] {
    return this.comments
      .filter((comment) => comment.parentId === commentId.toString())
      .sort(
        (a, b) =>
          new Date(a.timeStamp).getMilliseconds() -
          new Date(b.timeStamp).getMilliseconds()
      );
  }

  setActiveComment(activeComment: ActiveCommentType | null): void {
    this.activeComment = activeComment;
  }

  getTopLevelComments(): CommentDTO[] {
    return this.comments.filter((comment) => comment.parentId === 'null');
  }

  get isLoggedIn(): boolean {
    return this.userService.tokenIsPresent();
  }
}
