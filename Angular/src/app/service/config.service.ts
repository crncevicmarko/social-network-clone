import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class ConfigService {
  private _api_url = 'http://localhost:8080';

  get api_url(): string {
    return this._api_url;
  }
  private _user_url = this._api_url + '/users';

  get user_url(): string {
    return this._user_url;
  }

  private _update_user = this._user_url + '/user';

  get update_user(): string {
    return this._update_user;
  }

  private _user_name_url = this._user_url + '/user';

  get user_name_url(): string {
    return this._user_name_url;
  }

  private _login_url = this._user_url + '/login';

  get login_url(): string {
    return this._login_url;
  }

  private _whoami_url = this._user_url + '/whoami';

  get whoami_url(): string {
    return this._whoami_url;
  }

  private _users_url = this._user_url + '/all';

  get users_url(): string {
    return this._users_url;
  }

  //TODO: implementirati :)
  private _signup_url = this._user_url + '/signup';

  get signup_url(): string {
    return this._signup_url;
  }

  private _block_user_url = this._user_url + '/block';

  get block_user_url(): string {
    return this._block_user_url;
  }

  private _unBlock_user_url = this._user_url + '/unblock';

  get unBlock_user_url(): string {
    return this._unBlock_user_url;
  }

  private _search_user_url = this._user_url + '/search';

  get search(): string {
    return this._search_user_url;
  }

  private _friend_request_url = this._api_url + '/friendRequests';

  get send_friend_request(): string {
    return this._friend_request_url;
  }

  private _find_all_friends_url = this._friend_request_url + '/all';

  get find_friend_requests(): string {
    return this._find_all_friends_url;
  }

  private _posts_url = this._api_url + '/posts';

  get posts_url(): string {
    return this._posts_url;
  }

  private _find_posts_by_date = this._posts_url + '/date';

  get find_posts_by_date(): string {
    return this._find_posts_by_date;
  }

  private _find_posts_by_number_of_likes = this._posts_url + '/numberOfLikes';

  get find_posts_by_number_of_likes(): string {
    return this._find_posts_by_number_of_likes;
  }

  private _groups_url = this._api_url + '/groups';

  get groups_ulr(): string {
    return this._groups_url;
  }

  private _get_groups_By_user_id_url = this._groups_url + '/getAllByUserId';

  get get_groups_by_user_id(): string {
    return this._get_groups_By_user_id_url;
  }

  private _delete_group = this._groups_url + '/delete';

  get delete_group(): string {
    return this._delete_group;
  }

  private _updatePost_url = this._posts_url + '/update'; //PUT

  get updatePost_url(): string {
    return this._updatePost_url;
  }

  private _updateGroup_url = this._groups_url + '/update'; //POST

  get updateGroup_url(): string {
    return this._updateGroup_url;
  }

  private _deletePost = this._posts_url + '/delete';

  get delete_post(): string {
    return this._deletePost;
  }

  private _getComments = this.api_url + '/comments';

  get get_comments(): string {
    return this._getComments;
  }

  private _addComment = this._getComments;

  get addComment(): string {
    return this._addComment;
  }

  private _updateComment = this._getComments + '/update';

  get updateComment(): string {
    return this._updateComment;
  }
  private _deleteComment = this._getComments + '/delete';

  get deleteComment(): string {
    return this._deleteComment;
  }

  private _sort_comments = this._getComments + '/sort';

  get sort_comments(): string {
    return this._sort_comments;
  }

  private _report_url = this._api_url + '/reports';

  get report_url(): string {
    return this._report_url;
  }

  private _group_request = this._api_url + '/groupRequests';

  private _join_group = this._group_request + '/save';

  get group_request(): string {
    return this._join_group;
  }

  get get_group_requests(): string {
    return this._group_request;
  }

  private _update_group_request = this._group_request + '/update';

  get update_group_request(): string {
    return this._update_group_request;
  }
}
