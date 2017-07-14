System.register(["@angular/core", "@angular/http", "rxjs/Observable", "rxjs/add/operator/map", "../../model/dto-header-auth", "../../model/http-values"], function (exports_1, context_1) {
    "use strict";
    var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
        var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
        if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
        else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
        return c > 3 && r && Object.defineProperty(target, key, r), r;
    };
    var __metadata = (this && this.__metadata) || function (k, v) {
        if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
    };
    var __moduleName = context_1 && context_1.id;
    var core_1, http_1, Observable_1, dto_header_auth_1, http_values_1, AuthenticationService;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (http_1_1) {
                http_1 = http_1_1;
            },
            function (Observable_1_1) {
                Observable_1 = Observable_1_1;
            },
            function (_1) {
            },
            function (dto_header_auth_1_1) {
                dto_header_auth_1 = dto_header_auth_1_1;
            },
            function (http_values_1_1) {
                http_values_1 = http_values_1_1;
            }
        ],
        execute: function () {
            AuthenticationService = (function () {
                function AuthenticationService(_http) {
                    this._http = _http;
                    this.defaultUser = 'USER_READER';
                    this.defaultPassword = 'reader';
                    this.token = null;
                    this.userName = null;
                    this.authUrl = "gobii/v1/auth";
                }
                AuthenticationService.prototype.getToken = function () {
                    return this.token;
                }; // getToken()
                AuthenticationService.prototype.setToken = function (token) {
                    this.token = token;
                };
                AuthenticationService.prototype.getGobiiCropType = function () {
                    return this._gobiiCropType;
                };
                AuthenticationService.prototype.setGobiiCropType = function (gobiiCropType) {
                    this._gobiiCropType = gobiiCropType;
                };
                AuthenticationService.prototype.getUserName = function () {
                    return this.userName;
                };
                AuthenticationService.prototype.authenticate = function (userName, password) {
                    var _this = this;
                    var loginUser = userName ? userName : this.defaultUser;
                    var loginPassword = password ? password : this.defaultPassword;
                    var scope$ = this;
                    var requestBody = JSON.stringify("nothing");
                    var headers = http_values_1.HttpValues.makeLoginHeaders(loginUser, loginPassword);
                    return Observable_1.Observable.create(function (observer) {
                        _this
                            ._http
                            .post(scope$.authUrl, requestBody, { headers: headers })
                            .map(function (response) { return response.json(); })
                            .subscribe(function (json) {
                            var dtoHeaderAuth = dto_header_auth_1.DtoHeaderAuth
                                .fromJSON(json);
                            if (dtoHeaderAuth.getToken()) {
                                scope$.userName = userName;
                                scope$.setToken(dtoHeaderAuth.getToken());
                                scope$.setGobiiCropType(dtoHeaderAuth.getGobiiCropType());
                                observer.next(dtoHeaderAuth);
                                observer.complete();
                            }
                            else {
                                observer.error("No token was provided by server");
                            }
                        }, function (json) {
                            var message = json.status + ": " + json.statusText;
                            observer.error(message);
                        }); // subscribe
                    } // observer callback
                    ); // Observer.create() 
                }; // authenticate() 
                return AuthenticationService;
            }());
            AuthenticationService = __decorate([
                core_1.Injectable(),
                __metadata("design:paramtypes", [http_1.Http])
            ], AuthenticationService);
            exports_1("AuthenticationService", AuthenticationService);
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
        }
    };
});
//# sourceMappingURL=authentication.service.js.map