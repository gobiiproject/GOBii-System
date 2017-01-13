System.register([], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var Experiment;
    return {
        setters:[],
        execute: function() {
            Experiment = (function () {
                function Experiment(experimentId, experimentName, experimentCode, experimentDataFile, projectId, platformId, manifestId, createdBy, createdstring, modifiedBy, modifiedstring, status, platformName) {
                    this.experimentId = experimentId;
                    this.experimentName = experimentName;
                    this.experimentCode = experimentCode;
                    this.experimentDataFile = experimentDataFile;
                    this.projectId = projectId;
                    this.platformId = platformId;
                    this.manifestId = manifestId;
                    this.createdBy = createdBy;
                    this.createdstring = createdstring;
                    this.modifiedBy = modifiedBy;
                    this.modifiedstring = modifiedstring;
                    this.status = status;
                    this.platformName = platformName;
                }
                return Experiment;
            }());
            exports_1("Experiment", Experiment);
        }
    }
});
//# sourceMappingURL=experiment.js.map