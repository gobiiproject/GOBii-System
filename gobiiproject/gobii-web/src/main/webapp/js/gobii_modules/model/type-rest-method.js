System.register([], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var RestMethod;
    return {
        setters:[],
        execute: function() {
            (function (RestMethod) {
                RestMethod[RestMethod["POST"] = 0] = "POST";
                RestMethod[RestMethod["GET"] = 1] = "GET";
                RestMethod[RestMethod["PUT"] = 2] = "PUT";
                RestMethod[RestMethod["DELETE"] = 3] = "DELETE";
            })(RestMethod || (RestMethod = {}));
            exports_1("RestMethod", RestMethod);
        }
    }
});
//# sourceMappingURL=type-rest-method.js.map