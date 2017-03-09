System.register(["./../dto-header-status-message"], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var dto_header_status_message_1, Status;
    return {
        setters: [
            function (dto_header_status_message_1_1) {
                dto_header_status_message_1 = dto_header_status_message_1_1;
            }
        ],
        execute: function () {
            Status = (function () {
                function Status(succeeded, statusMessages) {
                    this.succeeded = succeeded;
                    this.statusMessages = statusMessages;
                }
                Status.fromJSON = function (json) {
                    var succeeded = json.succeeded;
                    var statusMessages = [];
                    json.statusMessages.forEach(function (m) {
                        statusMessages.push(dto_header_status_message_1.HeaderStatusMessage.fromJSON(m));
                    });
                    return new Status(succeeded, statusMessages); // new
                }; // fromJson()
                return Status;
            }());
            exports_1("Status", Status);
        }
    };
});
//# sourceMappingURL=status.js.map