System.register([], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var ProcessType;
    return {
        setters:[],
        execute: function() {
            (function (ProcessType) {
                ProcessType[ProcessType["CREATE"] = 0] = "CREATE";
                ProcessType[ProcessType["READ"] = 1] = "READ";
                ProcessType[ProcessType["UPDATE"] = 2] = "UPDATE";
                ProcessType[ProcessType["DELETE"] = 3] = "DELETE";
            })(ProcessType || (ProcessType = {}));
            exports_1("ProcessType", ProcessType);
            ;
        }
    }
});
//# sourceMappingURL=type-process.js.map