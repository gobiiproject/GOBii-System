System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var EntityType, EntitySubType;
    return {
        setters: [],
        execute: function () {
            (function (EntityType) {
                EntityType[EntityType["UNKNOWN"] = 0] = "UNKNOWN";
                EntityType[EntityType["Contacts"] = 1] = "Contacts";
                EntityType[EntityType["Projects"] = 2] = "Projects";
                EntityType[EntityType["Experiments"] = 3] = "Experiments";
                EntityType[EntityType["DataSets"] = 4] = "DataSets";
                EntityType[EntityType["CvTerms"] = 5] = "CvTerms";
                EntityType[EntityType["Mapsets"] = 6] = "Mapsets";
                EntityType[EntityType["Platforms"] = 7] = "Platforms";
            })(EntityType || (EntityType = {}));
            exports_1("EntityType", EntityType);
            (function (EntitySubType) {
                EntitySubType[EntitySubType["UNKNOWN"] = 0] = "UNKNOWN";
                EntitySubType[EntitySubType["CONTACT_PRINCIPLE_INVESTIGATOR"] = 1] = "CONTACT_PRINCIPLE_INVESTIGATOR";
                EntitySubType[EntitySubType["CONTACT_SUBMITED_BY"] = 2] = "CONTACT_SUBMITED_BY";
            })(EntitySubType || (EntitySubType = {}));
            exports_1("EntitySubType", EntitySubType);
        }
    };
});
//# sourceMappingURL=type-entity.js.map