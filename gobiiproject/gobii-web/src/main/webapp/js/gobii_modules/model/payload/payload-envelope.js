System.register(["./header", "./payload"], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var header_1, payload_1;
    var PayloadEnvelope;
    return {
        setters:[
            function (header_1_1) {
                header_1 = header_1_1;
            },
            function (payload_1_1) {
                payload_1 = payload_1_1;
            }],
        execute: function() {
            PayloadEnvelope = (function () {
                function PayloadEnvelope(header, payload) {
                    this.header = header;
                    this.payload = payload;
                }
                PayloadEnvelope.fromJSON = function (json) {
                    var header = header_1.Header.fromJSON(json.header);
                    var payload = payload_1.Payload.fromJSON(json.payload);
                    return new PayloadEnvelope(header, payload);
                }; // fromJson()
                PayloadEnvelope.wrapSingleDTOInJSON = function (payLoad) {
                    var returnVal = {};
                    returnVal.payload = { "data": [] };
                    returnVal.payload.data.push(payLoad);
                    // returnVal.processType = this.processType;
                    // returnVal.instructionFileName = this.instructionFileName;
                    // returnVal.gobiiCropType = this.gobiiCropType;
                    // returnVal.gobiiExtractorInstructions = [];
                    //
                    // this.gobiiExtractorInstructions.forEach(i => {
                    //     returnVal.gobiiExtractorInstructions.push(i.getJson());
                    // });
                    return returnVal;
                };
                return PayloadEnvelope;
            }());
            exports_1("PayloadEnvelope", PayloadEnvelope);
        }
    }
});
//# sourceMappingURL=payload-envelope.js.map