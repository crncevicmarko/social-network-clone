import { Component, ErrorHandler } from '@angular/core';
import { PostDTO } from '../model/postDTO';
import { NgForm } from '@angular/forms';
import { ApiService, AuthService } from '../service';
import { HttpErrorResponse } from '@angular/common/http';
import { UserDTO } from '../model/userDTO';
import { ReportService } from '../service/report.service';
import { ReportDTO } from '../model/reportDTO';
import { ReactionService } from '../service/reaction';
import { ReactionDTO } from '../model/reactionDTO';
import { CommentsService } from '../service/comment.service';
import { CommentDTO } from '../model/commentDTO';
import { Router } from '@angular/router';

@Component({
  selector: 'app-posts',
  templateUrl: './posts.component.html',
  styleUrls: ['./posts.component.css', '../shared-styles.css'],
})
export class PostsComponent {
  public posts!: PostDTO[];
  public addPost!: PostDTO;
  public updatePost!: PostDTO;
  public appPost!: PostDTO;
  public editPostId!: number;
  public content!: string;
  public addPosts!: string;
  public logedInUser!: UserDTO;
  public reportDTO: ReportDTO = {
    id: 0,
    timeStamp: '',
    reason: '',
    user_id: 0,
    post_id: 0,
    comment_id: 0,
  };
  public reactionDTO: ReactionDTO = {
    id: 0,
    timeStamp: '',
    reaction: '',
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
  reactioDTOList: ReactionDTO[] = [];
  showReportList = false;
  selectedReportOption!: string;

  public onRowClick(id: number, tekst: string) {
    this.editPostId = id;
    this.content = tekst;
  }

  constructor(
    private logOutService: AuthService,
    private postService: ApiService,
    private reportService: ReportService,
    private reactionService: ReactionService,
    private commentService: CommentsService,
    private router: Router
  ) {
    this.getAllPosts();
  }

  get isLoggedIn(): boolean {
    return this.logOutService.tokenIsPresent();
  }
  // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
  sortby1!: string;
  start_date!: string;
  end_date!: string;

  onSortComment1(form: { valid: any }) {
    if (form.valid) {
      if (this.sortby1 === 'latest_to_earliest') {
        console.log('Date:' + this.start_date);
        this.sortComments(1, this.start_date, this.end_date);
      } else if (this.sortby1 === 'earliest_to_latest') {
        console.log('Date:' + this.end_date);
        this.sortComments(0, this.start_date, this.end_date);
      }
    }
  }

  sortComments(order: number, start_date: string, end_date: string) {
    // this.commentService
    //   .sortComments(order, start_date, end_date)
    //   .subscribe((response: CommentDTO[]) => {});
    this.commentService.sortComments1(order, start_date, end_date);
  }

  // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
  sortby!: string;
  date!: string;
  likes!: number;

  onSortPost(form: { valid: any }) {
    if (form.valid) {
      if (this.sortby === 'number_of_likes') {
        console.log('Like:' + this.likes);
        this.getPostsByLikes(this.likes);
      } else if (this.sortby === 'date_of_creation') {
        console.log(this.date);
        this.getPostsByDate(this.date);
      }
    }
  }

  public getPostsByDate(date: string) {
    this.postService.getPostsByDate(date).subscribe(
      (response: PostDTO[]) => {
        this.posts = response;
        console.log(this.posts);
        for (var post of this.posts) {
          console.log(post);
        }
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  public getPostsByLikes(number_of_likes: number) {
    this.postService.getPostsByNumberOfLikes(number_of_likes).subscribe(
      (response: PostDTO[]) => {
        this.posts = response;
        for (var post of this.posts) {
          console.log(post);
        }
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
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
    // this.postService.addPost(3, addPost.value).subscribe(
    //   (response: PostDTO) => {
    //     this.addPost = response;
    //     this.addPosts = '';
    //     alert('You added post');
    //     this.getAllPosts();
    //   },
    //   (error: HttpErrorResponse) => {
    //     alert(error.message);
    //   }
    // );
  }

  public getAllPosts() {
    this.postService.getPosts().subscribe(
      (response: PostDTO[]) => {
        this.posts = response;
        for (var post of this.posts) {
          console.log(post);
        }
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
        this.router.navigate(['/login']);
      }
    );
  }

  highlights: string[] = [];
  extractHighlights(data: any): string[] {
    console.log('Podaci u extractHighlights funkciji: ', data[0].highlights);

    data.forEach((item: any) => {
      if (item.highlights) {
        if (
          item.highlights.commentsContent &&
          Array.isArray(item.highlights.commentsContent)
        ) {
          item.highlights.commentsContent.forEach((highlight: string) => {
            const strippedText = this.stripHtmlTags(highlight);
            this.highlights.push(strippedText);
          });
        }

        if (item.highlights.content && Array.isArray(item.highlights.content)) {
          item.highlights.content.forEach((highlight: string) => {
            const strippedText = this.stripHtmlTags(highlight);
            this.highlights.push(strippedText);
          });
        }
      }
    });

    return this.highlights;
  }

  // Function to strip HTML tags
  stripHtmlTags(str: any) {
    const tempDiv = document.createElement('div');
    tempDiv.innerHTML = str;
    return tempDiv.textContent || tempDiv.innerText || '';
  }

  // highlights: string[] = [];

  searchPosts(searchData: any) {
    this.posts = [];
    console.log('Form Data: ', searchData);
    this.postService.searchPosts(searchData).subscribe(
      (respones: any) => {
        console.log('Data: ', respones);
        this.highlights = this.extractHighlights(respones);
        console.log('Highlights: ', this.highlights);
        for (var val of respones) {
          this.postService.getPostById(val.postId).subscribe((r: PostDTO) => {
            console.log('Post: ', r);
            this.posts.push(r);
          });
        }
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
        console.log('Bla bla ');
        this.updatePost = response;
        this.getAllPosts();
        console.log('Pre likovanja: ');
        this.reallikePost(post);
        console.log('Posle likovanja: ');
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
        this.realDislikePost(post);
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  public reallikePost(post: PostDTO) {
    this.reactionDTO.reaction = 'LIKE';
    this.reactionService.save(0, post.id, this.reactionDTO).subscribe(
      (response: ReactionDTO) => {
        console.log('U: ');
        // this.updatePost = response;
        // this.reactionDTO = response;
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  public realDislikePost(post: PostDTO) {
    this.reactionDTO.reaction = 'DISLAKE';
    this.reactionService.save(0, post.id, this.reactionDTO).subscribe(
      (response: ReactionDTO) => {
        // this.updatePost = response;
        this.reactionDTO = response;
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }
  public getAllReactions(id: number) {
    this.reactionService.getByPostId(id).subscribe(
      (response: ReactionDTO[]) => {
        // this.updatePost = response;
        for (var reaction of response) {
          this.reactioDTOList.push(reaction);
        }
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
