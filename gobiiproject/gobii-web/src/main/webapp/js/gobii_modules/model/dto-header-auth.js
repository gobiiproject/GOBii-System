System.register([], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var DtoHeaderAuth;
    return {
        setters:[],
        execute: function() {
            DtoHeaderAuth = (function () {
                function DtoHeaderAuth(userName, password, token, gobiiCropType) {
                    this.userName = userName;
                    this.password = password;
                    this.token = token;
                    this.gobiiCropType = gobiiCropType;
                    this.userName = userName;
                    this.password = password;
                    this.token = token;
                    this.gobiiCropType = gobiiCropType;
                }
                DtoHeaderAuth.prototype.getToken = function () {
                    return this.token;
                };
                DtoHeaderAuth.prototype.getGobiiCropType = function () {
                    return this.gobiiCropType;
                };
                DtoHeaderAuth.fromJSON = function (json) {
                    return new DtoHeaderAuth(json['userName'], json['password'], json['token'], json['gobiiCropType']);
                };
                return DtoHeaderAuth;
            }());
            exports_1("DtoHeaderAuth", DtoHeaderAuth);
        }
    }
});
//# sourceMappingURL=dto-header-auth.js.map