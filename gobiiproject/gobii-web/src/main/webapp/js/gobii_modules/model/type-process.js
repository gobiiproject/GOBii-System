System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var ProcessType;
    return {
        setters: [],
        execute: function () {
            (function (ProcessType) {
                ProcessType[ProcessType["UNKNOWN"] = 0] = "UNKNOWN";
                ProcessType[ProcessType["CREATE"] = 1] = "CREATE";
                ProcessType[ProcessType["READ"] = 2] = "READ";
                ProcessType[ProcessType["UPDATE"] = 3] = "UPDATE";
                ProcessType[ProcessType["DELETE"] = 4] = "DELETE";
                ProcessType[ProcessType["NOTIFY"] = 5] = "NOTIFY";
            })(ProcessType || (ProcessType = {}));
            exports_1("ProcessType", ProcessType);
            ;
        }
    };
});
//# sourceMappingURL=type-process.js.map