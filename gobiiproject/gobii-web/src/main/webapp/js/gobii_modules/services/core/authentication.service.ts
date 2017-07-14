import {Injectable} from "@angular/core";
import {Http, Response, Headers} from "@angular/http";
import {Observable} from "rxjs/Observable";
import "rxjs/add/operator/map";

import {DtoHeaderAuth} from "../../model/dto-header-auth";
import {HttpValues} from "../../model/http-values";
import {PayloadEnvelope} from "../../model/payload/payload-envelope";

@Injectable()
export class AuthenticationService {


    constructor(private _http:Http) {
    }

    private defaultUser:string = 'USER_READER';
    private defaultPassword:string = 'reader';
    private token:string = null;
    private userName:string =  null;
    private _gobiiCropType:string;
    private authUrl:string = "gobii/v1/auth";


    public getToken():string {

        return this.token;

    } // getToken()

    private setToken(token:string) {
        this.token = token;
    }


    public getGobiiCropType():string {
        return this._gobiiCropType;
    }

    private setGobiiCropType(gobiiCropType:string){
        this._gobiiCropType =  gobiiCropType;
    }

    public getUserName(): string {
        return this.userName;
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
                    .post(scope$.authUrl, requestBody, {headers: headers})
                    .map(response => response.json())
                    .subscribe(json => {
                        let dtoHeaderAuth:DtoHeaderAuth = DtoHeaderAuth
                            .fromJSON(json);
                        if (dtoHeaderAuth.getToken()) {
                            scope$.userName = userName;
                            scope$.setToken(dtoHeaderAuth.getToken())
                            scope$.setGobiiCropType( dtoHeaderAuth.getGobiiCropType() );
                            observer.next(dtoHeaderAuth);
                            observer.complete();
                        } else {
                            observer.error("No token was provided by server");
                        }
                    },
                        json => {
                            let message:string = json.status + ": " + json.statusText;
                            observer.error(message);
                        }); // subscribe
            } // observer callback
        ); // Observer.create() 


    } // authenticate() 

}
/*
 // doing a plain xhr request also does not allow access to token response
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