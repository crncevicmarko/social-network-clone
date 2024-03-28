import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { PostDTO } from '../model/postDTO';
import { ReportDTO } from '../model/reportDTO';
import { HttpErrorResponse } from '@angular/common/http';
import { ApiService } from '../service';
import { ReportService } from '../service/report.service';
import { PostsComponent } from '../posts/posts.component';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-group',
  templateUrl: './group.component.html',
  styleUrls: ['./group.component.css', '../shared-styles.css'],
})
export class GroupComponent {
  groupId!: string;
  // postComponent = new PostsComponent(this.postService, this.reportService);
  public posts: PostDTO[] = [];
  public addPost!: PostDTO;
  public updatePost!: PostDTO;
  public appPost!: PostDTO;
  public editPostId!: number;
  public content!: string;
  public addPosts!: string;
  public reportDTO: ReportDTO = {
    id: 0,
    timeStamp: '',
    reason: '',
    user_id: 0,
    post_id: 0,
    comment_id: 0,
  };

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
  showReportList = false;
  selectedReportOption!: string;

  public onRowClick(id: number, tekst: string) {
    this.editPostId = id;
    this.content = tekst;
  }

  constructor(
    private postService: ApiService,
    private reportService: ReportService,
    private route: ActivatedRoute
  ) {
    this.getAllPosts();
  }

  ngOnInit() {
    const groupId = this.route.snapshot.paramMap.get('id');
    if (groupId !== null) {
      this.groupId = groupId;
    }
  }
  public deletePost(id: number) {
    // treba isDeleted
    this.postService.deletePost(id).subscribe(
      (response: void) => {
        this.getAllPosts();
        alert('You deleted post');
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }
  public onAddPost(addPost: NgForm): void {
    this.postService.addPost(1, addPost.value).subscribe(
      (response: PostDTO) => {
        this.addPost = response;
        this.addPosts = '';
        alert('You added post');
        this.getAllPosts();
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  public getAllPosts() {
    this.postService.getPosts().subscribe(
      (response: PostDTO[]) => {
        for (var post of response) {
          if (this.groupId !== null) {
            if (post.group_id.toString() === this.groupId) {
              this.posts.push(post);
            }
          }
        }
        // this.posts = response;
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  public onUpdatePost(updateform: NgForm) {
    if (this.editPostId != null) {
      this.postService.update(this.editPostId, updateform.value).subscribe(
        (response: PostDTO) => {
          this.updatePost = response;
          this.content = '';
          this.getAllPosts();
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
      );
    } else {
      alert('Morate da izaberete post!!!');
    }
  }

  public likePost(post: PostDTO) {
    post.numberOfLikes += 1;
    this.postService.update(post.id, post).subscribe(
      (response: PostDTO) => {
        this.updatePost = response;
        this.getAllPosts();
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  public disLikePost(post: PostDTO) {
    post.numberOfLikes -= 1;
    this.postService.update(post.id, post).subscribe(
      (response: PostDTO) => {
        this.updatePost = response;
        this.getAllPosts();
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  toggleReportList() {
    this.showReportList = !this.showReportList;
  }

  public report(selectedOption: string, post: PostDTO) {
    this.reportDTO.post_id = post.id;
    this.reportDTO.reason = selectedOption;
    this.reportService.add(this.reportDTO).subscribe(
      (response: ReportDTO) => {
        alert('Uspesno ste prijavili post');
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }
}
