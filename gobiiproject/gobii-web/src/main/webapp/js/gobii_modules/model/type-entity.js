System.register([], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var EntityType;
    return {
        setters:[],
        execute: function() {
            (function (EntityType) {
                EntityType[EntityType["DataSetNames"] = 0] = "DataSetNames";
                EntityType[EntityType["Contacts"] = 1] = "Contacts";
                EntityType[EntityType["Projects"] = 2] = "Projects";
                EntityType[EntityType["Experiments"] = 3] = "Experiments";
                EntityType[EntityType["DataSets"] = 4] = "DataSets";
                EntityType[EntityType["CvTerms"] = 5] = "CvTerms";
            })(EntityType || (EntityType = {}));
            exports_1("EntityType", EntityType);
            ;
        }
    }
});
//# sourceMappingURL=type-entity.js.map