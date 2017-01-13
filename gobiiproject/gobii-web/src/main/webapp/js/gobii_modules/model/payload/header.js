System.register(["../dto-header-auth", "./status"], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var dto_header_auth_1, status_1;
    var Header;
    return {
        setters:[
            function (dto_header_auth_1_1) {
                dto_header_auth_1 = dto_header_auth_1_1;
            },
            function (status_1_1) {
                status_1 = status_1_1;
            }],
        execute: function() {
            Header = (function () {
                function Header(cropType, dtoHeaderAuth, status) {
                    this.cropType = cropType;
                    this.dtoHeaderAuth = dtoHeaderAuth;
                    this.status = status;
                }
                Header.fromJSON = function (json) {
                    var cropType = json.cropType;
                    var dtoHeaderAuth = dto_header_auth_1.DtoHeaderAuth.fromJSON(json.dtoHeaderAuth);
                    var status = status_1.Status.fromJSON(json.status);
                    return new Header(cropType, dtoHeaderAuth, status); // new
                }; // fromJson()
                return Header;
            }());
            exports_1("Header", Header);
        }
    }
});
//# sourceMappingURL=header.js.map