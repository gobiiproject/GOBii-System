System.register(["../model/type-entity", "../model/cv-filter-type", "../model/type-extractor-filter", "../model/file-model-node", "../model/type-extract-format"], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var type_entity_1, cv_filter_type_1, type_extractor_filter_1, file_model_node_1, type_extract_format_1, Labels;
    return {
        setters: [
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
            function (type_extract_format_1_1) {
                type_extract_format_1 = type_extract_format_1_1;
            }
        ],
        execute: function () {
            Labels = (function () {
                function Labels() {
                    this.entityNodeLabels = new Map();
                    this.entitySubtypeNodeLabels = new Map();
                    this.cvFilterNodeLabels = new Map();
                    this.extractorFilterTypeLabels = new Map();
                    this.treeExtractorTypeLabels = new Map();
                    this.extractFormatTypeLabels = new Map();
                    this.entityNodeLabels[type_entity_1.EntityType.DataSets] = "Data Set";
                    this.entityNodeLabels[type_entity_1.EntityType.Platforms] = "Platform";
                    this.entityNodeLabels[type_entity_1.EntityType.Mapsets] = "Mapset";
                    this.entityNodeLabels[type_entity_1.EntityType.Projects] = "Project";
                    this.cvFilterNodeLabels[cv_filter_type_1.CvFilterType.DATASET_TYPE] = "Dataset Type";
                    this.entitySubtypeNodeLabels[type_entity_1.EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR] = "Principle Investigator";
                    this.entitySubtypeNodeLabels[type_entity_1.EntitySubType.CONTACT_SUBMITED_BY] = "Submit As";
                    this.extractorFilterTypeLabels[type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET] = "By Dataset";
                    this.extractorFilterTypeLabels[type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE] = "By Sample";
                    this.extractorFilterTypeLabels[type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER] = "By Marker";
                    this.treeExtractorTypeLabels[file_model_node_1.ExtractorItemType.SAMPLE_LIST_ITEM] = "Sample List";
                    this.treeExtractorTypeLabels[file_model_node_1.ExtractorItemType.SAMPLE_FILE] = "Sample File";
                    this.treeExtractorTypeLabels[file_model_node_1.ExtractorItemType.MARKER_LIST_ITEM] = "Marker List";
                    this.treeExtractorTypeLabels[file_model_node_1.ExtractorItemType.MARKER_FILE] = "Marker File";
                    this.treeExtractorTypeLabels[file_model_node_1.ExtractorItemType.CROP_TYPE] = "Crop Type";
                    this.treeExtractorTypeLabels[file_model_node_1.ExtractorItemType.EXPORT_FORMAT] = "Format";
                    this.treeExtractorTypeLabels[file_model_node_1.ExtractorItemType.JOB_ID] = "Job ID";
                    this.treeExtractorTypeLabels[file_model_node_1.ExtractorItemType.SAMPLE_LIST_TYPE] = "List Type";
                    this.extractFormatTypeLabels[type_extract_format_1.GobiiExtractFormat.HAPMAP] = "Hapmap";
                    this.extractFormatTypeLabels[type_extract_format_1.GobiiExtractFormat.FLAPJACK] = "Flapjack";
                    this.extractFormatTypeLabels[type_extract_format_1.GobiiExtractFormat.META_DATA_ONLY] = "Meta Data";
                }
                Labels.instance = function () {
                    if (this._instance === null) {
                        this._instance = new Labels();
                    }
                    return this._instance;
                };
                return Labels;
            }());
            Labels._instance = null;
            exports_1("Labels", Labels);
        }
    };
});
//# sourceMappingURL=entity-labels.js.map