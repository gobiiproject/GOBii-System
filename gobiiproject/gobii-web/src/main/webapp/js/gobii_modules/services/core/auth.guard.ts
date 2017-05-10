import {Injectable} from '@angular/core';
import {Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot} from '@angular/router';
import {AuthenticationService} from "./authentication.service";

@Injectable()
export class AuthGuard implements CanActivate {

    constructor(private router: Router,
                private authenticationService: AuthenticationService) {
    }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {

        let returnVal: boolean = false;


        returnVal = (this.authenticationService.getToken() != null);

        // not logged in so redirect to login page with the return url
        if (!returnVal) {
//            this.router.navigate(['/login'], {queryParams: {returnUrl: state.url}});
            this.router.navigate(['/login']);
        }

        return returnVal;
    }
}