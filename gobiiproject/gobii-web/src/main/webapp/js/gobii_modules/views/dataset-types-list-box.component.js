System.register(["@angular/core"], function (exports_1, context_1) {
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
    var core_1, DatasetTypeListBoxComponent;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            }
        ],
        execute: function () {
            DatasetTypeListBoxComponent = (function () {
                function DatasetTypeListBoxComponent() {
                    this.onDatasetTypeSelected = new core_1.EventEmitter();
                } // ctor
                DatasetTypeListBoxComponent.prototype.handleDatasetTypeSelected = function (arg) {
                    this.onDatasetTypeSelected.emit(this.nameIdList[arg.srcElement.selectedIndex].id);
                };
                DatasetTypeListBoxComponent.prototype.ngOnInit = function () {
                    return null;
                };
                return DatasetTypeListBoxComponent;
            }());
            DatasetTypeListBoxComponent = __decorate([
                core_1.Component({
                    selector: 'dataset-types-list-box',
                    outputs: ['onDatasetTypeSelected'],
                    inputs: ['nameIdList'],
                    template: "<select name=\"datasetTypes\" (change)=\"handleDatasetTypeSelected($event)\" >\n\t\t\t<option *ngFor=\"let nameId of nameIdList \" \n\t\t\t\tvalue={{nameId.id}}>{{nameId.name}}</option>\n\t\t</select>\n" // end template
                }),
                __metadata("design:paramtypes", [])
            ], DatasetTypeListBoxComponent);
            exports_1("DatasetTypeListBoxComponent", DatasetTypeListBoxComponent);
        }
    };
});
//# sourceMappingURL=dataset-types-list-box.component.js.map