///<reference path="../../../../../../typings/index.d.ts"/>
System.register(["@angular/core", "@angular/http", "../views/export-format.component", "../services/core/dto-request.service", "../services/core/authentication.service", "../views/contacts-list-box.component", "../views/project-list-box.component", "../views/experiment-list-box.component", "../views/dataset-checklist-box.component", "../model/extractor-instructions/data-set-extract", "../views/criteria-display.component", "../views/status-display-box.component", "../model/type-process", "../model/server-config", "../model/type-entity", "../views/crops-list-box.component", "../views/users-list-box.component", "../model/name-id", "../model/type-gobii-file", "../model/extractor-instructions/dto-extractor-instruction-files", "../model/extractor-instructions/gobii-extractor-instruction", "../services/app/dto-request-item-extractor-submission", "../services/app/dto-request-item-nameids", "../services/app/dto-request-item-serverconfigs", "../model/type-crop", "../model/type-entity-filter"], function(exports_1, context_1) {
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
    var core_1, http_1, export_format_component_1, dto_request_service_1, authentication_service_1, contacts_list_box_component_1, project_list_box_component_1, experiment_list_box_component_1, dataset_checklist_box_component_1, data_set_extract_1, criteria_display_component_1, status_display_box_component_1, type_process_1, server_config_1, type_entity_1, crops_list_box_component_1, users_list_box_component_1, name_id_1, type_gobii_file_1, dto_extractor_instruction_files_1, gobii_extractor_instruction_1, dto_request_item_extractor_submission_1, dto_request_item_nameids_1, dto_request_item_serverconfigs_1, type_crop_1, EntityFilters;
    var ExtractorRoot;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (http_1_1) {
                http_1 = http_1_1;
            },
            function (export_format_component_1_1) {
                export_format_component_1 = export_format_component_1_1;
            },
            function (dto_request_service_1_1) {
                dto_request_service_1 = dto_request_service_1_1;
            },
            function (authentication_service_1_1) {
                authentication_service_1 = authentication_service_1_1;
            },
            function (contacts_list_box_component_1_1) {
                contacts_list_box_component_1 = contacts_list_box_component_1_1;
            },
            function (project_list_box_component_1_1) {
                project_list_box_component_1 = project_list_box_component_1_1;
            },
            function (experiment_list_box_component_1_1) {
                experiment_list_box_component_1 = experiment_list_box_component_1_1;
            },
            function (dataset_checklist_box_component_1_1) {
                dataset_checklist_box_component_1 = dataset_checklist_box_component_1_1;
            },
            function (data_set_extract_1_1) {
                data_set_extract_1 = data_set_extract_1_1;
            },
            function (criteria_display_component_1_1) {
                criteria_display_component_1 = criteria_display_component_1_1;
            },
            function (status_display_box_component_1_1) {
                status_display_box_component_1 = status_display_box_component_1_1;
            },
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (server_config_1_1) {
                server_config_1 = server_config_1_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (crops_list_box_component_1_1) {
                crops_list_box_component_1 = crops_list_box_component_1_1;
            },
            function (users_list_box_component_1_1) {
                users_list_box_component_1 = users_list_box_component_1_1;
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
            function (type_crop_1_1) {
                type_crop_1 = type_crop_1_1;
            },
            function (EntityFilters_1) {
                EntityFilters = EntityFilters_1;
            }],
        execute: function() {
            // import { RouteConfig, ROUTER_DIRECTIVES, ROUTER_PROVIDERS } from 'angular2/router';
            // GOBii Imports
            ExtractorRoot = (function () {
                function ExtractorRoot(_dtoRequestServiceExtractorFile, _dtoRequestServiceNameIds, _dtoRequestServiceServerConfigs) {
                    this._dtoRequestServiceExtractorFile = _dtoRequestServiceExtractorFile;
                    this._dtoRequestServiceNameIds = _dtoRequestServiceNameIds;
                    this._dtoRequestServiceServerConfigs = _dtoRequestServiceServerConfigs;
                    this.title = 'Gobii Web';
                    this.dataSetCheckBoxEvents = [];
                    this.gobiiDatasetExtracts = [];
                    this.messages = [];
                    // ********************************************************************
                    // ********************************************** HAPMAP SELECTION
                    this.selectedFormatName = "Hapmap";
                    // ********************************************************************
                    // ********************************************** EXPERIMENT ID
                    this.displayExperimentDetail = false;
                    // ********************************************************************
                    // ********************************************** DATASET ID
                    this.displayDataSetDetail = false;
                    this.changeTrigger = 0;
                }
                ExtractorRoot.prototype.initializeServerConfigs = function () {
                    var _this = this;
                    var scope$ = this;
                    this._dtoRequestServiceServerConfigs.getResult(new dto_request_item_serverconfigs_1.DtoRequestItemServerConfigs()).subscribe(function (serverConfigs) {
                        if (serverConfigs && (serverConfigs.length > 0)) {
                            scope$.serverConfigList = serverConfigs;
                            var serverCrop_1 = _this._dtoRequestServiceServerConfigs.getGobiiCropType();
                            scope$.selectedServerConfig =
                                scope$.serverConfigList
                                    .filter(function (c) {
                                    return c.crop === type_crop_1.GobiiCropType[serverCrop_1];
                                })[0];
                            scope$.messages.push("Connected to database: " + scope$.selectedServerConfig.crop);
                            scope$.initializeContactsForSumission();
                            scope$.initializeContactsForPi();
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
                    //     .setGobiiCropType(GobiiCropType[this.selectedServerConfig.crop]);
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
                ExtractorRoot.prototype.handleContactForSubmissionSelected = function (arg) {
                    this.selectedContactIdForSubmitter = arg;
                };
                ExtractorRoot.prototype.initializeContactsForSumission = function () {
                    var scope$ = this;
                    this._dtoRequestServiceNameIds.getResult(new dto_request_item_nameids_1.DtoRequestItemNameIds(type_process_1.ProcessType.READ, type_entity_1.EntityType.AllContacts)).subscribe(function (nameIds) {
                        if (nameIds && (nameIds.length > 0)) {
                            scope$.contactNameIdListForSubmitter = nameIds;
                            scope$.selectedContactIdForSubmitter = nameIds[0].id;
                        }
                        else {
                            scope$.contactNameIdListForSubmitter = [new name_id_1.NameId(0, "ERROR NO USERS")];
                        }
                    }, function (dtoHeaderResponse) {
                        dtoHeaderResponse.statusMessages.forEach(function (m) { return scope$.messages.push("Rettrieving contacts: "
                            + m.message); });
                    });
                };
                ExtractorRoot.prototype.handleContactForPiSelected = function (arg) {
                    this.selectedContactIdForPi = arg;
                    this.initializeProjectNameIds();
                    //console.log("selected contact id:" + arg);
                };
                ExtractorRoot.prototype.initializeContactsForPi = function () {
                    var scope$ = this;
                    scope$._dtoRequestServiceNameIds.getResult(new dto_request_item_nameids_1.DtoRequestItemNameIds(type_process_1.ProcessType.READ, type_entity_1.EntityType.Contact, EntityFilters.ENTITY_FILTER_CONTACT_PRINICPLE_INVESTIGATOR)).subscribe(function (nameIds) {
                        if (nameIds && (nameIds.length > 0)) {
                            scope$.contactNameIdListForPi = nameIds;
                            scope$.selectedContactIdForPi = scope$.contactNameIdListForPi[0].id;
                        }
                        else {
                            scope$.contactNameIdListForPi = [new name_id_1.NameId(0, "ERROR NO USERS")];
                        }
                        scope$.initializeProjectNameIds();
                    }, function (dtoHeaderResponse) {
                        dtoHeaderResponse.statusMessages.forEach(function (m) { return scope$.messages.push("Retrieving contacts for PIs: "
                            + m.message); });
                    });
                };
                ExtractorRoot.prototype.handleFormatSelected = function (arg) {
                    this.selectedFormatName = arg;
                    //console.log("selected contact id:" + arg);
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
                    scope$._dtoRequestServiceNameIds.getResult(new dto_request_item_nameids_1.DtoRequestItemNameIds(type_process_1.ProcessType.READ, type_entity_1.EntityType.Project, this.selectedContactIdForPi)).subscribe(function (nameIds) {
                        if (nameIds && (nameIds.length > 0)) {
                            scope$.projectNameIdList = nameIds;
                            scope$.selectedProjectId = nameIds[0].id;
                        }
                        else {
                            scope$.projectNameIdList = [new name_id_1.NameId(0, "<none>")];
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
                    //console.log("selected contact id:" + arg);
                };
                ExtractorRoot.prototype.initializeExperimentNameIds = function () {
                    var scope$ = this;
                    if (this.selectedProjectId) {
                        this._dtoRequestServiceNameIds.getResult(new dto_request_item_nameids_1.DtoRequestItemNameIds(type_process_1.ProcessType.READ, type_entity_1.EntityType.Experiment, this.selectedProjectId)).subscribe(function (nameIds) {
                            if (nameIds && (nameIds.length > 0)) {
                                scope$.experimentNameIdList = nameIds;
                                scope$.selectedExperimentId = scope$.experimentNameIdList[0].id;
                            }
                            else {
                                scope$.experimentNameIdList = [new name_id_1.NameId(0, "<none>")];
                                scope$.selectedExperimentId = undefined;
                            }
                        }, function (dtoHeaderResponse) {
                            dtoHeaderResponse.statusMessages.forEach(function (m) { return scope$.messages.push("Retreving experiment names: "
                                + m.message); });
                        });
                    }
                    else {
                        scope$.experimentNameIdList = [new name_id_1.NameId(0, "<none>")];
                        scope$.selectedExperimentId = undefined;
                    }
                };
                ExtractorRoot.prototype.handleAddMessage = function (arg) {
                    this.messages.push(arg);
                };
                ExtractorRoot.prototype.handleCheckedDataSetItem = function (arg) {
                    if (type_process_1.ProcessType.CREATE == arg.processType) {
                        this.dataSetCheckBoxEvents.push(arg);
                        this.gobiiDatasetExtracts.push(new data_set_extract_1.GobiiDataSetExtract(type_gobii_file_1.GobiiFileType.GENERIC, false, Number(arg.id), arg.name, null));
                    }
                    else {
                        var indexOfEventToRemove = this.dataSetCheckBoxEvents.indexOf(arg);
                        this.dataSetCheckBoxEvents.splice(indexOfEventToRemove, 1);
                        this.gobiiDatasetExtracts =
                            this.gobiiDatasetExtracts
                                .filter(function (item) {
                                return item.getDataSetId() != Number(arg.id);
                            });
                    } // if-else we're adding
                };
                ExtractorRoot.prototype.handleExtractDataSetUnchecked = function (arg) {
                    // this.changeTrigger++;
                    // this.dataSetIdToUncheck = Number(arg.id);
                    var dataSetExtractsToRemove = this.gobiiDatasetExtracts
                        .filter(function (e) {
                        return e.getDataSetId() === Number(arg.id);
                    });
                    if (dataSetExtractsToRemove.length > 0) {
                        var idxToRemove = this.gobiiDatasetExtracts.indexOf(dataSetExtractsToRemove[0]);
                        this.gobiiDatasetExtracts.splice(idxToRemove, 1);
                    }
                    this.checkBoxEventChange = arg;
                };
                ExtractorRoot.prototype.handleExtractSubmission = function () {
                    var gobiiExtractorInstructions = [];
                    var gobiiFileType = type_gobii_file_1.GobiiFileType[this.selectedFormatName.toUpperCase()];
                    this.gobiiDatasetExtracts.forEach(function (e) { return e.setGobiiFileType(gobiiFileType); });
                    gobiiExtractorInstructions.push(new gobii_extractor_instruction_1.GobiiExtractorInstruction(this.gobiiDatasetExtracts, Number(this.selectedContactIdForSubmitter), null));
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
                    var extractorInstructionFilesDTORequest = new dto_extractor_instruction_files_1.ExtractorInstructionFilesDTO(gobiiExtractorInstructions, fileName, type_process_1.ProcessType.CREATE, type_crop_1.GobiiCropType[this.selectedServerConfig.crop]);
                    var extractorInstructionFilesDTOResponse = null;
                    var scope$ = this;
                    this._dtoRequestServiceExtractorFile.getResult(new dto_request_item_extractor_submission_1.DtoRequestItemExtractorSubmission(extractorInstructionFilesDTORequest))
                        .subscribe(function (extractorInstructionFilesDTO) {
                        extractorInstructionFilesDTOResponse = extractorInstructionFilesDTO;
                        scope$.messages.push("Extractor instruction file created on server: "
                            + extractorInstructionFilesDTOResponse.getInstructionFileName());
                    }, function (dtoHeaderResponse) {
                        dtoHeaderResponse.statusMessages.forEach(function (m) { return scope$.messages.push("Submitting extractor instructions: "
                            + m.message); });
                    });
                };
                ExtractorRoot.prototype.ngOnInit = function () {
                    this.initializeServerConfigs();
                };
                ExtractorRoot = __decorate([
                    core_1.Component({
                        selector: 'extractor-root',
                        directives: [export_format_component_1.ExportFormatComponent,
                            contacts_list_box_component_1.ContactsListBoxComponent,
                            project_list_box_component_1.ProjectListBoxComponent,
                            experiment_list_box_component_1.ExperimentListBoxComponent,
                            dataset_checklist_box_component_1.DataSetCheckListBoxComponent,
                            criteria_display_component_1.CriteriaDisplayComponent,
                            status_display_box_component_1.StatusDisplayComponent,
                            crops_list_box_component_1.CropsListBoxComponent,
                            users_list_box_component_1.UsersListBoxComponent],
                        styleUrls: ['/extractor-ui.css'],
                        providers: [
                            http_1.HTTP_PROVIDERS,
                            authentication_service_1.AuthenticationService,
                            dto_request_service_1.DtoRequestService
                        ],
                        template: "\n        <div class = \"panel panel-default\">\n        \n           <div class = \"panel-heading\">\n              <h1 class = \"panel-title\">GOBii Extractor</h1>\n           </div>\n           \n            <div class=\"container-fluid\">\n            \n                <div class=\"row\">\n                \n                    <div class=\"col-md-4\">\n                        <fieldset class=\"well the-fieldset\">\n                        <legend class=\"the-legend\">Crop</legend>\n                        <crops-list-box\n                            [serverConfigList]=\"serverConfigList\"\n                            [selectedServerConfig]=\"selectedServerConfig\"\n                            (onServerSelected)=\"handleServerSelected($event)\"></crops-list-box>\n                        </fieldset>\n                        \n                        <fieldset class=\"well the-fieldset\">\n                        <legend class=\"the-legend\">Submit As</legend>\n                        <users-list-box\n                            [nameIdList]=\"contactNameIdListForSubmitter\"\n                            (onUserSelected)=\"handleContactForSubmissionSelected($event)\">\n                        </users-list-box>\n                        </fieldset>\n                        \n                        <div class=\"col-md-12\">\n                            <export-format (onFormatSelected)=\"handleFormatSelected($event)\"></export-format>\n                        </div>\n                       \n                    </div>  <!-- outer grid column 1-->\n                \n                \n                \n                    <div class=\"col-md-4\"> \n                        <fieldset class=\"well the-fieldset\">\n                        <legend class=\"the-legend\">Principle Investigator</legend>\n                        <contacts-list-box [nameIdList]=\"contactNameIdListForPi\" (onContactSelected)=\"handleContactForPiSelected($event)\"></contacts-list-box>\n                        </fieldset>\n                        \n                        <fieldset class=\"well the-fieldset\">\n                        <legend class=\"the-legend\">Project</legend>\n                        <project-list-box [primaryInvestigatorId] = \"selectedContactIdForPi\"\n                            [nameIdList]=\"projectNameIdList\"\n                            [nameIdListPIs]=\"contactNameIdListForPi\"\n                            (onProjectSelected)=\"handleProjectSelected($event)\"\n                            (onAddMessage)=\"handleAddMessage($event)\"></project-list-box>\n                        </fieldset>\n                        \n                        <fieldset class=\"well the-fieldset\">\n                        <legend class=\"the-legend\">Experiment</legend>\n                        <experiment-list-box [projectId] = \"selectedProjectId\"\n                            [nameIdList] = \"experimentNameIdList\"\n                            (onExperimentSelected)=\"handleExperimentSelected($event)\"\n                            (onAddMessage)=\"handleAddMessage($event)\"></experiment-list-box>\n                        </fieldset>\n                        \n                        <fieldset class=\"well the-fieldset\">\n                        <legend class=\"the-legend\">Data Sets</legend>\n                        <dataset-checklist-box\n                            [checkBoxEventChange] = \"checkBoxEventChange\"\n                            [experimentId] = \"selectedExperimentId\" \n                            (onItemChecked)=\"handleCheckedDataSetItem($event)\"\n                            (onAddMessage) = \"handleAddMessage($event)\">\n                        </dataset-checklist-box>\n                        </fieldset>\n                        \n                    </div>  <!-- outer grid column 2-->\n                    <div class=\"col-md-4\">\n                         \n                            <fieldset class=\"well the-fieldset\" style=\"vertical-align: bottom;\">\n                                <legend class=\"the-legend\">Extract</legend>\n                                <criteria-display \n                                    [dataSetCheckBoxEvents] = \"dataSetCheckBoxEvents\"\n                                    (onItemUnChecked) = \"handleExtractDataSetUnchecked($event)\"></criteria-display>\n                            </fieldset>\n                            \n                            <form>\n                                <input type=\"button\" \n                                value=\"Submit\"\n                                 [disabled]=\"(gobiiDatasetExtracts.length === 0)\"\n                                (click)=\"handleExtractSubmission()\" >\n                            </form>\n                            \n                            <fieldset class=\"well the-fieldset\" style=\"vertical-align: bottom;\">\n                                <legend class=\"the-legend\">Status</legend>\n                                <status-display [messages] = \"messages\"></status-display>\n                            </fieldset>\n                                   \n                    </div>  <!-- outer grid column 3 (inner grid)-->\n                                        \n                </div> <!-- .row of outer grid -->\n                \n                    <div class=\"row\"><!-- begin .row 2 of outer grid-->\n                        <div class=\"col-md-3\"><!-- begin column 1 of outer grid -->\n                         \n                         </div><!-- end column 1 of outer grid -->\n                    \n                    </div><!-- end .row 2 of outer grid-->\n                \n            </div>" // end template
                    }), 
                    __metadata('design:paramtypes', [dto_request_service_1.DtoRequestService, dto_request_service_1.DtoRequestService, dto_request_service_1.DtoRequestService])
                ], ExtractorRoot);
                return ExtractorRoot;
            }());
            exports_1("ExtractorRoot", ExtractorRoot);
        }
    }
});
//# sourceMappingURL=app.extractorroot.js.map