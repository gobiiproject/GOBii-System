System.register(["./type-entity", "./cv-filter-type", "./guid"], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var type_entity_1, cv_filter_type_1, guid_1, ExtractorItemType, ExtractorCategoryType, CardinalityType, FileModelNode;
    return {
        setters: [
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (cv_filter_type_1_1) {
                cv_filter_type_1 = cv_filter_type_1_1;
            },
            function (guid_1_1) {
                guid_1 = guid_1_1;
            }
        ],
        execute: function () {
            (function (ExtractorItemType) {
                ExtractorItemType[ExtractorItemType["UNKNOWN"] = 0] = "UNKNOWN";
                ExtractorItemType[ExtractorItemType["ENTITY"] = 1] = "ENTITY";
                ExtractorItemType[ExtractorItemType["MARKER_FILE"] = 2] = "MARKER_FILE";
                ExtractorItemType[ExtractorItemType["MARKER_LIST_ITEM"] = 3] = "MARKER_LIST_ITEM";
                ExtractorItemType[ExtractorItemType["SAMPLE_LIST_ITEM"] = 4] = "SAMPLE_LIST_ITEM";
                ExtractorItemType[ExtractorItemType["SAMPLE_FILE"] = 5] = "SAMPLE_FILE";
                ExtractorItemType[ExtractorItemType["EXPORT_FORMAT"] = 6] = "EXPORT_FORMAT";
                ExtractorItemType[ExtractorItemType["CROP_TYPE"] = 7] = "CROP_TYPE";
                ExtractorItemType[ExtractorItemType["STATUS_DISPLAY_TREE_READY"] = 8] = "STATUS_DISPLAY_TREE_READY";
                ExtractorItemType[ExtractorItemType["JOB_ID"] = 9] = "JOB_ID";
                ExtractorItemType[ExtractorItemType["SAMPLE_LIST_TYPE"] = 10] = "SAMPLE_LIST_TYPE";
                ExtractorItemType[ExtractorItemType["LABEL"] = 11] = "LABEL";
                ExtractorItemType[ExtractorItemType["CLEAR_TREE"] = 12] = "CLEAR_TREE";
            })(ExtractorItemType || (ExtractorItemType = {}));
            exports_1("ExtractorItemType", ExtractorItemType);
            (function (ExtractorCategoryType) {
                ExtractorCategoryType[ExtractorCategoryType["CONTAINER"] = 0] = "CONTAINER";
                ExtractorCategoryType[ExtractorCategoryType["LEAF"] = 1] = "LEAF";
            })(ExtractorCategoryType || (ExtractorCategoryType = {}));
            exports_1("ExtractorCategoryType", ExtractorCategoryType);
            (function (CardinalityType) {
                CardinalityType[CardinalityType["ZERO_OR_ONE"] = 0] = "ZERO_OR_ONE";
                CardinalityType[CardinalityType["ZERO_OR_MORE"] = 1] = "ZERO_OR_MORE";
                CardinalityType[CardinalityType["ONE_ONLY"] = 2] = "ONE_ONLY";
                CardinalityType[CardinalityType["ONE_OR_MORE"] = 3] = "ONE_OR_MORE";
                CardinalityType[CardinalityType["MORE_THAN_ONE"] = 4] = "MORE_THAN_ONE";
            })(CardinalityType || (CardinalityType = {}));
            exports_1("CardinalityType", CardinalityType);
            FileModelNode = (function () {
                function FileModelNode(itemType, parent) {
                    this._parent = null;
                    this._children = [];
                    this._alternatePeerTypes = [];
                    this._cardinality = CardinalityType.ZERO_OR_MORE;
                    this._itemType = ExtractorItemType.ENTITY;
                    this._categoryType = ExtractorCategoryType.LEAF;
                    this._entityType = type_entity_1.EntityType.UNKNOWN;
                    this._entitySubType = type_entity_1.EntitySubType.UNKNOWN;
                    this._cvFilterType = cv_filter_type_1.CvFilterType.UNKNOWN;
                    this._fileItems = [];
                    this._fileModelNodeUniqueId = guid_1.Guid.generateUUID();
                    this._itemType = itemType;
                    this._parent = parent;
                }
                FileModelNode.build = function (itemType, parent) {
                    return new FileModelNode(itemType, parent);
                };
                FileModelNode.prototype.getAlternatePeerTypes = function () {
                    return this._alternatePeerTypes;
                };
                FileModelNode.prototype.setAlternatePeerTypes = function (value) {
                    this._alternatePeerTypes = value;
                    return this;
                };
                FileModelNode.prototype.getCardinality = function () {
                    return this._cardinality;
                };
                FileModelNode.prototype.setCardinality = function (value) {
                    this._cardinality = value;
                    return this;
                };
                FileModelNode.prototype.getParent = function () {
                    return this._parent;
                };
                FileModelNode.prototype.getChildren = function () {
                    return this._children;
                };
                FileModelNode.prototype.addChild = function (child) {
                    this._children.push(child);
                    return this;
                };
                FileModelNode.prototype.getItemType = function () {
                    return this._itemType;
                };
                FileModelNode.prototype.setItemType = function (value) {
                    this._itemType = value;
                    return this;
                };
                FileModelNode.prototype.getCategoryType = function () {
                    return this._categoryType;
                };
                FileModelNode.prototype.setCategoryType = function (value) {
                    this._categoryType = value;
                    return this;
                };
                FileModelNode.prototype.getCategoryName = function () {
                    return this._categoryName;
                };
                FileModelNode.prototype.setCategoryName = function (value) {
                    this._categoryName = value;
                    return this;
                };
                FileModelNode.prototype.getEntityType = function () {
                    return this._entityType;
                };
                FileModelNode.prototype.setEntityType = function (value) {
                    this._entityType = value;
                    return this;
                };
                FileModelNode.prototype.getEntitySubType = function () {
                    return this._entitySubType;
                };
                FileModelNode.prototype.setEntitySubType = function (value) {
                    this._entitySubType = value;
                    return this;
                };
                FileModelNode.prototype.getEntityName = function () {
                    return this._entityName;
                };
                FileModelNode.prototype.setEntityName = function (value) {
                    this._entityName = value;
                    return this;
                };
                FileModelNode.prototype.getCvFilterType = function () {
                    return this._cvFilterType;
                };
                FileModelNode.prototype.setCvFilterType = function (value) {
                    this._cvFilterType = value;
                    return this;
                };
                FileModelNode.prototype.getFileItems = function () {
                    return this._fileItems;
                };
                FileModelNode.prototype.setChildFileItems = function (value) {
                    this._fileItems = value;
                    return this;
                };
                FileModelNode.prototype.getFileModelNodeUniqueId = function () {
                    return this._fileModelNodeUniqueId;
                };
                FileModelNode.prototype.getRequired = function () {
                    return this._required;
                };
                FileModelNode.prototype.setRequired = function (value) {
                    this._required = value;
                    return this;
                };
                return FileModelNode;
            }());
            exports_1("FileModelNode", FileModelNode);
        }
    };
});
//# sourceMappingURL=file-model-node.js.map