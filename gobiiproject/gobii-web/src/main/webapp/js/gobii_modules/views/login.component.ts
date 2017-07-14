import {Component, OnInit} from '@angular/core';
import {Router, ActivatedRoute} from '@angular/router';
import {AuthenticationService} from "../services/core/authentication.service";

@Component({
//    moduleId: module.id,
    template: `<div class="container">
    <div class="col-md-6 col-md-offset-3">
    <h2>GOBII Login</h2>
    <form name="form" (ngSubmit)="f.form.valid && login()" #f="ngForm" novalidate>
                <div class="form-group" [ngClass]="{ 'has-error': f.submitted && !username.valid }">
                    <label for="username">Username</label>
                    <input type="text" class="form-control" name="username" [(ngModel)]="model.username" #username="ngModel" required />
                    <div *ngIf="f.submitted && !username.valid" class="help-block">Username is required</div>
                </div>
                <div class="form-group" [ngClass]="{ 'has-error': f.submitted && !password.valid }">
                    <label for="password">Password</label>
                    <input type="password" class="form-control" name="password" [(ngModel)]="model.password" #password="ngModel" required />
                    <div *ngIf="f.submitted && !password.valid" class="help-block">Password is required</div>
                </div>
                <div class="form-group">
                    <button [disabled]="loading" class="btn btn-primary">Login</button>
                    <img *ngIf="loading" src="data:image/gif;base64,R0lGODlhEAAQAPIAAP///wAAAMLCwkJCQgAAAGJiYoKCgpKSkiH/C05FVFNDQVBFMi4wAwEAAAAh/hpDcmVhdGVkIHdpdGggYWpheGxvYWQuaW5mbwAh+QQJCgAAACwAAAAAEAAQAAADMwi63P4wyklrE2MIOggZnAdOmGYJRbExwroUmcG2LmDEwnHQLVsYOd2mBzkYDAdKa+dIAAAh+QQJCgAAACwAAAAAEAAQAAADNAi63P5OjCEgG4QMu7DmikRxQlFUYDEZIGBMRVsaqHwctXXf7WEYB4Ag1xjihkMZsiUkKhIAIfkECQoAAAAsAAAAABAAEAAAAzYIujIjK8pByJDMlFYvBoVjHA70GU7xSUJhmKtwHPAKzLO9HMaoKwJZ7Rf8AYPDDzKpZBqfvwQAIfkECQoAAAAsAAAAABAAEAAAAzMIumIlK8oyhpHsnFZfhYumCYUhDAQxRIdhHBGqRoKw0R8DYlJd8z0fMDgsGo/IpHI5TAAAIfkECQoAAAAsAAAAABAAEAAAAzIIunInK0rnZBTwGPNMgQwmdsNgXGJUlIWEuR5oWUIpz8pAEAMe6TwfwyYsGo/IpFKSAAAh+QQJCgAAACwAAAAAEAAQAAADMwi6IMKQORfjdOe82p4wGccc4CEuQradylesojEMBgsUc2G7sDX3lQGBMLAJibufbSlKAAAh+QQJCgAAACwAAAAAEAAQAAADMgi63P7wCRHZnFVdmgHu2nFwlWCI3WGc3TSWhUFGxTAUkGCbtgENBMJAEJsxgMLWzpEAACH5BAkKAAAALAAAAAAQABAAAAMyCLrc/jDKSatlQtScKdceCAjDII7HcQ4EMTCpyrCuUBjCYRgHVtqlAiB1YhiCnlsRkAAAOwAAAAAAAAAAAA==" />
                </div>
            </form>
            <span>{{message}}</span>
            </div>
        </div>`
})


// this component and the entire login mechanism (AuthGuard, etc.) are borrowed form
// http://jasonwatmore.com/post/2016/09/29/angular-2-user-registration-and-login-example-tutorial
export class LoginComponent implements OnInit {
    model: any = {};
    loading = false;
    returnUrl: string;
    message: string;

    constructor(private route: ActivatedRoute,
                private router: Router,
                private authenticationService: AuthenticationService) {
    }

    ngOnInit() {
        // reset login status
        //this.authenticationService.logout();

        // get return url from route parameters or default to '/'
        this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '';
    }

    login() {
        this.loading = true;
//        this.router.navigate(['project']);
        this.loading = false;
        this.authenticationService.authenticate(this.model.username, this.model.password)
            .subscribe(
                dtoHeaderAuth => {

                    if(dtoHeaderAuth.getToken() != null ) {
//                        this.router.navigate([this.returnUrl]);
                        this.router.navigate(['']);
                    }
                },
                error => {
                   this.message = error;
                    this.loading = false;
                });

    }
}
