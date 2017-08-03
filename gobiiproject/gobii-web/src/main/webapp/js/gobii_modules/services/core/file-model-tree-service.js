System.register(["@angular/core", "../../model/file-model-tree-event", "../../model/file-model-node", "../../model/type-extractor-filter", "../../model/type-entity", "../../model/cv-filter-type", "rxjs/Subject", "rxjs/Observable", "../../model/type-process", "../../views/entity-labels", "../../model/dto-header-status-message", "../../model/tree-status-notification", "../../model/cardinality-expression", "../../model/model-tree-validation-error"], function (exports_1, context_1) {
    "use strict";
    var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
        var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
        if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
        else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
        return c > 3 && r && Object.defineProperty(target, key, r), r;
    };
    var __metadata = (this && this.__metadata) || function (k, v) {
        if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
    };
    var __moduleName = context_1 && context_1.id;
    var core_1, file_model_tree_event_1, file_model_node_1, type_extractor_filter_1, type_entity_1, cv_filter_type_1, Subject_1, Observable_1, type_process_1, entity_labels_1, dto_header_status_message_1, tree_status_notification_1, cardinality_expression_1, model_tree_validation_error_1, FileModelTreeService;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (file_model_tree_event_1_1) {
                file_model_tree_event_1 = file_model_tree_event_1_1;
            },
            function (file_model_node_1_1) {
                file_model_node_1 = file_model_node_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (cv_filter_type_1_1) {
                cv_filter_type_1 = cv_filter_type_1_1;
            },
            function (Subject_1_1) {
                Subject_1 = Subject_1_1;
            },
            function (Observable_1_1) {
                Observable_1 = Observable_1_1;
            },
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (entity_labels_1_1) {
                entity_labels_1 = entity_labels_1_1;
            },
            function (dto_header_status_message_1_1) {
                dto_header_status_message_1 = dto_header_status_message_1_1;
            },
            function (tree_status_notification_1_1) {
                tree_status_notification_1 = tree_status_notification_1_1;
            },
            function (cardinality_expression_1_1) {
                cardinality_expression_1 = cardinality_expression_1_1;
            },
            function (model_tree_validation_error_1_1) {
                model_tree_validation_error_1 = model_tree_validation_error_1_1;
            }
        ],
        execute: function () {
            FileModelTreeService = (function () {
                function FileModelTreeService() {
                    this.fileModelNodeTree = new Map();
                    this.cardinalityExpressions = new Map();
                    this.subjectTreeStateNotifications = new Subject_1.Subject();
                    this.subjectTreeNotifications = new Subject_1.Subject();
                    this.subjectFileItemNotifications = new Subject_1.Subject();
                }
                FileModelTreeService.prototype.validateModel = function () {
                    //When this method is implemented, it will confirm that the sturcture of the tree is correct;
                    //this is not just an intellectual exercise; the findFileModelNode() method (and probably others)
                    //make important assumptions about the structure of the model. These are the assumptions:
                    // 0) There is one, and only one, FileModelNode per EntityType and CvFilter value
                    // there are a bunch of other rules pertaining to how tree nodes are associated with FileModelNodes,
                    // but since that's in the presentation department, you'll see that over there in in the status tree.
                    return true;
                };
                FileModelTreeService.prototype.getFileItemsFromModel = function (fileModelNodes) {
                    var _this = this;
                    var returnVal = [];
                    fileModelNodes.forEach(function (currentModelNode) {
                        if (currentModelNode.getChildren().length > 0) {
                            var childFileItems = _this.getFileItemsFromModel(currentModelNode.getChildren());
                            returnVal = returnVal.concat(childFileItems);
                        }
                        else {
                            returnVal = returnVal.concat(currentModelNode.getFileItems());
                        }
                    });
                    return returnVal;
                }; //
                FileModelTreeService.prototype.getFileModelNodes = function (gobiiExtractFilterType) {
                    if (this.fileModelNodeTree.size === 0) {
                        // ************ SET UP VALIDATORS
                        this.cardinalityExpressions.set(file_model_node_1.CardinalityType.ZERO_OR_MORE, new cardinality_expression_1.CardinalityExpression(file_model_node_1.CardinalityType.ZERO_OR_MORE, function (n) {
                            return n > -1;
                        }, "There must be zero or more items"));
                        this.cardinalityExpressions.set(file_model_node_1.CardinalityType.ZERO_OR_ONE, new cardinality_expression_1.CardinalityExpression(file_model_node_1.CardinalityType.ZERO_OR_ONE, function (n) {
                            return n < 2;
                        }, "There must be no more than one items "));
                        this.cardinalityExpressions.set(file_model_node_1.CardinalityType.ONE_ONLY, new cardinality_expression_1.CardinalityExpression(file_model_node_1.CardinalityType.ONE_ONLY, function (n) {
                            return n == 1;
                        }, "There must be exactly one item"));
                        this.cardinalityExpressions.set(file_model_node_1.CardinalityType.MORE_THAN_ONE, new cardinality_expression_1.CardinalityExpression(file_model_node_1.CardinalityType.MORE_THAN_ONE, function (n) {
                            return n > 1;
                        }, "There must more than one item"));
                        this.cardinalityExpressions.set(file_model_node_1.CardinalityType.ONE_OR_MORE, new cardinality_expression_1.CardinalityExpression(file_model_node_1.CardinalityType.ONE_OR_MORE, function (n) {
                            return n >= 1;
                        }, "There must be one or more items"));
                        // **** FOR ALL EXTRACTION TYPES **********************************************************************
                        // **** THESE ARE ALL ROOT LEVEL NODES
                        var submissionItemsForAll = [];
                        submissionItemsForAll.push(file_model_node_1.FileModelNode.build(file_model_node_1.ExtractorItemType.JOB_ID, null)
                            .setCategoryType(file_model_node_1.ExtractorCategoryType.LEAF)
                            .setCategoryName(entity_labels_1.Labels.instance().treeExtractorTypeLabels[file_model_node_1.ExtractorItemType.JOB_ID])
                            .setCardinality(file_model_node_1.CardinalityType.ONE_ONLY)
                            .setRequired(true));
                        submissionItemsForAll.push(file_model_node_1.FileModelNode.build(file_model_node_1.ExtractorItemType.ENTITY, null)
                            .setCategoryType(file_model_node_1.ExtractorCategoryType.LEAF)
                            .setEntityType(type_entity_1.EntityType.Contacts)
                            .setEntitySubType(type_entity_1.EntitySubType.CONTACT_SUBMITED_BY)
                            .setEntityName(entity_labels_1.Labels.instance().entitySubtypeNodeLabels[type_entity_1.EntitySubType.CONTACT_SUBMITED_BY])
                            .setCardinality(file_model_node_1.CardinalityType.ONE_ONLY)
                            .setRequired(true));
                        submissionItemsForAll.push(file_model_node_1.FileModelNode.build(file_model_node_1.ExtractorItemType.EXPORT_FORMAT, null)
                            .setCategoryType(file_model_node_1.ExtractorCategoryType.LEAF)
                            .setCategoryName(entity_labels_1.Labels.instance().treeExtractorTypeLabels[file_model_node_1.ExtractorItemType.EXPORT_FORMAT])
                            .setEntityName(entity_labels_1.Labels.instance().treeExtractorTypeLabels[file_model_node_1.ExtractorItemType.EXPORT_FORMAT])
                            .setCardinality(file_model_node_1.CardinalityType.ONE_ONLY)
                            .setRequired(true));
                        submissionItemsForAll.push(file_model_node_1.FileModelNode.build(file_model_node_1.ExtractorItemType.ENTITY, null)
                            .setCategoryType(file_model_node_1.ExtractorCategoryType.LEAF)
                            .setEntityType(type_entity_1.EntityType.Mapsets)
                            .setEntityName(entity_labels_1.Labels.instance().entityNodeLabels[type_entity_1.EntityType.Mapsets])
                            .setCardinality(file_model_node_1.CardinalityType.ZERO_OR_ONE)
                            .setRequired(false));
                        // **** SET UP EXTRACT BY DATASET  **********************************************************************
                        // -- Data set type
                        var submissionItemsForDataSet = [];
                        submissionItemsForDataSet = submissionItemsForDataSet.concat(submissionItemsForAll);
                        submissionItemsForDataSet.push(
                        //                FileModelNode.build(ExtractorItemType.CATEGORY, null)
                        file_model_node_1.FileModelNode.build(file_model_node_1.ExtractorItemType.ENTITY, null)
                            .setCategoryType(file_model_node_1.ExtractorCategoryType.CONTAINER)
                            .setEntityType(type_entity_1.EntityType.DataSets)
                            .setEntityName(entity_labels_1.Labels.instance().entityNodeLabels[type_entity_1.EntityType.DataSets])
                            .setCardinality(file_model_node_1.CardinalityType.ONE_OR_MORE)
                            .setRequired(false));
                        this.fileModelNodeTree.set(type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET, submissionItemsForDataSet);
                        // **** SET UP EXTRACT BY SAMPLES  **********************************************************************
                        // -- Data set type
                        var submissionItemsForBySample = [];
                        submissionItemsForBySample = submissionItemsForBySample.concat(submissionItemsForAll);
                        submissionItemsForBySample.push(file_model_node_1.FileModelNode.build(file_model_node_1.ExtractorItemType.ENTITY, null)
                            .setCategoryType(file_model_node_1.ExtractorCategoryType.LEAF)
                            .setEntityType(type_entity_1.EntityType.CvTerms)
                            .setCvFilterType(cv_filter_type_1.CvFilterType.DATASET_TYPE)
                            .setEntityName(entity_labels_1.Labels.instance().cvFilterNodeLabels[cv_filter_type_1.CvFilterType.DATASET_TYPE])
                            .setCardinality(file_model_node_1.CardinalityType.ONE_ONLY)
                            .setRequired(false));
                        // -- Sample List Type
                        submissionItemsForBySample.push(file_model_node_1.FileModelNode.build(file_model_node_1.ExtractorItemType.SAMPLE_LIST_TYPE, null)
                            .setCategoryType(file_model_node_1.ExtractorCategoryType.LEAF)
                            .setEntityName(entity_labels_1.Labels.instance().treeExtractorTypeLabels[file_model_node_1.ExtractorItemType.SAMPLE_LIST_TYPE])
                            .setCategoryName(entity_labels_1.Labels.instance().treeExtractorTypeLabels[file_model_node_1.ExtractorItemType.SAMPLE_LIST_TYPE])
                            .setCardinality(file_model_node_1.CardinalityType.ONE_ONLY)
                            .setRequired(true));
                        // -- Platforms
                        submissionItemsForBySample.push(file_model_node_1.FileModelNode.build(file_model_node_1.ExtractorItemType.ENTITY, null)
                            .setCategoryType(file_model_node_1.ExtractorCategoryType.CONTAINER)
                            .setEntityType(type_entity_1.EntityType.Platforms)
                            .setEntityName(entity_labels_1.Labels.instance().entityNodeLabels[type_entity_1.EntityType.Platforms])
                            .setCardinality(file_model_node_1.CardinalityType.ZERO_OR_MORE)
                            .setRequired(false));
                        // -- Samples Criteria
                        var currentParent = null;
                        submissionItemsForBySample
                            .push(currentParent =
                            file_model_node_1.FileModelNode.build(file_model_node_1.ExtractorItemType.ENTITY, null)
                                .setCategoryType(file_model_node_1.ExtractorCategoryType.CONTAINER)
                                .setEntityName("Sample Crieria")
                                .setCardinality(file_model_node_1.CardinalityType.ONE_OR_MORE)
                                .setAlternatePeerTypes([type_entity_1.EntityType.Projects, type_entity_1.EntityType.Contacts])
                                .setRequired(false)
                                .addChild(file_model_node_1.FileModelNode.build(file_model_node_1.ExtractorItemType.ENTITY, currentParent)
                                .setCategoryType(file_model_node_1.ExtractorCategoryType.LEAF)
                                .setEntityType(type_entity_1.EntityType.Contacts)
                                .setEntitySubType(type_entity_1.EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR)
                                .setEntityName(entity_labels_1.Labels.instance().entitySubtypeNodeLabels[type_entity_1.EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR])
                                .setCardinality(file_model_node_1.CardinalityType.ONE_ONLY)
                                .setRequired(false))
                                .addChild(file_model_node_1.FileModelNode.build(file_model_node_1.ExtractorItemType.ENTITY, currentParent)
                                .setCategoryType(file_model_node_1.ExtractorCategoryType.LEAF)
                                .setEntityType(type_entity_1.EntityType.Projects)
                                .setEntityName(entity_labels_1.Labels.instance().entityNodeLabels[type_entity_1.EntityType.Projects])
                                .setCardinality(file_model_node_1.CardinalityType.ONE_OR_MORE)
                                .setRequired(false)).addChild(file_model_node_1.FileModelNode.build(file_model_node_1.ExtractorItemType.SAMPLE_FILE, currentParent)
                                .setCategoryType(file_model_node_1.ExtractorCategoryType.LEAF)
                                .setEntityName(entity_labels_1.Labels.instance().treeExtractorTypeLabels[file_model_node_1.ExtractorItemType.SAMPLE_FILE])
                                .setCategoryName(entity_labels_1.Labels.instance().treeExtractorTypeLabels[file_model_node_1.ExtractorItemType.SAMPLE_FILE])
                                .setCardinality(file_model_node_1.CardinalityType.ONE_ONLY)
                                .setRequired(false)).addChild(file_model_node_1.FileModelNode.build(file_model_node_1.ExtractorItemType.SAMPLE_LIST_ITEM, currentParent)
                                .setCategoryType(file_model_node_1.ExtractorCategoryType.CONTAINER)
                                .setEntityName(entity_labels_1.Labels.instance().treeExtractorTypeLabels[file_model_node_1.ExtractorItemType.SAMPLE_LIST_ITEM])
                                .setCategoryName(entity_labels_1.Labels.instance().treeExtractorTypeLabels[file_model_node_1.ExtractorItemType.SAMPLE_LIST_ITEM])
                                .setCardinality(file_model_node_1.CardinalityType.ONE_OR_MORE)
                                .setRequired(false)));
                        this.fileModelNodeTree
                            .set(type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE, submissionItemsForBySample);
                        // **** SET UP EXTRACT BY MARKERS  **********************************************************************
                        var submissionItemsForByMarkers = [];
                        submissionItemsForByMarkers = submissionItemsForByMarkers.concat(submissionItemsForAll);
                        submissionItemsForByMarkers.push(file_model_node_1.FileModelNode.build(file_model_node_1.ExtractorItemType.ENTITY, null)
                            .setCategoryType(file_model_node_1.ExtractorCategoryType.LEAF)
                            .setEntityType(type_entity_1.EntityType.CvTerms)
                            .setCvFilterType(cv_filter_type_1.CvFilterType.DATASET_TYPE)
                            .setEntityName(entity_labels_1.Labels.instance().cvFilterNodeLabels[cv_filter_type_1.CvFilterType.DATASET_TYPE])
                            .setCardinality(file_model_node_1.CardinalityType.ONE_ONLY)
                            .setRequired(false));
                        // the validation algorithm effectively OR's the children: thus the children
                        // are ONE_OR_MORE
                        submissionItemsForByMarkers
                            .push(currentParent =
                            file_model_node_1.FileModelNode.build(file_model_node_1.ExtractorItemType.ENTITY, null)
                                .setCategoryType(file_model_node_1.ExtractorCategoryType.CONTAINER)
                                .setEntityName("Markers Crieria")
                                .setCardinality(file_model_node_1.CardinalityType.ONE_OR_MORE)
                                .setAlternatePeerTypes([type_entity_1.EntityType.Platforms])
                                .setRequired(true)
                                .addChild(file_model_node_1.FileModelNode.build(file_model_node_1.ExtractorItemType.ENTITY, currentParent)
                                .setCategoryType(file_model_node_1.ExtractorCategoryType.CONTAINER)
                                .setEntityType(type_entity_1.EntityType.Platforms)
                                .setEntityName(entity_labels_1.Labels.instance().entityNodeLabels[type_entity_1.EntityType.Platforms])
                                .setCardinality(file_model_node_1.CardinalityType.ONE_OR_MORE)
                                .setRequired(false)).addChild(file_model_node_1.FileModelNode.build(file_model_node_1.ExtractorItemType.MARKER_FILE, currentParent)
                                .setCategoryType(file_model_node_1.ExtractorCategoryType.LEAF)
                                .setEntityName(entity_labels_1.Labels.instance().treeExtractorTypeLabels[file_model_node_1.ExtractorItemType.MARKER_FILE])
                                .setCategoryName(entity_labels_1.Labels.instance().treeExtractorTypeLabels[file_model_node_1.ExtractorItemType.MARKER_FILE])
                                .setCardinality(file_model_node_1.CardinalityType.ONE_OR_MORE)
                                .setRequired(false)).addChild(file_model_node_1.FileModelNode.build(file_model_node_1.ExtractorItemType.MARKER_LIST_ITEM, currentParent)
                                .setCategoryType(file_model_node_1.ExtractorCategoryType.CONTAINER)
                                .setEntityName(entity_labels_1.Labels.instance().treeExtractorTypeLabels[file_model_node_1.ExtractorItemType.MARKER_LIST_ITEM])
                                .setCategoryName(entity_labels_1.Labels.instance().treeExtractorTypeLabels[file_model_node_1.ExtractorItemType.MARKER_LIST_ITEM])
                                .setCardinality(file_model_node_1.CardinalityType.ONE_OR_MORE)
                                .setRequired(false)).addChild(file_model_node_1.FileModelNode.build(file_model_node_1.ExtractorItemType.ENTITY, currentParent)
                                .setCategoryType(file_model_node_1.ExtractorCategoryType.CONTAINER)
                                .setEntityType(type_entity_1.EntityType.MarkerGroups)
                                .setEntityName(entity_labels_1.Labels.instance().entityNodeLabels[type_entity_1.EntityType.MarkerGroups])
                                .setCardinality(file_model_node_1.CardinalityType.ONE_OR_MORE)
                                .setRequired(false)));
                        this.fileModelNodeTree
                            .set(type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER, submissionItemsForByMarkers);
                        if (this.validateModel() == true) {
                            this.subjectTreeStateNotifications.next(new tree_status_notification_1.TreeStatusNotification(type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN, file_model_tree_event_1.FileModelState.READY, null));
                        }
                        else {
                        }
                    }
                    return this.fileModelNodeTree.get(gobiiExtractFilterType);
                };
                FileModelTreeService.prototype.validateTree = function (gobiiExtractFilterType) {
                    var _this = this;
                    var returnVal = [];
                    var fileModelNodes = this.fileModelNodeTree.get(gobiiExtractFilterType);
                    // we only iterate the first level because we just happen to know, for this purpose,
                    // that the tree only has two levels; if we add more, this must change
                    fileModelNodes.forEach(function (currentFileModelNode) {
                        if (currentFileModelNode.getCategoryType() === file_model_node_1.ExtractorCategoryType.LEAF ||
                            (currentFileModelNode.getCategoryType() === file_model_node_1.ExtractorCategoryType.CONTAINER
                                && currentFileModelNode.getChildren().length === 0)) {
                            var currentCardinalityExpression = _this.cardinalityExpressions
                                .get(currentFileModelNode.getCardinality());
                            var fileItemCount = currentFileModelNode.getFileItems().length;
                            if (!currentCardinalityExpression.isValid(fileItemCount)) {
                                returnVal.push(new model_tree_validation_error_1.ModelTreeValidationError(currentCardinalityExpression.message, currentFileModelNode));
                            }
                        }
                        else if (currentFileModelNode.getCategoryType() === file_model_node_1.ExtractorCategoryType.CONTAINER) {
                            var atLeastOneIsSatisfied = false;
                            for (var idx = 0; (idx < currentFileModelNode.getChildren().length) && (atLeastOneIsSatisfied === false); idx++) {
                                var currentFileModelNodeChild = currentFileModelNode.getChildren()[idx];
                                var currentChildCardinalityExpression = _this.cardinalityExpressions
                                    .get(currentFileModelNodeChild.getCardinality());
                                var childFileItemCount = currentFileModelNodeChild.getFileItems().length;
                                atLeastOneIsSatisfied = currentChildCardinalityExpression.isValid(childFileItemCount);
                                var validatorIsWRONG = atLeastOneIsSatisfied;
                            }
                            if (atLeastOneIsSatisfied === false) {
                                currentFileModelNode.getChildren().forEach(function (currentChildNode) {
                                    var currentChildCardinalityExpression = _this.cardinalityExpressions
                                        .get(currentChildNode.getCardinality());
                                    returnVal.push(new model_tree_validation_error_1.ModelTreeValidationError(currentChildCardinalityExpression.message, currentChildNode));
                                });
                            } // if none of the required children are present
                        }
                        else {
                            ; // we don't have a type like this yet?
                        } // if-else the model node has child model nodes
                    }); // for each first level node
                    return returnVal;
                };
                FileModelTreeService.prototype.processNotification = function (fileItem) {
                    var returnVal = new file_model_tree_event_1.FileModelTreeEvent(fileItem, null, null, null);
                    return returnVal;
                };
                FileModelTreeService.prototype.mutate = function (fileItem) {
                    var returnVal = null;
                    if (fileItem.getGobiiExtractFilterType() != type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN) {
                        var fileModelNode = this.findFileModelNode(fileItem.getGobiiExtractFilterType(), fileItem);
                        if (fileModelNode != null) {
                            if (fileItem.getProcessType() === type_process_1.ProcessType.CREATE || fileItem.getProcessType() === type_process_1.ProcessType.UPDATE) {
                                this.placeNodeInModel(fileModelNode, fileItem);
                                // this condition is going to required further thought . . .
                                // you have to if cardiality of aprent is ONE_OR_MORE, then
                                // you have to check for siblings. Not sure how complex we
                                // need to make this
                                if ((fileModelNode.getCardinality() === file_model_node_1.CardinalityType.ONE_OR_MORE
                                    || fileModelNode.getCardinality() === file_model_node_1.CardinalityType.ONE_ONLY
                                    || fileModelNode.getCardinality() === file_model_node_1.CardinalityType.MORE_THAN_ONE)
                                    && fileModelNode.getCategoryType() != file_model_node_1.ExtractorCategoryType.CONTAINER) {
                                    if (fileModelNode.getParent() == null) {
                                        fileItem.setRequired(true);
                                    }
                                }
                                returnVal = new file_model_tree_event_1.FileModelTreeEvent(fileItem, fileModelNode, file_model_tree_event_1.FileModelState.SUBMISSION_INCOMPLETE, null);
                            }
                            else if (fileItem.getProcessType() === type_process_1.ProcessType.DELETE) {
                                if (this.removeFromModel(fileModelNode, fileItem)) {
                                    returnVal = new file_model_tree_event_1.FileModelTreeEvent(fileItem, fileModelNode, file_model_tree_event_1.FileModelState.SUBMISSION_INCOMPLETE, null);
                                }
                                else {
                                    var message = "The specified file item could not be removed because it does not exist in the model";
                                    if (fileModelNode && fileModelNode.getCategoryName()) {
                                        message += "; model category: " + fileModelNode.getCategoryName();
                                    }
                                    if (fileItem && fileItem.getItemName()) {
                                        message += "; fileitem name: " + fileItem.getItemName();
                                    }
                                    returnVal = new file_model_tree_event_1.FileModelTreeEvent(fileItem, fileModelNode, file_model_tree_event_1.FileModelState.ERROR, message);
                                }
                            }
                            else {
                                returnVal = new file_model_tree_event_1.FileModelTreeEvent(fileItem, null, file_model_tree_event_1.FileModelState.ERROR, "Unhandled file item process type: " + type_process_1.ProcessType[fileItem.getProcessType()]);
                            }
                        }
                        else {
                            // this condition deals with a design flaw: not all entity types are handled for all
                            // extract filter types; this is not an error, but we want to validate that the entity
                            // does match at least one exractor type; if it does, then we mark it as a mismatch
                            // and subscribers know to ignore this type of event; in the future, if we want the
                            // status tree to maintain state across extraction filter types, this could be useful
                            var remainingExtractorTypes = [type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET,
                                type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE,
                                type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER];
                            remainingExtractorTypes.splice(remainingExtractorTypes.indexOf(fileItem.getGobiiExtractFilterType()), 1);
                            var fileModelNode_1 = null;
                            for (var idx = 0; idx < remainingExtractorTypes.length && fileModelNode_1 == null; idx++) {
                                var currentGobiiExtractFilterType = remainingExtractorTypes[idx];
                                fileModelNode_1 = this.findFileModelNode(currentGobiiExtractFilterType, fileItem);
                                if (fileModelNode_1 != null) {
                                    fileItem.setGobiiExtractFilterType(currentGobiiExtractFilterType);
                                }
                            }
                            if (fileModelNode_1 != null) {
                                returnVal = new file_model_tree_event_1.FileModelTreeEvent(fileItem, fileModelNode_1, file_model_tree_event_1.FileModelState.MISMATCHED_EXTRACTOR_FILTER_TYPE, null);
                            }
                            else {
                                returnVal = new file_model_tree_event_1.FileModelTreeEvent(fileItem, null, file_model_tree_event_1.FileModelState.ERROR, "Unable to find a FileModelNode for fileItem in any extractor type tree");
                            }
                        } // if else found a file mode node for the file item's specified extractor filter type
                    }
                    else {
                        returnVal = new file_model_tree_event_1.FileModelTreeEvent(fileItem, null, file_model_tree_event_1.FileModelState.ERROR, "An invalid extract filter type was specified");
                    } // if-else extractor filter type is not set
                    return returnVal;
                };
                FileModelTreeService.prototype.findFileModelNode = function (gobiiExtractFilterType, fileItem) {
                    var fileModelNodes = this.getFileModelNodes(gobiiExtractFilterType);
                    var returnVal = null;
                    for (var idx = 0; (idx < fileModelNodes.length) && (returnVal == null); idx++) {
                        var currentTemplate = fileModelNodes[idx];
                        returnVal = this.findTemplateByCriteria(currentTemplate, fileItem.getExtractorItemType(), fileItem.getEntityType(), fileItem.getEntitySubType(), fileItem.getCvFilterType());
                    }
                    return returnVal;
                };
                FileModelTreeService.prototype.findFileModelNodeByUniqueId = function (fileModelNodes, fileModelNodeUniqueId) {
                    var returnVal = null;
                    for (var idx = 0; (returnVal == null) && (idx < fileModelNodes.length); idx++) {
                        var currentNode = fileModelNodes[idx];
                        if (currentNode.getFileModelNodeUniqueId() === fileModelNodeUniqueId) {
                            returnVal = currentNode;
                        }
                        else {
                            returnVal = this.findFileModelNodeByUniqueId(currentNode.getChildren(), fileModelNodeUniqueId);
                        }
                    }
                    return returnVal;
                };
                FileModelTreeService.prototype.findTemplateByCriteria = function (fileModelNode, extractorItemType, entityType, entitySubType, cvFilterType) {
                    var returnVal = null;
                    if (fileModelNode.getChildren() != null) {
                        for (var idx = 0; (idx < fileModelNode.getChildren().length) && (returnVal == null); idx++) {
                            var currentTemplate = fileModelNode.getChildren()[idx];
                            returnVal = this.findTemplateByCriteria(currentTemplate, extractorItemType, entityType, entitySubType, cvFilterType);
                        }
                    }
                    if (returnVal === null) {
                        if (extractorItemType == fileModelNode.getItemType()
                            && entityType == fileModelNode.getEntityType()
                            && entitySubType == fileModelNode.getEntitySubType()
                            && cvFilterType == fileModelNode.getCvFilterType()) {
                            returnVal = fileModelNode;
                        }
                    }
                    return returnVal;
                };
                FileModelTreeService.prototype.placeNodeInModel = function (fileModelNode, fileItem) {
                    if (fileModelNode.getCategoryType() === file_model_node_1.ExtractorCategoryType.LEAF) {
                        // a leaf should never have more than one
                        if (fileModelNode.getFileItems().length === 0) {
                            fileModelNode.getFileItems().push(fileItem);
                        }
                        else {
                            fileModelNode.getFileItems()[0] = fileItem;
                        }
                    }
                    else if (fileModelNode.getCategoryType() === file_model_node_1.ExtractorCategoryType.CONTAINER) {
                        var existingItems = fileModelNode.getFileItems().filter(function (item) {
                            return item.getFileItemUniqueId() === fileItem.getFileItemUniqueId();
                        });
                        if (existingItems.length === 0) {
                            fileModelNode.getFileItems().push(fileItem);
                        }
                        else {
                            var idx = fileModelNode.getFileItems().indexOf(existingItems[0]);
                            fileModelNode.getFileItems()[idx] = fileItem;
                        }
                    }
                    else {
                    }
                }; //
                FileModelTreeService.prototype.removeFromModel = function (fileModelNode, fileItem) {
                    var returnVal = false;
                    if (fileModelNode.getCategoryType() === file_model_node_1.ExtractorCategoryType.LEAF) {
                        // a leaf should never have more than one
                        if (fileModelNode.getFileItems()
                            && fileModelNode.getFileItems().length > 0
                            && fileModelNode.getFileItems()[0].getFileItemUniqueId() === fileItem.getFileItemUniqueId()) {
                            returnVal = (fileModelNode.getFileItems().splice(0, 1)).length > 0;
                        }
                    }
                    else if (fileModelNode.getCategoryType() === file_model_node_1.ExtractorCategoryType.CONTAINER) {
                        var existingItem = fileModelNode.getFileItems().find(function (item) {
                            return item.getFileItemUniqueId() === fileItem.getFileItemUniqueId();
                        });
                        var idxOfItemToRemove = fileModelNode.getFileItems().indexOf(existingItem);
                        returnVal = (fileModelNode.getFileItems().splice(idxOfItemToRemove, 1)).length > 0;
                    }
                    else {
                    }
                    return returnVal;
                };
                FileModelTreeService.prototype.treeStateNotifications = function () {
                    return this.subjectTreeStateNotifications;
                };
                FileModelTreeService.prototype.treeNotifications = function () {
                    return this.subjectTreeNotifications;
                };
                FileModelTreeService.prototype.fileItemNotifications = function () {
                    return this.subjectFileItemNotifications;
                };
                FileModelTreeService.prototype.put = function (fileItem) {
                    var _this = this;
                    return Observable_1.Observable.create(function (observer) {
                        var foo = "foo";
                        var fileTreeEvent = null;
                        if (fileItem.getProcessType() !== type_process_1.ProcessType.NOTIFY) {
                            fileTreeEvent = _this.mutate(fileItem);
                        }
                        else {
                            fileTreeEvent = _this.processNotification(fileItem);
                        }
                        if (fileTreeEvent.fileModelState != file_model_tree_event_1.FileModelState.ERROR) {
                            observer.next(fileTreeEvent);
                            observer.complete();
                            _this.subjectTreeNotifications.next(fileTreeEvent);
                            _this.subjectFileItemNotifications.next(fileTreeEvent.fileItem);
                            var modelTreeValidationErrors = _this.validateTree(fileItem.getGobiiExtractFilterType());
                            var fileModelState = (modelTreeValidationErrors.length === 0) ? file_model_tree_event_1.FileModelState.SUBMISSION_READY : file_model_tree_event_1.FileModelState.READY;
                            var treeStatusNotification = new tree_status_notification_1.TreeStatusNotification(fileItem.getGobiiExtractFilterType(), fileModelState, modelTreeValidationErrors);
                            _this.subjectTreeStateNotifications.next(treeStatusNotification);
                        }
                        else {
                            var headerStatusMessage = new dto_header_status_message_1.HeaderStatusMessage("Error mutating file item in file model tree service: "
                                + fileTreeEvent.message
                                + " processing file item: "
                                + JSON.stringify(fileItem, null, '\t'), null, null);
                            observer.error(headerStatusMessage);
                        }
                    });
                };
                FileModelTreeService.prototype.getFileModel = function (gobiiExtractFilterType) {
                    var _this = this;
                    return Observable_1.Observable.create(function (observer) {
                        var nodesForFilterType = _this.getFileModelNodes(gobiiExtractFilterType);
                        observer.next(nodesForFilterType);
                        observer.complete();
                    });
                };
                FileModelTreeService.prototype.getFileItems = function (gobiiExtractFilterType) {
                    var _this = this;
                    return Observable_1.Observable.create(function (observer) {
                        var nodesForFilterType = _this.getFileModelNodes(gobiiExtractFilterType);
                        var fileItemsForExtractorFilterType = _this.getFileItemsFromModel(nodesForFilterType);
                        observer.next(fileItemsForExtractorFilterType);
                        observer.complete();
                    });
                };
                FileModelTreeService.prototype.getFileModelNode = function (gobiiExtractFilterType, fileModelNodeUniqueId) {
                    var _this = this;
                    return Observable_1.Observable.create(function (observer) {
                        var fileModelNodes = _this.fileModelNodeTree.get(gobiiExtractFilterType);
                        var fileModeNode = _this.findFileModelNodeByUniqueId(fileModelNodes, fileModelNodeUniqueId);
                        observer.next(fileModeNode);
                        observer.complete();
                    });
                };
                FileModelTreeService.prototype.getTreeState = function (gobiiExtractFilterType) {
                    var _this = this;
                    return Observable_1.Observable.create(function (observer) {
                        var modelTreeValidationErrors = _this.validateTree(gobiiExtractFilterType);
                        var fileModelState = (modelTreeValidationErrors.length === 0) ? file_model_tree_event_1.FileModelState.SUBMISSION_READY : file_model_tree_event_1.FileModelState.READY;
                        var treeStatusNotification = new tree_status_notification_1.TreeStatusNotification(gobiiExtractFilterType, fileModelState, modelTreeValidationErrors);
                        observer.next(treeStatusNotification);
                        observer.complete();
                    });
                };
                return FileModelTreeService;
            }());
            FileModelTreeService = __decorate([
                core_1.Injectable(),
                __metadata("design:paramtypes", [])
            ], FileModelTreeService);
            exports_1("FileModelTreeService", FileModelTreeService);
        }
    };
});
//# sourceMappingURL=file-model-tree-service.js.map