System.register(["@angular/core", "../services/core/dto-request.service", "../model/extractor-instructions/data-set-extract", "../model/type-process", "../model/gobii-file-item", "../model/server-config", "../model/type-entity", "../model/name-id", "../model/type-gobii-file", "../model/extractor-instructions/dto-extractor-instruction-files", "../model/extractor-instructions/gobii-extractor-instruction", "../services/app/dto-request-item-extractor-submission", "../services/app/dto-request-item-serverconfigs", "../model/type-entity-filter", "../model/type-extractor-filter", "../model/type-extractor-sample-list", "../model/cv-filter-type", "../services/core/file-model-tree-service", "../model/file-model-node", "../model/type-extract-format", "../model/file-model-tree-event", "../model/dto-header-status-message", "../model/name-id-request-params", "../model/file_name", "../views/entity-labels", "../services/app/dto-request-item-contact", "../services/core/authentication.service", "../model/name-id-label-type", "../model/type-status-level"], function (exports_1, context_1) {
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
    var core_1, dto_request_service_1, data_set_extract_1, type_process_1, gobii_file_item_1, server_config_1, type_entity_1, name_id_1, type_gobii_file_1, dto_extractor_instruction_files_1, gobii_extractor_instruction_1, dto_request_item_extractor_submission_1, dto_request_item_serverconfigs_1, type_entity_filter_1, type_extractor_filter_1, type_extractor_sample_list_1, cv_filter_type_1, file_model_tree_service_1, file_model_node_1, type_extract_format_1, file_model_tree_event_1, dto_header_status_message_1, name_id_request_params_1, file_name_1, entity_labels_1, dto_request_item_contact_1, authentication_service_1, name_id_label_type_1, type_status_level_1, ExtractorRoot;
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
            function (gobii_file_item_1_1) {
                gobii_file_item_1 = gobii_file_item_1_1;
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
            function (dto_request_item_serverconfigs_1_1) {
                dto_request_item_serverconfigs_1 = dto_request_item_serverconfigs_1_1;
            },
            function (type_entity_filter_1_1) {
                type_entity_filter_1 = type_entity_filter_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (type_extractor_sample_list_1_1) {
                type_extractor_sample_list_1 = type_extractor_sample_list_1_1;
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
            },
            function (dto_header_status_message_1_1) {
                dto_header_status_message_1 = dto_header_status_message_1_1;
            },
            function (name_id_request_params_1_1) {
                name_id_request_params_1 = name_id_request_params_1_1;
            },
            function (file_name_1_1) {
                file_name_1 = file_name_1_1;
            },
            function (entity_labels_1_1) {
                entity_labels_1 = entity_labels_1_1;
            },
            function (dto_request_item_contact_1_1) {
                dto_request_item_contact_1 = dto_request_item_contact_1_1;
            },
            function (authentication_service_1_1) {
                authentication_service_1 = authentication_service_1_1;
            },
            function (name_id_label_type_1_1) {
                name_id_label_type_1 = name_id_label_type_1_1;
            },
            function (type_status_level_1_1) {
                type_status_level_1 = type_status_level_1_1;
            }
        ],
        execute: function () {
            ExtractorRoot = (function () {
                function ExtractorRoot(_dtoRequestServiceExtractorFile, _dtoRequestServiceNameIds, _dtoRequestServiceContact, _authenticationService, _dtoRequestServiceServerConfigs, _fileModelTreeService) {
                    this._dtoRequestServiceExtractorFile = _dtoRequestServiceExtractorFile;
                    this._dtoRequestServiceNameIds = _dtoRequestServiceNameIds;
                    this._dtoRequestServiceContact = _dtoRequestServiceContact;
                    this._authenticationService = _authenticationService;
                    this._dtoRequestServiceServerConfigs = _dtoRequestServiceServerConfigs;
                    this._fileModelTreeService = _fileModelTreeService;
                    this.title = 'Gobii Web';
                    //    private selectedExportTypeEvent:GobiiExtractFilterType;
                    this.datasetFileItemEvents = [];
                    this.gobiiDatasetExtracts = [];
                    this.criteriaInvalid = true;
                    this.loggedInUser = null;
                    this.messages = [];
                    // ********************************************************************
                    // ********************************************** EXPORT TYPE SELECTION AND FLAGS
                    this.displayAvailableDatasets = true;
                    this.displaySelectorPi = true;
                    this.doPrincipleInvestigatorTreeNotifications = false;
                    this.displaySelectorProject = true;
                    this.displaySelectorExperiment = true;
                    this.displaySelectorDataType = false;
                    this.displaySelectorPlatform = false;
                    this.displayIncludedDatasetsGrid = true;
                    this.displaySampleListTypeSelector = false;
                    this.displaySampleMarkerBox = false;
                    this.reinitProjectList = false;
                    // ********************************************************************
                    // ********************************************** HAPMAP SELECTION
                    this.selectedExtractFormat = type_extract_format_1.GobiiExtractFormat.HAPMAP;
                    // ********************************************************************
                    // ********************************************** EXPERIMENT ID
                    this.displayExperimentDetail = false;
                    // ********************************************************************
                    // ********************************************** PLATFORM SELECTION
                    //     private platformsNameIdList: NameId[];
                    //     private selectedPlatformId: string;
                    //
                    //     private handlePlatformSelected(arg) {
                    //         this.selectedPlatformId = arg.id;
                    //     }
                    //
                    //     private handlePlatformChecked(fileItemEvent: GobiiFileItem) {
                    //
                    //
                    //         this._fileModelTreeService.put(fileItemEvent).subscribe(
                    //             null,
                    //             headerResponse => {
                    //                 this.handleHeaderStatusMessage(headerResponse)
                    //             });
                    //
                    //     }
                    //
                    //     private initializePlatforms() {
                    //         let scope$ = this;
                    //         scope$._dtoRequestServiceNameIds.get(new DtoRequestItemNameIds(
                    //             EntityType.Platforms,
                    //             EntityFilter.NONE)).subscribe(nameIds => {
                    //
                    //                 if (nameIds && ( nameIds.length > 0 )) {
                    //                     scope$.platformsNameIdList = nameIds;
                    //                     scope$.selectedPlatformId = scope$.platformsNameIdList[0].id;
                    //                 } else {
                    //                     scope$.platformsNameIdList = [new NameId("0", "ERROR NO PLATFORMS", EntityType.Platforms)];
                    //                 }
                    //             },
                    //             dtoHeaderResponse => {
                    //                 dtoHeaderResponse.statusMessages.forEach(m => scope$.messages.push("Retrieving PlatformTypes: "
                    //                     + m.message))
                    //             });
                    //     }
                    // ********************************************************************
                    // ********************************************** DATASET ID
                    this.displayDataSetDetail = false;
                    // ********************************************************************
                    // ********************************************** MARKER/SAMPLE selection
                    // ********************************************************************
                    // ********************************************** Extract file submission
                    this.treeStatusNotification = null;
                    this.submitButtonStyleDefault = "btn btn-primary";
                    this.buttonStyleSubmitReady = "btn btn-success";
                    this.buttonStyleSubmitNotReady = "btn btn-warning";
                    this.submitButtonStyle = this.buttonStyleSubmitNotReady;
                    this.clearButtonStyle = this.submitButtonStyleDefault;
                    this.nameIdRequestParamsContactsPi = name_id_request_params_1.NameIdRequestParams
                        .build("Contact-PI", type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET, type_entity_1.EntityType.Contacts)
                        .setEntitySubType(type_entity_1.EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR);
                    this.nameIdRequestParamsExperiments = name_id_request_params_1.NameIdRequestParams
                        .build("Experiments", type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET, type_entity_1.EntityType.Experiments)
                        .setEntityFilter(type_entity_filter_1.EntityFilter.BYTYPEID);
                    this.nameIdRequestParamsDatasetType = name_id_request_params_1.NameIdRequestParams
                        .build("Cv-DataType", type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET, type_entity_1.EntityType.CvTerms)
                        .setCvFilterType(cv_filter_type_1.CvFilterType.DATASET_TYPE)
                        .setEntityFilter(type_entity_filter_1.EntityFilter.BYTYPENAME)
                        .setEntityFilterValue(cv_filter_type_1.CvFilters.get(cv_filter_type_1.CvFilterType.DATASET_TYPE))
                        .setMameIdLabelType(name_id_label_type_1.NameIdLabelType.SELECT_A);
                    this.nameIdRequestParamsMapsets = name_id_request_params_1.NameIdRequestParams
                        .build("Mapsets", type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET, type_entity_1.EntityType.Mapsets)
                        .setMameIdLabelType(name_id_label_type_1.NameIdLabelType.NO);
                    this.nameIdRequestParamsPlatforms = name_id_request_params_1.NameIdRequestParams
                        .build("Platforms", type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET, type_entity_1.EntityType.Platforms);
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
                            scope$.initializeSubmissionContact();
                            scope$.currentStatus = "GOBII Server " + gobiiVersion;
                            scope$.handleAddMessage("Connected to crop config: " + scope$.selectedServerConfig.crop);
                        }
                        else {
                            scope$.serverConfigList = [new server_config_1.ServerConfig("<ERROR NO SERVERS>", "<ERROR>", "<ERROR>", 0)];
                        }
                    }, function (dtoHeaderResponse) {
                        dtoHeaderResponse.statusMessages.forEach(function (m) { return scope$.handleAddMessage("Retrieving server configs: "
                            + m.message); });
                    });
                }; // initializeServerConfigs()
                ExtractorRoot.prototype.initializeSubmissionContact = function () {
                    var _this = this;
                    this.loggedInUser = this._authenticationService.getUserName();
                    var scope$ = this;
                    scope$._dtoRequestServiceContact.get(new dto_request_item_contact_1.DtoRequestItemContact(dto_request_item_contact_1.ContactSearchType.BY_USERNAME, this.loggedInUser)).subscribe(function (contact) {
                        if (contact && contact.contactId && contact.contactId > 0) {
                            //loggedInUser
                            scope$._fileModelTreeService.put(gobii_file_item_1.GobiiFileItem.build(scope$.gobiiExtractFilterType, type_process_1.ProcessType.CREATE)
                                .setEntityType(type_entity_1.EntityType.Contacts)
                                .setEntitySubType(type_entity_1.EntitySubType.CONTACT_SUBMITED_BY)
                                .setCvFilterType(cv_filter_type_1.CvFilterType.UNKNOWN)
                                .setExtractorItemType(file_model_node_1.ExtractorItemType.ENTITY)
                                .setItemName(contact.email)
                                .setItemId(contact.contactId.toLocaleString())).subscribe(null, function (headerStatusMessage) {
                                _this.handleHeaderStatusMessage(headerStatusMessage);
                            });
                        }
                        else {
                            scope$.handleAddMessage("There is no contact associated with user " + _this.loggedInUser);
                        }
                    }, function (dtoHeaderResponse) {
                        dtoHeaderResponse.statusMessages.forEach(function (m) { return scope$.handleAddMessage("Retrieving contacts for submission: "
                            + m.message); });
                    });
                    //   _dtoRequestServiceContact
                };
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
                    var _this = this;
                    var foo = "foo";
                    this._fileModelTreeService
                        .fileItemNotifications()
                        .subscribe(function (fileItem) {
                        if (fileItem.getProcessType() === type_process_1.ProcessType.NOTIFY
                            && fileItem.getExtractorItemType() === file_model_node_1.ExtractorItemType.STATUS_DISPLAY_TREE_READY) {
                            var jobId = file_name_1.FileName.makeUniqueFileId();
                            _this._fileModelTreeService
                                .put(gobii_file_item_1.GobiiFileItem
                                .build(arg, type_process_1.ProcessType.CREATE)
                                .setExtractorItemType(file_model_node_1.ExtractorItemType.JOB_ID)
                                .setItemId(jobId)
                                .setItemName(jobId))
                                .subscribe(function (fmte) {
                                _this._fileModelTreeService
                                    .getTreeState(_this.gobiiExtractFilterType)
                                    .subscribe(function (ts) {
                                    _this.handleTreeStatusChanged(ts);
                                }, function (hsm) {
                                    _this.handleHeaderStatusMessage(hsm);
                                });
                            }, function (headerStatusMessage) {
                                _this.handleHeaderStatusMessage(headerStatusMessage);
                            });
                        }
                    });
                    this.gobiiExtractFilterType = arg;
                    //        let extractorFilterItemType: GobiiFileItem = GobiiFileItem.bui(this.gobiiExtractFilterType)
                    if (this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET) {
                        this.doPrincipleInvestigatorTreeNotifications = false;
                        this.nameIdRequestParamsContactsPi.setMameIdLabelType(name_id_label_type_1.NameIdLabelType.UNKNOWN);
                        this.displaySelectorPi = true;
                        this.displaySelectorProject = true;
                        this.displaySelectorExperiment = true;
                        this.displayAvailableDatasets = true;
                        this.displayIncludedDatasetsGrid = true;
                        this.displaySelectorDataType = false;
                        this.displaySelectorPlatform = false;
                        this.displaySampleListTypeSelector = false;
                        this.displaySampleMarkerBox = false;
                        this.reinitProjectList = false;
                    }
                    else if (this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE) {
                        //            this.initializePlatforms();
                        this.displaySelectorPi = true;
                        this.doPrincipleInvestigatorTreeNotifications = true;
                        this.nameIdRequestParamsContactsPi.setMameIdLabelType(name_id_label_type_1.NameIdLabelType.ALL);
                        this.displaySelectorProject = true;
                        this.displaySelectorDataType = true;
                        this.displaySelectorPlatform = true;
                        this.displaySampleListTypeSelector = true;
                        this.displaySelectorExperiment = false;
                        this.displayAvailableDatasets = false;
                        this.displayIncludedDatasetsGrid = false;
                        this.displaySampleMarkerBox = false;
                        this.reinitProjectList = true;
                    }
                    else if (this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER) {
                        //            this.initializePlatforms();
                        this.displaySelectorDataType = true;
                        this.displaySelectorPlatform = true;
                        this.displaySampleMarkerBox = true;
                        this.displaySelectorPi = false;
                        this.doPrincipleInvestigatorTreeNotifications = false;
                        this.nameIdRequestParamsContactsPi.setMameIdLabelType(name_id_label_type_1.NameIdLabelType.UNKNOWN);
                        this.displaySelectorProject = false;
                        this.displaySelectorExperiment = false;
                        this.displayAvailableDatasets = false;
                        this.displayIncludedDatasetsGrid = false;
                        this.displaySampleListTypeSelector = false;
                        this.reinitProjectList = false;
                    }
                    this.initializeSubmissionContact();
                    //changing modes will have nuked the submit as item in the tree, so we need to re-event (sic.) it:
                };
                ExtractorRoot.prototype.handleContactForPiSelected = function (arg) {
                    this.selectedContactIdForPi = arg.id;
                    //console.log("selected contact itemId:" + arg);
                };
                ExtractorRoot.prototype.handleFormatSelected = function (arg) {
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
                ExtractorRoot.prototype.handleProjectSelected = function (arg) {
                    this.selectedProjectId = arg;
                    this.displayExperimentDetail = false;
                    this.displayDataSetDetail = false;
                    this.nameIdRequestParamsExperiments.setEntityFilterValue(this.selectedProjectId);
                };
                ExtractorRoot.prototype.handleExperimentSelected = function (arg) {
                    this.selectedExperimentId = arg.id;
                    this.selectedExperimentDetailId = arg.id;
                    this.displayExperimentDetail = true;
                    //console.log("selected contact itemId:" + arg);
                };
                ExtractorRoot.prototype.handleAddMessage = function (arg) {
                    this.messages.unshift(arg);
                };
                ExtractorRoot.prototype.handleClearMessages = function () {
                    this.messages = [];
                };
                ExtractorRoot.prototype.handleHeaderStatusMessage = function (statusMessage) {
                    if (!statusMessage.statusLevel || statusMessage.statusLevel != type_status_level_1.StatusLevel.WARNING) {
                        this.handleAddMessage(statusMessage.message);
                    }
                    else {
                        console.log(statusMessage.message);
                    }
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
                    //this.handleFormatSelected(GobiiExtractFormat.HAPMAP);
                };
                ExtractorRoot.prototype.handleTreeStatusChanged = function (treeStatusNotification) {
                    if (treeStatusNotification.gobiiExractFilterType === this.gobiiExtractFilterType) {
                        this.treeStatusNotification = treeStatusNotification;
                        this.setSubmitButtonState();
                    } // does the filter type match
                };
                ExtractorRoot.prototype.setSubmitButtonState = function () {
                    var returnVal = false;
                    if (this.treeStatusNotification.fileModelState == file_model_tree_event_1.FileModelState.SUBMISSION_READY) {
                        this.submitButtonStyle = this.buttonStyleSubmitReady;
                        returnVal = true;
                    }
                    else {
                        this.submitButtonStyle = this.buttonStyleSubmitNotReady;
                        returnVal = false;
                    }
                    return returnVal;
                };
                ExtractorRoot.prototype.handleOnMouseOverSubmit = function (arg, isEnter) {
                    // this.criteriaInvalid = true;
                    var _this = this;
                    if (isEnter) {
                        this.setSubmitButtonState();
                        this.treeStatusNotification.modelTreeValidationErrors.forEach(function (mtv) {
                            var currentMessage;
                            if (mtv.fileModelNode.getItemType() === file_model_node_1.ExtractorItemType.ENTITY) {
                                currentMessage = mtv.fileModelNode.getEntityName();
                            }
                            else {
                                currentMessage = entity_labels_1.Labels.instance().treeExtractorTypeLabels[mtv.fileModelNode.getItemType()];
                            }
                            currentMessage += ": " + mtv.message;
                            _this.handleAddMessage(currentMessage);
                        });
                    }
                    // else {
                    //     this.submitButtonStyle = this.submitButtonStyleDefault;
                    // }
                    //#eee
                    var foo = "foo";
                };
                ExtractorRoot.prototype.handleClearTree = function () {
                    var _this = this;
                    this._fileModelTreeService.put(gobii_file_item_1.GobiiFileItem
                        .build(this.gobiiExtractFilterType, type_process_1.ProcessType.NOTIFY)
                        .setExtractorItemType(file_model_node_1.ExtractorItemType.CLEAR_TREE)).subscribe(null, function (headerResponse) {
                        _this.handleResponseHeader(headerResponse);
                    });
                };
                // In theory this method should be unnecessary because there should not be any duplicates;
                // however, in testing, it was discovered that there can be duplicate datasets and
                // duplicate platforms. I suspect that the root cause of this issue is the checkbox component:
                // because it keeps a history of selected items, it may be reposting existing items in a way that
                // is not detected by the file item service. In particular, it strikes me that if an item is added
                // in one extract type (e.g., by data set), and then selected again in another (by samples), there
                // could be duplicate items in the tree service, because it is specific to extract filter type.
                // TreeService::getFileItems() should be filtering correctly, but perhaps it's not. In any case,
                // at this point in the release cycle, it is too late to to do the trouble shooting to figure this out,
                // because I am unable to reproduce the issue in my local testing. This method at leaset reports
                // warnings to the effect that the problem exists, but results in an extract that is free of duplicates.
                // Technically, sample and marker list item duplicates should be eliminated in the list item control,
                // but it is also too late for that.
                ExtractorRoot.prototype.eliminateDuplicateEntities = function (extractorItemType, entityType, fileItems) {
                    var _this = this;
                    var returnVal = [];
                    if (fileItems
                        .filter(function (fi) {
                        return fi.getExtractorItemType() === extractorItemType
                            && fi.getEntityType() === entityType;
                    })
                        .length == fileItems.length) {
                        fileItems.forEach(function (ifi) {
                            if (returnVal.filter(function (rfi) {
                                return rfi.getItemId() === ifi.getItemId();
                            }).length === 0) {
                                returnVal.push(ifi);
                            }
                            else {
                                var message = "A duplicate ";
                                message += file_model_node_1.ExtractorItemType[extractorItemType];
                                message += " (" + type_entity_1.EntityType[entityType] + ") ";
                                message += "item was found; ";
                                if (ifi.getItemName()) {
                                    message += "name: " + ifi.getItemName() + "; ";
                                }
                                if (ifi.getItemId()) {
                                    message += "id: " + ifi.getItemId();
                                }
                                _this.handleHeaderStatusMessage(new dto_header_status_message_1.HeaderStatusMessage(message, type_status_level_1.StatusLevel.WARNING, null));
                            }
                        });
                    }
                    else {
                        this.handleHeaderStatusMessage(new dto_header_status_message_1.HeaderStatusMessage("The elimination array contains mixed entities", type_status_level_1.StatusLevel.WARNING, null));
                    }
                    return returnVal;
                };
                ExtractorRoot.prototype.handleExtractSubmission = function () {
                    var _this = this;
                    if (this.setSubmitButtonState()) {
                        var scope$_1 = this;
                        var gobiiExtractorInstructions = [];
                        var gobiiDataSetExtracts_1 = [];
                        var mapsetIds_1 = [];
                        var submitterContactid_1 = null;
                        var jobId_1 = null;
                        var markerFileName_1 = null;
                        var sampleFileName_1 = null;
                        var sampleListType_1;
                        scope$_1._fileModelTreeService.getFileItems(scope$_1.gobiiExtractFilterType).subscribe(function (fileItems) {
                            // ******** JOB ID
                            var fileItemJobId = fileItems.find(function (item) {
                                return item.getExtractorItemType() === file_model_node_1.ExtractorItemType.JOB_ID;
                            });
                            if (fileItemJobId != null) {
                                jobId_1 = fileItemJobId.getItemId();
                            }
                            // ******** MARKER FILE
                            var fileItemMarkerFile = fileItems.find(function (item) {
                                return item.getExtractorItemType() === file_model_node_1.ExtractorItemType.MARKER_FILE;
                            });
                            if (fileItemMarkerFile != null) {
                                markerFileName_1 = fileItemMarkerFile.getItemId();
                            }
                            // ******** SAMPLE FILE
                            var fileItemSampleFile = fileItems.find(function (item) {
                                return item.getExtractorItemType() === file_model_node_1.ExtractorItemType.SAMPLE_FILE;
                            });
                            if (fileItemSampleFile != null) {
                                sampleFileName_1 = fileItemSampleFile.getItemId();
                            }
                            // ******** SUBMITTER CONTACT
                            var submitterFileItem = fileItems.find(function (item) {
                                return (item.getEntityType() === type_entity_1.EntityType.Contacts)
                                    && (item.getEntitySubType() === type_entity_1.EntitySubType.CONTACT_SUBMITED_BY);
                            });
                            submitterContactid_1 = Number(submitterFileItem.getItemId());
                            // ******** MAPSET IDs
                            var mapsetFileItems = fileItems
                                .filter(function (item) {
                                return item.getEntityType() === type_entity_1.EntityType.Mapsets;
                            });
                            mapsetFileItems = _this.eliminateDuplicateEntities(file_model_node_1.ExtractorItemType.ENTITY, type_entity_1.EntityType.Mapsets, mapsetFileItems);
                            mapsetIds_1 = mapsetFileItems
                                .map(function (item) {
                                return Number(item.getItemId());
                            });
                            // ******** EXPORT FORMAT
                            var exportFileItem = fileItems.find(function (item) {
                                return item.getExtractorItemType() === file_model_node_1.ExtractorItemType.EXPORT_FORMAT;
                            });
                            // these probably should be just one enum
                            var gobiiFileType = null;
                            var extractFormat = type_extract_format_1.GobiiExtractFormat[exportFileItem.getItemId()];
                            if (extractFormat === type_extract_format_1.GobiiExtractFormat.FLAPJACK) {
                                gobiiFileType = type_gobii_file_1.GobiiFileType.FLAPJACK;
                            }
                            else if (extractFormat === type_extract_format_1.GobiiExtractFormat.HAPMAP) {
                                gobiiFileType = type_gobii_file_1.GobiiFileType.HAPMAP;
                            }
                            else if (extractFormat === type_extract_format_1.GobiiExtractFormat.META_DATA_ONLY) {
                                gobiiFileType = type_gobii_file_1.GobiiFileType.META_DATA;
                            }
                            // ******** DATA SET TYPE
                            var dataTypeFileItem = fileItems.find(function (item) {
                                return item.getEntityType() === type_entity_1.EntityType.CvTerms
                                    && item.getCvFilterType() === cv_filter_type_1.CvFilterType.DATASET_TYPE;
                            });
                            var datasetType = dataTypeFileItem != null ? new name_id_1.NameId(dataTypeFileItem.getItemId(), dataTypeFileItem.getItemName(), type_entity_1.EntityType.CvTerms) : null;
                            // ******** PRINCIPLE INVESTIGATOR CONCEPT
                            var principleInvestigatorFileItem = fileItems.find(function (item) {
                                return item.getEntityType() === type_entity_1.EntityType.Contacts
                                    && item.getEntitySubType() === type_entity_1.EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR;
                            });
                            var principleInvestigator = principleInvestigatorFileItem != null ? new name_id_1.NameId(principleInvestigatorFileItem.getItemId(), principleInvestigatorFileItem.getItemName(), type_entity_1.EntityType.Contacts) : null;
                            // ******** PROJECT
                            var projectFileItem = fileItems.find(function (item) {
                                return item.getEntityType() === type_entity_1.EntityType.Projects;
                            });
                            var project = projectFileItem != null ? new name_id_1.NameId(projectFileItem.getItemId(), projectFileItem.getItemName(), type_entity_1.EntityType.Projects) : null;
                            // ******** PLATFORMS
                            var platformFileItems = fileItems.filter(function (item) {
                                return item.getEntityType() === type_entity_1.EntityType.Platforms;
                            });
                            platformFileItems = _this.eliminateDuplicateEntities(file_model_node_1.ExtractorItemType.ENTITY, type_entity_1.EntityType.Platforms, platformFileItems);
                            var platformIds = platformFileItems.map(function (item) {
                                return Number(item.getItemId());
                            });
                            // ******** MARKERS
                            var markerListItems = fileItems
                                .filter(function (fi) {
                                return fi.getExtractorItemType() === file_model_node_1.ExtractorItemType.MARKER_LIST_ITEM;
                            });
                            markerListItems = _this.eliminateDuplicateEntities(file_model_node_1.ExtractorItemType.MARKER_LIST_ITEM, type_entity_1.EntityType.UNKNOWN, markerListItems);
                            var markerList = markerListItems
                                .map(function (mi) {
                                return mi.getItemId();
                            });
                            // ******** SAMPLES
                            var sampleListItems = fileItems
                                .filter(function (fi) {
                                return fi.getExtractorItemType() === file_model_node_1.ExtractorItemType.SAMPLE_LIST_ITEM;
                            });
                            sampleListItems = _this.eliminateDuplicateEntities(file_model_node_1.ExtractorItemType.SAMPLE_LIST_ITEM, type_entity_1.EntityType.UNKNOWN, sampleListItems);
                            var sampleList = sampleListItems
                                .map(function (mi) {
                                return mi.getItemId();
                            });
                            var sampleListTypeFileItem = fileItems.find(function (item) {
                                return item.getExtractorItemType() === file_model_node_1.ExtractorItemType.SAMPLE_LIST_TYPE;
                            });
                            if (sampleListTypeFileItem != null) {
                                sampleListType_1 = type_extractor_sample_list_1.GobiiSampleListType[sampleListTypeFileItem.getItemId()];
                            }
                            if (_this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET) {
                                var dataSetItems = fileItems
                                    .filter(function (item) {
                                    return item.getEntityType() === type_entity_1.EntityType.DataSets;
                                });
                                dataSetItems = _this.eliminateDuplicateEntities(file_model_node_1.ExtractorItemType.ENTITY, type_entity_1.EntityType.DataSets, dataSetItems);
                                dataSetItems.forEach(function (datsetFileItem) {
                                    var dataSet = new name_id_1.NameId(datsetFileItem.getItemId(), datsetFileItem.getItemName(), type_entity_1.EntityType.CvTerms);
                                    gobiiDataSetExtracts_1.push(new data_set_extract_1.GobiiDataSetExtract(gobiiFileType, false, null, _this.gobiiExtractFilterType, null, null, markerFileName_1, null, datasetType, platformIds, null, null, dataSet));
                                });
                            }
                            else if (_this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER) {
                                gobiiDataSetExtracts_1.push(new data_set_extract_1.GobiiDataSetExtract(gobiiFileType, false, null, _this.gobiiExtractFilterType, markerList, null, markerFileName_1, null, datasetType, platformIds, null, null, null));
                            }
                            else if (_this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE) {
                                gobiiDataSetExtracts_1.push(new data_set_extract_1.GobiiDataSetExtract(gobiiFileType, false, null, _this.gobiiExtractFilterType, null, sampleList, sampleFileName_1, sampleListType_1, datasetType, platformIds, principleInvestigator, project, null));
                            }
                            else {
                                _this.handleAddMessage("Unhandled extract filter type: " + type_extractor_filter_1.GobiiExtractFilterType[_this.gobiiExtractFilterType]);
                            }
                        });
                        gobiiExtractorInstructions.push(new gobii_extractor_instruction_1.GobiiExtractorInstruction(gobiiDataSetExtracts_1, submitterContactid_1, null, mapsetIds_1));
                        var fileName = jobId_1;
                        var extractorInstructionFilesDTORequest = new dto_extractor_instruction_files_1.ExtractorInstructionFilesDTO(gobiiExtractorInstructions, fileName);
                        var extractorInstructionFilesDTOResponse_1 = null;
                        this._dtoRequestServiceExtractorFile.post(new dto_request_item_extractor_submission_1.DtoRequestItemExtractorSubmission(extractorInstructionFilesDTORequest))
                            .subscribe(function (extractorInstructionFilesDTO) {
                            extractorInstructionFilesDTOResponse_1 = extractorInstructionFilesDTO;
                            scope$_1.handleAddMessage("Extractor instruction file created on server: "
                                + extractorInstructionFilesDTOResponse_1.getInstructionFileName());
                            var newJobId = file_name_1.FileName.makeUniqueFileId();
                            _this._fileModelTreeService
                                .put(gobii_file_item_1.GobiiFileItem
                                .build(_this.gobiiExtractFilterType, type_process_1.ProcessType.CREATE)
                                .setExtractorItemType(file_model_node_1.ExtractorItemType.JOB_ID)
                                .setItemId(newJobId)
                                .setItemName(newJobId))
                                .subscribe(function (e) {
                                _this.handleClearTree();
                            }, function (headerStatusMessage) {
                                _this.handleHeaderStatusMessage(headerStatusMessage);
                            });
                        }, function (headerResponse) {
                            scope$_1.handleResponseHeader(headerResponse);
                        });
                    } // if submission state is READY
                };
                ExtractorRoot.prototype.ngOnInit = function () {
                    var _this = this;
                    this._fileModelTreeService
                        .treeStateNotifications()
                        .subscribe(function (ts) {
                        _this.handleTreeStatusChanged(ts);
                    });
                    this.initializeServerConfigs();
                    this.handleExportTypeSelected(type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET);
                }; // ngOnInit()
                return ExtractorRoot;
            }());
            ExtractorRoot = __decorate([
                core_1.Component({
                    selector: 'extractor-root',
                    styleUrls: ['/extractor-ui.css'],
                    template: "<div class = \"panel panel-default\">\n        \n           <div class = \"panel-heading\">\n                <img src=\"images/gobii_logo.png\" alt=\"GOBii Project\"/>\n\n                    <div class=\"panel panel-primary\">\n                      <div class=\"panel-heading\">\n                        <h3 class=\"panel-title\">Connected to {{currentStatus}}</h3>\n                      </div>\n                      <div class=\"panel-body\">\n                    \n                    <div class=\"col-md-1\">\n                    \n                        <crops-list-box\n                            [serverConfigList]=\"serverConfigList\"\n                            [selectedServerConfig]=\"selectedServerConfig\"\n                            (onServerSelected)=\"handleServerSelected($event)\"></crops-list-box>\n                     </div>\n                                                \n                    <div class=\"col-md-5\">\n                       <export-type\n                        (onExportTypeSelected)=\"handleExportTypeSelected($event)\"></export-type>\n                     </div>\n                    \n\n                        <div class=\"col-md-4\">\n                        <div class = \"well\">\n                            <table>\n                                <tr>\n                                    <td colspan=\"2\">\n                                        <export-format\n                                         [gobiiExtractFilterType] = \"gobiiExtractFilterType\"\n                                         (onFormatSelected)=\"handleFormatSelected($event)\">\n                                          </export-format>\n                              </td>\n                              <td style=\"vertical-align: top;\">\n                                     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; \n                                     <name-id-list-box\n                                        [gobiiExtractFilterType] = \"gobiiExtractFilterType\"\n                                        [nameIdRequestParams]=\"nameIdRequestParamsMapsets\"\n                                        (onError) = \"handleHeaderStatusMessage($event)\">\n                                    </name-id-list-box>\n                              </td>\n                              </tr>\n                              </table>\n                              \n                        </div>\n                        </div>\n    \n                     \n                    <div class=\"col-md-2\">\n                       <p style=\"text-align: right; font-weight: bold;\">{{loggedInUser}}</p>\n                     </div>\n                     \n                    </div> <!-- panel body -->\n                    </div> <!-- panel primary -->\n\n           </div>\n           \n            <div class=\"container-fluid\">\n            \n                <div class=\"row\">\n                \n                    <div class=\"col-md-4\">\n                    \n                    <div class=\"panel panel-primary\">\n                      <div class=\"panel-heading\">\n                        <h3 class=\"panel-title\">Filters</h3>\n                      </div>\n                      <div class=\"panel-body\">\n\n                          <div *ngIf=\"displaySelectorPi\">\n                            <label class=\"the-label\">Principle Investigator:</label><BR>\n                            <name-id-list-box\n                                [gobiiExtractFilterType] = \"gobiiExtractFilterType\"\n                                [nameIdRequestParams]=\"nameIdRequestParamsContactsPi\"\n                                [notifyOnInit]=\"!doPrincipleInvestigatorTreeNotifications\"\n                                [doTreeNotifications] = \"doPrincipleInvestigatorTreeNotifications\"\n                                (onNameIdSelected)=\"handleContactForPiSelected($event)\"\n                                (onError) = \"handleHeaderStatusMessage($event)\">\n                            </name-id-list-box>\n                            \n                        </div>\n                        \n                        <div *ngIf=\"displaySelectorProject\" >\n                            <BR>\n                            <BR>\n                            <label class=\"the-label\">Project:</label><BR>\n                            <project-list-box [primaryInvestigatorId] = \"selectedContactIdForPi\"\n                                [gobiiExtractFilterType] = \"gobiiExtractFilterType\"\n                                [reinitProjectList] = \"reinitProjectList\"\n                                (onProjectSelected)=\"handleProjectSelected($event)\"\n                                (onAddHeaderStatus)=\"handleHeaderStatusMessage($event)\"></project-list-box>\n                        </div>\n\n                        <div *ngIf=\"displaySelectorDataType\" >\n                            <BR>\n                            <BR>\n                            <label class=\"the-label\">Dataset Types:</label><BR>\n                            <name-id-list-box\n                                [gobiiExtractFilterType] = \"gobiiExtractFilterType\"\n                                [notifyOnInit]=\"false\"\n                                [nameIdRequestParams]=\"nameIdRequestParamsDatasetType\"\n                                (onError) = \"handleHeaderStatusMessage($event)\">\n                            </name-id-list-box>\n                        </div>\n\n                        \n                        <div *ngIf=\"displaySelectorExperiment\">\n                            <BR>\n                            <BR>\n                            <label class=\"the-label\">Experiment:</label><BR>\n                               <name-id-list-box\n                                [gobiiExtractFilterType] = \"gobiiExtractFilterType\"\n                                [notifyOnInit]=\"true\"\n                                [doTreeNotifications]= \"false\"\n                                [nameIdRequestParams]=\"nameIdRequestParamsExperiments\"\n                                (onNameIdSelected) = \"handleExperimentSelected($event)\"\n                                (onError) = \"handleHeaderStatusMessage($event)\">\n                            </name-id-list-box>\n                            \n                        </div>\n\n                        <div *ngIf=\"displaySelectorPlatform\">\n                            <BR>\n                            <BR>\n                            <label class=\"the-label\">Platforms:</label><BR>\n                            <checklist-box\n                                [nameIdRequestParams] = \"nameIdRequestParamsPlatforms\"\n                                [gobiiExtractFilterType] = \"gobiiExtractFilterType\"\n                                [retainHistory] = \"false\"\n                                (onAddStatusMessage) = \"handleHeaderStatusMessage($event)\">\n                            </checklist-box>\n                         </div>\n\n\n                        <div *ngIf=\"displayAvailableDatasets\">\n                            <BR>\n                            <BR>\n                            <label class=\"the-label\">Data Sets</label><BR>\n                            <dataset-checklist-box\n                                [gobiiExtractFilterType] = \"gobiiExtractFilterType\"\n                                [experimentId] = \"selectedExperimentId\" \n                                (onAddStatusMessage) = \"handleHeaderStatusMessage($event)\">\n                            </dataset-checklist-box>\n                        </div>\n                    </div> <!-- panel body -->\n                    </div> <!-- panel primary -->\n                       \n\n                        <div *ngIf=\"displaySampleListTypeSelector\">\n                    <div class=\"panel panel-primary\">\n                      <div class=\"panel-heading\">\n                        <h3 class=\"panel-title\">Included Samples</h3>\n                      </div>\n                      <div class=\"panel-body\">\n                                <sample-list-type\n                                    [gobiiExtractFilterType] = \"gobiiExtractFilterType\"\n                                    (onHeaderStatusMessage)=\"handleHeaderStatusMessage($event)\">\n                                 </sample-list-type>\n                                <hr style=\"width: 100%; color: black; height: 1px; background-color:black;\" />\n                                <sample-marker-box \n                                    [gobiiExtractFilterType] = \"gobiiExtractFilterType\"\n                                    (onSampleMarkerError)=\"handleHeaderStatusMessage($event)\">\n                                </sample-marker-box>\n                    </div> <!-- panel body -->\n                    </div> <!-- panel primary -->\n                        </div>\n                        \n                        <div *ngIf=\"displaySampleMarkerBox\">\n                    <div class=\"panel panel-primary\">\n                      <div class=\"panel-heading\">\n                        <h3 class=\"panel-title\">Included Markers</h3>\n                      </div>\n                      <div class=\"panel-body\">\n                                <sample-marker-box \n                                    [gobiiExtractFilterType] = \"gobiiExtractFilterType\"\n                                    (onSampleMarkerError)=\"handleHeaderStatusMessage($event)\">\n                                </sample-marker-box>\n                    </div> <!-- panel body -->\n                    </div> <!-- panel primary -->\n                        </div>\n                       \n                    </div>  <!-- outer grid column 1-->\n                \n                \n                \n                    <div class=\"col-md-4\"> \n\n                    <div class=\"panel panel-primary\">\n                      <div class=\"panel-heading\">\n                        <h3 class=\"panel-title\">Extraction Criteria</h3>\n                      </div>\n                      <div class=\"panel-body\">\n                            <status-display-tree\n                                [fileItemEventChange] = \"treeFileItemEvent\"\n                                [gobiiExtractFilterTypeEvent] = \"gobiiExtractFilterType\"\n                                (onAddMessage)=\"handleHeaderStatusMessage($event)\"\n                                (onTreeReady)=\"handleStatusTreeReady($event)\">\n                            </status-display-tree>\n                            \n                            <BR>\n                            \n                            <button type=\"submit\"\n                            [class]=\"submitButtonStyle\"\n                            (mouseenter)=\"handleOnMouseOverSubmit($event,true)\"\n                            (mouseleave)=\"handleOnMouseOverSubmit($event,false)\"\n                            (click)=\"handleExtractSubmission()\">Submit</button>\n                               \n                            <button type=\"clear\"\n                            [class]=\"clearButtonStyle\"\n                            (click)=\"handleClearTree()\">Clear</button>\n                               \n                    </div> <!-- panel body -->\n                    </div> <!-- panel primary -->\n\n                    </div>  <!-- outer grid column 2-->\n                    \n                    \n                    <div class=\"col-md-4\">\n\n                            \n                        <div>\n                    <div class=\"panel panel-primary\">\n                      <div class=\"panel-heading\">\n                        <h3 class=\"panel-title\">Status Messages</h3>\n                      </div>\n                      <div class=\"panel-body\">\n                                <status-display [messages] = \"messages\"></status-display>\n                            <BR>\n                            <button type=\"clear\"\n                            class=\"btn btn-primary\"\n                            (click)=\"handleClearMessages()\">Clear</button>\n                    </div> <!-- panel body -->\n\n                    </div> <!-- panel primary -->\n                        </div>\n                            \n                                   \n                    </div>  <!-- outer grid column 3 (inner grid)-->\n                                        \n                </div> <!-- .row of outer grid -->\n                \n                    <div class=\"row\"><!-- begin .row 2 of outer grid-->\n                        <div class=\"col-md-3\"><!-- begin column 1 of outer grid -->\n                         \n                         </div><!-- end column 1 of outer grid -->\n                    \n                    </div><!-- end .row 2 of outer grid-->\n                \n            </div>" // end template
                }) // @Component
                ,
                __metadata("design:paramtypes", [dto_request_service_1.DtoRequestService,
                    dto_request_service_1.DtoRequestService,
                    dto_request_service_1.DtoRequestService,
                    authentication_service_1.AuthenticationService,
                    dto_request_service_1.DtoRequestService,
                    file_model_tree_service_1.FileModelTreeService])
            ], ExtractorRoot);
            exports_1("ExtractorRoot", ExtractorRoot);
        }
    };
});
//# sourceMappingURL=app.extractorroot.js.map