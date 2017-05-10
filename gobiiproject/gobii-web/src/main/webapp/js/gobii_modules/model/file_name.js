System.register(["./type-extractor-filter"], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var type_extractor_filter_1, FileName;
    return {
        setters: [
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            }
        ],
        execute: function () {
            FileName = (function () {
                function FileName() {
                }
                FileName.makeUniqueFileId = function () {
                    var date = new Date();
                    var returnVal = date.getFullYear()
                        + "_"
                        + ('0' + (date.getMonth() + 1)).slice(-2)
                        + "_"
                        + ('0' + date.getDate()).slice(-2)
                        + "_"
                        + ('0' + date.getHours()).slice(-2)
                        + "_"
                        + ('0' + date.getMinutes()).slice(-2)
                        + "_"
                        + ('0' + date.getSeconds()).slice(-2);
                    return returnVal;
                };
                ;
                FileName.makeFileNameFromJobId = function (gobiiExtractFilterType, jobId) {
                    var returnVal;
                    var suffix = null;
                    if (gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER) {
                        suffix = "_markers";
                    }
                    else {
                        suffix = "_samples";
                    }
                    returnVal = jobId + suffix;
                    return returnVal;
                };
                return FileName;
            }());
            exports_1("FileName", FileName);
        }
    };
});
//# sourceMappingURL=file_name.js.map