System.register(["@angular/core", "../model/name-id", "../services/core/dto-request.service", "../services/app/dto-request-item-nameids", "../model/type-process", "../model/type-entity", "../model/event-checkbox", "../services/app/dto-request-item-dataset", "../services/app/dto-request-item-analysis"], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
        var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
        if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
        else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
        return c > 3 && r && Object.defineProperty(target, key, r), r;
    };
    var __metadata = (this && this.__metadata) || function (k, v) {
        if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
    };
    var core_1, name_id_1, dto_request_service_1, dto_request_item_nameids_1, type_process_1, type_entity_1, event_checkbox_1, dto_request_item_dataset_1, dto_request_item_analysis_1;
    var DataSetCheckListBoxComponent;
    return {
        setters:[
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
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (event_checkbox_1_1) {
                event_checkbox_1 = event_checkbox_1_1;
            },
            function (dto_request_item_dataset_1_1) {
                dto_request_item_dataset_1 = dto_request_item_dataset_1_1;
            },
            function (dto_request_item_analysis_1_1) {
                dto_request_item_analysis_1 = dto_request_item_analysis_1_1;
            }],
        execute: function() {
            DataSetCheckListBoxComponent = (function () {
                function DataSetCheckListBoxComponent(_dtoRequestServiceNameId, _dtoRequestServiceDataSetDetail, _dtoRequestServiceAnalysisDetail) {
                    this._dtoRequestServiceNameId = _dtoRequestServiceNameId;
                    this._dtoRequestServiceDataSetDetail = _dtoRequestServiceDataSetDetail;
                    this._dtoRequestServiceAnalysisDetail = _dtoRequestServiceAnalysisDetail;
                    this.checkBoxEvents = [];
                    this.onItemChecked = new core_1.EventEmitter();
                    this.onAddMessage = new core_1.EventEmitter();
                    this.analysisNames = [];
                    this.analysisTypes = [];
                } // ctor
                DataSetCheckListBoxComponent.prototype.handleItemChecked = function (arg) {
                    var itemToChange = this.checkBoxEvents.filter(function (e) {
                        return e.id == arg.currentTarget.value;
                    })[0];
                    //let indexOfItemToChange:number = this.checkBoxEvents.indexOf(arg.currentTarget.name);
                    itemToChange.processType = arg.currentTarget.checked ? type_process_1.ProcessType.CREATE : type_process_1.ProcessType.DELETE;
                    itemToChange.checked = arg.currentTarget.checked;
                    this.onItemChecked.emit(itemToChange);
                }; // handleItemChecked()
                DataSetCheckListBoxComponent.prototype.handleAddMessage = function (arg) {
                    this.onAddMessage.emit(arg);
                };
                DataSetCheckListBoxComponent.prototype.handleItemSelected = function (arg) {
                    var selectedDataSetId = Number(arg.currentTarget.children[0].value);
                    if (this.previousSelectedItem) {
                        this.previousSelectedItem.style = "";
                    }
                    arg.currentTarget.style = "background-color:#b3d9ff";
                    this.previousSelectedItem = arg.currentTarget;
                    this.setDatasetDetails(selectedDataSetId);
                };
                DataSetCheckListBoxComponent.prototype.setList = function () {
                    // we can get this event whenver the item is clicked, not necessarily when the checkbox
                    var scope$ = this;
                    scope$.nameIdList = [];
                    scope$.checkBoxEvents = [];
                    scope$.setDatasetDetails(undefined);
                    if (scope$.experimentId) {
                        this._dtoRequestServiceNameId.getResult(new dto_request_item_nameids_1.DtoRequestItemNameIds(type_process_1.ProcessType.READ, type_entity_1.EntityType.DataSetNamesByExperimentId, this.experimentId)).subscribe(function (nameIds) {
                            if (nameIds && (nameIds.length > 0)) {
                                scope$.nameIdList = nameIds;
                                scope$.checkBoxEvents = [];
                                scope$.nameIdList.forEach(function (n) {
                                    scope$.checkBoxEvents.push(new event_checkbox_1.CheckBoxEvent(type_process_1.ProcessType.CREATE, n.id, n.name, false));
                                });
                                scope$.setDatasetDetails(scope$.nameIdList[0].id);
                            }
                            else {
                                scope$.nameIdList = [new name_id_1.NameId(0, "<none>")];
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
                        scope$_1._dtoRequestServiceDataSetDetail.getResult(new dto_request_item_dataset_1.DtoRequestItemDataSet(dataSetId))
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
                    var scope$ = this;
                    scope$._dtoRequestServiceNameId
                        .getResult(new dto_request_item_nameids_1.DtoRequestItemNameIds(type_process_1.ProcessType.READ, type_entity_1.EntityType.CvGroupTerms, "analysis_type"))
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
                    if (changes['checkBoxEventChange'] && changes['checkBoxEventChange'].currentValue) {
                        this.itemChangedEvent = changes['checkBoxEventChange'].currentValue;
                        if (this.itemChangedEvent) {
                            var itemToChange = this.checkBoxEvents.filter(function (e) {
                                return e.id == changes['checkBoxEventChange'].currentValue.id;
                            })[0];
                            //let indexOfItemToChange:number = this.checkBoxEvents.indexOf(arg.currentTarget.name);
                            if (itemToChange) {
                                itemToChange.processType = changes['checkBoxEventChange'].currentValue.processType;
                                itemToChange.checked = changes['checkBoxEventChange'].currentValue.checked;
                            }
                        }
                    }
                };
                DataSetCheckListBoxComponent = __decorate([
                    core_1.Component({
                        selector: 'dataset-checklist-box',
                        inputs: ['experimentId', 'checkBoxEventChange'],
                        outputs: ['onItemChecked', 'onAddMessage'],
                        template: "<form>\n                    <div style=\"overflow:auto; height: 80px; border: 1px solid #336699; padding-left: 5px\">\n                        <div *ngFor=\"let checkBoxEvent of checkBoxEvents\" \n                            (click)=handleItemSelected($event) \n                            (hover)=handleItemHover($event)>\n                            <input  type=\"checkbox\" \n                                (click)=handleItemChecked($event)\n                                [checked]=\"checkBoxEvent.checked\"\n                                value={{checkBoxEvent.id}} \n                                name=\"{{checkBoxEvent.name}}\">&nbsp;{{checkBoxEvent.name}}\n                        </div>            \n                    </div>\n                </form>\n                <div *ngIf=\"dataSet\">\n                    <BR>\n                     <fieldset>\n                        <b>Name:</b> {{dataSet.name}}<BR>\n                        <b>Data Table:</b> {{dataSet.dataTable}}<BR>\n                        <b>Data File:</b> {{dataSet.dataFile}}<BR>\n                        <b>Quality Table:</b> {{dataSet.qualityTable}}<BR>\n                        <b>Quality File:</b> {{dataSet.qualityFile}}<BR>\n                        <div *ngIf=\"analysisNames && (analysisNames.length > 0)\">\n                            <b>Analyses:</b> <ul style=\"list-style-type:none\">\n                                            <li *ngFor= \"let analysisName of analysisNames\" >{{analysisName}}</li>\n                                    </ul>\n                        </div>\n                        <div *ngIf=\"analysisTypes && (analysisTypes.length > 0)\">\n                            <b>Analysis Types:</b> <ul style=\"list-style-type:none\">\n                                            <li *ngFor= \"let analysisType of analysisTypes\" >{{analysisType}}</li>\n                                    </ul>\n                        </div>\n                      </fieldset> \n                </div>                \n" // end template
                    }), 
                    __metadata('design:paramtypes', [dto_request_service_1.DtoRequestService, dto_request_service_1.DtoRequestService, dto_request_service_1.DtoRequestService])
                ], DataSetCheckListBoxComponent);
                return DataSetCheckListBoxComponent;
            }());
            exports_1("DataSetCheckListBoxComponent", DataSetCheckListBoxComponent);
        }
    }
});
//# sourceMappingURL=dataset-checklist-box.component.js.map