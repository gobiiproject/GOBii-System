System.register(["@angular/core", "../../model/type-process", "../../model/analysis"], function(exports_1, context_1) {
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
    var core_1, type_process_1, analysis_1;
    var DtoRequestItemAnalysis;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (analysis_1_1) {
                analysis_1 = analysis_1_1;
            }],
        execute: function() {
            DtoRequestItemAnalysis = (function () {
                function DtoRequestItemAnalysis(analysisId) {
                    this.analysisId = analysisId;
                    this.processType = type_process_1.ProcessType.READ;
                    this.analysisId = analysisId;
                }
                DtoRequestItemAnalysis.prototype.getUrl = function () {
                    return "load/analysis";
                }; // getUrl()
                DtoRequestItemAnalysis.prototype.getRequestBody = function () {
                    return JSON.stringify({
                        "processType": type_process_1.ProcessType[this.processType],
                        "analysisId": this.analysisId
                    });
                };
                DtoRequestItemAnalysis.prototype.resultFromJson = function (json) {
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
                    returnVal = new analysis_1.Analysis(json.analysisId, json.analysisName, json.analysisDescription, json.anlaysisTypeId, json.program, json.programVersion, json.algorithm, json.sourceName, json.sourceVersion, json.sourceUri, json.referenceId, json.timeExecuted, json.status);
                    return returnVal;
                    //return [new NameId(1, 'foo'), new NameId(2, 'bar')];
                };
                DtoRequestItemAnalysis = __decorate([
                    core_1.Injectable(), 
                    __metadata('design:paramtypes', [Number])
                ], DtoRequestItemAnalysis);
                return DtoRequestItemAnalysis;
            }());
            exports_1("DtoRequestItemAnalysis", DtoRequestItemAnalysis); // DtoRequestItemNameIds() 
        }
    }
});
//# sourceMappingURL=dto-request-item-analysis.js.map