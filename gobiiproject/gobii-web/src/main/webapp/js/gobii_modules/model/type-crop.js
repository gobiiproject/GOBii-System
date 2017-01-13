System.register([], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var GobiiCropType;
    return {
        setters:[],
        execute: function() {
            (function (GobiiCropType) {
                // order is meaningful -- do not change it
                // it has to align with the same enum on the server
                GobiiCropType[GobiiCropType["UNDEFINED"] = 0] = "UNDEFINED";
                GobiiCropType[GobiiCropType["DEV"] = 1] = "DEV";
                GobiiCropType[GobiiCropType["TEST"] = 2] = "TEST";
                GobiiCropType[GobiiCropType["CHICKPEA"] = 3] = "CHICKPEA";
                GobiiCropType[GobiiCropType["MAIZE"] = 4] = "MAIZE";
                GobiiCropType[GobiiCropType["RICE"] = 5] = "RICE";
                GobiiCropType[GobiiCropType["SORGHUM"] = 6] = "SORGHUM";
                GobiiCropType[GobiiCropType["WHEAT"] = 7] = "WHEAT";
            })(GobiiCropType || (GobiiCropType = {}));
            exports_1("GobiiCropType", GobiiCropType);
        }
    }
});
//# sourceMappingURL=type-crop.js.map