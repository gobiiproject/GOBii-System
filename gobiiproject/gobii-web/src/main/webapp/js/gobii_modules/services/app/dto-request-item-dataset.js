System.register(["@angular/core", "../../model/type-process", "../../model/dataset"], function(exports_1, context_1) {
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
    var core_1, type_process_1, dataset_1;
    var DtoRequestItemDataSet;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (dataset_1_1) {
                dataset_1 = dataset_1_1;
            }],
        execute: function() {
            DtoRequestItemDataSet = (function () {
                function DtoRequestItemDataSet(dataSetId) {
                    this.dataSetId = dataSetId;
                    this.processType = type_process_1.ProcessType.READ;
                    this.dataSetId = dataSetId;
                }
                DtoRequestItemDataSet.prototype.getUrl = function () {
                    return "load/dataset";
                }; // getUrl()
                DtoRequestItemDataSet.prototype.getRequestBody = function () {
                    return JSON.stringify({
                        "processType": type_process_1.ProcessType[this.processType],
                        "dataSetId": this.dataSetId
                    });
                };
                DtoRequestItemDataSet.prototype.resultFromJson = function (json) {
                    var returnVal;
                    console.log("*************ENTITY NAME: " + json.entityName);
                    console.log(json.dtoHeaderResponse.succeeded ? "succeeded" : "error: " + json.dtoHeaderResponse.statusMessages);
                    console.log(json.namesById);
                    // let arrayOfIds = Object.keys(json.serverConfigs);
                    // arrayOfIds.forEach(crop => {
                    //     let currentCrop = crop;
                    //     let currentDomain:string = json.serverConfigs[crop].domain;
                    //     let currentContextRoot:string = json.serverConfigs[crop].contextRoot;
                    //     let currentPort:number = Number(json.serverConfigs[crop].port);
                    //     returnVal.push(new ServerConfig(currentCrop,
                    //         currentDomain,
                    //         currentContextRoot,
                    //         currentPort));
                    // });
                    returnVal = new dataset_1.DataSet(json.dataSetId, json.name, json.experimentId, json.callingAnalysisId, json.dataTable, json.dataFile, json.qualityTable, json.qualityFile, json.status, json.typeId, json.analysesIds);
                    return returnVal;
                    //return [new NameId(1, 'foo'), new NameId(2, 'bar')];
                };
                DtoRequestItemDataSet = __decorate([
                    core_1.Injectable(), 
                    __metadata('design:paramtypes', [Number])
                ], DtoRequestItemDataSet);
                return DtoRequestItemDataSet;
            }());
            exports_1("DtoRequestItemDataSet", DtoRequestItemDataSet); // DtoRequestItemNameIds() 
        }
    }
});
//# sourceMappingURL=dto-request-item-dataset.js.map