System.register(["@angular/core", "../services/core/dto-request.service", "../model/extractor-instructions/data-set-extract", "../model/type-process", "../model/file-item", "../model/server-config", "../model/type-entity", "../model/name-id", "../model/type-gobii-file", "../model/extractor-instructions/dto-extractor-instruction-files", "../model/extractor-instructions/gobii-extractor-instruction", "../services/app/dto-request-item-extractor-submission", "../services/app/dto-request-item-nameids", "../services/app/dto-request-item-serverconfigs", "../model/type-entity-filter", "../model/type-extractor-filter", "../model/cv-filter-type", "../services/core/file-model-tree-service", "../model/file-model-node", "../model/type-extract-format", "../model/file-model-tree-event"], function (exports_1, context_1) {
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
    var core_1, dto_request_service_1, data_set_extract_1, type_process_1, file_item_1, server_config_1, type_entity_1, name_id_1, type_gobii_file_1, dto_extractor_instruction_files_1, gobii_extractor_instruction_1, dto_request_item_extractor_submission_1, dto_request_item_nameids_1, dto_request_item_serverconfigs_1, type_entity_filter_1, type_extractor_filter_1, cv_filter_type_1, file_model_tree_service_1, file_model_node_1, type_extract_format_1, file_model_tree_event_1, ExtractorRoot;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (dto_request_service_1_1) {
                dto_request_service_1 = dto_request_service_1_1;
            },
            function (data_set_extract_1_1) {
                data_set_extract_1 = data_set_extract_1_1;
            },
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (file_item_1_1) {
                file_item_1 = file_item_1_1;
            },
            function (server_config_1_1) {
                server_config_1 = server_config_1_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (name_id_1_1) {
                name_id_1 = name_id_1_1;
            },
            function (type_gobii_file_1_1) {
                type_gobii_file_1 = type_gobii_file_1_1;
            },
            function (dto_extractor_instruction_files_1_1) {
                dto_extractor_instruction_files_1 = dto_extractor_instruction_files_1_1;
            },
            function (gobii_extractor_instruction_1_1) {
                gobii_extractor_instruction_1 = gobii_extractor_instruction_1_1;
            },
            function (dto_request_item_extractor_submission_1_1) {
                dto_request_item_extractor_submission_1 = dto_request_item_extractor_submission_1_1;
            },
            function (dto_request_item_nameids_1_1) {
                dto_request_item_nameids_1 = dto_request_item_nameids_1_1;
            },
            function (dto_request_item_serverconfigs_1_1) {
                dto_request_item_serverconfigs_1 = dto_request_item_serverconfigs_1_1;
            },
            function (type_entity_filter_1_1) {
                type_entity_filter_1 = type_entity_filter_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (cv_filter_type_1_1) {
                cv_filter_type_1 = cv_filter_type_1_1;
            },
            function (file_model_tree_service_1_1) {
                file_model_tree_service_1 = file_model_tree_service_1_1;
            },
            function (file_model_node_1_1) {
                file_model_node_1 = file_model_node_1_1;
            },
            function (type_extract_format_1_1) {
                type_extract_format_1 = type_extract_format_1_1;
            },
            function (file_model_tree_event_1_1) {
                file_model_tree_event_1 = file_model_tree_event_1_1;
            }
        ],
        execute: function () {
            ExtractorRoot = (function () {
                function ExtractorRoot(_dtoRequestServiceExtractorFile, _dtoRequestServiceNameIds, _dtoRequestServiceServerConfigs, _fileModelTreeService) {
                    this._dtoRequestServiceExtractorFile = _dtoRequestServiceExtractorFile;
                    this._dtoRequestServiceNameIds = _dtoRequestServiceNameIds;
                    this._dtoRequestServiceServerConfigs = _dtoRequestServiceServerConfigs;
                    this._fileModelTreeService = _fileModelTreeService;
                    this.title = 'Gobii Web';
                    //    private selectedExportTypeEvent:GobiiExtractFilterType;
                    this.datasetFileItemEvents = [];
                    this.gobiiDatasetExtracts = [];
                    this.messages = [];
                    // ********************************************************************
                    // ********************************************** EXPORT TYPE SELECTION AND FLAGS
                    this.displayAvailableDatasets = true;
                    this.displaySelectorPi = true;
                    this.displaySelectorProject = true;
                    this.displaySelectorExperiment = true;
                    this.displaySelectorDataType = false;
                    this.displaySelectorPlatform = false;
                    this.displayIncludedDatasetsGrid = true;
                    this.displaySampleListTypeSelector = false;
                    this.displaySampleMarkerBox = false;
                    // ********************************************************************
                    // ********************************************** HAPMAP SELECTION
                    this.selectedExtractFormat = type_extract_format_1.GobiiExtractFormat.HAPMAP;
                    // ********************************************************************
                    // ********************************************** EXPERIMENT ID
                    this.displayExperimentDetail = false;
                    // ********************************************************************
                    // ********************************************** DATASET ID
                    this.displayDataSetDetail = false;
                    //private datasetFileItemEventChange: FileItem;
                    this.changeTrigger = 0;
                    // ********************************************************************
                    // ********************************************** MARKER/SAMPLE selection
                    this.markerList = null;
                    this.sampleList = null;
                    this.uploadFileName = null;
                }
                ExtractorRoot.prototype.initializeServerConfigs = function () {
                    var _this = this;
                    var scope$ = this;
                    this._dtoRequestServiceServerConfigs.get(new dto_request_item_serverconfigs_1.DtoRequestItemServerConfigs()).subscribe(function (serverConfigs) {
                        if (serverConfigs && (serverConfigs.length > 0)) {
                            scope$.serverConfigList = serverConfigs;
                            var serverCrop_1 = _this._dtoRequestServiceServerConfigs.getGobiiCropType();
                            var gobiiVersion = _this._dtoRequestServiceServerConfigs.getGobbiiVersion();
                            scope$.selectedServerConfig =
                                scope$.serverConfigList
                                    .filter(function (c) {
                                    return c.crop === serverCrop_1;
                                })[0];
                            scope$.currentStatus = "GOBII Server " + gobiiVersion;
                            scope$.messages.push("Connected to database: " + scope$.selectedServerConfig.crop);
                            scope$.initializeContactsForSumission();
                            scope$.initializeContactsForPi();
                            scope$.initializeMapsetsForSumission();
                        }
                        else {
                            scope$.serverConfigList = [new server_config_1.ServerConfig("<ERROR NO SERVERS>", "<ERROR>", "<ERROR>", 0)];
                        }
                    }, function (dtoHeaderResponse) {
                        dtoHeaderResponse.statusMessages.forEach(function (m) { return scope$.messages.push("Retrieving server configs: "
                            + m.message); });
                    });
                }; // initializeServerConfigs()
                ExtractorRoot.prototype.handleServerSelected = function (arg) {
                    this.selectedServerConfig = arg;
                    // this._dtoRequestServiceNameIds
                    //     .setCropType(GobiiCropType[this.selectedServerConfig.crop]);
                    var currentPath = window.location.pathname;
                    var currentPage = currentPath.substr(currentPath.lastIndexOf('/') + 1, currentPath.length);
                    var newDestination = "http://"
                        + this.selectedServerConfig.domain
                        + ":"
                        + this.selectedServerConfig.port
                        + this.selectedServerConfig.contextRoot
                        + currentPage;
                    //        console.log(newDestination);
                    window.location.href = newDestination;
                }; // handleServerSelected()
                ExtractorRoot.prototype.handleExportTypeSelected = function (arg) {
                    var foo = "foo";
                    this.gobiiExtractFilterType = arg;
                    //        let extractorFilterItemType: FileItem = FileItem.bui(this.gobiiExtractFilterType)
                    if (this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET) {
                        this.displaySelectorPi = true;
                        this.displaySelectorProject = true;
                        this.displaySelectorExperiment = true;
                        this.displayAvailableDatasets = true;
                        this.displayIncludedDatasetsGrid = true;
                        this.displaySelectorDataType = false;
                        this.displaySelectorPlatform = false;
                        this.displaySampleListTypeSelector = false;
                        this.displaySampleMarkerBox = false;
                    }
                    else if (this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE) {
                        this.initializeDatasetTypes();
                        this.initializePlatforms();
                        this.displaySelectorPi = true;
                        this.displaySelectorProject = true;
                        this.displaySelectorDataType = true;
                        this.displaySelectorPlatform = true;
                        this.displaySampleListTypeSelector = true;
                        this.displaySelectorExperiment = false;
                        this.displayAvailableDatasets = false;
                        this.displayIncludedDatasetsGrid = false;
                        this.displaySampleMarkerBox = false;
                    }
                    else if (this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER) {
                        this.initializeDatasetTypes();
                        this.initializePlatforms();
                        this.displaySelectorDataType = true;
                        this.displaySelectorPlatform = true;
                        this.displaySampleMarkerBox = true;
                        this.displaySelectorPi = false;
                        this.displaySelectorProject = false;
                        this.displaySelectorExperiment = false;
                        this.displayAvailableDatasets = false;
                        this.displayIncludedDatasetsGrid = false;
                        this.displaySampleListTypeSelector = false;
                    }
                };
                ExtractorRoot.prototype.handleContactForSubmissionSelected = function (arg) {
                    var _this = this;
                    this.selectedContactIdForSubmitter = arg.id;
                    var fileItem = file_item_1.FileItem
                        .build(this.gobiiExtractFilterType, type_process_1.ProcessType.UPDATE)
                        .setEntityType(type_entity_1.EntityType.Contacts)
                        .setEntitySubType(type_entity_1.EntitySubType.CONTACT_SUBMITED_BY)
                        .setItemId(arg.id)
                        .setItemName(arg.name);
                    this._fileModelTreeService.put(fileItem)
                        .subscribe(null, function (headerResponse) {
                        _this.handleResponseHeader(headerResponse);
                    });
                };
                ExtractorRoot.prototype.initializeContactsForSumission = function () {
                    var _this = this;
                    var scope$ = this;
                    this._dtoRequestServiceNameIds.get(new dto_request_item_nameids_1.DtoRequestItemNameIds(type_entity_1.EntityType.Contacts)).subscribe(function (nameIds) {
                        if (nameIds && (nameIds.length > 0)) {
                            scope$.contactNameIdListForSubmitter = nameIds;
                            scope$.selectedContactIdForSubmitter = nameIds[0].id;
                            _this.handleContactForSubmissionSelected(nameIds[0]);
                        }
                        else {
                            scope$.contactNameIdListForSubmitter = [new name_id_1.NameId("0", "ERROR NO USERS", type_entity_1.EntityType.Contacts)];
                        }
                    }, function (dtoHeaderResponse) {
                        dtoHeaderResponse.statusMessages.forEach(function (m) { return scope$.messages.push("Rettrieving contacts: "
                            + m.message); });
                    });
                };
                ExtractorRoot.prototype.handleContactForPiSelected = function (arg) {
                    this.selectedContactIdForPi = arg;
                    this.initializeProjectNameIds();
                    //console.log("selected contact itemId:" + arg);
                };
                ExtractorRoot.prototype.initializeContactsForPi = function () {
                    var scope$ = this;
                    scope$._dtoRequestServiceNameIds.get(new dto_request_item_nameids_1.DtoRequestItemNameIds(type_entity_1.EntityType.Contacts, type_entity_filter_1.EntityFilter.NONE)).subscribe(function (nameIds) {
                        if (nameIds && (nameIds.length > 0)) {
                            scope$.contactNameIdListForPi = nameIds;
                            scope$.selectedContactIdForPi = scope$.contactNameIdListForPi[0].id;
                        }
                        else {
                            scope$.contactNameIdListForPi = [new name_id_1.NameId("0", "ERROR NO USERS", type_entity_1.EntityType.Contacts)];
                        }
                        scope$.initializeProjectNameIds();
                    }, function (dtoHeaderResponse) {
                        dtoHeaderResponse.statusMessages.forEach(function (m) { return scope$.messages.push("Retrieving contacts for PIs: "
                            + m.message); });
                    });
                };
                ExtractorRoot.prototype.handleFormatSelected = function (arg) {
                    var _this = this;
                    this.selectedExtractFormat = arg;
                    var extractFilterTypeFileItem = file_item_1.FileItem
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
                ExtractorRoot.prototype.handleProjectSelected = function (arg) {
                    this.selectedProjectId = arg;
                    this.displayExperimentDetail = false;
                    this.displayDataSetDetail = false;
                    this.initializeExperimentNameIds();
                };
                ExtractorRoot.prototype.initializeProjectNameIds = function () {
                    var _this = this;
                    var scope$ = this;
                    scope$._dtoRequestServiceNameIds.get(new dto_request_item_nameids_1.DtoRequestItemNameIds(type_entity_1.EntityType.Projects, type_entity_filter_1.EntityFilter.BYTYPEID, this.selectedContactIdForPi)).subscribe(function (nameIds) {
                        if (nameIds && (nameIds.length > 0)) {
                            scope$.projectNameIdList = nameIds;
                            scope$.selectedProjectId = nameIds[0].id;
                        }
                        else {
                            scope$.projectNameIdList = [new name_id_1.NameId("0", "<none>", type_entity_1.EntityType.Projects)];
                            scope$.selectedProjectId = undefined;
                        }
                        _this.initializeExperimentNameIds();
                    }, function (dtoHeaderResponse) {
                        dtoHeaderResponse.statusMessages.forEach(function (m) { return scope$.messages.push("Retriving project names: "
                            + m.message); });
                    });
                };
                ExtractorRoot.prototype.handleExperimentSelected = function (arg) {
                    this.selectedExperimentId = arg;
                    this.selectedExperimentDetailId = arg;
                    this.displayExperimentDetail = true;
                    //console.log("selected contact itemId:" + arg);
                };
                ExtractorRoot.prototype.initializeExperimentNameIds = function () {
                    var scope$ = this;
                    if (this.selectedProjectId) {
                        this._dtoRequestServiceNameIds.get(new dto_request_item_nameids_1.DtoRequestItemNameIds(type_entity_1.EntityType.Experiments, type_entity_filter_1.EntityFilter.BYTYPEID, this.selectedProjectId)).subscribe(function (nameIds) {
                            if (nameIds && (nameIds.length > 0)) {
                                scope$.experimentNameIdList = nameIds;
                                scope$.selectedExperimentId = scope$.experimentNameIdList[0].id;
                            }
                            else {
                                scope$.experimentNameIdList = [new name_id_1.NameId("0", "<none>", type_entity_1.EntityType.Experiments)];
                                scope$.selectedExperimentId = undefined;
                            }
                        }, function (dtoHeaderResponse) {
                            dtoHeaderResponse.statusMessages.forEach(function (m) { return scope$.messages.push("Retreving experiment names: "
                                + m.message); });
                        });
                    }
                    else {
                        scope$.experimentNameIdList = [new name_id_1.NameId("0", "<none>", type_entity_1.EntityType.Experiments)];
                        scope$.selectedExperimentId = undefined;
                    }
                };
                ExtractorRoot.prototype.handleDatasetTypeSelected = function (arg) {
                    this.selectedDatasetTypeId = arg;
                };
                ExtractorRoot.prototype.initializeDatasetTypes = function () {
                    var scope$ = this;
                    scope$._dtoRequestServiceNameIds.get(new dto_request_item_nameids_1.DtoRequestItemNameIds(type_entity_1.EntityType.CvTerms, type_entity_filter_1.EntityFilter.BYTYPENAME, cv_filter_type_1.CvFilters.get(cv_filter_type_1.CvFilterType.DATASET_TYPE))).subscribe(function (nameIds) {
                        if (nameIds && (nameIds.length > 0)) {
                            scope$.datasetTypeNameIdList = nameIds;
                            scope$.selectedDatasetTypeId = scope$.datasetTypeNameIdList[0].id;
                        }
                        else {
                            scope$.datasetTypeNameIdList = [new name_id_1.NameId("0", "ERROR NO DATASET TYPES", type_entity_1.EntityType.CvTerms)];
                        }
                    }, function (dtoHeaderResponse) {
                        dtoHeaderResponse.statusMessages.forEach(function (m) { return scope$.messages.push("Retrieving DatasetTypes: "
                            + m.message); });
                    });
                };
                ExtractorRoot.prototype.handlePlatformSelected = function (arg) {
                    this.selectedPlatformId = arg.id;
                };
                ExtractorRoot.prototype.handlePlatformChecked = function (arg) {
                    this.checkedPlatformId = arg.id;
                };
                ExtractorRoot.prototype.initializePlatforms = function () {
                    var scope$ = this;
                    scope$._dtoRequestServiceNameIds.get(new dto_request_item_nameids_1.DtoRequestItemNameIds(type_entity_1.EntityType.Platforms, type_entity_filter_1.EntityFilter.NONE)).subscribe(function (nameIds) {
                        if (nameIds && (nameIds.length > 0)) {
                            scope$.platformsNameIdList = nameIds;
                            scope$.selectedPlatformId = scope$.platformsNameIdList[0].id;
                        }
                        else {
                            scope$.platformsNameIdList = [new name_id_1.NameId("0", "ERROR NO PLATFORMS", type_entity_1.EntityType.Platforms)];
                        }
                    }, function (dtoHeaderResponse) {
                        dtoHeaderResponse.statusMessages.forEach(function (m) { return scope$.messages.push("Retrieving PlatformTypes: "
                            + m.message); });
                    });
                };
                ExtractorRoot.prototype.handleAddMessage = function (arg) {
                    this.messages.push(arg);
                };
                ExtractorRoot.prototype.handleHeaderStatusMessage = function (statusMessage) {
                    this.handleAddMessage(statusMessage.message);
                };
                ExtractorRoot.prototype.handleResponseHeader = function (header) {
                    var _this = this;
                    if (header.status !== null && header.status.statusMessages != null) {
                        header.status.statusMessages.forEach(function (statusMessage) {
                            _this.handleHeaderStatusMessage(statusMessage);
                        });
                    }
                };
                ExtractorRoot.prototype.handleStatusTreeReady = function (headerStatusMessage) {
                    this.handleFormatSelected(type_extract_format_1.GobiiExtractFormat.HAPMAP);
                    //this.handleContactForSubmissionSelected(this.contactNameIdListForSubmitter[0]);
                };
                ExtractorRoot.prototype.makeDatasetExtract = function () {
                    this.gobiiDatasetExtracts.push(new data_set_extract_1.GobiiDataSetExtract(type_gobii_file_1.GobiiFileType.GENERIC, false, Number(this.selectedDatasetId), this.selectedDatasetName, null, this.gobiiExtractFilterType, this.markerList, this.sampleList, this.uploadFileName, this.selectedSampleListType, null, null));
                };
                ExtractorRoot.prototype.handleCheckedDataSetItem = function (arg) {
                    var _this = this;
                    this.selectedDatasetId = arg.getItemId();
                    if (type_process_1.ProcessType.CREATE == arg.getProcessType()) {
                        this.makeDatasetExtract();
                    }
                    else {
                        var indexOfEventToRemove = this.datasetFileItemEvents.indexOf(arg);
                        this.datasetFileItemEvents.splice(indexOfEventToRemove, 1);
                        this.gobiiDatasetExtracts =
                            this.gobiiDatasetExtracts
                                .filter(function (item) {
                                return item.getdataSetId() != Number(arg.getItemId());
                            });
                    } // if-else we're adding
                    //this.treeFileItemEvent = FileItem.fromFileItem(arg);
                    var fileItemEvent = file_item_1.FileItem.fromFileItem(arg, this.gobiiExtractFilterType);
                    this._fileModelTreeService.put(fileItemEvent).subscribe(null, function (headerResponse) {
                        _this.handleResponseHeader(headerResponse);
                    });
                };
                ExtractorRoot.prototype.handleExtractDataSetUnchecked = function (arg) {
                    // this.changeTrigger++;
                    // this.dataSetIdToUncheck = Number(arg.itemId);
                    this.datasetFileItemEvents.push(arg);
                    var dataSetExtractsToRemove = this.gobiiDatasetExtracts
                        .filter(function (e) {
                        return e.getdataSetId() === Number(arg.getItemId());
                    });
                    if (dataSetExtractsToRemove.length > 0) {
                        var idxToRemove = this.gobiiDatasetExtracts.indexOf(dataSetExtractsToRemove[0]);
                        this.gobiiDatasetExtracts.splice(idxToRemove, 1);
                    }
                    // this.datasetFileItemEventChange = arg;
                    this.treeFileItemEvent = file_item_1.FileItem.fromFileItem(arg, this.gobiiExtractFilterType);
                };
                ExtractorRoot.prototype.handleMapsetSelected = function (arg) {
                    var _this = this;
                    if (Number(arg.id) > 0) {
                        this.selectedMapsetId = arg.id;
                        var fileItem = file_item_1.FileItem.build(this.gobiiExtractFilterType, type_process_1.ProcessType.CREATE)
                            .setEntityType(type_entity_1.EntityType.Mapsets)
                            .setCvFilterType(cv_filter_type_1.CvFilterType.UKNOWN)
                            .setItemId(arg.id)
                            .setItemName(arg.name)
                            .setChecked(true)
                            .setRequired(null);
                        this._fileModelTreeService.put(fileItem).subscribe(null, function (headerResponse) {
                            _this.handleResponseHeader(headerResponse);
                        });
                    }
                    else {
                        this.selectedMapsetId = undefined;
                    }
                };
                ExtractorRoot.prototype.initializeMapsetsForSumission = function () {
                    var scope$ = this;
                    scope$.nullMapsetName = "<none>";
                    this._dtoRequestServiceNameIds.get(new dto_request_item_nameids_1.DtoRequestItemNameIds(type_entity_1.EntityType.Mapsets)).subscribe(function (nameIds) {
                        scope$.mapsetNameIdList = [new name_id_1.NameId("0", scope$.nullMapsetName, type_entity_1.EntityType.Mapsets)];
                        if (nameIds && (nameIds.length > 0)) {
                            scope$.mapsetNameIdList = scope$.mapsetNameIdList.concat(nameIds);
                            scope$.selectedMapsetId = nameIds[0].id;
                        }
                    }, function (dtoHeaderResponse) {
                        dtoHeaderResponse.statusMessages.forEach(function (m) { return scope$.messages.push("Rettrieving mapsets: "
                            + m.message); });
                    });
                };
                ExtractorRoot.prototype.handleSampleMarkerListComplete = function (arg) {
                    var sampleMarkerList = arg;
                    if (sampleMarkerList.isArray) {
                        if (this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE) {
                            this.sampleList = sampleMarkerList.items;
                        }
                        else if (this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER) {
                            this.markerList = sampleMarkerList.items;
                        }
                    }
                    else {
                        this.uploadFileName = sampleMarkerList.uploadFileName;
                    }
                    this.makeDatasetExtract();
                };
                ExtractorRoot.prototype.handleSampleListTypeSelected = function (arg) {
                    this.selectedSampleListType = arg;
                };
                // ********************************************************************
                // ********************************************** Extract file submission
                ExtractorRoot.prototype.handleExtractSubmission = function () {
                    var _this = this;
                    var scope$ = this;
                    var gobiiExtractorInstructions = [];
                    var gobiiDataSetExtracts = [];
                    var mapsetIds = [];
                    var submitterContactid = null;
                    scope$._fileModelTreeService.getFileItems(scope$.gobiiExtractFilterType).subscribe(function (fileItems) {
                        var submitterFileItem = fileItems.find(function (item) {
                            return (item.getEntityType() === type_entity_1.EntityType.Contacts)
                                && (item.getEntitySubType() === type_entity_1.EntitySubType.CONTACT_SUBMITED_BY);
                        });
                        submitterContactid = Number(submitterFileItem.getItemId());
                        mapsetIds = fileItems
                            .filter(function (item) {
                            return item.getEntityType() === type_entity_1.EntityType.Mapsets;
                        })
                            .map(function (item) {
                            return Number(item.getItemId());
                        });
                        var exportFileItem = fileItems.find(function (item) {
                            return item.getExtractorItemType() === file_model_node_1.ExtractorItemType.EXPORT_FORMAT;
                        });
                        var extractFormat = type_extract_format_1.GobiiExtractFormat[exportFileItem.getItemId()];
                        var gobiiFileType = type_gobii_file_1.GobiiFileType[type_extract_format_1.GobiiExtractFormat[extractFormat]];
                        var dataTypeFileItem = fileItems.find(function (item) {
                            return item.getEntityType() === type_entity_1.EntityType.CvTerms
                                && item.getCvFilterType() === cv_filter_type_1.CvFilterType.DATASET_TYPE;
                        });
                        var datSetTypeName = dataTypeFileItem != null ? dataTypeFileItem.getItemName() : null;
                        var platformFileItems = fileItems.filter(function (item) {
                            return item.getEntityType() === type_entity_1.EntityType.Platforms;
                        });
                        var platformIds = platformFileItems.map(function (item) {
                            return Number(item.getItemId());
                        });
                        fileItems
                            .filter(function (item) {
                            return item.getEntityType() === type_entity_1.EntityType.DataSets;
                        })
                            .forEach(function (datsetFileItem) {
                            gobiiDataSetExtracts.push(new data_set_extract_1.GobiiDataSetExtract(gobiiFileType, false, Number(datsetFileItem.getItemId()), datsetFileItem.getItemName(), null, _this.gobiiExtractFilterType, _this.markerList, _this.sampleList, _this.uploadFileName, _this.selectedSampleListType, datSetTypeName, platformIds));
                        });
                    });
                    gobiiExtractorInstructions.push(new gobii_extractor_instruction_1.GobiiExtractorInstruction(gobiiDataSetExtracts, submitterContactid, null, mapsetIds));
                    var date = new Date();
                    var fileName = "extractor_"
                        + date.getFullYear()
                        + "_"
                        + (date.getMonth() + 1)
                        + "_"
                        + date.getDay()
                        + "_"
                        + date.getHours()
                        + "_"
                        + date.getMinutes()
                        + "_"
                        + date.getSeconds();
                    var extractorInstructionFilesDTORequest = new dto_extractor_instruction_files_1.ExtractorInstructionFilesDTO(gobiiExtractorInstructions, fileName);
                    var extractorInstructionFilesDTOResponse = null;
                    this._dtoRequestServiceExtractorFile.post(new dto_request_item_extractor_submission_1.DtoRequestItemExtractorSubmission(extractorInstructionFilesDTORequest))
                        .subscribe(function (extractorInstructionFilesDTO) {
                        extractorInstructionFilesDTOResponse = extractorInstructionFilesDTO;
                        scope$.messages.push("Extractor instruction file created on server: "
                            + extractorInstructionFilesDTOResponse.getInstructionFileName());
                    }, function (dtoHeaderResponse) {
                        scope$.handleResponseHeader(dtoHeaderResponse);
                    });
                };
                ExtractorRoot.prototype.ngOnInit = function () {
                    this._fileModelTreeService
                        .treeStateNotifications()
                        .subscribe(function (ts) {
                        if (ts.fileModelState == file_model_tree_event_1.FileModelState.SUBMISSION_READY) {
                        }
                    });
                    this.initializeServerConfigs();
                    this.handleExportTypeSelected(type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET);
                };
                return ExtractorRoot;
            }());
            ExtractorRoot = __decorate([
                core_1.Component({
                    selector: 'extractor-root',
                    styleUrls: ['/extractor-ui.css'],
                    template: "<div class = \"panel panel-default\">\n        \n           <div class = \"panel-heading\">\n                <img src=\"images/gobii_logo.png\" alt=\"GOBii Project\"/>\n\n                <fieldset class=\"well the-fieldset\">\n                    <div class=\"col-md-2\">\n                        <crops-list-box\n                            [serverConfigList]=\"serverConfigList\"\n                            [selectedServerConfig]=\"selectedServerConfig\"\n                            (onServerSelected)=\"handleServerSelected($event)\"></crops-list-box>\n                    </div>\n                    \n                    <div class=\"col-md-3\">\n                       <export-type\n                        (onExportTypeSelected)=\"handleExportTypeSelected($event)\"></export-type>\n                     </div>\n                     \n                </fieldset>\n           </div>\n           \n            <div class=\"container-fluid\">\n            \n                <div class=\"row\">\n                \n                    <div class=\"col-md-4\">\n                    \n                    <fieldset class=\"well the-fieldset\">\n                    <legend class=\"the-legend\">Submit As</legend>\n                    <users-list-box\n                        [nameIdList]=\"contactNameIdListForSubmitter\"\n                        (onUserSelected)=\"handleContactForSubmissionSelected($event)\">\n                    </users-list-box>\n                    </fieldset>\n                        \n                     <fieldset class=\"well the-fieldset\">\n                        <legend class=\"the-legend\">Filters</legend><BR>\n                        \n                        \n                        <div *ngIf=\"displaySelectorPi\">\n                            <label class=\"the-label\">Principle Investigator:</label><BR>\n                            <contacts-list-box [nameIdList]=\"contactNameIdListForPi\" (onContactSelected)=\"handleContactForPiSelected($event)\"></contacts-list-box>\n                        </div>\n                        \n                        <div *ngIf=\"displaySelectorProject\">\n                            <BR>\n                            <BR>\n                            <label class=\"the-label\">Project:</label><BR>\n                            <project-list-box [primaryInvestigatorId] = \"selectedContactIdForPi\"\n                                [nameIdList]=\"projectNameIdList\"\n                                [nameIdListPIs]=\"contactNameIdListForPi\"\n                                (onProjectSelected)=\"handleProjectSelected($event)\"\n                                (onAddMessage)=\"handleAddMessage($event)\"></project-list-box>\n                        </div>\n\n                        <div *ngIf=\"displaySelectorDataType\">\n                            <BR>\n                            <BR>\n                            <label class=\"the-label\">Dataset Types:</label><BR>\n                            <dataset-types-list-box [nameIdList]=\"datasetTypeNameIdList\" (onDatasetTypeSelected)=\"handleDatasetTypeSelected($event)\"></dataset-types-list-box>\n                        </div>\n\n                        \n                        <div *ngIf=\"displaySelectorExperiment\">\n                            <BR>\n                            <BR>\n                            <label class=\"the-label\">Experiment:</label><BR>\n                            <experiment-list-box [projectId] = \"selectedProjectId\"\n                                [nameIdList] = \"experimentNameIdList\"\n                                (onExperimentSelected)=\"handleExperimentSelected($event)\"\n                                (onAddMessage)=\"handleAddMessage($event)\"></experiment-list-box>\n                        </div>\n\n                        <div *ngIf=\"displaySelectorPlatform\">\n                            <BR>\n                            <BR>\n                            <label class=\"the-label\">Platforms:</label><BR>\n                            <checklist-box\n                                [fileItemEventChange] = \"platformFileItemEventChange\"\n                                [nameIdList] = \"platformsNameIdList\"\n                                (onItemSelected)=\"handlePlatformSelected($event)\"\n                                (onItemChecked)=\"handlePlatformChecked($event)\"\n                                (onAddMessage) = \"handleAddMessage($event)\">\n                            </checklist-box>\n                         </div>\n\n\n                        <div *ngIf=\"displayAvailableDatasets\">\n                            <BR>\n                            <BR>\n                            <label class=\"the-label\">Data Sets</label><BR>\n                            <dataset-checklist-box\n                                [fileItemEventChange] = \"datasetFileItemEventChange\"\n                                [experimentId] = \"selectedExperimentId\" \n                                (onItemChecked)=\"handleCheckedDataSetItem($event)\"\n                                (onAddMessage) = \"handleAddMessage($event)\">\n                            </dataset-checklist-box>\n                        </div>\n                    </fieldset>\n                       \n                       \n                    </div>  <!-- outer grid column 1-->\n                \n                \n                \n                    <div class=\"col-md-4\"> \n\n                        <div *ngIf=\"displaySampleListTypeSelector\">\n                            <fieldset class=\"well the-fieldset\" style=\"vertical-align: bottom;\">\n                                <legend class=\"the-legend\">Included Samples</legend>\n                                <sample-list-type\n                                    (onSampleListTypeSelected)=\"handleSampleListTypeSelected($event)\">\n                                 </sample-list-type>\n                                <hr style=\"width: 100%; color: black; height: 1px; background-color:black;\" />\n                                <sample-marker-box \n                                    (onMarkerSamplesCompleted) = \"handleSampleMarkerListComplete($event)\">\n                                </sample-marker-box>\n                            </fieldset>\n                        </div>\n                        \n                        <div *ngIf=\"displaySampleMarkerBox\">\n                            <fieldset class=\"well the-fieldset\" style=\"vertical-align: bottom;\">\n                                <legend class=\"the-legend\">Included Markers</legend>\n                                <sample-marker-box \n                                    (onMarkerSamplesCompleted) = \"handleSampleMarkerListComplete($event)\">\n                                </sample-marker-box>\n                            </fieldset>\n                        </div>\n\n                        \n                        <form>\n                           <fieldset class=\"well the-fieldset\">\n                                <legend class=\"the-legend\">Extract</legend>\n                           \n                                <export-format (onFormatSelected)=\"handleFormatSelected($event)\"></export-format>\n                                <BR>\n                           \n                                <mapsets-list-box [nameIdList]=\"mapsetNameIdList\" \n                                    (onMapsetSelected)=\"handleMapsetSelected($event)\"></mapsets-list-box>\n                            </fieldset>\n                        </form>\n                        \n                        \n                    </div>  <!-- outer grid column 2-->\n                    \n                    \n                    <div class=\"col-md-4\">\n\n                        <fieldset class=\"well the-fieldset\" style=\"vertical-align: bottom;\">\n                            <legend class=\"the-legend\">Extraction Criteria Summary</legend>\n                            <status-display-tree\n                                [fileItemEventChange] = \"treeFileItemEvent\"\n                                [gobiiExtractFilterTypeEvent] = \"gobiiExtractFilterType\"\n                                (onAddMessage)=\"handleHeaderStatusMessage($event)\"\n                                (onTreeReady)=\"handleStatusTreeReady($event)\">\n                            </status-display-tree>\n                            <BR>\n                                <input type=\"button\" \n                                value=\"Submit\"\n                                 [disabled]=\"(gobiiDatasetExtracts.length === 0)\"\n                                (click)=\"handleExtractSubmission()\" >\n                            \n                        </fieldset>\n                            \n                        <div>\n                            <fieldset class=\"well the-fieldset\" style=\"vertical-align: bottom;\">\n                                <legend class=\"the-legend\">Status: {{currentStatus}}</legend>\n                                <status-display [messages] = \"messages\"></status-display>\n                            </fieldset>\n                        </div>\n                            \n                                   \n                    </div>  <!-- outer grid column 3 (inner grid)-->\n                                        \n                </div> <!-- .row of outer grid -->\n                \n                    <div class=\"row\"><!-- begin .row 2 of outer grid-->\n                        <div class=\"col-md-3\"><!-- begin column 1 of outer grid -->\n                         \n                         </div><!-- end column 1 of outer grid -->\n                    \n                    </div><!-- end .row 2 of outer grid-->\n                \n            </div>" // end template
                }) // @Component
                ,
                __metadata("design:paramtypes", [dto_request_service_1.DtoRequestService,
                    dto_request_service_1.DtoRequestService,
                    dto_request_service_1.DtoRequestService,
                    file_model_tree_service_1.FileModelTreeService])
            ], ExtractorRoot);
            exports_1("ExtractorRoot", ExtractorRoot);
        }
    };
});
//# sourceMappingURL=app.extractorroot.js.map