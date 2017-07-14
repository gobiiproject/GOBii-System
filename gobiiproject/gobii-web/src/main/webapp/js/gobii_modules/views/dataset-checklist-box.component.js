System.register(["@angular/core", "../services/core/dto-request.service", "../model/type-entity", "../model/type-entity-filter", "../services/core/file-model-tree-service", "../model/type-extractor-filter", "../model/name-id-request-params"], function (exports_1, context_1) {
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
    var core_1, dto_request_service_1, type_entity_1, type_entity_filter_1, file_model_tree_service_1, type_extractor_filter_1, name_id_request_params_1, DataSetCheckListBoxComponent;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (dto_request_service_1_1) {
                dto_request_service_1 = dto_request_service_1_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (type_entity_filter_1_1) {
                type_entity_filter_1 = type_entity_filter_1_1;
            },
            function (file_model_tree_service_1_1) {
                file_model_tree_service_1 = file_model_tree_service_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (name_id_request_params_1_1) {
                name_id_request_params_1 = name_id_request_params_1_1;
            }
        ],
        execute: function () {
            DataSetCheckListBoxComponent = (function () {
                function DataSetCheckListBoxComponent(_dtoRequestServiceDataSetDetail, _dtoRequestServiceAnalysisDetail, _fileModelTreeService) {
                    this._dtoRequestServiceDataSetDetail = _dtoRequestServiceDataSetDetail;
                    this._dtoRequestServiceAnalysisDetail = _dtoRequestServiceAnalysisDetail;
                    this._fileModelTreeService = _fileModelTreeService;
                    this.onItemChecked = new core_1.EventEmitter();
                    this.onAddStatusMessage = new core_1.EventEmitter();
                    this.analysisNames = [];
                    this.analysisTypes = [];
                    this.nameIdRequestParamsDataset = name_id_request_params_1.NameIdRequestParams
                        .build("Datasets-by-experiment-id", type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET, type_entity_1.EntityType.DataSets)
                        .setEntityFilter(type_entity_filter_1.EntityFilter.BYTYPEID);
                } // ctor
                DataSetCheckListBoxComponent.prototype.handleItemChecked = function (arg) {
                    arg.setRequired(false);
                    this.onItemChecked.emit(arg);
                }; // handleItemChecked()
                DataSetCheckListBoxComponent.prototype.handleAddStatusMessage = function (arg) {
                    var foo = "foo";
                    this.onAddStatusMessage.emit(arg);
                };
                DataSetCheckListBoxComponent.prototype.handleItemSelected = function (arg) {
                    var selectedDataSetId = Number(arg.itemId);
                    //this.setDatasetDetails(selectedDataSetId);
                };
                // private setList(): void {
                //
                //     // we can get this event whenver the item is clicked, not necessarily when the checkbox
                //     let scope$ = this;
                //
                //     scope$.dataSetsNameIdList = [];
                //     scope$.fileItemEvents = [];
                //     scope$.setDatasetDetails(undefined);
                //
                //     if (scope$.experimentId) {
                //
                //         this._dtoRequestServiceNameId.get(new DtoRequestItemNameIds(
                //             EntityType.DataSets,
                //             EntityFilter.BYTYPEID,
                //             this.experimentId)).subscribe(nameIds => {
                //                 if (nameIds && ( nameIds.length > 0 )) {
                //
                //                     scope$.dataSetsNameIdList = nameIds;
                //                     // scope$.fileItemEvents = [];
                //                     // scope$.dataSetsNameIdList.forEach(n => {
                //                     //     scope$.fileItemEvents.push(new GobiiFileItem(
                //                     //         ProcessType.CREATE,
                //                     //         n.id,
                //                     //         n.name,
                //                     //         false
                //                     //     ));
                //                     // });
                //
                //                     scope$.setDatasetDetails(Number(scope$.dataSetsNameIdList[0].id));
                //
                //                 } else {
                //                     scope$.dataSetsNameIdList = [new NameId("0", "<none>", EntityType.DataSets)];
                //                     scope$.setDatasetDetails(undefined);
                //                 }
                //             },
                //             dtoHeaderResponse => {
                //                 dtoHeaderResponse.statusMessages.forEach(m => scope$.handleAddMessage("Retrieving dataset names by experiment id: "
                //                     + ": "
                //                     + m.message))
                //             });
                //
                //     }  else {
                //         scope$.dataSetsNameIdList = [];
                //     }// if we have an experiment id
                //
                // } // setList()
                // private setDatasetDetails(dataSetId: number): void {
                //
                //     if (dataSetId) {
                //         let scope$ = this;
                //         scope$._dtoRequestServiceDataSetDetail.get(new DtoRequestItemDataSet(dataSetId))
                //             .subscribe(dataSet => {
                //
                //                     if (dataSet) {
                //
                //                         scope$.dataSet = dataSet;
                //                         scope$.analysisNames = [];
                //                         scope$.analysisTypes = [];
                //
                //                         scope$.dataSet.analysesIds.forEach(
                //                             analysisId => {
                //                                 let currentAnalysisId: number = analysisId;
                //                                 if (currentAnalysisId) {
                //                                     scope$._dtoRequestServiceAnalysisDetail
                //                                         .getResult(new DtoRequestItemAnalysis(currentAnalysisId))
                //                                         .subscribe(analysis => {
                //                                                 scope$.analysisNames.push(analysis.analysisName);
                //                                                 if (analysis.anlaysisTypeId && scope$.nameIdListAnalysisTypes) {
                //
                //                                                     scope$
                //                                                         .nameIdListAnalysisTypes
                //                                                         .forEach(t => {
                //                                                             if (Number(t.id) === analysis.anlaysisTypeId) {
                //                                                                 scope$.analysisTypes.push(t.name);
                //                                                             }
                //                                                         });
                //
                //
                //                                                 } // if we have an analysis type id
                //                                             },
                //                                             headerStatusMessage => {
                //                                                 scope$.handleAddStatusMessage(headerStatusMessage)
                //                                             });
                //                                 }
                //                             }
                //                         );
                //                     }
                //                 },
                //                 dtoHeaderResponse => {
                //                     dtoHeaderResponse.statusMessages.forEach(m => scope$.handleAddStatusMessage(m));
                //
                //                 });
                //     } else {
                //
                //         this.dataSet = undefined;
                //         this.analysisNames = [];
                //         this.analysisTypes = [];
                //
                //     } // if else we got a dataset id
                // } // setList()
                DataSetCheckListBoxComponent.prototype.ngOnInit = function () {
                    if (this.experimentId != null) {
                        this.nameIdRequestParamsDataset.setEntityFilterValue(this.experimentId);
                    }
                };
                DataSetCheckListBoxComponent.prototype.ngOnChanges = function (changes) {
                    if (changes['experimentId']) {
                        this.experimentId = changes['experimentId'].currentValue;
                        this.nameIdRequestParamsDataset.setEntityFilterValue(this.experimentId);
                    }
                };
                return DataSetCheckListBoxComponent;
            }());
            DataSetCheckListBoxComponent = __decorate([
                core_1.Component({
                    selector: 'dataset-checklist-box',
                    inputs: ['experimentId', 'fileItemEventChange', 'gobiiExtractFilterType'],
                    outputs: ['onItemChecked', 'onAddStatusMessage'],
                    template: "<checklist-box\n                    [gobiiExtractFilterType] = \"gobiiExtractFilterType\"\n                    [nameIdRequestParams] = \"nameIdRequestParamsDataset\"\n                    [retainHistory] = \"true\"\n                    (onError) = \"handleAddStatusMessage($event)\"\n                    (onItemSelected)=\"handleItemSelected($event)\">\n                </checklist-box>\n                <div *ngIf=\"dataSet\">\n                    <BR>\n                     <fieldset>\n                        <b>Name:</b> {{dataSet.name}}<BR>\n                        <b>Data Table:</b> {{dataSet.dataTable}}<BR>\n                        <b>Data File:</b> {{dataSet.dataFile}}<BR>\n                        <b>Quality Table:</b> {{dataSet.qualityTable}}<BR>\n                        <b>Quality File:</b> {{dataSet.qualityFile}}<BR>\n                        <div *ngIf=\"analysisNames && (analysisNames.length > 0)\">\n                            <b>Analyses:</b> <ul style=\"list-style-type:none\">\n                                            <li *ngFor= \"let analysisName of analysisNames\" >{{analysisName}}</li>\n                                    </ul>\n                        </div>\n                        <div *ngIf=\"analysisTypes && (analysisTypes.length > 0)\">\n                            <b>Analysis Types:</b> <ul style=\"list-style-type:none\">\n                                            <li *ngFor= \"let analysisType of analysisTypes\" >{{analysisType}}</li>\n                                    </ul>\n                        </div>\n                      </fieldset> \n                </div>                \n" // end template
                }),
                __metadata("design:paramtypes", [dto_request_service_1.DtoRequestService,
                    dto_request_service_1.DtoRequestService,
                    file_model_tree_service_1.FileModelTreeService])
            ], DataSetCheckListBoxComponent);
            exports_1("DataSetCheckListBoxComponent", DataSetCheckListBoxComponent);
        }
    };
});
//# sourceMappingURL=dataset-checklist-box.component.js.map