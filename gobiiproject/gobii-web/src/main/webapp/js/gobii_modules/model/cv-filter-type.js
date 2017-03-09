System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var CvFilterType, CvFilters;
    return {
        setters: [],
        execute: function () {
            (function (CvFilterType) {
                CvFilterType[CvFilterType["UKNOWN"] = 0] = "UKNOWN";
                CvFilterType[CvFilterType["ANALYSIS_TYPE"] = 1] = "ANALYSIS_TYPE";
                CvFilterType[CvFilterType["DATASET_TYPE"] = 2] = "DATASET_TYPE";
            })(CvFilterType || (CvFilterType = {}));
            exports_1("CvFilterType", CvFilterType);
            ;
            CvFilters = (function () {
                function CvFilters() {
                }
                CvFilters.get = function (cvFilterType) {
                    if (this.cvValues === null) {
                        this.cvValues = new Map();
                        this.cvValues[CvFilterType.ANALYSIS_TYPE] = "analysis_type";
                        this.cvValues[CvFilterType.DATASET_TYPE] = "dataset_type";
                    }
                    return this.cvValues[cvFilterType];
                };
                return CvFilters;
            }());
            CvFilters.cvValues = null;
            exports_1("CvFilters", CvFilters);
        }
    };
});
//# sourceMappingURL=cv-filter-type.js.map