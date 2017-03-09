System.register(["@angular/core", "../model/file-item", "../model/GobiiTreeNode", "../model/type-entity", "../model/type-extractor-filter", "../model/file-model-node", "../model/cv-filter-type", "../services/core/file-model-tree-service", "../model/type-process", "../model/type-extract-format", "../model/dto-header-status-message", "./entity-labels"], function (exports_1, context_1) {
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
    var core_1, file_item_1, GobiiTreeNode_1, type_entity_1, type_extractor_filter_1, file_model_node_1, cv_filter_type_1, file_model_tree_service_1, type_process_1, type_extract_format_1, dto_header_status_message_1, entity_labels_1, StatusDisplayTreeComponent;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (file_item_1_1) {
                file_item_1 = file_item_1_1;
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
            }
        ],
        execute: function () {
            StatusDisplayTreeComponent = (function () {
                function StatusDisplayTreeComponent(_fileModelTreeService) {
                    var _this = this;
                    this._fileModelTreeService = _fileModelTreeService;
                    this.onAddMessage = new core_1.EventEmitter();
                    this.onTreeReady = new core_1.EventEmitter();
                    // *****************************************************************
                    // *********************  TREE NODE DATA STRUCTURES AND EVENTS
                    this.demoTreeNodes = [];
                    this.selectedDemoNodes = [];
                    this.gobiiTreeNodes = [];
                    this.selectedGobiiNodes = [];
                    // ********************************************************************************
                    // ********************* CHECKBOX (GOBII-SPECIFIC)  NODE DATA STRUCTURES AND EVENTS
                    this.gobiiExtractFilterType = type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN;
                    this.onItemChecked = new core_1.EventEmitter();
                    this.onItemSelected = new core_1.EventEmitter();
                    this.fileModelNodeTree = new Map();
                    // has to be in ctor because if you put it in ngOnInit(), there can be ngOnChange events
                    // before ngOnInit() is called.
                    this._fileModelTreeService
                        .treeNotifications()
                        .subscribe(function (te) {
                        if (te.fileItem.getProcessType() === type_process_1.ProcessType.CREATE
                            || te.fileItem.getProcessType() === type_process_1.ProcessType.UPDATE) {
                            _this.placeNodeInTree(te);
                        }
                        else if (te.fileItem.getProcessType() === type_process_1.ProcessType.DELETE) {
                            _this.removeNodeFromTree(te);
                        }
                        else {
                            var headerStatusMessage = new dto_header_status_message_1.HeaderStatusMessage("Error in status display tree processing file item type "
                                + file_model_node_1.ExtractorItemType[te.fileItem.getExtractorItemType()]
                                + ": Unknown porcess type: "
                                + type_process_1.ProcessType[te.fileItem.getProcessType()], null, null);
                            _this.handleAddStatusMessage(headerStatusMessage);
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
                StatusDisplayTreeComponent.prototype.nodeSelect = function (event) {
                    //      this.msgs.push({severity: 'info', summary: 'Node Selected', detail: event.node.label});
                };
                StatusDisplayTreeComponent.prototype.nodeUnselect = function (event) {
                    var _this = this;
                    var unselectedTreeNode = event.node;
                    var fileItem = this.makeFileItemFromTreeNode(unselectedTreeNode, false);
                    // if the item is required, take no action and in fact make it selected again
                    if (!fileItem.getRequired()) {
                        this._fileModelTreeService.put(fileItem).subscribe(null, function (headerResponse) {
                            _this.handleAddStatusMessage(headerResponse);
                        });
                    }
                    else {
                        this.selectedGobiiNodes.push(unselectedTreeNode);
                    }
                };
                StatusDisplayTreeComponent.prototype.makeFileItemFromTreeNode = function (gobiiTreeNode, checked) {
                    var returnVal = file_item_1.FileItem.build(this.gobiiExtractFilterType, (checked ? type_process_1.ProcessType.CREATE : type_process_1.ProcessType.DELETE))
                        .setEntityType(gobiiTreeNode.entityType)
                        .setCvFilterType(gobiiTreeNode.cvFilterType)
                        .setItemId(null)
                        .setItemName(gobiiTreeNode.label)
                        .setChecked(checked)
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
                StatusDisplayTreeComponent.prototype.addIconsToNode = function (statusTreeTemplate, treeNode) {
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
                    else if (statusTreeTemplate.getItemType() === file_model_node_1.ExtractorItemType.SAMPLE_LIST) {
                        treeNode.icon = "fa-eyedropper";
                        treeNode.expandedIcon = "fa-eyedropper";
                        treeNode.collapsedIcon = "fa-eyedropper";
                    }
                    else if (statusTreeTemplate.getItemType() === file_model_node_1.ExtractorItemType.MARKER_LIST) {
                        treeNode.icon = "fa-map-marker";
                        treeNode.expandedIcon = "fa-map-marker";
                        treeNode.collapsedIcon = "fa-map-marker";
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
                    if (fileModelNode.getCategoryType() === file_model_node_1.ExtractorCategoryType.ENTITY_CONTAINER) {
                        gobiiTreeNode.label = eventedFileItem.getItemName();
                    }
                    else {
                        if (eventedFileItem.getExtractorItemType() == file_model_node_1.ExtractorItemType.EXPORT_FORMAT) {
                            var gobiiExtractFormat = type_extract_format_1.GobiiExtractFormat[eventedFileItem.getItemId()];
                            gobiiTreeNode.label = fileModelNode.getEntityName() + ": " + entity_labels_1.Labels.instance().extractFormatTypeLabels[gobiiExtractFormat];
                        }
                        else {
                            gobiiTreeNode.label = fileModelNode.getEntityName() + ": " + eventedFileItem.getItemName();
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
                StatusDisplayTreeComponent.prototype.findTreeNodebyFileItemIdId = function (gobiiTreeNodes, fileItemId) {
                    var _this = this;
                    var returnVal = null;
                    gobiiTreeNodes.forEach(function (currentTreeNode) {
                        if (currentTreeNode.fileItemId === fileItemId) {
                            returnVal = currentTreeNode;
                        }
                        else {
                            returnVal = _this.findTreeNodebyModelNodeId(currentTreeNode.children, fileItemId);
                        }
                    });
                    return returnVal;
                };
                StatusDisplayTreeComponent.prototype.removeNodeFromTree = function (fileModelTreeEvent) {
                    if (fileModelTreeEvent.fileModelNode != null && fileModelTreeEvent.fileItem != null) {
                        if (fileModelTreeEvent.fileModelNode.getCategoryType() === file_model_node_1.ExtractorCategoryType.LEAF) {
                            var gobiiTreeNodeToBeRemoved = this.findTreeNodebyFileItemIdId(this.gobiiTreeNodes, fileModelTreeEvent.fileItem.getFileItemUniqueId());
                            if (gobiiTreeNodeToBeRemoved !== null) {
                                // will need a funciton to do this correctly
                                gobiiTreeNodeToBeRemoved.label = "name reset";
                            }
                            else {
                            } // if-else we found an existing node for the LEAF node's file item
                        }
                        else if (fileModelTreeEvent.fileModelNode.getCategoryType() === file_model_node_1.ExtractorCategoryType.ENTITY_CONTAINER) {
                            // there should not be a file item associated with the model because it's a container -- the file items are just for the children
                            var parentTreeNode = this.findTreeNodebyModelNodeId(this.gobiiTreeNodes, fileModelTreeEvent.fileModelNode.getFileModelNodeUniqueId());
                            if (parentTreeNode != null) {
                                var nodeToDelete = parentTreeNode.children.find(function (n) {
                                    return n.fileItemId === fileModelTreeEvent.fileItem.getFileItemUniqueId();
                                });
                                if (nodeToDelete != null) {
                                    var idxOfNodeToDelete = parentTreeNode.children.indexOf(nodeToDelete);
                                    parentTreeNode.children.splice(idxOfNodeToDelete, 1);
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
                                this.addEntityNameToNode(fileModelTreeEvent.fileModelNode, gobiiTreeLeafNodeTobeMutated, fileModelTreeEvent.fileItem);
                                this.addEntityIconToNode(fileModelTreeEvent.fileModelNode.getEntityType(), fileModelTreeEvent.fileModelNode.getCvFilterType(), gobiiTreeLeafNodeTobeMutated);
                                gobiiTreeLeafNodeTobeMutated.required = fileModelTreeEvent.fileItem.getRequired();
                                if (this.selectedGobiiNodes.indexOf(gobiiTreeLeafNodeTobeMutated) === -1) {
                                    this.selectedGobiiNodes.push(gobiiTreeLeafNodeTobeMutated);
                                }
                            }
                            else {
                                this.handleAddStatusMessage(new dto_header_status_message_1.HeaderStatusMessage("Error placing file item in the status tree: there is no gobii tree leaf node for model node "
                                    + fileModelTreeEvent.fileModelNode.getEntityName(), null, null));
                            } // if-else we found an existing node for the LEAF node's file item
                        }
                        else if (fileModelTreeEvent.fileModelNode.getCategoryType() === file_model_node_1.ExtractorCategoryType.ENTITY_CONTAINER) {
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
                                    var existingGobiiTreeNodeChild = this.findTreeNodebyFileItemIdId(this.gobiiTreeNodes, existingFileModelItem.getFileItemUniqueId());
                                    if (existingGobiiTreeNodeChild === null) {
                                        var newGobiiTreeNode = new GobiiTreeNode_1.GobiiTreeNode(fileModelTreeEvent.fileModelNode.getFileModelNodeUniqueId(), fileModelTreeEvent.fileItem.getFileItemUniqueId(), fileModelTreeEvent.fileItem.getRequired());
                                        newGobiiTreeNode.entityType = fileModelTreeEvent.fileItem.getEntityType();
                                        this.addEntityIconToNode(fileModelTreeEvent.fileModelNode.getEntityType(), fileModelTreeEvent.fileModelNode.getCvFilterType(), newGobiiTreeNode);
                                        this.addEntityNameToNode(fileModelTreeEvent.fileModelNode, newGobiiTreeNode, fileModelTreeEvent.fileItem);
                                        parentTreeNode.children.push(newGobiiTreeNode);
                                        parentTreeNode.expanded = true;
                                        this.selectedGobiiNodes.push(newGobiiTreeNode);
                                        this.selectedGobiiNodes.push(parentTreeNode);
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
                    this.gobiiTreeNodes = [];
                    var fileModelNodes = [];
                    this._fileModelTreeService.getFileModel(gobiiExtractorFilterType).subscribe(function (f) {
                        fileModelNodes = f;
                    });
                    fileModelNodes.forEach(function (currentFirstLevelFileModelNode) {
                        var currentTreeNode = _this.makeTreeNodeFromTemplate(currentFirstLevelFileModelNode);
                        if (currentTreeNode != null) {
                            _this.gobiiTreeNodes.push(currentTreeNode);
                        }
                    });
                };
                StatusDisplayTreeComponent.prototype.makeTreeNodeFromTemplate = function (fileModelNode) {
                    var _this = this;
                    var returnVal = null;
                    if (fileModelNode.getItemType() === file_model_node_1.ExtractorItemType.ENTITY) {
                        returnVal = new GobiiTreeNode_1.GobiiTreeNode(fileModelNode.getFileModelNodeUniqueId(), null, false);
                        returnVal.entityType = fileModelNode.getEntityType();
                        returnVal.label = fileModelNode.getEntityName();
                    }
                    else if (fileModelNode.getItemType() === file_model_node_1.ExtractorItemType.CATEGORY) {
                        returnVal = new GobiiTreeNode_1.GobiiTreeNode(fileModelNode.getFileModelNodeUniqueId(), null, false);
                        if (fileModelNode.getEntityType() != null
                            && fileModelNode.getEntityType() != type_entity_1.EntityType.UNKNOWN) {
                            returnVal.entityType = fileModelNode.getEntityType();
                        }
                        returnVal.label = fileModelNode.getCategoryName();
                    }
                    else if (fileModelNode.getItemType() == file_model_node_1.ExtractorItemType.EXPORT_FORMAT) {
                        returnVal = new GobiiTreeNode_1.GobiiTreeNode(fileModelNode.getFileModelNodeUniqueId(), null, false);
                        returnVal.label = fileModelNode.getCategoryName();
                    }
                    else if (fileModelNode.getItemType() == file_model_node_1.ExtractorItemType.SAMPLE_LIST) {
                        returnVal = new GobiiTreeNode_1.GobiiTreeNode(fileModelNode.getFileModelNodeUniqueId(), null, false);
                        returnVal.label = fileModelNode.getCategoryName();
                    }
                    else if (fileModelNode.getItemType() == file_model_node_1.ExtractorItemType.MARKER_LIST) {
                        returnVal = new GobiiTreeNode_1.GobiiTreeNode(fileModelNode.getFileModelNodeUniqueId(), null, false);
                        returnVal.label = fileModelNode.getCategoryName();
                    }
                    if (null != returnVal) {
                        var debug = "debug";
                        this.addIconsToNode(fileModelNode, returnVal);
                        returnVal.expanded = true;
                        fileModelNode.getChildren().forEach(function (stt) {
                            var currentTreeNode = _this.makeTreeNodeFromTemplate(stt);
                            if (null != currentTreeNode) {
                                returnVal.children.push(currentTreeNode);
                            }
                        }); // iterate child model node
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
                        var newGobiiExtractFilterType = changes['gobiiExtractFilterTypeEvent'].currentValue;
                        if (newGobiiExtractFilterType !== this.gobiiExtractFilterType) {
                            this.gobiiExtractFilterType = changes['gobiiExtractFilterTypeEvent'].currentValue;
                            //this.getTemplates(this.gobiiExtractFilterType, true);
                            this.setUpRequredItems(newGobiiExtractFilterType);
                            this.onTreeReady.emit(new dto_header_status_message_1.HeaderStatusMessage("ready", null, null));
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
                    template: " \n                    <p-tree [value]=\"gobiiTreeNodes\" \n                    selectionMode=\"checkbox\" \n                    [(selection)]=\"selectedGobiiNodes\"\n                    (onNodeUnselect)=\"nodeUnselect($event)\"\n                    styleClass=\"criteria-tree\"></p-tree>\n                    <!--<p-tree [value]=\"demoTreeNodes\" selectionMode=\"checkbox\" [(selection)]=\"selectedDemoNodes\"></p-tree>-->\n                    <!--<div>Selected Nodes: <span *ngFor=\"let file of selectedFiles2\">{{file.label}} </span></div>-->\n"
                }),
                __metadata("design:paramtypes", [file_model_tree_service_1.FileModelTreeService])
            ], StatusDisplayTreeComponent);
            exports_1("StatusDisplayTreeComponent", StatusDisplayTreeComponent);
        }
    };
});
//# sourceMappingURL=status-display-tree.component.js.map