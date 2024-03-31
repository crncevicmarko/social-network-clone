import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivate,
  Router,
  RouterStateSnapshot,
  UrlTree,
} from '@angular/router';
import { Observable } from 'rxjs';
import { UserRole } from '../model/user-role';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root',
})
export class RoleGuard implements CanActivate {
  constructor(private authService: AuthService, public router: Router) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ):
    | Observable<boolean | UrlTree>
    | Promise<boolean | UrlTree>
    | boolean
    | UrlTree {
    const expectedRoles = route.data['expectedRoles'] as UserRole[];
    const userRoles = this.authService.getUserRoles();

    // Check if any of the user's roles match the expected roles
    const hasRequiredRole = expectedRoles.some((role) =>
      userRoles.includes(role)
    );

    if (!hasRequiredRole) {
      // Redirect to login page
      return this.router.parseUrl('/posts');
    }

    return true;
  }
}
