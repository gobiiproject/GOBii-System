System.register(["@angular/core", "../model/type-extract-format"], function (exports_1, context_1) {
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
    var core_1, type_extract_format_1, ExportFormatComponent;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (type_extract_format_1_1) {
                type_extract_format_1 = type_extract_format_1_1;
            }
        ],
        execute: function () {
            ExportFormatComponent = (function () {
                function ExportFormatComponent() {
                    this.onFormatSelected = new core_1.EventEmitter();
                } // ctor
                ExportFormatComponent.prototype.handleContactSelected = function (arg) {
                    if (arg.srcElement.checked) {
                        var radioValue = arg.srcElement.value;
                        var gobiiExportFormat = type_extract_format_1.GobiiExtractFormat[radioValue];
                        this.onFormatSelected.emit(gobiiExportFormat);
                    }
                    var foo = arg;
                    //this.onContactSelected.emit(this.nameIdList[arg.srcElement.selectedIndex].id);
                };
                ExportFormatComponent.prototype.ngOnInit = function () {
                    /*
                     let id = +this._routeParams.get('id');
                     this._heroService.getHero(id)
                     .then(hero => this.hero = hero);
                     */
                };
                return ExportFormatComponent;
            }());
            ExportFormatComponent = __decorate([
                core_1.Component({
                    selector: 'export-format',
                    outputs: ['onFormatSelected'],
                    //  inputs: ['hero'],
                    //directives: [RADIO_GROUP_DIRECTIVES]
                    //  directives: [Alert]
                    template: "\n    \t\t  <label class=\"the-label\">Select Format:</label><BR>\n              &nbsp;&nbsp;&nbsp;<input type=\"radio\" (change)=\"handleContactSelected($event)\" name=\"format\" value=\"HAPMAP\" checked=\"checked\">Hapmap<br>\n              &nbsp;&nbsp;&nbsp;<input type=\"radio\" (change)=\"handleContactSelected($event)\" name=\"format\" value=\"FLAPJACK\">FlapJack<br>\n              &nbsp;&nbsp;&nbsp;<input type=\"radio\" (change)=\"handleContactSelected($event)\" name=\"format\" value=\"META_DATA_ONLY\">Dataset Metadata Only<br>\n\t" // end template
                }),
                __metadata("design:paramtypes", [])
            ], ExportFormatComponent);
            exports_1("ExportFormatComponent", ExportFormatComponent);
        }
    };
});
//# sourceMappingURL=export-format.component.js.map