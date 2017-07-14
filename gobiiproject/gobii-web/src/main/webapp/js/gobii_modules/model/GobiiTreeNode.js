System.register(["./type-entity", "./cv-filter-type"], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var type_entity_1, cv_filter_type_1, GobiiTreeNode;
    return {
        setters: [
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (cv_filter_type_1_1) {
                cv_filter_type_1 = cv_filter_type_1_1;
            }
        ],
        execute: function () {
            GobiiTreeNode = (function () {
                function GobiiTreeNode(parent, fileModelNodeId, fileItemId, required) {
                    this.entityType = type_entity_1.EntityType.UNKNOWN;
                    this.entitySubType = type_entity_1.EntitySubType.UNKNOWN;
                    this.cvFilterType = cv_filter_type_1.CvFilterType.UNKNOWN;
                    this.children = [];
                    this.required = false;
                    this.parent = parent;
                    this.fileModelNodeId = fileModelNodeId;
                    this.fileItemId = fileItemId;
                    this.required = required;
                }
                return GobiiTreeNode;
            }());
            exports_1("GobiiTreeNode", GobiiTreeNode);
        }
    };
});
//# sourceMappingURL=GobiiTreeNode.js.map