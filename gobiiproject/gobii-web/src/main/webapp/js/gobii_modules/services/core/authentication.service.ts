import {Injectable} from "@angular/core";
import {Http, Response, Headers} from "@angular/http";
import {Observable} from "rxjs/Observable";
import "rxjs/add/operator/map";

import {DtoHeaderAuth} from "../../model/dto-header-auth";
import {HttpValues} from "../../model/http-values";
import {GobiiCropType} from "../../model/type-crop";

@Injectable()
export class AuthenticationService {


    constructor(private _http:Http) {
    }

    private defaultUser:string = 'USER_READER';
    private defaultPassword:string = 'reader';
    private token:string = '';
    private _gobiiCropType:GobiiCropType;

    public getToken():Observable<string> {

        let scope$ = this;

        return Observable.create(observer => {

            if (!scope$.token) {

                scope$.authenticateDefault()
                    .subscribe(dtoHeaderAuth => {
                            scope$.token = dtoHeaderAuth.getToken();
                            scope$._gobiiCropType = GobiiCropType[dtoHeaderAuth.getGobiiCropType()];
                            observer.next(scope$.token);
                            observer.complete();
                        },
                        error => observer.error(error));
            } else {
                observer.next(scope$.token);
                observer.complete();

            }// if we don't already have a token


        }); // Observable

    } // getToken()

    private setToken(token:string) {
        this.token = token;
    }


    getGobiiCropType():GobiiCropType {
        return this._gobiiCropType;
    }

    
    public authenticateDefault():Observable<DtoHeaderAuth> {
        return this.authenticate(null, null);
    }

    public authenticate(userName:string, password:string):Observable<DtoHeaderAuth> {

        let loginUser = userName ? userName : this.defaultUser;
        let loginPassword = password ? password : this.defaultPassword;

        let scope$ = this;
        let requestBody = JSON.stringify("nothing");
        let headers = HttpValues.makeLoginHeaders(loginUser, loginPassword);

        return Observable.create(observer => {
                this
                    ._http
                    .post("load/auth", requestBody, {headers: headers})
                    .map(response => response.json())
                    .subscribe(json => {
                        let dtoHeaderAuth:DtoHeaderAuth = DtoHeaderAuth
                            .fromJSON(json);
                        if (dtoHeaderAuth.getToken()) {
                            scope$.setToken(dtoHeaderAuth.getToken())
                            observer.next(dtoHeaderAuth);
                            observer.complete();
                        } else {
                            observer.error("No token was provided by server");
                        }

                    }) // subscribe
            } // observer callback
        ); // Observer.create() 


    } // authenticate() 

}
/*
 // doing a plain xhr request also does not allow access to token header
 var xhr = new XMLHttpRequest();
 var url = "load/auth";
 xhr.open("POST", url, true);
 xhr.setRequestHeader('Content-Type', 'application/json');
 xhr.setRequestHeader('Accept', 'application/json');
 xhr.setRequestHeader(HeaderNames.headerUserName, loginUser);
 xhr.setRequestHeader(HeaderNames.headerPassword, loginPassword);

 xhr.onreadystatechange = function() {//Call a function when the state changes.
 if(xhr.readyState == 4 && xhr.status == 200) {
 console.log(xhr.responseText);
 }
 }
 xhr.send(null);
 */