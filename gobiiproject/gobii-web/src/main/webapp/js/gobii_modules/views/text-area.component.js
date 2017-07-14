System.register(["@angular/core"], function (exports_1, context_1) {
    "use strict";
    var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
        var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
        if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
        else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
        return c > 3 && r && Object.defineProperty(target, key, r), r;
    };
    var __moduleName = context_1 && context_1.id;
    var core_1, TextAreaComponent;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            }
        ],
        execute: function () {
            TextAreaComponent = (function () {
                function TextAreaComponent() {
                    this.log = '';
                    this.onTextboxClicked = new core_1.EventEmitter();
                    this.onTextboxDataComplete = new core_1.EventEmitter();
                }
                TextAreaComponent.prototype.logText = function (value) {
                    this.log += "Text changed to '" + value + "'\n";
                };
                TextAreaComponent.prototype.handleTextboxClicked = function (arg) {
                    this.onTextboxClicked.emit(arg);
                };
                TextAreaComponent.prototype.handleTextboxDataComplete = function (arg) {
                    var items = arg.split("\n");
                    this.onTextboxDataComplete.emit(items);
                    this.textValue = '';
                };
                return TextAreaComponent;
            }());
            TextAreaComponent = __decorate([
                core_1.Component({
                    selector: 'text-area',
                    outputs: ['onTextboxDataComplete', 'onTextboxClicked'],
                    template: "\n        <textarea ref-textarea \n        [(ngModel)]=\"textValue\" rows=\"4\" style=\"width: 100%;\"\n        (click)=\"handleTextboxClicked($event)\"></textarea><br/>\n        <button (click)=\"handleTextboxDataComplete(textarea.value)\">Add To Extract</button>\n        <button (click)=\"textValue=''\">Clear</button>\n        \n         <!--<h2>Log <button (click)=\"log=''\">Clear</button></h2>-->\n        <!--<pre>{{log}}</pre>-->\n"
                })
            ], TextAreaComponent);
            exports_1("TextAreaComponent", TextAreaComponent);
        }
    };
});
//# sourceMappingURL=text-area.component.js.map