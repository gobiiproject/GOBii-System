System.register([], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var EntityType;
    return {
        setters:[],
        execute: function() {
            (function (EntityType) {
                EntityType[EntityType["DataSetNames"] = 0] = "DataSetNames";
                EntityType[EntityType["Contact"] = 1] = "Contact";
                EntityType[EntityType["AllContacts"] = 2] = "AllContacts";
                EntityType[EntityType["Project"] = 3] = "Project";
                EntityType[EntityType["Experiment"] = 4] = "Experiment";
                EntityType[EntityType["DataSetNamesByExperimentId"] = 5] = "DataSetNamesByExperimentId";
                EntityType[EntityType["CvGroupTerms"] = 6] = "CvGroupTerms";
            })(EntityType || (EntityType = {}));
            exports_1("EntityType", EntityType);
            ;
        }
    }
});
//# sourceMappingURL=type-entity.js.map