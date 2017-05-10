System.register(["@angular/core", "../model/type-extract-format", "../services/core/file-model-tree-service", "../model/gobii-file-item", "../model/type-process", "../model/file-model-node", "../model/type-extractor-filter"], function (exports_1, context_1) {
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
    var core_1, type_extract_format_1, file_model_tree_service_1, gobii_file_item_1, type_process_1, file_model_node_1, type_extractor_filter_1, ExportFormatComponent;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (type_extract_format_1_1) {
                type_extract_format_1 = type_extract_format_1_1;
            },
            function (file_model_tree_service_1_1) {
                file_model_tree_service_1 = file_model_tree_service_1_1;
            },
            function (gobii_file_item_1_1) {
                gobii_file_item_1 = gobii_file_item_1_1;
            },
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (file_model_node_1_1) {
                file_model_node_1 = file_model_node_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            }
        ],
        execute: function () {
            ExportFormatComponent = (function () {
                function ExportFormatComponent(_fileModelTreeService) {
                    this._fileModelTreeService = _fileModelTreeService;
                    this.fileFormat = "HAPMAP";
                    this.onFormatSelected = new core_1.EventEmitter();
                    this.onError = new core_1.EventEmitter();
                    this.selectedExtractFormat = type_extract_format_1.GobiiExtractFormat.HAPMAP;
                } // ctor
                // private nameIdList: NameId[];
                // private selectedNameId: string = null;
                ExportFormatComponent.prototype.ngOnInit = function () {
                    // in the current version, this doesn't work: each component in the page
                    // is initialized once at a time. Thus, even though the tree is being built
                    // built in the tree component's constructor, nginit() here still is triggered
                    // before the tree is instanced. Thus the format is not displayed in the tree because
                    // the tree isn't there yet to receive the event from the dataservice. ngInit() works
                    // to initialize the submitted as user in the tree is because it is calling a web service
                    // with an observer: while the service is being called, the rest of the control hierarchy is
                    // being built, so there is just enough enough latency in the web service call that the tree
                    // is there when the fileItem is posted to the tree model service. I have providen this
                    // because in the commented out code below, I make a call to the same webservice then
                    // post the GobiiFileItem for the format, and lo and behold, the tree is there and gets properly
                    // initialized with the format type. The only to make this is work is to post the format change
                    // to the model service _after_ the tree calls oncomplete. If we want to encapsulate all the
                    // service communication in the child components, the tree service will have to accommodate
                    // notification events to which these components will subscribe.
                    // let scope$ = this;
                    // this._dtoRequestService.get(new DtoRequestItemNameIds(
                    //     EntityType.Contacts,
                    //     null,
                    //     null)).subscribe(nameIds => {
                    //         if (nameIds && ( nameIds.length > 0 )) {
                    //             scope$.nameIdList = nameIds;
                    //             scope$.selectedNameId = nameIds[0].id;
                    //             this.updateTreeService(GobiiExtractFormat.HAPMAP);
                    //             //this.updateTreeService(nameIds[0]);
                    //         } else {
                    //             //scope$.nameIdList = [new NameId("0", "ERROR NO " + EntityType[scope$.entityType], scope$.entityType)];
                    //         }
                    //     },
                    //     responseHeader => {
                    //         this.handleResponseHeader(responseHeader);
                    //     });
                    // so, for now, this dispensation solves the problem. But I suspect it only works because the
                    // the tree component just happens to be the last one to be processed because it's at the end
                    // of the control tree, so it's the last one to get the property binding updates. If it were
                    // at the top of the control tree, we would have the reverse problem in that it would send out
                    // the of TREE_READY before the sibling components had been bound to their property values,
                    // and the component initialization would not work. perhaps. Tne work around for that would be that
                    // in this callback, we would check that the temlate-bound parameters had values; if they did not
                    // we would set a flag in this component saying, tree is ready; in ngInit, after the component properties
                    // are bound, we would check whether that flag is set, and if it was, then we would send
                    // the tree notification. I _think_ that would cover all the contingencies, but it's ugly.
                    // I am not sure whether reactive forms would address this issue.
                    //this.setDefault();
                };
                ExportFormatComponent.prototype.setDefault = function () {
                    this.updateTreeService(type_extract_format_1.GobiiExtractFormat.HAPMAP);
                    this.fileFormat = "HAPMAP";
                };
                ExportFormatComponent.prototype.handleResponseHeader = function (header) {
                    this.onError.emit(header);
                };
                ExportFormatComponent.prototype.handleFormatSelected = function (arg) {
                    if (arg.srcElement.checked) {
                        var radioValue = arg.srcElement.value;
                        this.selectedExtractFormat = type_extract_format_1.GobiiExtractFormat[radioValue];
                        this.updateTreeService(this.selectedExtractFormat);
                    }
                    var foo = arg;
                    //this.onContactSelected.emit(this.nameIdList[arg.srcElement.selectedIndex].id);
                };
                ExportFormatComponent.prototype.updateTreeService = function (arg) {
                    var _this = this;
                    this.selectedExtractFormat = arg;
                    var extractFilterTypeFileItem = gobii_file_item_1.GobiiFileItem
                        .build(this.gobiiExtractFilterType, type_process_1.ProcessType.UPDATE)
                        .setExtractorItemType(file_model_node_1.ExtractorItemType.EXPORT_FORMAT)
                        .setItemId(type_extract_format_1.GobiiExtractFormat[arg])
                        .setItemName(type_extract_format_1.GobiiExtractFormat[type_extract_format_1.GobiiExtractFormat[arg]]);
                    this._fileModelTreeService.put(extractFilterTypeFileItem)
                        .subscribe(null, function (headerResponse) {
                        _this.handleResponseHeader(headerResponse);
                    });
                    //console.log("selected contact itemId:" + arg);
                };
                ExportFormatComponent.prototype.ngOnChanges = function (changes) {
                    var _this = this;
                    this._fileModelTreeService
                        .fileItemNotifications()
                        .subscribe(function (fileItem) {
                        if (fileItem.getProcessType() === type_process_1.ProcessType.NOTIFY &&
                            ((fileItem.getExtractorItemType() === file_model_node_1.ExtractorItemType.STATUS_DISPLAY_TREE_READY)
                                || (fileItem.getExtractorItemType() === file_model_node_1.ExtractorItemType.CLEAR_TREE))) {
                            _this.setDefault();
                        }
                    });
                    if (changes['gobiiExtractFilterType']
                        && (changes['gobiiExtractFilterType'].currentValue != null)
                        && (changes['gobiiExtractFilterType'].currentValue != undefined)) {
                        if (changes['gobiiExtractFilterType'].currentValue != changes['gobiiExtractFilterType'].previousValue) {
                            var labelSuffix = " Metadata";
                            if (this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET) {
                                this.metaDataExtractname = "Dataset" + labelSuffix;
                            }
                            else if (this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER) {
                                this.metaDataExtractname = "Marker" + labelSuffix;
                            }
                            else if (this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE) {
                                this.metaDataExtractname = "Sample" + labelSuffix;
                            }
                            this.setDefault();
                        } // if we have a new filter type
                    } // if filter type changed
                }; // ngonChanges
                return ExportFormatComponent;
            }());
            ExportFormatComponent = __decorate([
                core_1.Component({
                    selector: 'export-format',
                    outputs: ['onFormatSelected', 'onError'],
                    inputs: ['gobiiExtractFilterType'],
                    //directives: [RADIO_GROUP_DIRECTIVES]
                    //  directives: [Alert]
                    template: "<form>\n                            <label class=\"the-legend\">Select Format:&nbsp;</label>\n                            <BR><input type=\"radio\" (change)=\"handleFormatSelected($event)\" [(ngModel)]=\"fileFormat\" name=\"fileFormat\" value=\"HAPMAP\" checked=\"checked\">\n                            <label  for=\"HAPMAP\" class=\"the-legend\">Hapmap</label>\n                            <BR><input type=\"radio\" (change)=\"handleFormatSelected($event)\" [(ngModel)]=\"fileFormat\" name=\"fileFormat\" value=\"FLAPJACK\">\n                            <label for=\"FLAPJACK\" class=\"the-legend\">Flapjack</label>\n                            <BR><input type=\"radio\" (change)=\"handleFormatSelected($event)\" [(ngModel)]=\"fileFormat\" name=\"fileFormat\" value=\"META_DATA_ONLY\">\n                            <label  for=\"META_DATA_ONLY\" class=\"the-legend\">{{metaDataExtractname}}</label>\n                </form>" // end template
                }),
                __metadata("design:paramtypes", [file_model_tree_service_1.FileModelTreeService])
            ], ExportFormatComponent);
            exports_1("ExportFormatComponent", ExportFormatComponent);
        }
    };
});
//# sourceMappingURL=export-format.component.js.map