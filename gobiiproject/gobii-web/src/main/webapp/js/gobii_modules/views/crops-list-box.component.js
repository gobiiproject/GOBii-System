System.register(["@angular/core"], function(exports_1, context_1) {
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
    var core_1;
    var CropsListBoxComponent;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            }],
        execute: function() {
            CropsListBoxComponent = (function () {
                function CropsListBoxComponent() {
                    this.onServerSelected = new core_1.EventEmitter();
                }
                CropsListBoxComponent.prototype.handleServerSelected = function (arg) {
                    this.onServerSelected.emit(this.serverConfigList[arg.srcElement.selectedIndex]);
                };
                CropsListBoxComponent.prototype.ngOnInit = function () {
                    return null;
                };
                CropsListBoxComponent.prototype.ngOnChanges = function (changes) {
                    this.selectedServerConfig = changes['selectedServerConfig'].currentValue;
                };
                CropsListBoxComponent = __decorate([
                    core_1.Component({
                        selector: 'crops-list-box',
                        inputs: ['serverConfigList', 'selectedServerConfig'],
                        outputs: ['onServerSelected'],
                        template: "<select name=\"serverConfigs\" (change)=\"handleServerSelected($event)\" disabled=\"true\">\n\t\t\t<option *ngFor=\"let serverConfig of serverConfigList\" \n                    value={{serverConfig.domain}}\n                    [attr.selected]=\"selectedServerConfig.crop\n                    === serverConfig.crop ? true : null\">\n                    {{serverConfig.crop}}\n\t\t\t</option>\n\t\t</select>\n" // end template
                    }), 
                    __metadata('design:paramtypes', [])
                ], CropsListBoxComponent);
                return CropsListBoxComponent;
            }());
            exports_1("CropsListBoxComponent", CropsListBoxComponent);
        }
    }
});
//# sourceMappingURL=crops-list-box.component.js.map