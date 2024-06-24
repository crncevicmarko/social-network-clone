import { HttpErrorResponse } from '@angular/common/http';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ActiveCommentType } from 'src/app/model/activeCommentDTO';
import { CommentDTO } from 'src/app/model/commentDTO';
import { PostDTO } from 'src/app/model/postDTO';
import { ReportDTO } from 'src/app/model/reportDTO';
import { UserDTO } from 'src/app/model/userDTO';
import { AuthService } from 'src/app/service';
import { CommentsService } from 'src/app/service/comment.service';
import { ReportService } from 'src/app/service/report.service';

@Component({
  selector: 'app-comment',
  templateUrl: './comment.component.html',
  styleUrls: ['./comment.component.css'],
})
export class CommentComponent implements OnInit {
  public logedInUser!: UserDTO;
  constructor(
    private userService: AuthService,
    private reportService: ReportService,
    private commentService: CommentsService
  ) {}
  @Input() curentUserId!: string;
  @Input() replies!: CommentDTO[];
  @Input() comment!: CommentDTO;
  @Input() order!: number;
  @Input() start_date!: string;
  @Input() end_date!: string;
  @Input() activeComment!: ActiveCommentType | null;
  @Output() setActiveComment = new EventEmitter<ActiveCommentType | null>();
  @Input() parentId: string | null = null;
  @Input() postId!: string;

  @Output() addComment = new EventEmitter<{
    text: string;
    postId: string;
    parentId: string | null;
  }>();

  @Output() updateComment = new EventEmitter<{
    text: string;
    commentId: number;
  }>();

  @Output() deleteComment = new EventEmitter<number>();

  reportOptions = [
    'BREAKES_RULES',
    'HARASSMENT',
    'HATE',
    'SHARING_PERSONAL_INFORMATION',
    'IMPERSONATION',
    'COPYING_VIOLATION',
    'TRADEMARK_VIOLATION',
    'SPAM',
    'SELF_HARM_OR_SUICIDE',
    'OTHER',
  ];

  public reportDTO: ReportDTO = {
    id: 0,
    timeStamp: '',
    reason: '',
    user_id: 0,
    post_id: 0,
    comment_id: 0,
  };

  showReportList = false;
  selectedReportOption!: string;

  canReply: boolean = false;
  canEdit: boolean = false;
  canDelete: boolean = false;
  replyId: string | null = null;
  ngOnInit(): void {
    this.logedInUser = this.userService.logedUser;
    this.canReply = Boolean(this.curentUserId);
    this.canEdit = this.curentUserId === this.comment.user_id.toString();
    this.canDelete =
      this.curentUserId === this.comment.user_id.toString() &&
      this.replies.length === 0;
    this.replyId = this.parentId ? this.parentId : this.comment.id.toString();
    for (var reply of this.replies) {
      console.log(
        'Replies:' + reply.text + ' replies parentId:   ' + reply.parentId
      );
    }
  }

  isReplying(): boolean {
    if (!this.activeComment) {
      return false;
    }
    return (
      this.activeComment.id === this.comment.id &&
      this.activeComment.type === 'replying'
    );
  }

  isEditing(): boolean {
    if (!this.activeComment) {
      return false;
    }
    return (
      this.activeComment.id === this.comment.id &&
      this.activeComment.type === 'editing'
    );
  }

  toggleReportList() {
    this.showReportList = !this.showReportList;
  }

  public report(selectedOption: string, comment: CommentDTO) {
    this.reportDTO.comment_id = comment.id;
    this.reportDTO.post_id = comment.post_id;
    this.reportDTO.reason = selectedOption;
    this.reportService.add(this.reportDTO).subscribe(
      (response: ReportDTO) => {
        alert('Uspesno ste prijavili comment');
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  get isLoggedIn(): boolean {
    return this.userService.tokenIsPresent();
  }
}
