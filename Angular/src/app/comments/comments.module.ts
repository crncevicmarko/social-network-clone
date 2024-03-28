import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CommentsComponent } from './components/comments/comments/comments.component';
import { CommentsService } from '../service/comment.service';
import { CommentComponent } from './components/comment/comment/comment.component';
import { CommentFormComponent } from './components/commentForm/comment-form/comment-form.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

@NgModule({
  declarations: [CommentsComponent, CommentComponent, CommentFormComponent],
  exports: [CommentsComponent, CommentComponent, CommentFormComponent],
  imports: [CommonModule, ReactiveFormsModule, FormsModule],
  providers: [CommentsService],
})
export class CommentsModule {}
