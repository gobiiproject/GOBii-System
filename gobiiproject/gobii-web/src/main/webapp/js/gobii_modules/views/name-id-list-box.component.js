System.register(["@angular/core", "../model/name-id", "../model/type-entity", "../model/cv-filter-type", "../model/gobii-file-item", "../model/type-process", "../services/core/file-model-tree-service", "../model/type-extractor-filter", "../model/file-model-node", "../services/core/name-id-service", "../model/dto-header-status-message", "./entity-labels", "../model/type-event-origin", "../model/name-id-label-type"], function (exports_1, context_1) {
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
    var core_1, name_id_1, type_entity_1, cv_filter_type_1, gobii_file_item_1, type_process_1, file_model_tree_service_1, type_extractor_filter_1, file_model_node_1, name_id_service_1, dto_header_status_message_1, entity_labels_1, type_event_origin_1, name_id_label_type_1, NameIdListBoxComponent;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (name_id_1_1) {
                name_id_1 = name_id_1_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (cv_filter_type_1_1) {
                cv_filter_type_1 = cv_filter_type_1_1;
            },
            function (gobii_file_item_1_1) {
                gobii_file_item_1 = gobii_file_item_1_1;
            },
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (file_model_tree_service_1_1) {
                file_model_tree_service_1 = file_model_tree_service_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (file_model_node_1_1) {
                file_model_node_1 = file_model_node_1_1;
            },
            function (name_id_service_1_1) {
                name_id_service_1 = name_id_service_1_1;
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
            function (name_id_label_type_1_1) {
                name_id_label_type_1 = name_id_label_type_1_1;
            }
        ],
        execute: function () {
            NameIdListBoxComponent = (function () {
                function NameIdListBoxComponent(_nameIdService, _fileModelTreeService, differs) {
                    this._nameIdService = _nameIdService;
                    this._fileModelTreeService = _fileModelTreeService;
                    this.differs = differs;
                    // useg
                    this.fileItemList = [];
                    this.notifyOnInit = false;
                    this.doTreeNotifications = true;
                    this.gobiiExtractFilterType = type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN;
                    this.selectedFileItemId = null;
                    this.onNameIdSelected = new core_1.EventEmitter();
                    this.onError = new core_1.EventEmitter();
                    this.currentSelection = null;
                    this.differ = differs.find({}).create(null);
                } // ctor
                // private notificationSent = false;
                NameIdListBoxComponent.prototype.ngOnInit = function () {
                    var _this = this;
                    var scope$ = this;
                    this._fileModelTreeService
                        .fileItemNotifications()
                        .subscribe(function (eventedFileItem) {
                        if (_this.doTreeNotifications) {
                            _this.updateSelectedItem(eventedFileItem);
                        }
                    }, function (responseHeader) {
                        _this.handleHeaderStatus(responseHeader);
                    });
                };
                NameIdListBoxComponent.prototype.updateSelectedItem = function (eventedFileItem) {
                    var fileItems = this.fileItemList;
                    var foo = "foo";
                    // we need to make sure that the evented item belongs to this control
                    // however, the incoming event may have other properties that changed, so we
                    // have to use the evented item
                    if (this.fileItemList
                        .find(function (fi) {
                        return fi.getFileItemUniqueId()
                            === eventedFileItem.getFileItemUniqueId();
                    })) {
                        if ((eventedFileItem.getGobiiEventOrigin() === type_event_origin_1.GobiiUIEventOrigin.CRITERIA_TREE)) {
                            if (this.nameIdRequestParams.getMameIdLabelType() != name_id_label_type_1.NameIdLabelType.UNKNOWN &&
                                (eventedFileItem.getProcessType() === type_process_1.ProcessType.DELETE)) {
                                this.selectedFileItemId = "0";
                            }
                            else {
                                this.selectedFileItemId = eventedFileItem.getItemId();
                            }
                            this.currentSelection = this.fileItemList[this.selectedFileItemId];
                        }
                    }
                };
                NameIdListBoxComponent.prototype.makeFileItemFromNameId = function (nameId, extractorItemType) {
                    return gobii_file_item_1.GobiiFileItem.build(this.gobiiExtractFilterType, type_process_1.ProcessType.CREATE)
                        .setEntityType(this.nameIdRequestParams.getEntityType())
                        .setEntitySubType(this.nameIdRequestParams.getEntitySubType())
                        .setCvFilterType(this.nameIdRequestParams.getCvFilterType())
                        .setExtractorItemType(extractorItemType)
                        .setItemName(nameId.name)
                        .setItemId(nameId.id);
                };
                NameIdListBoxComponent.prototype.initializeFileItems = function () {
                    var _this = this;
                    var scope$ = this;
                    this._nameIdService.get(this.nameIdRequestParams)
                        .subscribe(function (nameIds) {
                        _this.fileItemList = [];
                        if (nameIds && (nameIds.length > 0)) {
                            nameIds.forEach(function (ni) {
                                scope$.fileItemList.push(_this.makeFileItemFromNameId(ni, file_model_node_1.ExtractorItemType.ENTITY));
                            });
                            if (_this.nameIdRequestParams.getMameIdLabelType() != name_id_label_type_1.NameIdLabelType.UNKNOWN) {
                                var entityName = "";
                                if (scope$.nameIdRequestParams.getCvFilterType() !== cv_filter_type_1.CvFilterType.UNKNOWN) {
                                    entityName += entity_labels_1.Labels.instance().cvFilterNodeLabels[scope$.nameIdRequestParams.getCvFilterType()];
                                }
                                else if (scope$.nameIdRequestParams.getEntitySubType() !== type_entity_1.EntitySubType.UNKNOWN) {
                                    entityName += entity_labels_1.Labels.instance().entitySubtypeNodeLabels[scope$.nameIdRequestParams.getEntitySubType()];
                                }
                                else {
                                    entityName += entity_labels_1.Labels.instance().entityNodeLabels[scope$.nameIdRequestParams.getEntityType()];
                                }
                                var label = "";
                                switch (_this.nameIdRequestParams.getMameIdLabelType()) {
                                    case name_id_label_type_1.NameIdLabelType.SELECT_A:
                                        label = "Select a " + entityName;
                                        break;
                                    // we require that these entity labels all be in the singular
                                    case name_id_label_type_1.NameIdLabelType.ALL:
                                        label = "All " + entityName + "s";
                                        break;
                                    case name_id_label_type_1.NameIdLabelType.NO:
                                        label = "No " + entityName;
                                        break;
                                    default:
                                        _this.handleHeaderStatus(new dto_header_status_message_1.HeaderStatusMessage("Unknown label type "
                                            + name_id_label_type_1.NameIdLabelType[_this.nameIdRequestParams.getMameIdLabelType()], null, null));
                                }
                                var labelFileItem = _this.makeFileItemFromNameId(new name_id_1.NameId("0", label, _this.nameIdRequestParams.getEntityType()), file_model_node_1.ExtractorItemType.LABEL);
                                scope$.fileItemList.unshift(labelFileItem);
                                scope$.selectedFileItemId = "0";
                            }
                            else {
                                scope$.selectedFileItemId = scope$.fileItemList[0].getItemId();
                            }
                            scope$.currentSelection = scope$.fileItemList[0];
                            if (_this.notifyOnInit
                                && (_this.nameIdRequestParams.getMameIdLabelType() === name_id_label_type_1.NameIdLabelType.UNKNOWN)) {
                                _this.updateTreeService(scope$.fileItemList[0]);
                            }
                        }
                    }, function (responseHeader) {
                        _this.handleHeaderStatus(responseHeader);
                    });
                };
                NameIdListBoxComponent.prototype.handleHeaderStatus = function (headerStatusMessage) {
                    this.onError.emit(headerStatusMessage);
                };
                NameIdListBoxComponent.prototype.updateTreeService = function (eventedfileItem) {
                    var _this = this;
                    this.onNameIdSelected
                        .emit(new name_id_1.NameId(eventedfileItem.getItemId(), eventedfileItem.getItemName(), eventedfileItem.getEntityType()));
                    if (eventedfileItem.getItemId() != "0") {
                        if (this.doTreeNotifications) {
                            this._fileModelTreeService.put(eventedfileItem)
                                .subscribe(null, function (headerResponse) {
                                _this.handleHeaderStatus(headerResponse);
                            });
                        }
                    }
                };
                NameIdListBoxComponent.prototype.handleFileItemSelected = function (arg) {
                    var _this = this;
                    var foo = "foo";
                    if (this.currentSelection.getItemId() !== "0") {
                        this.currentSelection.setProcessType(type_process_1.ProcessType.DELETE);
                        this.updateTreeService(this.currentSelection);
                    }
                    //        let gobiiFileItem: GobiiFileItem = this.fileItemList[arg.srcElement.selectedIndex]
                    var gobiiFileItem = this.fileItemList.find(function (fi) {
                        return fi.getItemId() === _this.selectedFileItemId;
                    });
                    if (gobiiFileItem.getItemId() != "0") {
                        gobiiFileItem.setProcessType(type_process_1.ProcessType.UPDATE);
                        this.updateTreeService(gobiiFileItem);
                    }
                    this.currentSelection = gobiiFileItem;
                };
                NameIdListBoxComponent.prototype.ngOnChanges = function (changes) {
                    if (changes['gobiiExtractFilterType']
                        && (changes['gobiiExtractFilterType'].currentValue != null)
                        && (changes['gobiiExtractFilterType'].currentValue != undefined)) {
                        if (changes['gobiiExtractFilterType'].currentValue != changes['gobiiExtractFilterType'].previousValue) {
                            //this.notificationSent = false;
                            this.nameIdRequestParams.setGobiiExtractFilterType(this.gobiiExtractFilterType);
                            var scope$_1 = this;
                            this._fileModelTreeService
                                .fileItemNotifications()
                                .subscribe(function (fileItem) {
                                if (fileItem.getProcessType() === type_process_1.ProcessType.NOTIFY
                                    && fileItem.getExtractorItemType() === file_model_node_1.ExtractorItemType.STATUS_DISPLAY_TREE_READY) {
                                    scope$_1.initializeFileItems();
                                }
                            });
                        } // if we have a new filter type
                    } // if filter type changed
                    if (changes['nameIdRequestParams']
                        && (changes['nameIdRequestParams'].currentValue != null)
                        && (changes['nameIdRequestParams'].currentValue != undefined)) {
                    }
                }; // ngonChanges
                // angular change detection does not do deep comparison of objects that are
                // template properties. So we need to do some specialized change detection
                // references:
                //   https://juristr.com/blog/2016/04/angular2-change-detection/
                //   https://angular.io/docs/ts/latest/api/core/index/DoCheck-class.html
                //   http://blog.angular-university.io/how-does-angular-2-change-detection-really-work/
                //   https://blog.thoughtram.io/angular/2016/02/22/angular-2-change-detection-explained.html#what-causes-change
                NameIdListBoxComponent.prototype.ngDoCheck = function () {
                    var changes = this.differ.diff(this.nameIdRequestParams);
                    if (changes) {
                        if (this._nameIdService.validateRequest(this.nameIdRequestParams)) {
                            this.initializeFileItems();
                        }
                    }
                };
                return NameIdListBoxComponent;
            }()); // class
            NameIdListBoxComponent = __decorate([
                core_1.Component({
                    selector: 'name-id-list-box',
                    inputs: ['gobiiExtractFilterType',
                        'notifyOnInit',
                        'nameIdRequestParams',
                        'doTreeNotifications'],
                    outputs: ['onNameIdSelected', 'onError'],
                    template: "<select [(ngModel)]=\"selectedFileItemId\" (change)=\"handleFileItemSelected($event)\" >\n\t\t\t        <option *ngFor=\"let fileItem of fileItemList\" \n\t\t\t\t        [value]=\"fileItem.getItemId()\">{{fileItem.getItemName()}}</option>\n\t\t        </select>\n" // end template
                }),
                __metadata("design:paramtypes", [name_id_service_1.NameIdService,
                    file_model_tree_service_1.FileModelTreeService,
                    core_1.KeyValueDiffers])
            ], NameIdListBoxComponent);
            exports_1("NameIdListBoxComponent", NameIdListBoxComponent);
        }
    };
});
//# sourceMappingURL=name-id-list-box.component.js.map