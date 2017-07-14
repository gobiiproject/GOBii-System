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
    var core_1, StatusDisplayComponent;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            }
        ],
        execute: function () {
            StatusDisplayComponent = (function () {
                function StatusDisplayComponent() {
                    // useg
                    this.messages = [];
                } // ctor
                StatusDisplayComponent.prototype.handleClearMessages = function () {
                    this.messages = [];
                };
                StatusDisplayComponent.prototype.ngOnInit = function () {
                    return null;
                };
                StatusDisplayComponent.prototype.ngOnChanges = function (changes) {
                    this.messages = changes['messages'].currentValue;
                };
                return StatusDisplayComponent;
            }());
            StatusDisplayComponent = __decorate([
                core_1.Component({
                    selector: 'status-display',
                    inputs: ['messages'],
                    //directives: [RADIO_GROUP_DIRECTIVES]
                    template: "<div style=\"overflow:auto; height: 240px; border: 1px solid #336699; padding-left: 5px;\">\n                    <div *ngFor=\"let message of messages\">{{message}}\n                    <hr style=\"height:1px;border:none;color:#333;background-color:#333;\">\n                    </div>\n                </div>" // end template
                }),
                __metadata("design:paramtypes", [])
            ], StatusDisplayComponent);
            exports_1("StatusDisplayComponent", StatusDisplayComponent);
        }
    };
});
//# sourceMappingURL=status-display-box.component.js.map