System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var ModelTreeValidationError;
    return {
        setters: [],
        execute: function () {
            ModelTreeValidationError = (function () {
                function ModelTreeValidationError(message, fileModelNode) {
                    this.message = message;
                    this.fileModelNode = fileModelNode;
                }
                return ModelTreeValidationError;
            }());
            exports_1("ModelTreeValidationError", ModelTreeValidationError);
        }
    };
});
//# sourceMappingURL=model-tree-validation-error.js.map