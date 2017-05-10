import { Routes, RouterModule } from '@angular/router';


//import { RegisterComponent } from './register/index';

import {ExtractorRoot} from "./app.extractorroot";
import {LoginComponent} from "../views/login.component";
import {AuthGuard} from "../services/core/auth.guard";
import {ProjectListBoxComponent} from "../views/project-list-box.component";

const appRoutes: Routes = [
    { path: '', component: ExtractorRoot, canActivate: [AuthGuard] },
    { path: 'login', component: LoginComponent },
//    { path: 'project', component: ExtractorRoot},
//    { path: 'register', component: RegisterComponent },

    // otherwise redirect to home
    { path: '**', redirectTo: '' }
];

export const routing = RouterModule.forRoot(appRoutes);