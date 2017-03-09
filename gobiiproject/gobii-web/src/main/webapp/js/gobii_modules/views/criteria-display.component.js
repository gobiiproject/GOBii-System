System.register(["@angular/core", "../model/file-item", "../model/type-process", "../model/type-entity", "../model/type-extractor-filter", "../model/cv-filter-type"], function (exports_1, context_1) {
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
    var core_1, file_item_1, type_process_1, type_entity_1, type_extractor_filter_1, cv_filter_type_1, CriteriaDisplayComponent;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (file_item_1_1) {
                file_item_1 = file_item_1_1;
            },
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (cv_filter_type_1_1) {
                cv_filter_type_1 = cv_filter_type_1_1;
            }
        ],
        execute: function () {
            CriteriaDisplayComponent = (function () {
                function CriteriaDisplayComponent() {
                    // useg
                    this.dataSetFileItemEvents = [];
                    this.onItemUnChecked = new core_1.EventEmitter();
                    this.onItemSelected = new core_1.EventEmitter();
                } // ctor
                CriteriaDisplayComponent.prototype.ngOnInit = function () {
                    return null;
                };
                // In this component, every item starts out checked; unchecking it removes it
                CriteriaDisplayComponent.prototype.handleItemUnChecked = function (arg) {
                    var checkEvent = file_item_1.FileItem.build(type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN, type_process_1.ProcessType.DELETE)
                        .setEntityType(type_entity_1.EntityType.DataSets)
                        .setCvFilterType(cv_filter_type_1.CvFilterType.UKNOWN)
                        .setItemId(arg.currentTarget.value)
                        .setItemName(arg.currentTarget.name)
                        .setChecked(false)
                        .setRequired(false);
                    var itemToRemove = this.dataSetFileItemEvents
                        .filter(function (e) {
                        return e.getItemId() === arg.currentTarget.value;
                    })[0];
                    var indexOfItemToRemove = this.dataSetFileItemEvents.indexOf(itemToRemove);
                    if (indexOfItemToRemove > -1) {
                        this.dataSetFileItemEvents.splice(indexOfItemToRemove, 1);
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
                    this.dataSetFileItemEvents = changes['dataSetFileItemEvents'].currentValue;
                };
                return CriteriaDisplayComponent;
            }());
            CriteriaDisplayComponent = __decorate([
                core_1.Component({
                    selector: 'criteria-display',
                    inputs: ['dataSetFileItemEvents'],
                    outputs: ['onItemUnChecked', 'onItemSelected'],
                    template: "<form>\n                    <div style=\"overflow:auto; height: 80px; border: 1px solid #336699; padding-left: 5px\">\n                        <div *ngFor=\"let dataSetFileItemEvent of dataSetFileItemEvents\"\n                                (click)=handleItemSelected($event)\n                                (hover)=handleItemHover($event)>\n                                <input  type=\"checkbox\"\n                                    (click)=handleItemUnChecked($event)\n                                    value={{dataSetFileItemEvent.itemId}}\n                                    name=\"{{dataSetFileItemEvent.itemName}}\"\n                                    checked>&nbsp;{{dataSetFileItemEvent.itemName}}\n                        </div>\n                    </div>\n                </form>"
                }),
                __metadata("design:paramtypes", [])
            ], CriteriaDisplayComponent);
            exports_1("CriteriaDisplayComponent", CriteriaDisplayComponent);
        }
    };
});
//# sourceMappingURL=criteria-display.component.js.map