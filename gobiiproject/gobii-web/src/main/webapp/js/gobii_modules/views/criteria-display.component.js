System.register(["@angular/core", "../model/event-checkbox", "../model/type-process"], function(exports_1, context_1) {
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
    var core_1, event_checkbox_1, type_process_1;
    var CriteriaDisplayComponent;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (event_checkbox_1_1) {
                event_checkbox_1 = event_checkbox_1_1;
            },
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            }],
        execute: function() {
            CriteriaDisplayComponent = (function () {
                function CriteriaDisplayComponent() {
                    // useg
                    this.dataSetCheckBoxEvents = [];
                    this.onItemUnChecked = new core_1.EventEmitter();
                    this.onItemSelected = new core_1.EventEmitter();
                } // ctor
                CriteriaDisplayComponent.prototype.ngOnInit = function () {
                    return null;
                };
                // In this component, every item starts out checked; unchecking it removes it
                CriteriaDisplayComponent.prototype.handleItemUnChecked = function (arg) {
                    var checkEvent = new event_checkbox_1.CheckBoxEvent(type_process_1.ProcessType.DELETE, arg.currentTarget.value, arg.currentTarget.name, false);
                    var itemToRemove = this.dataSetCheckBoxEvents
                        .filter(function (e) {
                        return e.id === arg.currentTarget.value;
                    })[0];
                    var indexOfItemToRemove = this.dataSetCheckBoxEvents.indexOf(itemToRemove);
                    if (indexOfItemToRemove > -1) {
                        this.dataSetCheckBoxEvents.splice(indexOfItemToRemove, 1);
                    }
                    this.onItemUnChecked.emit(checkEvent);
                };
                CriteriaDisplayComponent.prototype.handleItemSelected = function (arg) {
                    var selectedDataSetId = Number(arg.currentTarget.children[0].value);
                    if (this.previousSelectedItem) {
                        this.previousSelectedItem.style = "";
                    }
                    arg.currentTarget.style = "background-color:#b3d9ff";
                    this.previousSelectedItem = arg.currentTarget;
                    this.onItemSelected.emit(selectedDataSetId);
                };
                CriteriaDisplayComponent.prototype.ngOnChanges = function (changes) {
                    this.dataSetCheckBoxEvents = changes['dataSetCheckBoxEvents'].currentValue;
                };
                CriteriaDisplayComponent = __decorate([
                    core_1.Component({
                        selector: 'criteria-display',
                        inputs: ['dataSetCheckBoxEvents'],
                        outputs: ['onItemUnChecked', 'onItemSelected'],
                        template: "<form>\n                    <div style=\"overflow:auto; height: 80px; border: 1px solid #336699; padding-left: 5px\">\n                        <div *ngFor=\"let dataSetCheckBoxEvent of dataSetCheckBoxEvents\"\n                                (click)=handleItemSelected($event)\n                                (hover)=handleItemHover($event)>\n                                <input  type=\"checkbox\"\n                                    (click)=handleItemUnChecked($event)\n                                    value={{dataSetCheckBoxEvent.id}}\n                                    name=\"{{dataSetCheckBoxEvent.name}}\"\n                                    checked>&nbsp;{{dataSetCheckBoxEvent.name}}\n                        </div>\n                    </div>\n                </form>"
                    }), 
                    __metadata('design:paramtypes', [])
                ], CriteriaDisplayComponent);
                return CriteriaDisplayComponent;
            }());
            exports_1("CriteriaDisplayComponent", CriteriaDisplayComponent);
        }
    }
});
//# sourceMappingURL=criteria-display.component.js.map