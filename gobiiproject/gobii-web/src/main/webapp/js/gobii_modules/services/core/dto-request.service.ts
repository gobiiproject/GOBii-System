import {Injectable} from "@angular/core";
import {HttpValues} from "../../model/http-values";
import {GobiiCropType} from "../../model/type-crop"
import {Http} from "@angular/http";
import {AuthenticationService} from "./authentication.service";
import {DtoRequestItem} from "./dto-request-item";
import {DtoHeaderResponse} from "../../model/dto-header-response";
import {Observable} from "rxjs/Observable";
import "rxjs/add/operator/map";

@Injectable()
export class DtoRequestService<T> {


    constructor(private _http:Http,
                private _authenticationService:AuthenticationService) {
    }


    public getAString():string {
        return 'a string';
    }

    getGobiiCropType():GobiiCropType {
        return this._authenticationService.getGobiiCropType();
    }
    
    
    public getResult(dtoRequestItem:DtoRequestItem<T>):Observable < T > {

        return Observable.create(observer => {

            this._authenticationService
                .getToken()
                .subscribe(token => {

                    let headers = HttpValues.makeTokenHeaders(token);

                    this._http
                        .post(dtoRequestItem.getUrl(),
                            dtoRequestItem.getRequestBody(),
                            {headers: headers})
                        .map(response => response.json())
                        .subscribe(json => {

                            let headerResponse:DtoHeaderResponse = DtoHeaderResponse.fromJSON(json);

                            if (headerResponse.succeeded) {
                                let result = dtoRequestItem.resultFromJson(json);
                                observer.next(result);
                                observer.complete();
                            } else {
                                observer.error(headerResponse);
                            }

                        }) // subscribe http

                }); // subscribe get authentication token

        }); // observable

    } // getPiNameIds()

}
