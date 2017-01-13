System.register([], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var HeaderStatusMessage;
    return {
        setters:[],
        execute: function() {
            HeaderStatusMessage = (function () {
                function HeaderStatusMessage(message, statusLevel, validationStatusType) {
                    this.message = message;
                    this.statusLevel = statusLevel;
                    this.validationStatusType = validationStatusType;
                }
                HeaderStatusMessage.fromJSON = function (json) {
                    return new HeaderStatusMessage(json.message, json.statusLevel, json.validationStatusType);
                }; // fromJSON
                return HeaderStatusMessage;
            }());
            exports_1("HeaderStatusMessage", HeaderStatusMessage);
        }
    }
});
//# sourceMappingURL=dto-header-status-message.js.map