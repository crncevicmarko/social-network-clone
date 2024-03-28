import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PostsComponent } from './posts/posts.component';
import { GroupService } from './service/group.service';
import { GroupsComponent } from './groups/groups.component';
import { LoginComponent } from './login/login.component';
import { SignUpComponent } from './sign-up/sign-up.component';
import { GroupComponent } from './group/group.component';
import { UserComponent } from './user/user.component';
import { PostComponent } from './post/post.component';
import { UsersComponent } from './users/users.component';
import { FriendRequestsComponent } from './friend-requests/friend-requests.component';
import { GroupRequestsComponent } from './group-requests/group-requests.component';

const routes: Routes = [
  { path: 'posts', component: PostsComponent },
  { path: 'groups', component: GroupsComponent },
  { path: 'login', component: LoginComponent },
  { path: 'signin', component: SignUpComponent },
  { path: 'group/:id', component: GroupComponent },
  { path: 'user', component: UserComponent },
  { path: 'posts/:id', component: PostComponent },
  { path: 'users', component: UsersComponent },
  { path: 'friendRequests', component: FriendRequestsComponent },
  { path: 'groupRequest', component: GroupRequestsComponent },
];
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
