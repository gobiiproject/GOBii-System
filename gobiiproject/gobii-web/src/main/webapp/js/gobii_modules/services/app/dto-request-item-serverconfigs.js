System.register(["@angular/core", "../../model/type-process", "../../model/server-config"], function (exports_1, context_1) {
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
    var core_1, type_process_1, server_config_1, DtoRequestItemServerConfigs;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (server_config_1_1) {
                server_config_1 = server_config_1_1;
            }
        ],
        execute: function () {
            DtoRequestItemServerConfigs = (function () {
                function DtoRequestItemServerConfigs() {
                    this.processType = type_process_1.ProcessType.READ;
                }
                DtoRequestItemServerConfigs.prototype.getUrl = function () {
                    return "gobii/v1/configsettings";
                }; // getUrl()
                DtoRequestItemServerConfigs.prototype.getRequestBody = function () {
                    return JSON.stringify({
                        "processType": type_process_1.ProcessType[this.processType],
                    });
                };
                DtoRequestItemServerConfigs.prototype.resultFromJson = function (json) {
                    var returnVal = [];
                    var serverConfigs = json.payload.data[0].serverConfigs;
                    var arrayOfIds = Object.keys(serverConfigs);
                    arrayOfIds.forEach(function (crop) {
                        var currentCrop = crop;
                        var currentDomain = serverConfigs[crop].domain;
                        var currentContextRoot = serverConfigs[crop].contextRoot;
                        var currentPort = Number(serverConfigs[crop].port);
                        returnVal.push(new server_config_1.ServerConfig(currentCrop, currentDomain, currentContextRoot, currentPort));
                    });
                    return returnVal;
                    //return [new NameId(1, 'foo'), new NameId(2, 'bar')];
                };
                return DtoRequestItemServerConfigs;
            }()); // DtoRequestItemNameIds() 
            DtoRequestItemServerConfigs = __decorate([
                core_1.Injectable(),
                __metadata("design:paramtypes", [])
            ], DtoRequestItemServerConfigs);
            exports_1("DtoRequestItemServerConfigs", DtoRequestItemServerConfigs);
        }
    };
});
//# sourceMappingURL=dto-request-item-serverconfigs.js.map