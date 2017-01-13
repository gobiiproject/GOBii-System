System.register(["@angular/core", "../../model/http-values", "@angular/http", "./authentication.service", "../../model/dto-header-response", "rxjs/Observable", "rxjs/add/operator/map"], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
        var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
        if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
        else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
        return c > 3 && r && Object.defineProperty(target, key, r), r;
    };
    var __metadata = (this && this.__metadata) || function (k, v) {
        if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
    };
    var core_1, http_values_1, http_1, authentication_service_1, dto_header_response_1, Observable_1;
    var DtoRequestService;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (http_values_1_1) {
                http_values_1 = http_values_1_1;
            },
            function (http_1_1) {
                http_1 = http_1_1;
            },
            function (authentication_service_1_1) {
                authentication_service_1 = authentication_service_1_1;
            },
            function (dto_header_response_1_1) {
                dto_header_response_1 = dto_header_response_1_1;
            },
            function (Observable_1_1) {
                Observable_1 = Observable_1_1;
            },
            function (_1) {}],
        execute: function() {
            DtoRequestService = (function () {
                function DtoRequestService(_http, _authenticationService) {
                    this._http = _http;
                    this._authenticationService = _authenticationService;
                }
                DtoRequestService.prototype.getAString = function () {
                    return 'a string';
                };
                DtoRequestService.prototype.getGobiiCropType = function () {
                    return this._authenticationService.getGobiiCropType();
                };
                DtoRequestService.prototype.getResult = function (dtoRequestItem) {
                    var _this = this;
                    return Observable_1.Observable.create(function (observer) {
                        _this._authenticationService
                            .getToken()
                            .subscribe(function (token) {
                            var headers = http_values_1.HttpValues.makeTokenHeaders(token);
                            _this._http
                                .post(dtoRequestItem.getUrl(), dtoRequestItem.getRequestBody(), { headers: headers })
                                .map(function (response) { return response.json(); })
                                .subscribe(function (json) {
                                var headerResponse = dto_header_response_1.DtoHeaderResponse.fromJSON(json);
                                if (headerResponse.succeeded) {
                                    var result = dtoRequestItem.resultFromJson(json);
                                    observer.next(result);
                                    observer.complete();
                                }
                                else {
                                    observer.error(headerResponse);
                                }
                            }); // subscribe http
                        }); // subscribe get authentication token
                    }); // observable
                }; // getPiNameIds()
                DtoRequestService = __decorate([
                    core_1.Injectable(), 
                    __metadata('design:paramtypes', [http_1.Http, authentication_service_1.AuthenticationService])
                ], DtoRequestService);
                return DtoRequestService;
            }());
            exports_1("DtoRequestService", DtoRequestService);
        }
    }
});
//# sourceMappingURL=dto-request.service.js.map