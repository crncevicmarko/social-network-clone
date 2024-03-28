import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { AngularMaterialModule } from './angular-material/angular-material.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { TokenInterceptor } from './interceptor/TokenInterceptor';
import { PostsComponent } from './posts/posts.component';
import { GroupsComponent } from './groups/groups.component';
import { LoginComponent } from './login/login.component';
import { SignUpComponent } from './sign-up/sign-up.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { CommentsModule } from './comments/comments.module';
import { GroupComponent } from './group/group.component';
import { UserComponent } from './user/user.component';
import { ReportsComponent } from './reports/reports.component';
import { PostComponent } from './post/post.component';
import { ChpsformComponent } from './chpsform/chpsform.component';
import { UsersComponent } from './users/users.component';
import { FriendRequestsComponent } from './friend-requests/friend-requests.component';
import { GroupRequestsComponent } from './group-requests/group-requests.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    PostsComponent,
    GroupsComponent,
    LoginComponent,
    SignUpComponent,
    GroupComponent,
    UserComponent,
    ReportsComponent,
    PostComponent,
    ChpsformComponent,
    UsersComponent,
    FriendRequestsComponent,
    GroupRequestsComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    CommentsModule,
    AngularMaterialModule,
    FormsModule,
    ReactiveFormsModule,
    NgbModule,
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true,
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
