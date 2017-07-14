System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var GobiiExtractFormat;
    return {
        setters: [],
        execute: function () {
            (function (GobiiExtractFormat) {
                GobiiExtractFormat[GobiiExtractFormat["UNKNOWN"] = 0] = "UNKNOWN";
                GobiiExtractFormat[GobiiExtractFormat["HAPMAP"] = 1] = "HAPMAP";
                GobiiExtractFormat[GobiiExtractFormat["FLAPJACK"] = 2] = "FLAPJACK";
                GobiiExtractFormat[GobiiExtractFormat["META_DATA_ONLY"] = 3] = "META_DATA_ONLY";
            })(GobiiExtractFormat || (GobiiExtractFormat = {}));
            exports_1("GobiiExtractFormat", GobiiExtractFormat);
        }
    };
});
//# sourceMappingURL=type-extract-format.js.map