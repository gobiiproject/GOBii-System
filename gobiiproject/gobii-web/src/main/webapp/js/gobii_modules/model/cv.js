System.register([], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var Cv;
    return {
        setters:[],
        execute: function() {
            Cv = (function () {
                function Cv(cv_id, group, term, definition, rank) {
                    this.cv_id = cv_id;
                    this.group = group;
                    this.term = term;
                    this.definition = definition;
                    this.rank = rank;
                }
                return Cv;
            }());
            exports_1("Cv", Cv);
        }
    }
});
//# sourceMappingURL=cv.js.map