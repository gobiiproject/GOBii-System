System.register(["./dto-header-status-message"], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var dto_header_status_message_1;
    var DtoHeaderResponse;
    return {
        setters:[
            function (dto_header_status_message_1_1) {
                dto_header_status_message_1 = dto_header_status_message_1_1;
            }],
        execute: function() {
            DtoHeaderResponse = (function () {
                function DtoHeaderResponse(succeeded, statusMessages) {
                    this.succeeded = succeeded;
                    this.statusMessages = statusMessages;
                }
                DtoHeaderResponse.fromJSON = function (json) {
                    var statusMessages = [];
                    json.dtoHeaderResponse.statusMessages.forEach(function (m) {
                        statusMessages.push(dto_header_status_message_1.HeaderStatusMessage.fromJSON(m));
                    });
                    return new DtoHeaderResponse(json.dtoHeaderResponse.succeeded, statusMessages); // new
                }; // fromJson()
                return DtoHeaderResponse;
            }());
            exports_1("DtoHeaderResponse", DtoHeaderResponse);
        }
    }
});
//# sourceMappingURL=dto-header-response.js.map