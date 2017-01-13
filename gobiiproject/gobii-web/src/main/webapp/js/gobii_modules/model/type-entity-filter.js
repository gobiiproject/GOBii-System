System.register([], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var EntityFilter;
    return {
        setters:[],
        execute: function() {
            (function (EntityFilter) {
                EntityFilter[EntityFilter["NONE"] = 0] = "NONE";
                EntityFilter[EntityFilter["BYTYPEID"] = 1] = "BYTYPEID";
                EntityFilter[EntityFilter["BYTYPENAME"] = 2] = "BYTYPENAME";
            })(EntityFilter || (EntityFilter = {}));
            exports_1("EntityFilter", EntityFilter);
        }
    }
});
//# sourceMappingURL=type-entity-filter.js.map