System.register(["@angular/core", "../model/type-process", "../model/gobii-file-item", "../model/type-extractor-filter", "../model/cv-filter-type", "../services/core/file-model-tree-service", "../services/core/name-id-service", "../model/file-model-node"], function (exports_1, context_1) {
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
    var core_1, type_process_1, gobii_file_item_1, type_extractor_filter_1, cv_filter_type_1, file_model_tree_service_1, name_id_service_1, file_model_node_1, CheckListBoxComponent;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (gobii_file_item_1_1) {
                gobii_file_item_1 = gobii_file_item_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (cv_filter_type_1_1) {
                cv_filter_type_1 = cv_filter_type_1_1;
            },
            function (file_model_tree_service_1_1) {
                file_model_tree_service_1 = file_model_tree_service_1_1;
            },
            function (name_id_service_1_1) {
                name_id_service_1 = name_id_service_1_1;
            },
            function (file_model_node_1_1) {
                file_model_node_1 = file_model_node_1_1;
            }
        ],
        execute: function () {
            CheckListBoxComponent = (function () {
                function CheckListBoxComponent(_fileModelTreeService, _nameIdService, differs) {
                    this._fileModelTreeService = _fileModelTreeService;
                    this._nameIdService = _nameIdService;
                    this.differs = differs;
                    this.gobiiExtractFilterType = type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN;
                    this.onError = new core_1.EventEmitter();
                    this.fileItemEvents = [];
                    this.onItemChecked = new core_1.EventEmitter();
                    this.onItemSelected = new core_1.EventEmitter();
                    this.checkedFileItemHistory = [];
                    this.differ = differs.find({}).create(null);
                } // ctor
                CheckListBoxComponent.prototype.handleItemChecked = function (arg) {
                    var itemToChange = this.fileItemEvents.filter(function (e) {
                        return e.getItemId() === arg.currentTarget.value;
                    })[0];
                    //let indexOfItemToChange:number = this.fileItemEvents.indexOf(arg.currentTarget.name);
                    itemToChange.setProcessType(arg.currentTarget.checked ? type_process_1.ProcessType.CREATE : type_process_1.ProcessType.DELETE);
                    itemToChange.setChecked(arg.currentTarget.checked);
                    this.updateCheckedItemHistory(itemToChange);
                    this.onItemChecked.emit(itemToChange);
                    this.updateTreeService(itemToChange);
                }; // handleItemChecked()
                CheckListBoxComponent.prototype.updateCheckedItemHistory = function (fileItem) {
                    var historyFileItem = this
                        .checkedFileItemHistory
                        .find(function (fi) {
                        return (fi.getEntityType() === fileItem.getEntityType()
                            && fi.getItemId() === fileItem.getItemId()
                            && fi.getItemName() === fileItem.getItemName());
                    });
                    if (fileItem.getChecked() === true) {
                        if (historyFileItem == null) {
                            this.checkedFileItemHistory.push(fileItem);
                        }
                    }
                    else {
                        if (historyFileItem != null) {
                            var idx = this.checkedFileItemHistory.indexOf(historyFileItem);
                            this.checkedFileItemHistory.splice(idx, 1);
                        }
                    }
                };
                CheckListBoxComponent.prototype.wasItemPreviouslyChecked = function (fileItem) {
                    var checkedFileItem = this.checkedFileItemHistory.find(function (fi) {
                        return fi.getEntityType() === fileItem.getEntityType()
                            && fi.getItemId() === fileItem.getItemId()
                            && fi.getItemName() === fileItem.getItemName();
                    });
                    return checkedFileItem != null;
                };
                CheckListBoxComponent.prototype.handleItemSelected = function (arg) {
                    if (this.previousSelectedItem) {
                        this.previousSelectedItem.style = "";
                    }
                    arg.currentTarget.style = "background-color:#b3d9ff";
                    this.previousSelectedItem = arg.currentTarget;
                    var idValue = arg.currentTarget.children[0].value;
                    var selectedFileItem = this.fileItemEvents.filter(function (e) {
                        return e.getItemId() === idValue;
                    })[0];
                    // let fileItemEvent: GobiiFileItem = GobiiFileItem.build(
                    //     GobiiExtractFilterType.UNKNOWN,
                    //     ProcessType.READ)
                    //     .setEntityType(this.nameIdRequestParams.getEntityType())
                    //     .setCvFilterType(CvFilterType.UNKNOWN)
                    //     .setItemId(arg.currentTarget.children[0].value)
                    //     .setItemName(arg.currentTarget.children[0].name)
                    //     .setChecked(false)
                    //     .setRequired(false);
                    if (selectedFileItem) {
                        this.onItemSelected.emit(selectedFileItem);
                    }
                };
                CheckListBoxComponent.prototype.initializeNameIds = function () {
                    var _this = this;
                    var scope$ = this;
                    this._nameIdService.get(this.nameIdRequestParams)
                        .subscribe(function (nameIds) {
                        if (nameIds && (nameIds.length > 0)) {
                            scope$.nameIdList = nameIds;
                            scope$.setList(scope$.nameIdList);
                        }
                    }, function (responseHeader) {
                        _this.handleHeaderStatus(responseHeader);
                    });
                };
                CheckListBoxComponent.prototype.updateTreeService = function (fileItem) {
                    var _this = this;
                    this._fileModelTreeService.put(fileItem)
                        .subscribe(null, function (headerResponse) {
                        _this.handleHeaderStatus(headerResponse);
                    });
                };
                CheckListBoxComponent.prototype.handleHeaderStatus = function (headerStatusMessage) {
                    this.onError.emit(headerStatusMessage);
                };
                CheckListBoxComponent.prototype.setList = function (nameIdList) {
                    var _this = this;
                    // we can get this event whenver the item is clicked, not necessarily when the checkbox
                    var scope$ = this;
                    scope$.nameIdList = nameIdList;
                    if (scope$.nameIdList && (scope$.nameIdList.length > 0)) {
                        scope$.fileItemEvents = [];
                        if (!scope$.retainHistory) {
                            scope$.checkedFileItemHistory = [];
                        }
                        scope$.nameIdList.forEach(function (n) {
                            var currentFileItem = gobii_file_item_1.GobiiFileItem.build(_this.gobiiExtractFilterType, type_process_1.ProcessType.CREATE)
                                .setExtractorItemType(file_model_node_1.ExtractorItemType.ENTITY)
                                .setEntityType(_this.nameIdRequestParams.getEntityType())
                                .setCvFilterType(cv_filter_type_1.CvFilterType.UNKNOWN)
                                .setItemId(n.id)
                                .setItemName(n.name)
                                .setChecked(false)
                                .setRequired(false);
                            if (scope$.wasItemPreviouslyChecked(currentFileItem)) {
                                currentFileItem.setChecked(true);
                            }
                            scope$.fileItemEvents.push(currentFileItem);
                        });
                    }
                    else {
                        scope$.fileItemEvents = [];
                    }
                }; // setList()
                CheckListBoxComponent.prototype.ngOnInit = function () {
                    var _this = this;
                    this._fileModelTreeService
                        .fileItemNotifications()
                        .subscribe(function (eventedFileItem) {
                        if (eventedFileItem) {
                            var itemToChange = _this.fileItemEvents.find(function (e) {
                                return e.getEntityType() == eventedFileItem.getEntityType()
                                    && e.getItemName() == eventedFileItem.getItemName();
                            });
                            //let indexOfItemToChange:number = this.fileItemEvents.indexOf(arg.currentTarget.name);
                            if (itemToChange) {
                                itemToChange.setProcessType(eventedFileItem.getProcessType());
                                itemToChange.setChecked(eventedFileItem.getChecked());
                                _this.updateCheckedItemHistory(itemToChange);
                            }
                        }
                    }, function (responseHeader) {
                        _this.handleHeaderStatus(responseHeader);
                    });
                    if (this._nameIdService.validateRequest(this.nameIdRequestParams)) {
                        this.initializeNameIds();
                    }
                };
                CheckListBoxComponent.prototype.resetList = function () {
                    if (this._nameIdService.validateRequest(this.nameIdRequestParams)) {
                        this.initializeNameIds();
                    }
                    else {
                        this.setList([]);
                    }
                };
                CheckListBoxComponent.prototype.ngOnChanges = function (changes) {
                    var _this = this;
                    if (changes['gobiiExtractFilterType']
                        && (changes['gobiiExtractFilterType'].currentValue != null)
                        && (changes['gobiiExtractFilterType'].currentValue != undefined)) {
                        if (changes['gobiiExtractFilterType'].currentValue != changes['gobiiExtractFilterType'].previousValue) {
                            this.nameIdRequestParams.setGobiiExtractFilterType(this.gobiiExtractFilterType);
                            this.resetList();
                            this._fileModelTreeService
                                .fileItemNotifications()
                                .subscribe(function (fileItem) {
                                if (fileItem.getProcessType() === type_process_1.ProcessType.NOTIFY
                                    && fileItem.getExtractorItemType() === file_model_node_1.ExtractorItemType.STATUS_DISPLAY_TREE_READY) {
                                    _this.resetList();
                                }
                            });
                        } // if we have a new filter type
                    } // if filter type changed
                };
                CheckListBoxComponent.prototype.ngDoCheck = function () {
                    var changes = this.differ.diff(this.nameIdRequestParams);
                    if (changes) {
                        this.resetList();
                    }
                };
                return CheckListBoxComponent;
            }());
            CheckListBoxComponent = __decorate([
                core_1.Component({
                    selector: 'checklist-box',
                    inputs: ['gobiiExtractFilterType',
                        'nameIdRequestParams',
                        'retainHistory'],
                    outputs: ['onItemSelected',
                        'onItemChecked',
                        'onError'],
                    template: "<form>\n                    <div style=\"overflow:auto; height: 80px; border: 1px solid #336699; padding-left: 5px\">\n                        <div *ngFor=\"let fileItemEvent of fileItemEvents\" \n                            (click)=handleItemSelected($event) \n                            (hover)=handleItemHover($event)>\n                            <input  type=\"checkbox\" \n                                (click)=handleItemChecked($event)\n                                [checked]=\"fileItemEvent.getChecked()\"\n                                value={{fileItemEvent.getItemId()}} \n                                name=\"{{fileItemEvent.getItemName()}}\">&nbsp;{{fileItemEvent.getItemName()}}\n                        </div>            \n                    </div>\n                </form>" // end template
                }),
                __metadata("design:paramtypes", [file_model_tree_service_1.FileModelTreeService,
                    name_id_service_1.NameIdService,
                    core_1.KeyValueDiffers])
            ], CheckListBoxComponent);
            exports_1("CheckListBoxComponent", CheckListBoxComponent);
        }
    };
});
//# sourceMappingURL=checklist-box.component.js.map