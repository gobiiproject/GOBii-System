System.register(["@angular/core", "../../model/type-process", "../../model/cv"], function(exports_1, context_1) {
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
    var core_1, type_process_1, cv_1;
    var DtoRequestItemCv;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (cv_1_1) {
                cv_1 = cv_1_1;
            }],
        execute: function() {
            DtoRequestItemCv = (function () {
                function DtoRequestItemCv(cvId) {
                    this.cvId = cvId;
                    this.processType = type_process_1.ProcessType.READ;
                    this.cvId = cvId;
                }
                DtoRequestItemCv.prototype.getUrl = function () {
                    return "load/cv";
                }; // getUrl()
                DtoRequestItemCv.prototype.getRequestBody = function () {
                    return JSON.stringify({
                        "processType": type_process_1.ProcessType[this.processType],
                        "cvId": this.cvId
                    });
                };
                DtoRequestItemCv.prototype.resultFromJson = function (json) {
                    var returnVal;
                    console.log("*************ENTITY NAME: " + json.entityName);
                    console.log(json.dtoHeaderResponse.succeeded ? "succeeded" : "error: " + json.dtoHeaderResponse.statusMessages);
                    console.log(json.namesById);
                    returnVal = new cv_1.Cv(json.cv_id, json.group, json.term, json.definition, json.rank);
                    return returnVal;
                    //return [new NameId(1, 'foo'), new NameId(2, 'bar')];
                };
                DtoRequestItemCv = __decorate([
                    core_1.Injectable(), 
                    __metadata('design:paramtypes', [Number])
                ], DtoRequestItemCv);
                return DtoRequestItemCv;
            }());
            exports_1("DtoRequestItemCv", DtoRequestItemCv); // DtoRequestItemNameIds() 
        }
    }
});
//# sourceMappingURL=dto-request-item-cv.js.map