System.register(["@angular/core", "../../model/type-process", "../../model/server-config"], function(exports_1, context_1) {
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
    var core_1, type_process_1, server_config_1;
    var DtoRequestItemServerConfigs;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (server_config_1_1) {
                server_config_1 = server_config_1_1;
            }],
        execute: function() {
            DtoRequestItemServerConfigs = (function () {
                function DtoRequestItemServerConfigs() {
                    this.processType = type_process_1.ProcessType.READ;
                }
                DtoRequestItemServerConfigs.prototype.getUrl = function () {
                    return "load/configsettings";
                }; // getUrl()
                DtoRequestItemServerConfigs.prototype.getRequestBody = function () {
                    return JSON.stringify({
                        "processType": type_process_1.ProcessType[this.processType],
                    });
                };
                DtoRequestItemServerConfigs.prototype.resultFromJson = function (json) {
                    var returnVal = [];
                    console.log("*************ENTITY NAME: " + json.entityName);
                    console.log(json.dtoHeaderResponse.succeeded ? "succeeded" : "error: " + json.dtoHeaderResponse.statusMessages);
                    console.log(json.namesById);
                    var arrayOfIds = Object.keys(json.serverConfigs);
                    arrayOfIds.forEach(function (crop) {
                        var currentCrop = crop;
                        var currentDomain = json.serverConfigs[crop].domain;
                        var currentContextRoot = json.serverConfigs[crop].contextRoot;
                        var currentPort = Number(json.serverConfigs[crop].port);
                        returnVal.push(new server_config_1.ServerConfig(currentCrop, currentDomain, currentContextRoot, currentPort));
                    });
                    return returnVal;
                    //return [new NameId(1, 'foo'), new NameId(2, 'bar')];
                };
                DtoRequestItemServerConfigs = __decorate([
                    core_1.Injectable(), 
                    __metadata('design:paramtypes', [])
                ], DtoRequestItemServerConfigs);
                return DtoRequestItemServerConfigs;
            }());
            exports_1("DtoRequestItemServerConfigs", DtoRequestItemServerConfigs); // DtoRequestItemNameIds() 
        }
    }
});
//# sourceMappingURL=dto-request-item-serverconfigs.js.map