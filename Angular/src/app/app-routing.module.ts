import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PostsComponent } from './posts/posts.component';
import { GroupService } from './service/group.service';
import { RoleGuard } from './service/role-guard.service';
import { GroupsComponent } from './groups/groups.component';
import { LoginComponent } from './login/login.component';
import { SignUpComponent } from './sign-up/sign-up.component';
import { GroupComponent } from './group/group.component';
import { UserComponent } from './user/user.component';
import { PostComponent } from './post/post.component';
import { UsersComponent } from './users/users.component';
import { FriendRequestsComponent } from './friend-requests/friend-requests.component';
import { GroupRequestsComponent } from './group-requests/group-requests.component';
import { UserRole } from './model/user-role';

const routes: Routes = [
  { path: 'posts', component: PostsComponent },
  { path: 'groups', component: GroupsComponent },
  { path: 'login', component: LoginComponent },
  { path: 'signin', component: SignUpComponent },
  { path: 'group/:id', component: GroupComponent },
  {
    path: 'profile',
    component: UserComponent,
    canActivate: [RoleGuard],
    data: { expectedRoles: [UserRole.Admin, UserRole.User] },
  },
  { path: 'posts/:id', component: PostComponent },
  {
    path: 'users',
    component: UsersComponent,
    canActivate: [RoleGuard],
    data: { expectedRoles: [UserRole.Admin, UserRole.User] },
  },
  {
    path: 'friendRequests',
    component: FriendRequestsComponent,
    canActivate: [RoleGuard],
    data: { expectedRoles: [UserRole.User] },
  },
  {
    path: 'groupRequest',
    component: GroupRequestsComponent,
    canActivate: [RoleGuard],
    data: { expectedRoles: [UserRole.User] },
  },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
];
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
