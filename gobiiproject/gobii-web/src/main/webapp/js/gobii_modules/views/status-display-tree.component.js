System.register(["@angular/core", "../model/gobii-file-item", "../model/GobiiTreeNode", "../model/type-entity", "../model/type-extractor-filter", "../model/file-model-node", "../model/cv-filter-type", "../services/core/file-model-tree-service", "../model/file-model-tree-event", "../model/type-process", "../model/type-extract-format", "../model/dto-header-status-message", "./entity-labels", "../model/type-event-origin", "../model/type-status-level"], function (exports_1, context_1) {
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
    var core_1, gobii_file_item_1, GobiiTreeNode_1, type_entity_1, type_extractor_filter_1, file_model_node_1, cv_filter_type_1, file_model_tree_service_1, file_model_tree_event_1, type_process_1, type_extract_format_1, dto_header_status_message_1, entity_labels_1, type_event_origin_1, type_status_level_1, StatusDisplayTreeComponent;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (gobii_file_item_1_1) {
                gobii_file_item_1 = gobii_file_item_1_1;
            },
            function (GobiiTreeNode_1_1) {
                GobiiTreeNode_1 = GobiiTreeNode_1_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (file_model_node_1_1) {
                file_model_node_1 = file_model_node_1_1;
            },
            function (cv_filter_type_1_1) {
                cv_filter_type_1 = cv_filter_type_1_1;
            },
            function (file_model_tree_service_1_1) {
                file_model_tree_service_1 = file_model_tree_service_1_1;
            },
            function (file_model_tree_event_1_1) {
                file_model_tree_event_1 = file_model_tree_event_1_1;
            },
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (type_extract_format_1_1) {
                type_extract_format_1 = type_extract_format_1_1;
            },
            function (dto_header_status_message_1_1) {
                dto_header_status_message_1 = dto_header_status_message_1_1;
            },
            function (entity_labels_1_1) {
                entity_labels_1 = entity_labels_1_1;
            },
            function (type_event_origin_1_1) {
                type_event_origin_1 = type_event_origin_1_1;
            },
            function (type_status_level_1_1) {
                type_status_level_1 = type_status_level_1_1;
            }
        ],
        execute: function () {
            StatusDisplayTreeComponent = (function () {
                function StatusDisplayTreeComponent(_fileModelTreeService) {
                    var _this = this;
                    this._fileModelTreeService = _fileModelTreeService;
                    this.containerCollapseThreshold = 10;
                    this.onAddMessage = new core_1.EventEmitter();
                    this.onTreeReady = new core_1.EventEmitter();
                    // *****************************************************************
                    // *********************  TREE NODE DATA STRUCTURES AND EVENTS
                    this.demoTreeNodes = [];
                    this.selectedDemoNodes = [];
                    this.gobiiTreeNodes = [];
                    this.selectedGobiiNodes = [];
                    this.treeIsInitialized = false;
                    // ********************************************************************************
                    // ********************* CHECKBOX (GOBII-SPECIFIC)  NODE DATA STRUCTURES AND EVENTS
                    this.gobiiExtractFilterType = type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN;
                    this.onItemChecked = new core_1.EventEmitter();
                    this.onItemSelected = new core_1.EventEmitter();
                    // has to be in ctor because if you put it in ngOnInit(), there can be ngOnChange events
                    // before ngOnInit() is called.
                    this._fileModelTreeService
                        .treeNotifications()
                        .subscribe(function (fileModelTreeEvent) {
                        if (_this.treeIsInitialized) {
                            if (fileModelTreeEvent.fileModelState != file_model_tree_event_1.FileModelState.MISMATCHED_EXTRACTOR_FILTER_TYPE) {
                                if (fileModelTreeEvent.fileItem.getProcessType() === type_process_1.ProcessType.CREATE
                                    || fileModelTreeEvent.fileItem.getProcessType() === type_process_1.ProcessType.UPDATE) {
                                    _this.placeNodeInTree(fileModelTreeEvent);
                                }
                                else if (fileModelTreeEvent.fileItem.getProcessType() === type_process_1.ProcessType.DELETE) {
                                    _this.removeNodeFromTree(fileModelTreeEvent);
                                }
                                else if (fileModelTreeEvent.fileItem.getProcessType() === type_process_1.ProcessType.NOTIFY) {
                                    if (fileModelTreeEvent.fileItem.getExtractorItemType() === file_model_node_1.ExtractorItemType.CLEAR_TREE) {
                                        _this.clearTree();
                                    }
                                }
                                else {
                                    var headerStatusMessage = new dto_header_status_message_1.HeaderStatusMessage("Error in status display tree processing file item type "
                                        + file_model_node_1.ExtractorItemType[fileModelTreeEvent.fileItem.getExtractorItemType()]
                                        + ": Unknown porcess type: "
                                        + type_process_1.ProcessType[fileModelTreeEvent.fileItem.getProcessType()], null, null);
                                    _this.handleAddStatusMessage(headerStatusMessage);
                                }
                            }
                        }
                        else {
                            var message = "Warning: a fileItem was posted ot the tree before the tree was initialized:  "
                                + entity_labels_1.Labels.instance().treeExtractorTypeLabels[fileModelTreeEvent.fileModelNode.getItemType()];
                            if (fileModelTreeEvent.fileItem && fileModelTreeEvent.fileItem.getItemName()) {
                                message += " for fileItem of name " + fileModelTreeEvent.fileItem.getItemName();
                            }
                            _this.handleAddStatusMessage(new dto_header_status_message_1.HeaderStatusMessage(message, type_status_level_1.StatusLevel.WARNING, null));
                        }
                    });
                }
                StatusDisplayTreeComponent.prototype.handleAddStatusMessage = function (headerStatusMessage) {
                    this.onAddMessage.emit(headerStatusMessage);
                };
                StatusDisplayTreeComponent.prototype.ngOnInit = function () {
                    var foo = "bar";
                    // this.makeDemoTreeNodes();
                    // this.setUpRequredItems();
                };
                StatusDisplayTreeComponent.prototype.findRemovableFileItems = function (treeNode) {
                    var _this = this;
                    var returnVal = [];
                    if (treeNode.fileItemId
                        && !treeNode.required) {
                        var currentFileItem = this.makeFileItemFromTreeNode(treeNode, type_process_1.ProcessType.DELETE)
                            .setGobiiEventOrigin(type_event_origin_1.GobiiUIEventOrigin.CRITERIA_TREE);
                        if ((currentFileItem.getExtractorItemType() != file_model_node_1.ExtractorItemType.ENTITY)
                            || currentFileItem.getItemId()) {
                            returnVal.push(currentFileItem);
                        }
                    }
                    if (treeNode.children) {
                        treeNode.children.forEach(function (tn) {
                            var currentItemsToRemove = _this.findRemovableFileItems(tn);
                            returnVal.push.apply(returnVal, currentItemsToRemove);
                        });
                    }
                    return returnVal;
                };
                StatusDisplayTreeComponent.prototype.clearTree = function () {
                    var _this = this;
                    var itemsToRemove = [];
                    this.gobiiTreeNodes.forEach(function (fin) {
                        var childItemsToRemove = _this.findRemovableFileItems(fin);
                        itemsToRemove.push.apply(itemsToRemove, childItemsToRemove);
                    });
                    var nodesToDeselect = this.selectedGobiiNodes.filter(function (tn) {
                        return tn && !tn.required;
                    });
                    nodesToDeselect.forEach(function (tn) {
                        var idxOfSelectedNodeParentNode = _this.selectedGobiiNodes.indexOf(tn);
                        if (idxOfSelectedNodeParentNode >= 0) {
                            var deleted = _this.selectedGobiiNodes.splice(idxOfSelectedNodeParentNode, 1);
                        }
                    });
                    itemsToRemove.forEach(function (itr) {
                        if (itr) {
                            _this._fileModelTreeService.put(itr).subscribe(function (fmte) {
                            }, function (headerResponse) {
                                _this.handleAddStatusMessage(headerResponse);
                            });
                        }
                    });
                };
                StatusDisplayTreeComponent.prototype.nodeSelect = function (event) {
                    // Unless a node already is checked such that it has data, we don't allow checking
                    // something because it has no meaning without data in it; these would typically
                    // by CONTAINER type nodes: once they have children they're selected, and it which
                    // point we deal with check events in nodeUnselect()
                    // yes this is a bit of a kludge; version 4 of PrimeNG will add a selectable proeprty
                    // to TreeNode which will enable us to approch selectability of nodes in general in
                    // a more systematic and elegant way
                    var _this = this;
                    var selectedGobiiTreeNode = event.node;
                    selectedGobiiTreeNode.children.forEach(function (childNode) {
                        _this.removeItemFromSelectedNodes(childNode);
                    });
                    this.removeItemFromSelectedNodes(selectedGobiiTreeNode);
                };
                StatusDisplayTreeComponent.prototype.getFileItemsToDeselect = function (parentNode) {
                    var _this = this;
                    var returnVal = [];
                    if (parentNode.fileItemId !== null) {
                        var parentFileItem = this.makeFileItemFromTreeNode(parentNode, type_process_1.ProcessType.DELETE);
                        returnVal.push(parentFileItem);
                    }
                    parentNode.children.forEach(function (gtn) {
                        if (gtn.fileItemId !== null) {
                            var currentFileItem = _this.makeFileItemFromTreeNode(gtn, type_process_1.ProcessType.DELETE);
                            returnVal.push(currentFileItem);
                        }
                        var childNodes = _this.getFileItemsToDeselect(gtn);
                        returnVal.push.apply(childNodes);
                    });
                    // fileItem.setGobiiEventOrigin(GobiiUIEventOrigin.CRITERIA_TREE);
                    returnVal.forEach(function (fi) {
                        fi.setGobiiEventOrigin(type_event_origin_1.GobiiUIEventOrigin.CRITERIA_TREE);
                    });
                    return returnVal;
                };
                // we need to disable partial selection because when you click
                // a node that's partially selected, you don't get the unselect event
                // which breaks everything
                StatusDisplayTreeComponent.prototype.unsetPartialSelect = function (gobiiTreeNode) {
                    var thereAreSelectedChildren = false;
                    if (gobiiTreeNode.partialSelected) {
                        gobiiTreeNode.partialSelected = false;
                        var foo = "foo";
                        var _loop_1 = function (idx) {
                            var currentTreeNode = gobiiTreeNode.children[idx];
                            thereAreSelectedChildren = this_1.selectedGobiiNodes.find(function (fi) {
                                return fi
                                    && fi.fileItemId
                                    && (fi.fileItemId === currentTreeNode.fileItemId);
                            }) != undefined;
                        };
                        var this_1 = this;
                        for (var idx = 0; (idx < gobiiTreeNode.children.length) && !thereAreSelectedChildren; idx++) {
                            _loop_1(idx);
                        }
                        if (thereAreSelectedChildren) {
                            this.selectedGobiiNodes.push(gobiiTreeNode);
                        }
                    }
                    if ((gobiiTreeNode.parent !== null)
                        && (gobiiTreeNode.parent !== undefined)) {
                        this.unsetPartialSelect(gobiiTreeNode.parent);
                    }
                };
                StatusDisplayTreeComponent.prototype.nodeUnselect = function (event) {
                    var _this = this;
                    // this funditonality is nearly working;
                    // but it breaks down in the marker criteria section of the
                    // tree. There is no more time to work on this. It must just
                    // effectively disabled for now: you can only select and deselect
                    // from the controls outside the tree
                    var unselectedTreeNode = event.node;
                    this.unsetPartialSelect(unselectedTreeNode);
                    this.selectedGobiiNodes.push(unselectedTreeNode);
                    unselectedTreeNode.children.forEach(function (tn) {
                        _this.selectedGobiiNodes.push(tn);
                    });
                    /*
                     let unselectedTreeNode: GobiiTreeNode = event.node;
            
                     if (( !unselectedTreeNode.required )) {
            
            
                     let itemsToRemove: GobiiFileItem[] = this.getFileItemsToDeselect(unselectedTreeNode);
            
                     this.unsetPartialSelect(unselectedTreeNode);
            
                     itemsToRemove.forEach(itr => {
                     this._fileModelTreeService.put(itr).subscribe(
                     fmte => {
            
                     },
                     headerResponse => {
                     this.handleAddStatusMessage(headerResponse)
                     });
                     })
            
                     } else {
                     // essentially disallow the selection
                     this.selectedGobiiNodes.push(unselectedTreeNode);
                     }
                     */
                };
                StatusDisplayTreeComponent.prototype.makeFileItemFromTreeNode = function (gobiiTreeNode, processType) {
                    var _this = this;
                    var fileModelNode = null;
                    this._fileModelTreeService
                        .getFileModelNode(this.gobiiExtractFilterType, gobiiTreeNode.fileModelNodeId)
                        .subscribe(function (fmn) { return fileModelNode = fmn; }, function (hsm) { return _this.handleAddStatusMessage(hsm); });
                    var fileItemFromModel = fileModelNode
                        .getFileItems()
                        .find(function (fi) { return fi.getFileItemUniqueId() === gobiiTreeNode.fileItemId; });
                    var itemId = null;
                    if (fileItemFromModel) {
                        itemId = fileItemFromModel.getItemId();
                    }
                    // in theory we should be able ot just return the fileItem
                    // we got from the model node. I tried this. And I set the
                    // gobiiExtractFiltertime, process mode, and reuired value
                    // from the tree mode. But the notification for controls
                    // to deselect the item did not work. So we are only using
                    // the fileitem from the model node to set the item id for
                    // now. Sigh.
                    var returnVal = gobii_file_item_1.GobiiFileItem.build(this.gobiiExtractFilterType, processType)
                        .setExtractorItemType(fileModelNode.getItemType())
                        .setEntityType(gobiiTreeNode.entityType)
                        .setEntitySubType(gobiiTreeNode.entitySubType)
                        .setCvFilterType(gobiiTreeNode.cvFilterType)
                        .setItemId(itemId)
                        .setItemName(gobiiTreeNode.label)
                        .setRequired(gobiiTreeNode.required);
                    returnVal.setFileItemUniqueId(gobiiTreeNode.fileItemId);
                    return returnVal;
                };
                StatusDisplayTreeComponent.prototype.nodeExpandMessage = function (event) {
                };
                StatusDisplayTreeComponent.prototype.nodeExpand = function (event) {
                    if (event.node) {
                    }
                };
                StatusDisplayTreeComponent.prototype.nodeCollapse = function (event) {
                    if (event.node) {
                    }
                };
                StatusDisplayTreeComponent.prototype.viewFile = function (file) {
                    //      this.msgs.push({severity: 'info', summary: 'Node Selected with Right Click', detail: file.label});
                };
                StatusDisplayTreeComponent.prototype.unselectFile = function () {
                    //        this.selectedFile2 = null;
                };
                StatusDisplayTreeComponent.prototype.expandAll = function () {
                    var _this = this;
                    this.gobiiTreeNodes.forEach(function (node) {
                        _this.expandRecursive(node, true);
                    });
                };
                StatusDisplayTreeComponent.prototype.collapseAll = function () {
                    var _this = this;
                    this.gobiiTreeNodes.forEach(function (node) {
                        _this.expandRecursive(node, false);
                    });
                };
                StatusDisplayTreeComponent.prototype.addCountToContainerNode = function (node) {
                    var foo = "foo";
                    var parenPosition = node.label.indexOf("(");
                    if (parenPosition > 0) {
                        node.label = node.label.substring(0, parenPosition);
                    }
                    if (node.children.length > 0) {
                        node.label += " (" + node.children.length + ")";
                    }
                };
                StatusDisplayTreeComponent.prototype.expandRecursive = function (node, isExpand) {
                    var _this = this;
                    node.expanded = isExpand;
                    if (node.children) {
                        node.children.forEach(function (childNode) {
                            _this.expandRecursive(childNode, isExpand);
                        });
                    }
                };
                // ********************************************************************************
                // ********************* CHECKBOX/TREE NODE CONVERSION FUNCTIONS
                StatusDisplayTreeComponent.prototype.addEntityIconToNode = function (entityType, cvFilterType, treeNode) {
                    if (entityType === type_entity_1.EntityType.DataSets) {
                        treeNode.icon = "fa-database";
                        treeNode.expandedIcon = "fa-folder-expanded";
                        treeNode.collapsedIcon = "fa-database";
                    }
                    else if (entityType === type_entity_1.EntityType.Contacts) {
                        treeNode.icon = "fa-user-o";
                        treeNode.expandedIcon = "fa-user-o";
                        treeNode.collapsedIcon = "fa-user-o";
                    }
                    else if (entityType === type_entity_1.EntityType.Mapsets) {
                        treeNode.icon = "fa-map-o";
                        treeNode.expandedIcon = "fa-map-o";
                        treeNode.collapsedIcon = "fa-map-o";
                    }
                    else if (entityType === type_entity_1.EntityType.Platforms) {
                        treeNode.icon = "fa-calculator";
                        treeNode.expandedIcon = "fa-calculator";
                        treeNode.collapsedIcon = "fa-calculator";
                    }
                    else if (entityType === type_entity_1.EntityType.Projects) {
                        treeNode.icon = "fa-clipboard";
                        treeNode.expandedIcon = "fa-clipboard";
                        treeNode.collapsedIcon = "fa-clipboard";
                    }
                    else if (entityType === type_entity_1.EntityType.CvTerms) {
                        if (cvFilterType === cv_filter_type_1.CvFilterType.DATASET_TYPE) {
                            treeNode.icon = "fa-file-excel-o";
                            treeNode.expandedIcon = "fa-file-excel-o";
                            treeNode.collapsedIcon = "fa-file-excel-o";
                        }
                    }
                };
                StatusDisplayTreeComponent.prototype.addIconsToNode = function (statusTreeTemplate, treeNode, isParent) {
                    // if( fileModelNode.getItemType() == ExtractorItemType.ENTITY ) {
                    if (statusTreeTemplate.getEntityType() != null
                        && statusTreeTemplate.getEntityType() != type_entity_1.EntityType.UNKNOWN) {
                        this.addEntityIconToNode(statusTreeTemplate.getEntityType(), statusTreeTemplate.getCvFilterType(), treeNode);
                    }
                    else if (statusTreeTemplate.getItemType() === file_model_node_1.ExtractorItemType.EXPORT_FORMAT) {
                        treeNode.icon = "fa-columns";
                        treeNode.expandedIcon = "fa-columns";
                        treeNode.collapsedIcon = "fa-columns";
                    }
                    else if (statusTreeTemplate.getItemType() === file_model_node_1.ExtractorItemType.SAMPLE_FILE) {
                        treeNode.icon = "fa-file-text-o";
                        treeNode.expandedIcon = "fa-file-text-o";
                        treeNode.collapsedIcon = "fa-file-text-o";
                    }
                    else if (statusTreeTemplate.getItemType() === file_model_node_1.ExtractorItemType.SAMPLE_LIST_ITEM) {
                        if (isParent) {
                            treeNode.icon = "fa-list-ul";
                            treeNode.expandedIcon = "fa-list-ul";
                            treeNode.collapsedIcon = "fa-list-ul";
                        }
                        else {
                            treeNode.icon = "fa-eyedropper";
                            treeNode.expandedIcon = "fa-eyedropper";
                            treeNode.collapsedIcon = "fa-eyedropper";
                        }
                    }
                    else if (statusTreeTemplate.getItemType() === file_model_node_1.ExtractorItemType.MARKER_FILE) {
                        treeNode.icon = "fa-file-text-o";
                        treeNode.expandedIcon = "fa-file-text-o";
                        treeNode.collapsedIcon = "fa-file-text-o";
                    }
                    else if (statusTreeTemplate.getItemType() === file_model_node_1.ExtractorItemType.MARKER_LIST_ITEM) {
                        if (isParent) {
                            treeNode.icon = "fa-list-ul";
                            treeNode.expandedIcon = "fa-list-ul";
                            treeNode.collapsedIcon = "fa-list-ul";
                        }
                        else {
                            treeNode.icon = "fa-map-marker";
                            treeNode.expandedIcon = "fa-map-marker";
                            treeNode.collapsedIcon = "fa-map-marker";
                        }
                    }
                    else if (statusTreeTemplate.getItemType() === file_model_node_1.ExtractorItemType.JOB_ID) {
                        treeNode.icon = "fa-info-circle";
                        treeNode.expandedIcon = "fa-info-circle";
                        treeNode.collapsedIcon = "fa-info-circle";
                    }
                    else if (statusTreeTemplate.getItemType() === file_model_node_1.ExtractorItemType.SAMPLE_LIST_TYPE) {
                        treeNode.icon = "fa-info-circle";
                        treeNode.expandedIcon = "fa-info-circle";
                        treeNode.collapsedIcon = "fa-info-circle";
                    }
                    else {
                        //     }
                        // } else if (fileModelNode.getItemType() == ExtractorItemType.CATEGORY ) {
                        treeNode.icon = "fa-folder";
                        treeNode.expandedIcon = "fa-folder-expanded";
                        treeNode.collapsedIcon = "fa-folder";
                    }
                };
                StatusDisplayTreeComponent.prototype.addEntityNameToNode = function (fileModelNode, gobiiTreeNode, eventedFileItem) {
                    if (fileModelNode.getCategoryType() === file_model_node_1.ExtractorCategoryType.CONTAINER) {
                        gobiiTreeNode.label = eventedFileItem.getItemName();
                    }
                    else {
                        if (eventedFileItem.getExtractorItemType() == file_model_node_1.ExtractorItemType.EXPORT_FORMAT) {
                            var gobiiExtractFormat = type_extract_format_1.GobiiExtractFormat[eventedFileItem.getItemId()];
                            gobiiTreeNode.label = fileModelNode.getEntityName() + ": " + entity_labels_1.Labels.instance().extractFormatTypeLabels[gobiiExtractFormat];
                        }
                        else if (eventedFileItem.getExtractorItemType() == file_model_node_1.ExtractorItemType.JOB_ID) {
                            gobiiTreeNode.label = entity_labels_1.Labels.instance().treeExtractorTypeLabels[file_model_node_1.ExtractorItemType.JOB_ID]
                                + ": " + eventedFileItem.getItemId();
                        }
                        else {
                            if (eventedFileItem.getProcessType() !== type_process_1.ProcessType.DELETE) {
                                gobiiTreeNode.label = fileModelNode.getEntityName() + ": " + eventedFileItem.getItemName();
                            }
                            else {
                                gobiiTreeNode.label = fileModelNode.getEntityName();
                            }
                        }
                    }
                };
                StatusDisplayTreeComponent.prototype.findTreeNodebyModelNodeId = function (gobiiTreeNodes, fileModelNodeId) {
                    var foo = "bar";
                    var returnVal = null;
                    for (var idx = 0; (idx < gobiiTreeNodes.length) && (returnVal === null); idx++) {
                        var currentTreeNode = gobiiTreeNodes[idx];
                        if (currentTreeNode.fileModelNodeId === fileModelNodeId) {
                            returnVal = currentTreeNode;
                        }
                        else {
                            returnVal = this.findTreeNodebyModelNodeId(currentTreeNode.children, fileModelNodeId);
                        }
                    } // iterate gobii nodes
                    return returnVal;
                };
                StatusDisplayTreeComponent.prototype.findTreeNodebyFileItemUniqueId = function (gobiiTreeNodes, fileItemId) {
                    var returnVal = null;
                    for (var idx = 0; (idx < gobiiTreeNodes.length) && (returnVal === null); idx++) {
                        var currentTreeNode = gobiiTreeNodes[idx];
                        if (currentTreeNode.fileItemId === fileItemId) {
                            returnVal = currentTreeNode;
                        }
                        else {
                            returnVal = this.findTreeNodebyFileItemUniqueId(currentTreeNode.children, fileItemId);
                        }
                    }
                    return returnVal;
                };
                StatusDisplayTreeComponent.prototype.removeItemFromSelectedNodes = function (gobiiTreeNode) {
                    // let selectedNode: GobiiTreeNode = this.selectedGobiiNodes.find(stn => {
                    //     return stn.fileItemId === gobiiTreeNode.fileItemId
                    // });
                    //        if (selectedNode) {
                    if (gobiiTreeNode) {
                        var idxOfSelectedNodeParentNode = this.selectedGobiiNodes.indexOf(gobiiTreeNode);
                        if (idxOfSelectedNodeParentNode >= 0) {
                            var deleted = this.selectedGobiiNodes.splice(idxOfSelectedNodeParentNode, 1);
                            var foo = "foo";
                        }
                    }
                };
                StatusDisplayTreeComponent.prototype.removeNodeFromTree = function (fileModelTreeEvent) {
                    if (fileModelTreeEvent.fileModelNode != null && fileModelTreeEvent.fileItem != null) {
                        if (fileModelTreeEvent.fileModelNode.getCategoryType() === file_model_node_1.ExtractorCategoryType.LEAF) {
                            var gobiiTreeNodeToBeRemoved = this.findTreeNodebyFileItemUniqueId(this.gobiiTreeNodes, fileModelTreeEvent.fileItem.getFileItemUniqueId());
                            if (gobiiTreeNodeToBeRemoved !== null) {
                                // will need a funciton to do this correctly
                                this.addEntityNameToNode(fileModelTreeEvent.fileModelNode, gobiiTreeNodeToBeRemoved, fileModelTreeEvent.fileItem);
                                this.removeItemFromSelectedNodes(gobiiTreeNodeToBeRemoved);
                            }
                            else {
                            } // if-else we found an existing node for the LEAF node's file item
                        }
                        else if (fileModelTreeEvent.fileModelNode.getCategoryType() === file_model_node_1.ExtractorCategoryType.CONTAINER) {
                            // there should not be a file item associated with the model because it's a container -- the file items are just for the children
                            var parentTreeNode = this.findTreeNodebyModelNodeId(this.gobiiTreeNodes, fileModelTreeEvent.fileModelNode.getFileModelNodeUniqueId());
                            if (parentTreeNode != null) {
                                var nodeToDelete = parentTreeNode.children.find(function (n) {
                                    return n.fileItemId === fileModelTreeEvent.fileItem.getFileItemUniqueId();
                                });
                                if (nodeToDelete != null) {
                                    var idxOfNodeToDelete = parentTreeNode.children.indexOf(nodeToDelete);
                                    parentTreeNode.children.splice(idxOfNodeToDelete, 1);
                                    this.addCountToContainerNode(parentTreeNode);
                                    if (parentTreeNode.children.length === 0) {
                                        this.removeItemFromSelectedNodes(parentTreeNode);
                                    }
                                }
                            }
                            else {
                            }
                        } // if-else -if on extractor category type
                    }
                    else {
                    } // there i sno file mode node for tree event
                };
                StatusDisplayTreeComponent.prototype.placeNodeInTree = function (fileModelTreeEvent) {
                    if (fileModelTreeEvent.fileModelNode != null && fileModelTreeEvent.fileItem != null) {
                        if (fileModelTreeEvent.fileModelNode.getCategoryType() === file_model_node_1.ExtractorCategoryType.LEAF) {
                            var gobiiTreeLeafNodeTobeMutated = this.findTreeNodebyModelNodeId(this.gobiiTreeNodes, fileModelTreeEvent.fileModelNode.getFileModelNodeUniqueId());
                            if (gobiiTreeLeafNodeTobeMutated != null) {
                                gobiiTreeLeafNodeTobeMutated.fileItemId = fileModelTreeEvent.fileItem.getFileItemUniqueId();
                                gobiiTreeLeafNodeTobeMutated.entityType = fileModelTreeEvent.fileItem.getEntityType();
                                gobiiTreeLeafNodeTobeMutated.entitySubType = fileModelTreeEvent.fileItem.getEntitySubType();
                                gobiiTreeLeafNodeTobeMutated.cvFilterType = fileModelTreeEvent.fileItem.getCvFilterType();
                                this.addEntityNameToNode(fileModelTreeEvent.fileModelNode, gobiiTreeLeafNodeTobeMutated, fileModelTreeEvent.fileItem);
                                this.addIconsToNode(fileModelTreeEvent.fileModelNode, gobiiTreeLeafNodeTobeMutated, false);
                                gobiiTreeLeafNodeTobeMutated.required = fileModelTreeEvent.fileModelNode.getRequired();
                                if (this.selectedGobiiNodes.indexOf(gobiiTreeLeafNodeTobeMutated) === -1) {
                                    this.selectedGobiiNodes.push(gobiiTreeLeafNodeTobeMutated);
                                }
                                if (gobiiTreeLeafNodeTobeMutated.parent !== null) {
                                    if (this.selectedGobiiNodes.indexOf(gobiiTreeLeafNodeTobeMutated.parent) === -1) {
                                        this.selectedGobiiNodes.push(gobiiTreeLeafNodeTobeMutated.parent);
                                    }
                                }
                            }
                            else {
                                var message = "Error placing file item in the status tree: there is no gobii tree leaf node for model node "
                                    + entity_labels_1.Labels.instance().treeExtractorTypeLabels[fileModelTreeEvent.fileModelNode.getItemType()];
                                if (fileModelTreeEvent.fileItem && fileModelTreeEvent.fileItem.getItemName()) {
                                    message += " for fileItem of name " + fileModelTreeEvent.fileItem.getItemName();
                                }
                                this.handleAddStatusMessage(new dto_header_status_message_1.HeaderStatusMessage(message, null, null));
                            } // if-else we found an existing node for the LEAF node's file item
                        }
                        else if (fileModelTreeEvent.fileModelNode.getCategoryType() === file_model_node_1.ExtractorCategoryType.CONTAINER) {
                            // there should not be a file item associated with the model because it's a container -- the file items are just for the children
                            var parentTreeNode = this.findTreeNodebyModelNodeId(this.gobiiTreeNodes, fileModelTreeEvent.fileModelNode.getFileModelNodeUniqueId());
                            if (parentTreeNode != null) {
                                var existingFileModelItem = fileModelTreeEvent
                                    .fileModelNode
                                    .getFileItems()
                                    .find(function (item) {
                                    return item.getFileItemUniqueId() === fileModelTreeEvent.fileItem.getFileItemUniqueId();
                                });
                                if (existingFileModelItem !== null) {
                                    var existingGobiiTreeNodeChild = this.findTreeNodebyFileItemUniqueId(this.gobiiTreeNodes, existingFileModelItem.getFileItemUniqueId());
                                    if (existingGobiiTreeNodeChild === null) {
                                        var newGobiiTreeNode = new GobiiTreeNode_1.GobiiTreeNode(parentTreeNode, fileModelTreeEvent.fileModelNode.getFileModelNodeUniqueId(), fileModelTreeEvent.fileItem.getFileItemUniqueId(), fileModelTreeEvent.fileModelNode.getRequired());
                                        newGobiiTreeNode.entityType = fileModelTreeEvent.fileItem.getEntityType();
                                        this.addIconsToNode(fileModelTreeEvent.fileModelNode, newGobiiTreeNode, false);
                                        this.addEntityNameToNode(fileModelTreeEvent.fileModelNode, newGobiiTreeNode, fileModelTreeEvent.fileItem);
                                        parentTreeNode.children.push(newGobiiTreeNode);
                                        parentTreeNode.expanded = true;
                                        parentTreeNode.required = false; //make it clickable
                                        newGobiiTreeNode.parent = parentTreeNode;
                                        this.selectedGobiiNodes.push(newGobiiTreeNode);
                                        this.addCountToContainerNode(parentTreeNode);
                                        if (parentTreeNode.children.length >= this.containerCollapseThreshold) {
                                            parentTreeNode.expanded = false;
                                        }
                                        else {
                                            parentTreeNode.expanded = true;
                                        }
                                        if (this.selectedGobiiNodes.indexOf(parentTreeNode) < 0) {
                                            this.selectedGobiiNodes.push(parentTreeNode);
                                        }
                                    }
                                    else {
                                    } // if-else there already exists a corresponding tree node
                                }
                                else {
                                } // if else we found an existing file item
                            }
                            else {
                            } // if-else we found a tree node to serve as parent for the container's item tree nodes
                        } // if-else -if on extractor category type
                    }
                    else {
                    } // there i sno file mode node for tree event
                }; // place node in tree
                StatusDisplayTreeComponent.prototype.setUpRequredItems = function (gobiiExtractorFilterType) {
                    var _this = this;
                    this.treeIsInitialized = false;
                    this.gobiiTreeNodes = [];
                    var fileModelNodes = [];
                    this._fileModelTreeService.getFileModel(gobiiExtractorFilterType).subscribe(function (f) {
                        fileModelNodes = f;
                    });
                    fileModelNodes.forEach(function (currentFirstLevelFileModelNode) {
                        var currentTreeNode = _this.makeTreeNodeFromTemplate(null, currentFirstLevelFileModelNode);
                        if (currentTreeNode != null) {
                            _this.gobiiTreeNodes.push(currentTreeNode);
                        }
                    });
                    this.treeIsInitialized = true;
                    this._fileModelTreeService.put(gobii_file_item_1.GobiiFileItem
                        .build(this.gobiiExtractFilterType, type_process_1.ProcessType.NOTIFY)
                        .setExtractorItemType(file_model_node_1.ExtractorItemType.STATUS_DISPLAY_TREE_READY)).subscribe(null, function (headerResponse) {
                        _this.handleAddStatusMessage(headerResponse);
                    });
                };
                StatusDisplayTreeComponent.prototype.makeTreeNodeFromTemplate = function (parentNode, fileModelNode) {
                    var _this = this;
                    var returnVal = null;
                    if (fileModelNode.getItemType() === file_model_node_1.ExtractorItemType.ENTITY) {
                        returnVal = new GobiiTreeNode_1.GobiiTreeNode(parentNode, fileModelNode.getFileModelNodeUniqueId(), null, fileModelNode.getRequired());
                        returnVal.entityType = fileModelNode.getEntityType();
                        returnVal.label = fileModelNode.getEntityName();
                    }
                    else if (fileModelNode.getItemType() == file_model_node_1.ExtractorItemType.EXPORT_FORMAT) {
                        returnVal = new GobiiTreeNode_1.GobiiTreeNode(parentNode, fileModelNode.getFileModelNodeUniqueId(), null, fileModelNode.getRequired());
                        returnVal.label = fileModelNode.getCategoryName();
                    }
                    else if (fileModelNode.getItemType() == file_model_node_1.ExtractorItemType.SAMPLE_LIST_ITEM) {
                        returnVal = new GobiiTreeNode_1.GobiiTreeNode(parentNode, fileModelNode.getFileModelNodeUniqueId(), null, fileModelNode.getRequired());
                        returnVal.label = fileModelNode.getCategoryName();
                    }
                    else if (fileModelNode.getItemType() == file_model_node_1.ExtractorItemType.MARKER_FILE) {
                        returnVal = new GobiiTreeNode_1.GobiiTreeNode(parentNode, fileModelNode.getFileModelNodeUniqueId(), null, fileModelNode.getRequired());
                        returnVal.label = fileModelNode.getCategoryName();
                    }
                    else if (fileModelNode.getItemType() == file_model_node_1.ExtractorItemType.JOB_ID) {
                        returnVal = new GobiiTreeNode_1.GobiiTreeNode(parentNode, fileModelNode.getFileModelNodeUniqueId(), null, fileModelNode.getRequired());
                        returnVal.label = fileModelNode.getCategoryName();
                    }
                    else {
                        returnVal = new GobiiTreeNode_1.GobiiTreeNode(parentNode, fileModelNode.getFileModelNodeUniqueId(), null, fileModelNode.getRequired());
                        if (fileModelNode.getEntityType() != null
                            && fileModelNode.getEntityType() != type_entity_1.EntityType.UNKNOWN) {
                            returnVal.entityType = fileModelNode.getEntityType();
                        }
                        returnVal.label = fileModelNode.getCategoryName();
                    }
                    if (null != returnVal) {
                        var debug = "debug";
                        this.addIconsToNode(fileModelNode, returnVal, true);
                        returnVal.expanded = true;
                        fileModelNode.getChildren().forEach(function (stt) {
                            var currentTreeNode = _this.makeTreeNodeFromTemplate(returnVal, stt);
                            if (null != currentTreeNode) {
                                returnVal.children.push(currentTreeNode);
                            }
                        }); // iterate child model node
                    }
                    else {
                        this.handleAddStatusMessage(new dto_header_status_message_1.HeaderStatusMessage("Unable to make tree node for file model of type " + entity_labels_1.Labels.instance().treeExtractorTypeLabels[fileModelNode.getItemType()], null, null));
                    } // if we created a tree node
                    return returnVal;
                };
                StatusDisplayTreeComponent.prototype.ngOnChanges = function (changes) {
                    if (changes['fileItemEventChange'] && changes['fileItemEventChange'].currentValue) {
                        var itemChangedEvent = changes['fileItemEventChange'].currentValue;
                    }
                    else if (changes['gobiiExtractFilterTypeEvent']
                        && (changes['gobiiExtractFilterTypeEvent'].currentValue != null)
                        && (changes['gobiiExtractFilterTypeEvent'].currentValue != undefined)) {
                        if (changes['gobiiExtractFilterTypeEvent'].currentValue !== changes['gobiiExtractFilterTypeEvent'].previousValue) {
                            this.gobiiExtractFilterType = changes['gobiiExtractFilterTypeEvent'].currentValue;
                            this.setUpRequredItems(this.gobiiExtractFilterType);
                        }
                    }
                };
                StatusDisplayTreeComponent.prototype.makeDemoTreeNodes = function () {
                    this.demoTreeNodes = [
                        {
                            "label": "Documents",
                            "data": "Documents Folder",
                            "expandedIcon": "fa-folder-open",
                            "collapsedIcon": "fa-folder",
                            "children": [{
                                    "label": "Work",
                                    "data": "Work Folder",
                                    "expandedIcon": "fa-folder-open",
                                    "collapsedIcon": "fa-folder",
                                    "children": [{
                                            "label": "Expenses.doc",
                                            "icon": "fa-file-word-o",
                                            "data": "Expenses Document"
                                        }, { "label": "Resume.doc", "icon": "fa-file-word-o", "data": "Resume Document" }]
                                },
                                {
                                    "label": "Home",
                                    "data": "Home Folder",
                                    "expandedIcon": "fa-folder-open",
                                    "collapsedIcon": "fa-folder",
                                    "children": [{
                                            "label": "Invoices.txt",
                                            "icon": "fa-file-word-o",
                                            "data": "Invoices for this month"
                                        }]
                                }]
                        },
                        {
                            "label": "Pictures",
                            "data": "Pictures Folder",
                            "expandedIcon": "fa-folder-open",
                            "collapsedIcon": "fa-folder",
                            "children": [
                                { "label": "barcelona.jpg", "icon": "fa-file-image-o", "data": "Barcelona Photo" },
                                { "label": "logo.jpg", "icon": "fa-file-image-o", "data": "PrimeFaces Logo" },
                                { "label": "primeui.png", "icon": "fa-file-image-o", "data": "PrimeUI Logo" }
                            ]
                        },
                        {
                            "label": "Movies",
                            "data": "Movies Folder",
                            "expandedIcon": "fa-folder-open",
                            "collapsedIcon": "fa-folder",
                            "children": [{
                                    "label": "Al Pacino",
                                    "data": "Pacino Movies",
                                    "children": [{
                                            "label": "Scarface",
                                            "icon": "fa-file-video-o",
                                            "data": "Scarface Movie"
                                        }, { "label": "Serpico", "icon": "fa-file-video-o", "data": "Serpico Movie" }]
                                },
                                {
                                    "label": "Robert De Niro",
                                    "data": "De Niro Movies",
                                    "children": [{
                                            "label": "Goodfellas",
                                            "icon": "fa-file-video-o",
                                            "data": "Goodfellas Movie"
                                        }, {
                                            "label": "Untouchables",
                                            "icon": "fa-file-video-o",
                                            "data": "Untouchables Movie"
                                        }]
                                }]
                        }
                    ];
                    this.selectedDemoNodes.push(this.demoTreeNodes[1].children[0]);
                    this.demoTreeNodes[1].partialSelected = true;
                    this.demoTreeNodes[1].expanded = true;
                };
                return StatusDisplayTreeComponent;
            }());
            StatusDisplayTreeComponent = __decorate([
                core_1.Component({
                    selector: 'status-display-tree',
                    inputs: ['fileItemEventChange', 'gobiiExtractFilterTypeEvent'],
                    outputs: ['onItemSelected', 'onItemChecked', 'onAddMessage', 'onTreeReady'],
                    template: " \n                    <p-tree [value]=\"gobiiTreeNodes\" \n                    selectionMode=\"checkbox\" \n                    propagateSelectionUp=\"false\"\n                    propagateSelectionDown=\"false\"\n                    [(selection)]=\"selectedGobiiNodes\"\n                    (onNodeUnselect)=\"nodeUnselect($event)\"\n                    (onNodeSelect)=\"nodeSelect($event)\"\n                    (onNodeExpand)=\"nodeExpand($event)\"\n                    (onNodeCollapse)=\"nodeCollapse($event)\"\n                    [style]=\"{'width':'100%'}\"\n                    styleClass=\"criteria-tree\"></p-tree>\n                    <!--<p-tree [value]=\"demoTreeNodes\" selectionMode=\"checkbox\" [(selection)]=\"selectedDemoNodes\"></p-tree>-->\n                    <!--<div>Selected Nodes: <span *ngFor=\"let file of selectedFiles2\">{{file.label}} </span></div>-->\n"
                }),
                __metadata("design:paramtypes", [file_model_tree_service_1.FileModelTreeService])
            ], StatusDisplayTreeComponent);
            exports_1("StatusDisplayTreeComponent", StatusDisplayTreeComponent);
        }
    };
});
//# sourceMappingURL=status-display-tree.component.js.map