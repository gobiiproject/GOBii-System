System.register(["./data-set-extract"], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var data_set_extract_1, GobiiExtractorInstruction;
    return {
        setters: [
            function (data_set_extract_1_1) {
                data_set_extract_1 = data_set_extract_1_1;
            }
        ],
        execute: function () {
            GobiiExtractorInstruction = (function () {
                function GobiiExtractorInstruction(dataSetExtracts, contactId, contactEmail, mapsetIds) {
                    this.dataSetExtracts = dataSetExtracts;
                    this.contactId = contactId;
                    this.contactEmail = contactEmail;
                    this.mapsetIds = mapsetIds;
                    this.dataSetExtracts = dataSetExtracts;
                }
                GobiiExtractorInstruction.prototype.getDataSetExtracts = function () {
                    return this.dataSetExtracts;
                };
                GobiiExtractorInstruction.prototype.setDataSetExtracts = function (value) {
                    this.dataSetExtracts = value;
                };
                GobiiExtractorInstruction.prototype.getContactId = function () {
                    return this.contactId;
                };
                GobiiExtractorInstruction.prototype.setContactId = function (contactId) {
                    this.contactId = contactId;
                };
                GobiiExtractorInstruction.prototype.setContactEmail = function (contactEmail) {
                    this.contactEmail = contactEmail;
                };
                GobiiExtractorInstruction.prototype.getContactEmail = function () {
                    return this.contactEmail;
                };
                GobiiExtractorInstruction.prototype.setMapsetIds = function (mapsetIds) {
                    this.mapsetIds = mapsetIds;
                };
                GobiiExtractorInstruction.prototype.getMapsetIds = function () {
                    return this.mapsetIds;
                };
                GobiiExtractorInstruction.prototype.getJson = function () {
                    var returnVal = {};
                    returnVal.contactId = this.contactId;
                    returnVal.contactEmail = this.contactEmail;
                    returnVal.mapsetIds = this.mapsetIds;
                    returnVal.dataSetExtracts = [];
                    this.dataSetExtracts.forEach(function (e) {
                        returnVal.dataSetExtracts.push(e.getJson());
                    });
                    return returnVal;
                };
                GobiiExtractorInstruction.fromJson = function (json) {
                    var dataSetExtracts = [];
                    json.dataSetExtracts.forEach(function (e) { return dataSetExtracts.push(data_set_extract_1.GobiiDataSetExtract.fromJson(e)); });
                    var returnVal = new GobiiExtractorInstruction(dataSetExtracts, json.contactId, json.contactEmail, json.mapsetIds);
                    return returnVal;
                };
                return GobiiExtractorInstruction;
            }());
            exports_1("GobiiExtractorInstruction", GobiiExtractorInstruction);
        }
    };
});
//# sourceMappingURL=gobii-extractor-instruction.js.map