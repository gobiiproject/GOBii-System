System.register(["@angular/core", "../../model/file-model-tree-event", "../../model/file-model-node", "../../model/type-extractor-filter", "../../model/type-entity", "../../model/cv-filter-type", "rxjs/Subject", "rxjs/Observable", "../../model/type-process", "../../views/entity-labels", "../../model/dto-header-response", "../../model/dto-header-status-message", "../../model/tree-status-notification"], function (exports_1, context_1) {
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
    var core_1, file_model_tree_event_1, file_model_node_1, type_extractor_filter_1, type_entity_1, cv_filter_type_1, Subject_1, Observable_1, type_process_1, entity_labels_1, dto_header_response_1, dto_header_status_message_1, tree_status_notification_1, FileModelTreeService;
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
            function (dto_header_response_1_1) {
                dto_header_response_1 = dto_header_response_1_1;
            },
            function (dto_header_status_message_1_1) {
                dto_header_status_message_1 = dto_header_status_message_1_1;
            },
            function (tree_status_notification_1_1) {
                tree_status_notification_1 = tree_status_notification_1_1;
            }
        ],
        execute: function () {
            FileModelTreeService = (function () {
                function FileModelTreeService() {
                    this.fileModelNodeTree = new Map();
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
                        // **** FOR ALL EXTRACTION TYPES **********************************************************************
                        // **** THESE ARE ALL ROOT LEVEL NODES
                        var submissionItemsForAll = [];
                        submissionItemsForAll.push(file_model_node_1.FileModelNode.build(file_model_node_1.ExtractorItemType.ENTITY, null)
                            .setCategoryType(file_model_node_1.ExtractorCategoryType.LEAF)
                            .setEntityType(type_entity_1.EntityType.Contacts)
                            .setEntityName(entity_labels_1.Labels.instance().entitySubtypeNodeLabels[type_entity_1.EntitySubType.CONTACT_SUBMITED_BY])
                            .setCardinality(file_model_node_1.CardinalityType.ONE_ONLY));
                        submissionItemsForAll.push(file_model_node_1.FileModelNode.build(file_model_node_1.ExtractorItemType.EXPORT_FORMAT, null)
                            .setCategoryType(file_model_node_1.ExtractorCategoryType.LEAF)
                            .setCategoryName(entity_labels_1.Labels.instance().treeExtractorTypeLabels[file_model_node_1.ExtractorItemType.EXPORT_FORMAT])
                            .setEntityName(entity_labels_1.Labels.instance().treeExtractorTypeLabels[file_model_node_1.ExtractorItemType.EXPORT_FORMAT])
                            .setCardinality(file_model_node_1.CardinalityType.ONE_ONLY));
                        submissionItemsForAll.push(file_model_node_1.FileModelNode.build(file_model_node_1.ExtractorItemType.CATEGORY, null)
                            .setCategoryType(file_model_node_1.ExtractorCategoryType.ENTITY_CONTAINER)
                            .setEntityType(type_entity_1.EntityType.Mapsets)
                            .setCategoryName(entity_labels_1.Labels.instance().entityNodeLabels[type_entity_1.EntityType.Mapsets])
                            .setCardinality(file_model_node_1.CardinalityType.ZERO_OR_ONE));
                        // **** SET UP EXTRACT BY DATASET  **********************************************************************
                        // -- Data set type
                        var submissionItemsForDataSet = [];
                        submissionItemsForDataSet = submissionItemsForDataSet.concat(submissionItemsForAll);
                        submissionItemsForDataSet.push(file_model_node_1.FileModelNode.build(file_model_node_1.ExtractorItemType.CATEGORY, null)
                            .setCategoryType(file_model_node_1.ExtractorCategoryType.ENTITY_CONTAINER)
                            .setEntityType(type_entity_1.EntityType.DataSets)
                            .setCategoryName(entity_labels_1.Labels.instance().entityNodeLabels[type_entity_1.EntityType.DataSets])
                            .setCardinality(file_model_node_1.CardinalityType.ONE_OR_MORE));
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
                            .setCardinality(file_model_node_1.CardinalityType.ONE_ONLY));
                        // -- Platforms
                        var currentParent = null;
                        submissionItemsForBySample.push(file_model_node_1.FileModelNode.build(file_model_node_1.ExtractorItemType.CATEGORY, null)
                            .setCategoryType(file_model_node_1.ExtractorCategoryType.ENTITY_CONTAINER)
                            .setEntityType(type_entity_1.EntityType.Platforms)
                            .setCategoryName(entity_labels_1.Labels.instance().entityNodeLabels[type_entity_1.EntityType.Platforms])
                            .setCardinality(file_model_node_1.CardinalityType.ZERO_OR_MORE));
                        // -- Samples
                        submissionItemsForBySample
                            .push(currentParent =
                            file_model_node_1.FileModelNode.build(file_model_node_1.ExtractorItemType.CATEGORY, null)
                                .setCategoryType(file_model_node_1.ExtractorCategoryType.MODEL_CONTAINER)
                                .setCategoryName("Sample Crieria")
                                .setCardinality(file_model_node_1.CardinalityType.ONE_OR_MORE)
                                .setAlternatePeerTypes([type_entity_1.EntityType.Projects, type_entity_1.EntityType.Contacts])
                                .addChild(file_model_node_1.FileModelNode.build(file_model_node_1.ExtractorItemType.ENTITY, currentParent)
                                .setCategoryType(file_model_node_1.ExtractorCategoryType.LEAF)
                                .setEntityType(type_entity_1.EntityType.Contacts)
                                .setEntityName(entity_labels_1.Labels.instance().entitySubtypeNodeLabels[type_entity_1.EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR])
                                .setCardinality(file_model_node_1.CardinalityType.ZERO_OR_ONE))
                                .addChild(file_model_node_1.FileModelNode.build(file_model_node_1.ExtractorItemType.ENTITY, currentParent)
                                .setCategoryType(file_model_node_1.ExtractorCategoryType.ENTITY_CONTAINER)
                                .setEntityType(type_entity_1.EntityType.Projects)
                                .setEntityName(entity_labels_1.Labels.instance().entityNodeLabels[type_entity_1.EntityType.Projects])
                                .setCardinality(file_model_node_1.CardinalityType.ZERO_OR_MORE))
                                .addChild(file_model_node_1.FileModelNode.build(file_model_node_1.ExtractorItemType.SAMPLE_LIST, currentParent)
                                .setCategoryType(file_model_node_1.ExtractorCategoryType.CATEGORY_CONTAINER)
                                .setEntityName(entity_labels_1.Labels.instance().treeExtractorTypeLabels[file_model_node_1.ExtractorItemType.SAMPLE_LIST])
                                .setCategoryName(entity_labels_1.Labels.instance().treeExtractorTypeLabels[file_model_node_1.ExtractorItemType.SAMPLE_LIST])
                                .setCardinality(file_model_node_1.CardinalityType.ZERO_OR_MORE)));
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
                            .setCardinality(file_model_node_1.CardinalityType.ONE_ONLY));
                        submissionItemsForByMarkers
                            .push(currentParent =
                            file_model_node_1.FileModelNode.build(file_model_node_1.ExtractorItemType.CATEGORY, null)
                                .setCategoryType(file_model_node_1.ExtractorCategoryType.MODEL_CONTAINER)
                                .setCategoryName("Markers Crieria")
                                .setCardinality(file_model_node_1.CardinalityType.ONE_OR_MORE)
                                .setAlternatePeerTypes([type_entity_1.EntityType.Platforms])
                                .addChild(file_model_node_1.FileModelNode.build(file_model_node_1.ExtractorItemType.ENTITY, currentParent)
                                .setCategoryType(file_model_node_1.ExtractorCategoryType.ENTITY_CONTAINER)
                                .setEntityType(type_entity_1.EntityType.Platforms)
                                .setEntityName(entity_labels_1.Labels.instance().entityNodeLabels[type_entity_1.EntityType.Platforms])
                                .setCardinality(file_model_node_1.CardinalityType.ZERO_OR_MORE))
                                .addChild(file_model_node_1.FileModelNode.build(file_model_node_1.ExtractorItemType.MARKER_LIST, currentParent)
                                .setCategoryType(file_model_node_1.ExtractorCategoryType.CATEGORY_CONTAINER)
                                .setEntityName(entity_labels_1.Labels.instance().treeExtractorTypeLabels[file_model_node_1.ExtractorItemType.MARKER_LIST])
                                .setCategoryName(entity_labels_1.Labels.instance().treeExtractorTypeLabels[file_model_node_1.ExtractorItemType.MARKER_LIST])
                                .setCardinality(file_model_node_1.CardinalityType.ZERO_OR_MORE)));
                        this.fileModelNodeTree
                            .set(type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER, submissionItemsForByMarkers);
                        if (this.validateModel() == true) {
                            this.subjectTreeStateNotifications.next(new tree_status_notification_1.TreeStatusNotification(file_model_tree_event_1.FileModelState.READY, null));
                        }
                        else {
                        }
                    }
                    return this.fileModelNodeTree.get(gobiiExtractFilterType);
                };
                FileModelTreeService.prototype.mutate = function (fileItem) {
                    var returnVal = null;
                    if (fileItem.getGobiiExtractFilterType() != type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN) {
                        var fileModelNode = this.findFileModelNode(fileItem.getGobiiExtractFilterType(), fileItem.getEntityType(), fileItem.getCvFilterType());
                        if (fileItem.getProcessType() === type_process_1.ProcessType.CREATE || fileItem.getProcessType() === type_process_1.ProcessType.UPDATE) {
                            this.placeNodeInModel(fileModelNode, fileItem);
                            // this condition is going to required further thought . . .
                            // you have to if cardiality of aprent is ONE_OR_MORE, then
                            // you have to check for siblings. Not sure how complex we
                            // need to make this
                            if ((fileModelNode.getCardinality() === file_model_node_1.CardinalityType.ONE_OR_MORE
                                || fileModelNode.getCardinality() === file_model_node_1.CardinalityType.ONE_ONLY
                                || fileModelNode.getCardinality() === file_model_node_1.CardinalityType.MORE_THAN_ONE)
                                && fileModelNode.getCategoryType() != file_model_node_1.ExtractorCategoryType.ENTITY_CONTAINER) {
                                if (fileModelNode.getParent() == null) {
                                    fileItem.setRequired(true);
                                }
                            }
                            returnVal = new file_model_tree_event_1.FileModelTreeEvent(fileItem, fileModelNode, file_model_tree_event_1.FileModelState.SUBMISSION_INCOMPLETE, null);
                        }
                        else if (fileItem.getProcessType() === type_process_1.ProcessType.DELETE) {
                            this.removeFromModel(fileModelNode, fileItem);
                            returnVal = new file_model_tree_event_1.FileModelTreeEvent(fileItem, fileModelNode, file_model_tree_event_1.FileModelState.SUBMISSION_INCOMPLETE, null);
                        }
                        else {
                            returnVal = new file_model_tree_event_1.FileModelTreeEvent(fileItem, null, file_model_tree_event_1.FileModelState.ERROR, "Unhandled file item process type: " + type_process_1.ProcessType[fileItem.getProcessType()]);
                        }
                    }
                    else {
                        returnVal = new file_model_tree_event_1.FileModelTreeEvent(fileItem, null, file_model_tree_event_1.FileModelState.ERROR, "An invalid extract filter type was specified");
                    }
                    return returnVal;
                };
                FileModelTreeService.prototype.findFileModelNode = function (gobiiExtractFilterType, entityType, cvFilter) {
                    var fileModelNodes = this.getFileModelNodes(gobiiExtractFilterType);
                    var returnVal = null;
                    for (var idx = 0; (idx < fileModelNodes.length) && (returnVal == null); idx++) {
                        var currentTemplate = fileModelNodes[idx];
                        returnVal = this.findTemplateByCriteria(currentTemplate, entityType, cvFilter);
                    }
                    return returnVal;
                };
                FileModelTreeService.prototype.findTemplateByCriteria = function (fileModelNode, entityType, cvFilterType) {
                    var returnVal = null;
                    if (fileModelNode.getChildren() != null) {
                        for (var idx = 0; (idx < fileModelNode.getChildren().length) && (returnVal == null); idx++) {
                            var currentTemplate = fileModelNode.getChildren()[idx];
                            returnVal = this.findTemplateByCriteria(currentTemplate, entityType, cvFilterType);
                        }
                    }
                    if (returnVal === null) {
                        if (entityType == fileModelNode.getEntityType() && cvFilterType == fileModelNode.getCvFilterType()) {
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
                    else if (fileModelNode.getCategoryType() === file_model_node_1.ExtractorCategoryType.ENTITY_CONTAINER) {
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
                    if (fileModelNode.getCategoryType() === file_model_node_1.ExtractorCategoryType.LEAF) {
                        // a leaf should never have more than one
                        if (fileModelNode.getFileItems()[0].getFileItemUniqueId() === fileItem.getFileItemUniqueId()) {
                            fileModelNode.getFileItems().splice(0, 1);
                        }
                    }
                    else if (fileModelNode.getCategoryType() === file_model_node_1.ExtractorCategoryType.ENTITY_CONTAINER) {
                        var existingItem = fileModelNode.getFileItems().find(function (item) {
                            return item.getFileItemUniqueId() === fileItem.getFileItemUniqueId();
                        });
                        var idxOfItemToRemove = fileModelNode.getFileItems().indexOf(existingItem);
                        fileModelNode.getFileItems().splice(idxOfItemToRemove, 1);
                    }
                    else {
                    }
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
                        var fileTreeEvent = _this.mutate(fileItem);
                        if (fileTreeEvent.fileModelState != file_model_tree_event_1.FileModelState.ERROR) {
                            observer.next(fileTreeEvent);
                            observer.complete();
                            _this.subjectTreeNotifications.next(fileTreeEvent);
                            _this.subjectFileItemNotifications.next(fileTreeEvent.fileItem);
                        }
                        else {
                            var headerResponse = new dto_header_response_1.DtoHeaderResponse(false, [new dto_header_status_message_1.HeaderStatusMessage("Error mutating file item in file model tree service: "
                                    + fileTreeEvent.message
                                    + " processing file item: "
                                    + fileItem.getFileItemUniqueId()
                                    + " (" + (fileItem.getItemName() !== null ? fileItem.getItemName() : "unnamed") + ")", null, null)]);
                            observer.error(headerResponse);
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