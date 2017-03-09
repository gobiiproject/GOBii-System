System.register(["@angular/core", "../model/name-id", "../services/core/dto-request.service", "../services/app/dto-request-item-nameids", "../model/type-entity", "../services/app/dto-request-item-dataset", "../services/app/dto-request-item-analysis", "../model/type-entity-filter", "../model/cv-filter-type", "../services/core/file-model-tree-service"], function (exports_1, context_1) {
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
    var core_1, name_id_1, dto_request_service_1, dto_request_item_nameids_1, type_entity_1, dto_request_item_dataset_1, dto_request_item_analysis_1, type_entity_filter_1, cv_filter_type_1, file_model_tree_service_1, DataSetCheckListBoxComponent;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (name_id_1_1) {
                name_id_1 = name_id_1_1;
            },
            function (dto_request_service_1_1) {
                dto_request_service_1 = dto_request_service_1_1;
            },
            function (dto_request_item_nameids_1_1) {
                dto_request_item_nameids_1 = dto_request_item_nameids_1_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (dto_request_item_dataset_1_1) {
                dto_request_item_dataset_1 = dto_request_item_dataset_1_1;
            },
            function (dto_request_item_analysis_1_1) {
                dto_request_item_analysis_1 = dto_request_item_analysis_1_1;
            },
            function (type_entity_filter_1_1) {
                type_entity_filter_1 = type_entity_filter_1_1;
            },
            function (cv_filter_type_1_1) {
                cv_filter_type_1 = cv_filter_type_1_1;
            },
            function (file_model_tree_service_1_1) {
                file_model_tree_service_1 = file_model_tree_service_1_1;
            }
        ],
        execute: function () {
            DataSetCheckListBoxComponent = (function () {
                function DataSetCheckListBoxComponent(_dtoRequestServiceNameId, _dtoRequestServiceDataSetDetail, _dtoRequestServiceAnalysisDetail, _fileModelTreeService) {
                    this._dtoRequestServiceNameId = _dtoRequestServiceNameId;
                    this._dtoRequestServiceDataSetDetail = _dtoRequestServiceDataSetDetail;
                    this._dtoRequestServiceAnalysisDetail = _dtoRequestServiceAnalysisDetail;
                    this._fileModelTreeService = _fileModelTreeService;
                    this.fileItemEvents = [];
                    this.onItemChecked = new core_1.EventEmitter();
                    this.onAddMessage = new core_1.EventEmitter();
                    this.analysisNames = [];
                    this.analysisTypes = [];
                } // ctor
                DataSetCheckListBoxComponent.prototype.handleItemChecked = function (arg) {
                    arg.setRequired(false);
                    this.onItemChecked.emit(arg);
                }; // handleItemChecked()
                DataSetCheckListBoxComponent.prototype.handleAddMessage = function (arg) {
                    this.onAddMessage.emit(arg);
                };
                DataSetCheckListBoxComponent.prototype.handleItemSelected = function (arg) {
                    var selectedDataSetId = Number(arg.itemId);
                    this.setDatasetDetails(selectedDataSetId);
                };
                DataSetCheckListBoxComponent.prototype.setList = function () {
                    // we can get this event whenver the item is clicked, not necessarily when the checkbox
                    var scope$ = this;
                    scope$.dataSetsNameIdList = [];
                    scope$.fileItemEvents = [];
                    scope$.setDatasetDetails(undefined);
                    if (scope$.experimentId) {
                        this._dtoRequestServiceNameId.get(new dto_request_item_nameids_1.DtoRequestItemNameIds(type_entity_1.EntityType.DataSets, type_entity_filter_1.EntityFilter.BYTYPEID, this.experimentId)).subscribe(function (nameIds) {
                            if (nameIds && (nameIds.length > 0)) {
                                scope$.dataSetsNameIdList = nameIds;
                                // scope$.fileItemEvents = [];
                                // scope$.dataSetsNameIdList.forEach(n => {
                                //     scope$.fileItemEvents.push(new FileItem(
                                //         ProcessType.CREATE,
                                //         n.id,
                                //         n.name,
                                //         false
                                //     ));
                                // });
                                scope$.setDatasetDetails(Number(scope$.dataSetsNameIdList[0].id));
                            }
                            else {
                                scope$.dataSetsNameIdList = [new name_id_1.NameId("0", "<none>", type_entity_1.EntityType.DataSets)];
                                scope$.setDatasetDetails(undefined);
                            }
                        }, function (dtoHeaderResponse) {
                            dtoHeaderResponse.statusMessages.forEach(function (m) { return scope$.handleAddMessage("Retrieving dataset names by experiment id: "
                                + ": "
                                + m.message); });
                        });
                    } // if we have an experiment id
                }; // setList()
                DataSetCheckListBoxComponent.prototype.setDatasetDetails = function (dataSetId) {
                    if (dataSetId) {
                        var scope$_1 = this;
                        scope$_1._dtoRequestServiceDataSetDetail.get(new dto_request_item_dataset_1.DtoRequestItemDataSet(dataSetId))
                            .subscribe(function (dataSet) {
                            if (dataSet) {
                                scope$_1.dataSet = dataSet;
                                scope$_1.analysisNames = [];
                                scope$_1.analysisTypes = [];
                                scope$_1.dataSet.analysesIds.forEach(function (analysisId) {
                                    var currentAnalysisId = analysisId;
                                    if (currentAnalysisId) {
                                        scope$_1._dtoRequestServiceAnalysisDetail
                                            .getResult(new dto_request_item_analysis_1.DtoRequestItemAnalysis(currentAnalysisId))
                                            .subscribe(function (analysis) {
                                            scope$_1.analysisNames.push(analysis.analysisName);
                                            if (analysis.anlaysisTypeId && scope$_1.nameIdListAnalysisTypes) {
                                                scope$_1
                                                    .nameIdListAnalysisTypes
                                                    .forEach(function (t) {
                                                    if (Number(t.id) === analysis.anlaysisTypeId) {
                                                        scope$_1.analysisTypes.push(t.name);
                                                    }
                                                });
                                            } // if we have an analysis type id
                                        }, function (dtoHeaderResponse) {
                                            dtoHeaderResponse.statusMessages.forEach(function (m) { return scope$_1.handleAddMessage(m.message); });
                                        });
                                    }
                                });
                            }
                        }, function (dtoHeaderResponse) {
                            dtoHeaderResponse.statusMessages.forEach(function (m) { return scope$_1.handleAddMessage(m.message); });
                        });
                    }
                    else {
                        this.dataSet = undefined;
                        this.analysisNames = [];
                        this.analysisTypes = [];
                    } // if else we got a dataset id
                }; // setList()
                DataSetCheckListBoxComponent.prototype.ngOnInit = function () {
                    var _this = this;
                    this._fileModelTreeService
                        .fileItemNotifications()
                        .subscribe(function (fileItem) {
                        _this.datasetFileItemEventChange = fileItem;
                    });
                    var scope$ = this;
                    scope$._dtoRequestServiceNameId
                        .get(new dto_request_item_nameids_1.DtoRequestItemNameIds(type_entity_1.EntityType.CvTerms, type_entity_filter_1.EntityFilter.BYTYPENAME, cv_filter_type_1.CvFilters.get(cv_filter_type_1.CvFilterType.ANALYSIS_TYPE)))
                        .subscribe(function (nameIdList) {
                        scope$.nameIdListAnalysisTypes = nameIdList;
                        if (_this.experimentId) {
                            _this.setList();
                        }
                    }, function (dtoHeaderResponse) {
                        dtoHeaderResponse.statusMessages.forEach(function (m) { return scope$.handleAddMessage(m.message); });
                    });
                };
                DataSetCheckListBoxComponent.prototype.ngOnChanges = function (changes) {
                    if (changes['experimentId']) {
                        this.experimentId = changes['experimentId'].currentValue;
                        this.setList();
                    }
                };
                return DataSetCheckListBoxComponent;
            }());
            DataSetCheckListBoxComponent = __decorate([
                core_1.Component({
                    selector: 'dataset-checklist-box',
                    inputs: ['experimentId', 'fileItemEventChange'],
                    outputs: ['onItemChecked', 'onAddMessage'],
                    template: "<checklist-box\n                    [fileItemEventChange] = \"datasetFileItemEventChange\"\n                    [nameIdList] = \"dataSetsNameIdList\"\n                    (onItemSelected)=\"handleItemSelected($event)\"\n                    (onItemChecked)=\"handleItemChecked($event)\"\n                    (onAddMessage) = \"handleAddMessage($event)\">\n                </checklist-box>\n                <div *ngIf=\"dataSet\">\n                    <BR>\n                     <fieldset>\n                        <b>Name:</b> {{dataSet.name}}<BR>\n                        <b>Data Table:</b> {{dataSet.dataTable}}<BR>\n                        <b>Data File:</b> {{dataSet.dataFile}}<BR>\n                        <b>Quality Table:</b> {{dataSet.qualityTable}}<BR>\n                        <b>Quality File:</b> {{dataSet.qualityFile}}<BR>\n                        <div *ngIf=\"analysisNames && (analysisNames.length > 0)\">\n                            <b>Analyses:</b> <ul style=\"list-style-type:none\">\n                                            <li *ngFor= \"let analysisName of analysisNames\" >{{analysisName}}</li>\n                                    </ul>\n                        </div>\n                        <div *ngIf=\"analysisTypes && (analysisTypes.length > 0)\">\n                            <b>Analysis Types:</b> <ul style=\"list-style-type:none\">\n                                            <li *ngFor= \"let analysisType of analysisTypes\" >{{analysisType}}</li>\n                                    </ul>\n                        </div>\n                      </fieldset> \n                </div>                \n" // end template
                }),
                __metadata("design:paramtypes", [dto_request_service_1.DtoRequestService,
                    dto_request_service_1.DtoRequestService,
                    dto_request_service_1.DtoRequestService,
                    file_model_tree_service_1.FileModelTreeService])
            ], DataSetCheckListBoxComponent);
            exports_1("DataSetCheckListBoxComponent", DataSetCheckListBoxComponent);
        }
    };
});
//# sourceMappingURL=dataset-checklist-box.component.js.map