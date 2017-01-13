System.register(['@angular/core'], function(exports_1, context_1) {
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
    var ExportFormatComponent;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            }],
        execute: function() {
            ExportFormatComponent = (function () {
                function ExportFormatComponent() {
                    this.onFormatSelected = new core_1.EventEmitter();
                } // ctor
                ExportFormatComponent.prototype.handleContactSelected = function (arg) {
                    if (arg.srcElement.checked) {
                        this.onFormatSelected.emit(arg.srcElement.value);
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
                ExportFormatComponent = __decorate([
                    core_1.Component({
                        selector: 'export-format',
                        outputs: ['onFormatSelected'],
                        //  inputs: ['hero'],
                        //directives: [RADIO_GROUP_DIRECTIVES]
                        //  directives: [Alert]
                        template: "\n\t\t<form>\n\t\t\t<fieldset class=\"well the-fieldset\">\n\t\t\t<legend class=\"the-legend\">Export Format</legend>\n              <input type=\"radio\" (change)=\"handleContactSelected($event)\" name=\"format\" value=\"Hapmap\" checked=\"checked\">Hapmap<br>\n              <input type=\"radio\" (change)=\"handleContactSelected($event)\" name=\"format\" value=\"FlapJack\">FlapJack<br>\n              <input type=\"radio\" (change)=\"handleContactSelected($event)\" name=\"format\" value=\"VCF\" disabled=\"true\">VCF<br>\n              <input type=\"radio\" (change)=\"handleContactSelected($event)\" name=\"format\" value=\"HDF5\" disabled=\"true\">HDF5<br>\n              <input type=\"radio\" (change)=\"handleContactSelected($event)\" name=\"format\" value=\"PLINK CSV\" disabled=\"true\">PLINK CSV<br>\n\t\t\t</fieldset>\n\t\t\t\n\t\t</form>\n\t" // end template
                    }), 
                    __metadata('design:paramtypes', [])
                ], ExportFormatComponent);
                return ExportFormatComponent;
            }());
            exports_1("ExportFormatComponent", ExportFormatComponent);
        }
    }
});
//# sourceMappingURL=export-format.component.js.map