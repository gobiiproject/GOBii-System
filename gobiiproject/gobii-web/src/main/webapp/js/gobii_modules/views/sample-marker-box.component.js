System.register(["@angular/core", "../model/sample-marker-list"], function (exports_1, context_1) {
    "use strict";
    var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
        var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
        if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
        else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
        return c > 3 && r && Object.defineProperty(target, key, r), r;
    };
    var __moduleName = context_1 && context_1.id;
    var core_1, sample_marker_list_1, SampleMarkerBoxComponent;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (sample_marker_list_1_1) {
                sample_marker_list_1 = sample_marker_list_1_1;
            }
        ],
        execute: function () {
            SampleMarkerBoxComponent = (function () {
                function SampleMarkerBoxComponent() {
                    this.onMarkerSamplesCompleted = new core_1.EventEmitter();
                }
                // private handleUserSelected(arg) {
                //     this.onUserSelected.emit(this.nameIdList[arg.srcElement.selectedIndex].id);
                // }
                //
                // constructor(private _dtoRequestService:DtoRequestService<NameId[]>) {
                //
                // } // ctor
                SampleMarkerBoxComponent.prototype.handleTextBoxDataSubmitted = function (arg) {
                    var sampleMarkerList = new sample_marker_list_1.SampleMarkerList(true, arg, null);
                    this.onMarkerSamplesCompleted.emit(sampleMarkerList);
                };
                SampleMarkerBoxComponent.prototype.ngOnInit = function () {
                    return null;
                };
                return SampleMarkerBoxComponent;
            }());
            SampleMarkerBoxComponent = __decorate([
                core_1.Component({
                    selector: 'sample-marker-box',
                    outputs: ['onMarkerSamplesCompleted'],
                    template: "<div class=\"container-fluid\">\n            \n                <div class=\"row\">\n                \n                    <div class=\"col-md-2\"> \n                        <input type=\"radio\" \n                            (change)=\"handleListTypeSelected($event)\" \n                            name=\"listType\" \n                            value=\"uploadFile\" \n                            checked=\"checked\">&nbsp;File\n                    </div> \n                    \n                    <div class=\"col-md-8\">\n                        <uploader></uploader>\n                    </div> \n                    \n                 </div>\n                 \n                <div class=\"row\">\n                \n                    <div class=\"col-md-2\">\n                        <input type=\"radio\" \n                            (change)=\"handleListTypeSelected($event)\" \n                            name=\"listType\" \n                            value=\"pasteList\" >&nbsp;List\n                    </div> \n                    \n                    <div class=\"col-md-8\">\n                        <text-area\n                        (onTextboxDataComplete)=\"handleTextBoxDataSubmitted($event)\"></text-area>\n                    </div> \n                    \n                 </div>\n                 \n"
                })
            ], SampleMarkerBoxComponent);
            exports_1("SampleMarkerBoxComponent", SampleMarkerBoxComponent);
        }
    };
});
//# sourceMappingURL=sample-marker-box.component.js.map