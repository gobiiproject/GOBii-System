System.register(["./type-process", "./guid", "./type-entity", "./cv-filter-type", "./type-extractor-filter", "./file-model-node", "./type-event-origin"], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var type_process_1, guid_1, type_entity_1, cv_filter_type_1, type_extractor_filter_1, file_model_node_1, type_event_origin_1, GobiiFileItem;
    return {
        setters: [
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (guid_1_1) {
                guid_1 = guid_1_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (cv_filter_type_1_1) {
                cv_filter_type_1 = cv_filter_type_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (file_model_node_1_1) {
                file_model_node_1 = file_model_node_1_1;
            },
            function (type_event_origin_1_1) {
                type_event_origin_1 = type_event_origin_1_1;
            }
        ],
        execute: function () {
            GobiiFileItem = (function () {
                function GobiiFileItem(_gobiiExtractFilterType, _processType, _extractorItemType, _entityType, _entitySubType, _cvFilterType, _itemId, _itemName, _checked, _required, _gobiiEventOrigin) {
                    this._gobiiExtractFilterType = _gobiiExtractFilterType;
                    this._processType = _processType;
                    this._extractorItemType = _extractorItemType;
                    this._entityType = _entityType;
                    this._entitySubType = _entitySubType;
                    this._cvFilterType = _cvFilterType;
                    this._itemId = _itemId;
                    this._itemName = _itemName;
                    this._checked = _checked;
                    this._required = _required;
                    this._gobiiEventOrigin = _gobiiEventOrigin;
                    this._gobiiExtractFilterType = _gobiiExtractFilterType;
                    this._processType = _processType;
                    this._entityType = _entityType;
                    this._entitySubType = _entitySubType;
                    this._extractorItemType = _extractorItemType;
                    this._cvFilterType = _cvFilterType;
                    this._itemId = _itemId;
                    this._itemName = _itemName;
                    this._checked = _checked;
                    this._required = _required;
                    this._gobiiEventOrigin = _gobiiEventOrigin;
                    this._fileItemUniqueId = guid_1.Guid.generateUUID();
                    if (this._cvFilterType === null) {
                        this._cvFilterType = cv_filter_type_1.CvFilterType.UNKNOWN;
                    }
                    if (this._extractorItemType == null) {
                        this._extractorItemType = file_model_node_1.ExtractorItemType.UNKNOWN;
                    }
                    if (this._entityType == null) {
                        this._entityType = type_entity_1.EntityType.UNKNOWN;
                    }
                    if (this._entitySubType == null) {
                        this._entitySubType = type_entity_1.EntitySubType.UNKNOWN;
                    }
                    if (this._gobiiEventOrigin == null) {
                        this._gobiiEventOrigin = type_event_origin_1.GobiiUIEventOrigin.UNKNOWN;
                    }
                }
                GobiiFileItem.build = function (gobiiExtractFilterType, processType) {
                    var returnVal = new GobiiFileItem(gobiiExtractFilterType, processType, file_model_node_1.ExtractorItemType.UNKNOWN, type_entity_1.EntityType.UNKNOWN, type_entity_1.EntitySubType.UNKNOWN, cv_filter_type_1.CvFilterType.UNKNOWN, null, null, null, null, type_event_origin_1.GobiiUIEventOrigin.UNKNOWN);
                    return returnVal;
                };
                GobiiFileItem.prototype.setFileItemUniqueId = function (fileItemUniqueId) {
                    this._fileItemUniqueId = fileItemUniqueId;
                    return this;
                };
                GobiiFileItem.prototype.getFileItemUniqueId = function () {
                    return this._fileItemUniqueId;
                };
                GobiiFileItem.prototype.getGobiiExtractFilterType = function () {
                    return this._gobiiExtractFilterType;
                };
                GobiiFileItem.prototype.setGobiiExtractFilterType = function (value) {
                    if (value != null) {
                        this._gobiiExtractFilterType = value;
                    }
                    else {
                        this._gobiiExtractFilterType = type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN;
                    }
                    return this;
                };
                GobiiFileItem.prototype.getProcessType = function () {
                    return this._processType;
                };
                GobiiFileItem.prototype.setProcessType = function (value) {
                    if (value != null) {
                        this._processType = value;
                    }
                    else {
                        this._processType = type_process_1.ProcessType.UNKNOWN;
                    }
                    return this;
                };
                GobiiFileItem.prototype.getExtractorItemType = function () {
                    return this._extractorItemType;
                };
                GobiiFileItem.prototype.setExtractorItemType = function (value) {
                    if (value != null) {
                        this._extractorItemType = value;
                    }
                    else {
                        this._extractorItemType = file_model_node_1.ExtractorItemType.UNKNOWN;
                    }
                    return this;
                };
                GobiiFileItem.prototype.getEntityType = function () {
                    return this._entityType;
                };
                GobiiFileItem.prototype.setEntityType = function (value) {
                    if (value != null) {
                        this._entityType = value;
                    }
                    else {
                        this._entityType = type_entity_1.EntityType.UNKNOWN;
                    }
                    return this;
                };
                GobiiFileItem.prototype.getEntitySubType = function () {
                    return this._entitySubType;
                };
                GobiiFileItem.prototype.setEntitySubType = function (value) {
                    if (value != null) {
                        this._entitySubType = value;
                    }
                    else {
                        this._entitySubType = type_entity_1.EntitySubType.UNKNOWN;
                    }
                    return this;
                };
                GobiiFileItem.prototype.getCvFilterType = function () {
                    return this._cvFilterType;
                };
                GobiiFileItem.prototype.setCvFilterType = function (value) {
                    if (value != null) {
                        this._cvFilterType = value;
                    }
                    else {
                        this._cvFilterType = cv_filter_type_1.CvFilterType.UNKNOWN;
                    }
                    return this;
                };
                GobiiFileItem.prototype.getItemId = function () {
                    return this._itemId;
                };
                GobiiFileItem.prototype.setItemId = function (value) {
                    this._itemId = value;
                    return this;
                };
                GobiiFileItem.prototype.getItemName = function () {
                    return this._itemName;
                };
                GobiiFileItem.prototype.setItemName = function (value) {
                    this._itemName = value;
                    return this;
                };
                GobiiFileItem.prototype.getChecked = function () {
                    return this._checked;
                };
                GobiiFileItem.prototype.setChecked = function (value) {
                    this._checked = value;
                    return this;
                };
                GobiiFileItem.prototype.getRequired = function () {
                    return this._required;
                };
                GobiiFileItem.prototype.setRequired = function (value) {
                    this._required = value;
                    return this;
                };
                GobiiFileItem.prototype.getGobiiEventOrigin = function () {
                    return this._gobiiEventOrigin;
                };
                GobiiFileItem.prototype.setGobiiEventOrigin = function (value) {
                    this._gobiiEventOrigin = value;
                    return this;
                };
                return GobiiFileItem;
            }()); // GobiiFileItem()
            exports_1("GobiiFileItem", GobiiFileItem);
        }
    };
});
//# sourceMappingURL=gobii-file-item.js.map