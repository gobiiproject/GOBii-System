System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var TreeStatusNotification;
    return {
        setters: [],
        execute: function () {
            TreeStatusNotification = (function () {
                function TreeStatusNotification(gobiiExractFilterType, fileModelState, modelTreeValidationErrors) {
                    this.gobiiExractFilterType = gobiiExractFilterType;
                    this.fileModelState = fileModelState;
                    this.modelTreeValidationErrors = modelTreeValidationErrors;
                    if (modelTreeValidationErrors === null) {
                        this.modelTreeValidationErrors = [];
                    }
                }
                return TreeStatusNotification;
            }());
            exports_1("TreeStatusNotification", TreeStatusNotification);
        }
    };
});
//# sourceMappingURL=tree-status-notification.js.map