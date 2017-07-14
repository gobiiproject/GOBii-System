import {Injectable} from "@angular/core";
import {HttpValues} from "../../model/http-values";
import {Http} from "@angular/http";
import {AuthenticationService} from "./authentication.service";
import {DtoRequestItem} from "./dto-request-item";
import {DtoHeaderResponse} from "../../model/dto-header-response";
import {PayloadEnvelope} from "../../model/payload/payload-envelope";
import {Observable} from "rxjs/Observable";
import "rxjs/add/operator/map";
import {Header} from "../../model/payload/header";
import {Status} from "../../model/payload/status";
import {HeaderStatusMessage} from "../../model/dto-header-status-message";

@Injectable()
export class DtoRequestService<T> {


    constructor(private _http: Http,
                private _authenticationService: AuthenticationService) {
    }


    public getAString(): string {
        return 'a string';
    }

    getGobiiCropType(): string {
        return this._authenticationService.getGobiiCropType();
    }

    private _gobbiiVersion;

    getGobbiiVersion() {
        return this._gobbiiVersion;
    }

    public post(dtoRequestItem: DtoRequestItem<T>): Observable < T > {

        let scope$ = this;

        return Observable.create(observer => {


            let token: string = this._authenticationService
                .getToken();

            if (token) {

                let headers = HttpValues.makeTokenHeaders(token, scope$._authenticationService.getGobiiCropType());

                this._http
                    .post(dtoRequestItem.getUrl(),
                        dtoRequestItem.getRequestBody(),
                        {headers: headers})
                    .map(response => response.json())
                    .subscribe(json => {

                            let payloadResponse: PayloadEnvelope = PayloadEnvelope.fromJSON(json);

                            if (payloadResponse.header.status.succeeded) {
                                let result = dtoRequestItem.resultFromJson(json);
                                observer.next(result);
                                observer.complete();
                            } else {
                                observer.error(payloadResponse.header);
                            }

                        },
                        json => {
                            let obj = JSON.parse(json._body)
                            let payloadResponse: PayloadEnvelope = PayloadEnvelope.fromJSON(obj);
                            observer.error(payloadResponse.header);
                        }); // subscribe http

            } else {
                let header: Header = new Header(null, null, new Status(false, [new HeaderStatusMessage("Unauthenticated", null, null)]), null);
                observer.error(header);
            }

        }); // observable
    }


    public get(dtoRequestItem: DtoRequestItem<T>): Observable < T > {

        let scope$ = this;
        return Observable.create(observer => {

            let token: string = this._authenticationService
                .getToken();

            if (token) {

                let headers = HttpValues.makeTokenHeaders(token, scope$._authenticationService.getGobiiCropType());

                this._http
                    .get(dtoRequestItem.getUrl(),
                        {headers: headers})
                    .map(response => response.json())
                    .subscribe(json => {

                            let payloadResponse: PayloadEnvelope = PayloadEnvelope.fromJSON(json);

                            if (payloadResponse.header.status.succeeded) {
                                scope$._gobbiiVersion = payloadResponse.header.gobiiVersion;
                                let result = dtoRequestItem.resultFromJson(json);
                                observer.next(result);
                                observer.complete();
                            } else {
                                observer.error(payloadResponse);
                            }

                        },
                        json => {
                            let obj = JSON.parse(json._body)
                            let payloadResponse: PayloadEnvelope = PayloadEnvelope.fromJSON(obj);
                            observer.error(payloadResponse.header);
                        }); // subscribe http

            } else {
                let header: Header = new Header(null, null, new Status(false, [new HeaderStatusMessage("Unauthenticated", null, null)]), null);
                observer.error(header);
            }

        }); // observable

    }


}
