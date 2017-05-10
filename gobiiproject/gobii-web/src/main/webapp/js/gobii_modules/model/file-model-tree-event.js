System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var FileModelState, FileModelTreeEvent;
    return {
        setters: [],
        execute: function () {
            (function (FileModelState) {
                FileModelState[FileModelState["UNKNOWN"] = 0] = "UNKNOWN";
                FileModelState[FileModelState["READY"] = 1] = "READY";
                FileModelState[FileModelState["SUBMISSION_INCOMPLETE"] = 2] = "SUBMISSION_INCOMPLETE";
                FileModelState[FileModelState["MISMATCHED_EXTRACTOR_FILTER_TYPE"] = 3] = "MISMATCHED_EXTRACTOR_FILTER_TYPE";
                FileModelState[FileModelState["ERROR"] = 4] = "ERROR";
                FileModelState[FileModelState["SUBMISSION_READY"] = 5] = "SUBMISSION_READY";
            })(FileModelState || (FileModelState = {}));
            exports_1("FileModelState", FileModelState);
            FileModelTreeEvent = (function () {
                function FileModelTreeEvent(fileItem, fileModelNode, fileModelState, message) {
                    this.fileItem = fileItem;
                    this.fileModelNode = fileModelNode;
                    this.fileModelState = fileModelState;
                    this.message = message;
                    this.fileItem = fileItem;
                    this.fileModelNode = fileModelNode;
                    this.fileModelState = fileModelState;
                }
                return FileModelTreeEvent;
            }());
            exports_1("FileModelTreeEvent", FileModelTreeEvent);
        }
    };
});
//# sourceMappingURL=file-model-tree-event.js.map