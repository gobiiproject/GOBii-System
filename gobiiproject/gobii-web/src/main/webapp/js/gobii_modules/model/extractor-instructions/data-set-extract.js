System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var GobiiDataSetExtract;
    return {
        setters: [],
        execute: function () {
            GobiiDataSetExtract = (function () {
                function GobiiDataSetExtract(gobiiFileType, accolate, extractDestinationDirectory, gobiiExtractFilterType, markerList, sampleList, listFileName, gobiiSampleListType, gobiiDatasetType, platformIds, principleInvestigator, project, dataSet) {
                    // this.setGobiiFileType(gobiiFileType);
                    // this.setAccolate(accolate);
                    // this.setDataSetId(dataSetId);
                    // this.setDataSetName(dataSetName);
                    // this.setExtractDestinationDirectory(extractDestinationDirectory);
                    // this.setGobiiFileType(gobiiExtractFilterType);
                    //
                    this.gobiiFileType = gobiiFileType;
                    this.accolate = accolate;
                    this.extractDestinationDirectory = extractDestinationDirectory;
                    this.gobiiExtractFilterType = gobiiExtractFilterType;
                    this.markerList = markerList;
                    this.sampleList = sampleList;
                    this.listFileName = listFileName;
                    this.gobiiSampleListType = gobiiSampleListType;
                    this.gobiiDatasetType = gobiiDatasetType;
                    this.platformIds = platformIds;
                    this.principleInvestigator = principleInvestigator;
                    this.project = project;
                    this.dataSet = dataSet;
                } // ctor 
                GobiiDataSetExtract.prototype.getgobiiFileType = function () {
                    return this.gobiiFileType;
                };
                GobiiDataSetExtract.prototype.setgobiiFileType = function (value) {
                    this.gobiiFileType = value;
                };
                GobiiDataSetExtract.prototype.getaccolate = function () {
                    return this.accolate;
                };
                GobiiDataSetExtract.prototype.setaccolate = function (value) {
                    this.accolate = value;
                };
                GobiiDataSetExtract.prototype.getextractDestinationDirectory = function () {
                    return this.extractDestinationDirectory;
                };
                GobiiDataSetExtract.prototype.setextractDestinationDirectory = function (value) {
                    this.extractDestinationDirectory = value;
                };
                GobiiDataSetExtract.prototype.getgobiiExtractFilterType = function () {
                    return this.gobiiExtractFilterType;
                };
                GobiiDataSetExtract.prototype.setgobiiExtractFilterType = function (value) {
                    this.gobiiExtractFilterType = value;
                };
                GobiiDataSetExtract.prototype.getmarkerList = function () {
                    return this.markerList;
                };
                GobiiDataSetExtract.prototype.setmarkerList = function (value) {
                    this.markerList = value;
                };
                GobiiDataSetExtract.prototype.getsampleList = function () {
                    return this.sampleList;
                };
                GobiiDataSetExtract.prototype.setsampleList = function (value) {
                    this.sampleList = value;
                };
                GobiiDataSetExtract.prototype.getlistFileName = function () {
                    return this.listFileName;
                };
                GobiiDataSetExtract.prototype.setlistFileName = function (value) {
                    this.listFileName = value;
                };
                GobiiDataSetExtract.prototype.getgobiiSampleListType = function () {
                    return this.gobiiSampleListType;
                };
                GobiiDataSetExtract.prototype.setgobiiSampleListType = function (value) {
                    this.gobiiSampleListType = value;
                };
                GobiiDataSetExtract.prototype.getgobiiDatasetType = function () {
                    return this.gobiiDatasetType;
                };
                GobiiDataSetExtract.prototype.setgobiiDatasetType = function (value) {
                    this.gobiiDatasetType = value;
                };
                GobiiDataSetExtract.prototype.getplatformIds = function () {
                    return this.platformIds;
                };
                GobiiDataSetExtract.prototype.setplatformIds = function (value) {
                    this.platformIds = value;
                };
                GobiiDataSetExtract.prototype.getJson = function () {
                    var returnVal = {};
                    returnVal.gobiiFileType = this.gobiiFileType;
                    returnVal.accolate = this.accolate;
                    returnVal.extractDestinationDirectory = this.extractDestinationDirectory;
                    returnVal.gobiiExtractFilterType = this.gobiiExtractFilterType;
                    returnVal.markerList = this.markerList;
                    returnVal.sampleList = this.sampleList;
                    returnVal.listFileName = this.listFileName;
                    returnVal.gobiiSampleListType = this.gobiiSampleListType;
                    returnVal.gobiiDatasetType = this.gobiiDatasetType;
                    returnVal.platformIds = this.platformIds;
                    returnVal.principleInvestigator = this.principleInvestigator;
                    returnVal.project = this.project;
                    returnVal.dataSet = this.dataSet;
                    return returnVal;
                };
                GobiiDataSetExtract.fromJson = function (json) {
                    var returnVal = new GobiiDataSetExtract(json.gobiiFileType, json.accolate, json.extractDestinationDirectory, json.gobiiExtractFilterType, json.markerList, json.sampleList, json.listFileName, json.gobiiSampleListType, json.gobiiDatasetType, json.platformIds, json.principleInvestigator, json.project, json.dataSet);
                    return returnVal;
                };
                return GobiiDataSetExtract;
            }());
            exports_1("GobiiDataSetExtract", GobiiDataSetExtract);
        }
    };
});
//# sourceMappingURL=data-set-extract.js.map