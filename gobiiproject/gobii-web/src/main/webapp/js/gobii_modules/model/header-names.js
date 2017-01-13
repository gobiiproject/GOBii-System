System.register([], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var HeaderNames;
    return {
        setters:[],
        execute: function() {
            HeaderNames = (function () {
                function HeaderNames() {
                }
                HeaderNames.headerToken = "X-Auth-Token";
                HeaderNames.headerUserName = "X-Username";
                HeaderNames.headerPassword = "X-Password";
                HeaderNames.headerGobiiCrop = "Gobii-Crop-Type";
                return HeaderNames;
            }());
            exports_1("HeaderNames", HeaderNames);
        }
    }
});
//# sourceMappingURL=header-names.js.map