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
    var SearchCriteriaBySamplesComponent;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            }],
        execute: function() {
            //import {CORE_DIRECTIVES, FORM_DIRECTIVES, NgClass, NgStyle} from 'angular2/common';
            SearchCriteriaBySamplesComponent = (function () {
                function SearchCriteriaBySamplesComponent() {
                } // ctor
                SearchCriteriaBySamplesComponent.prototype.ngOnInit = function () {
                    /*
                            let id = +this._routeParams.get('id');
                            this._heroService.getHero(id)
                              .then(hero => this.hero = hero);
                    */
                };
                SearchCriteriaBySamplesComponent = __decorate([
                    core_1.Component({
                        selector: 'search-criteria-samples',
                        template: "\n\t\t\t<form>\n\t\t\t\t<fieldset class=\"well the-fieldset\">\n\t\t\t\t<legend class=\"the-legend\">Search Criteria</legend>\n\t\t\t\t\n\t\t\t\tSamples:\n\t\t\t\t<input type=\"file\"/>\n\t\t\t\t<BR>\n\t\t\t\tMarkers:\n\t\t\t\t<input type=\"file/>\n\t\t\t\t\n\t\t\t\t</fieldset>\n\t\t\t</form>\n  " // end template
                    }), 
                    __metadata('design:paramtypes', [])
                ], SearchCriteriaBySamplesComponent);
                return SearchCriteriaBySamplesComponent;
            }());
            exports_1("SearchCriteriaBySamplesComponent", SearchCriteriaBySamplesComponent);
        }
    }
});
//# sourceMappingURL=page-by-samples.component.js.map