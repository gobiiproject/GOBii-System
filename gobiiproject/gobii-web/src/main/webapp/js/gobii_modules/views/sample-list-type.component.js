System.register(["@angular/core", "../model/type-extractor-sample-list"], function (exports_1, context_1) {
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
    var core_1, type_extractor_sample_list_1, SampleListTypeComponent;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (type_extractor_sample_list_1_1) {
                type_extractor_sample_list_1 = type_extractor_sample_list_1_1;
            }
        ],
        execute: function () {
            SampleListTypeComponent = (function () {
                function SampleListTypeComponent() {
                    this.onSampleListTypeSelected = new core_1.EventEmitter();
                } // ctor
                SampleListTypeComponent.prototype.handleExportTypeSelected = function (arg) {
                    if (arg.srcElement.checked) {
                        var radioValue = arg.srcElement.value;
                        var gobiiExtractFilterType = type_extractor_sample_list_1.GobiiSampleListType[radioValue];
                        this.onSampleListTypeSelected.emit(gobiiExtractFilterType);
                    }
                };
                SampleListTypeComponent.prototype.ngOnInit = function () {
                };
                return SampleListTypeComponent;
            }());
            SampleListTypeComponent = __decorate([
                core_1.Component({
                    selector: 'sample-list-type',
                    outputs: ['onSampleListTypeSelected'],
                    template: "<label class=\"the-label\">Export By:</label><BR>\n                  <input type=\"radio\" (change)=\"handleExportTypeSelected($event)\" name=\"format\" value=\"GERMPLASM_NAME\" checked=\"checked\">Germplasm Name<BR>\n                  <input type=\"radio\" (change)=\"handleExportTypeSelected($event)\" name=\"format\" value=\"EXTERNAL_CODE\">External Code<BR>\n                  <input type=\"radio\" (change)=\"handleExportTypeSelected($event)\" name=\"format\" value=\"DNA_SAMPLE\">DNA Sample<BR>" // end template
                }),
                __metadata("design:paramtypes", [])
            ], SampleListTypeComponent);
            exports_1("SampleListTypeComponent", SampleListTypeComponent);
        }
    };
});
//# sourceMappingURL=sample-list-type.component.js.map