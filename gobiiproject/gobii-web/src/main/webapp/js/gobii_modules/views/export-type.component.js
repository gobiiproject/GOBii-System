System.register(["@angular/core", "../model/type-extractor-filter"], function (exports_1, context_1) {
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
    var core_1, type_extractor_filter_1, ExportTypeComponent;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            }
        ],
        execute: function () {
            ExportTypeComponent = (function () {
                function ExportTypeComponent() {
                    this.onExportTypeSelected = new core_1.EventEmitter();
                } // ctor
                ExportTypeComponent.prototype.handleExportTypeSelected = function (arg) {
                    if (arg.srcElement.checked) {
                        var radioValue = arg.srcElement.value;
                        var entityFilter = type_extractor_filter_1.GobiiExtractFilterType[radioValue];
                        this.onExportTypeSelected.emit(entityFilter);
                    }
                };
                ExportTypeComponent.prototype.ngOnInit = function () {
                };
                return ExportTypeComponent;
            }());
            ExportTypeComponent = __decorate([
                core_1.Component({
                    selector: 'export-type',
                    outputs: ['onExportTypeSelected'],
                    template: "<label class=\"the-label\">Extract By:&nbsp;</label>\n                  <input type=\"radio\" (change)=\"handleExportTypeSelected($event)\" name=\"format\" value=\"WHOLE_DATASET\" checked=\"checked\">Data Set&nbsp;\n                  <input type=\"radio\" (change)=\"handleExportTypeSelected($event)\" name=\"format\" value=\"BY_SAMPLE\" disabled>Sample&nbsp;\n                  <input type=\"radio\" (change)=\"handleExportTypeSelected($event)\" name=\"format\" value=\"BY_MARKER\" disabled>Marker&nbsp;" // end template
                }),
                __metadata("design:paramtypes", [])
            ], ExportTypeComponent);
            exports_1("ExportTypeComponent", ExportTypeComponent);
        }
    };
});
//# sourceMappingURL=export-type.component.js.map