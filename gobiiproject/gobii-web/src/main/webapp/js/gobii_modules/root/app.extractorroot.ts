///<reference path="../../../../../../typings/index.d.ts"/>
import {Component, OnInit} from "@angular/core";
import {DtoRequestService} from "../services/core/dto-request.service";
import {GobiiDataSetExtract} from "../model/extractor-instructions/data-set-extract";
import {ProcessType} from "../model/type-process";
import {FileItem} from "../model/file-item";
import {ServerConfig} from "../model/server-config";
import {EntityType, EntitySubType} from "../model/type-entity";
import {NameId} from "../model/name-id";
import {GobiiFileType} from "../model/type-gobii-file";
import {ExtractorInstructionFilesDTO} from "../model/extractor-instructions/dto-extractor-instruction-files";
import {GobiiExtractorInstruction} from "../model/extractor-instructions/gobii-extractor-instruction";
import {DtoRequestItemExtractorSubmission} from "../services/app/dto-request-item-extractor-submission";
import {DtoRequestItemNameIds} from "../services/app/dto-request-item-nameids";
import {DtoRequestItemServerConfigs} from "../services/app/dto-request-item-serverconfigs";
import {EntityFilter} from "../model/type-entity-filter";
import {SampleMarkerList} from "../model/sample-marker-list";
import {GobiiExtractFilterType} from "../model/type-extractor-filter";
import {GobiiSampleListType} from "../model/type-extractor-sample-list";
import {CvFilters, CvFilterType} from "../model/cv-filter-type";
import {FileModelTreeService} from "../services/core/file-model-tree-service";
import {ExtractorItemType} from "../model/file-model-node";
import {DtoHeaderResponse} from "../model/dto-header-response";
import {GobiiExtractFormat} from "../model/type-extract-format";
import {FileModelState} from "../model/file-model-tree-event";
import forEach = require("core-js/fn/array/for-each");
import {platform} from "os";
import {Header} from "../model/payload/header";
import {HeaderStatusMessage} from "../model/dto-header-status-message";

// import { RouteConfig, ROUTER_DIRECTIVES, ROUTER_PROVIDERS } from 'angular2/router';

// GOBii Imports


@Component({
    selector: 'extractor-root',
    styleUrls: ['/extractor-ui.css'],
    template: `<div class = "panel panel-default">
        
           <div class = "panel-heading">
                <img src="images/gobii_logo.png" alt="GOBii Project"/>

                <fieldset class="well the-fieldset">
                    <div class="col-md-2">
                        <crops-list-box
                            [serverConfigList]="serverConfigList"
                            [selectedServerConfig]="selectedServerConfig"
                            (onServerSelected)="handleServerSelected($event)"></crops-list-box>
                    </div>
                    
                    <div class="col-md-3">
                       <export-type
                        (onExportTypeSelected)="handleExportTypeSelected($event)"></export-type>
                     </div>
                     
                </fieldset>
           </div>
           
            <div class="container-fluid">
            
                <div class="row">
                
                    <div class="col-md-4">
                    
                    <fieldset class="well the-fieldset">
                    <legend class="the-legend">Submit As</legend>
                    <users-list-box
                        [nameIdList]="contactNameIdListForSubmitter"
                        (onUserSelected)="handleContactForSubmissionSelected($event)">
                    </users-list-box>
                    </fieldset>
                        
                     <fieldset class="well the-fieldset">
                        <legend class="the-legend">Filters</legend><BR>
                        
                        
                        <div *ngIf="displaySelectorPi">
                            <label class="the-label">Principle Investigator:</label><BR>
                            <contacts-list-box [nameIdList]="contactNameIdListForPi" (onContactSelected)="handleContactForPiSelected($event)"></contacts-list-box>
                        </div>
                        
                        <div *ngIf="displaySelectorProject">
                            <BR>
                            <BR>
                            <label class="the-label">Project:</label><BR>
                            <project-list-box [primaryInvestigatorId] = "selectedContactIdForPi"
                                [nameIdList]="projectNameIdList"
                                [nameIdListPIs]="contactNameIdListForPi"
                                (onProjectSelected)="handleProjectSelected($event)"
                                (onAddMessage)="handleAddMessage($event)"></project-list-box>
                        </div>

                        <div *ngIf="displaySelectorDataType">
                            <BR>
                            <BR>
                            <label class="the-label">Dataset Types:</label><BR>
                            <dataset-types-list-box [nameIdList]="datasetTypeNameIdList" (onDatasetTypeSelected)="handleDatasetTypeSelected($event)"></dataset-types-list-box>
                        </div>

                        
                        <div *ngIf="displaySelectorExperiment">
                            <BR>
                            <BR>
                            <label class="the-label">Experiment:</label><BR>
                            <experiment-list-box [projectId] = "selectedProjectId"
                                [nameIdList] = "experimentNameIdList"
                                (onExperimentSelected)="handleExperimentSelected($event)"
                                (onAddMessage)="handleAddMessage($event)"></experiment-list-box>
                        </div>

                        <div *ngIf="displaySelectorPlatform">
                            <BR>
                            <BR>
                            <label class="the-label">Platforms:</label><BR>
                            <checklist-box
                                [fileItemEventChange] = "platformFileItemEventChange"
                                [nameIdList] = "platformsNameIdList"
                                (onItemSelected)="handlePlatformSelected($event)"
                                (onItemChecked)="handlePlatformChecked($event)"
                                (onAddMessage) = "handleAddMessage($event)">
                            </checklist-box>
                         </div>


                        <div *ngIf="displayAvailableDatasets">
                            <BR>
                            <BR>
                            <label class="the-label">Data Sets</label><BR>
                            <dataset-checklist-box
                                [fileItemEventChange] = "datasetFileItemEventChange"
                                [experimentId] = "selectedExperimentId" 
                                (onItemChecked)="handleCheckedDataSetItem($event)"
                                (onAddMessage) = "handleAddMessage($event)">
                            </dataset-checklist-box>
                        </div>
                    </fieldset>
                       
                       
                    </div>  <!-- outer grid column 1-->
                
                
                
                    <div class="col-md-4"> 

                        <div *ngIf="displaySampleListTypeSelector">
                            <fieldset class="well the-fieldset" style="vertical-align: bottom;">
                                <legend class="the-legend">Included Samples</legend>
                                <sample-list-type
                                    (onSampleListTypeSelected)="handleSampleListTypeSelected($event)">
                                 </sample-list-type>
                                <hr style="width: 100%; color: black; height: 1px; background-color:black;" />
                                <sample-marker-box 
                                    (onMarkerSamplesCompleted) = "handleSampleMarkerListComplete($event)">
                                </sample-marker-box>
                            </fieldset>
                        </div>
                        
                        <div *ngIf="displaySampleMarkerBox">
                            <fieldset class="well the-fieldset" style="vertical-align: bottom;">
                                <legend class="the-legend">Included Markers</legend>
                                <sample-marker-box 
                                    (onMarkerSamplesCompleted) = "handleSampleMarkerListComplete($event)">
                                </sample-marker-box>
                            </fieldset>
                        </div>

                        
                        <form>
                           <fieldset class="well the-fieldset">
                                <legend class="the-legend">Extract</legend>
                           
                                <export-format (onFormatSelected)="handleFormatSelected($event)"></export-format>
                                <BR>
                           
                                <mapsets-list-box [nameIdList]="mapsetNameIdList" 
                                    (onMapsetSelected)="handleMapsetSelected($event)"></mapsets-list-box>
                            </fieldset>
                        </form>
                        
                        
                    </div>  <!-- outer grid column 2-->
                    
                    
                    <div class="col-md-4">

                        <fieldset class="well the-fieldset" style="vertical-align: bottom;">
                            <legend class="the-legend">Extraction Criteria Summary</legend>
                            <status-display-tree
                                [fileItemEventChange] = "treeFileItemEvent"
                                [gobiiExtractFilterTypeEvent] = "gobiiExtractFilterType"
                                (onAddMessage)="handleHeaderStatusMessage($event)"
                                (onTreeReady)="handleStatusTreeReady($event)">
                            </status-display-tree>
                            <BR>
                                <input type="button" 
                                value="Submit"
                                 [disabled]="(gobiiDatasetExtracts.length === 0)"
                                (click)="handleExtractSubmission()" >
                            
                        </fieldset>
                            
                        <div>
                            <fieldset class="well the-fieldset" style="vertical-align: bottom;">
                                <legend class="the-legend">Status: {{currentStatus}}</legend>
                                <status-display [messages] = "messages"></status-display>
                            </fieldset>
                        </div>
                            
                                   
                    </div>  <!-- outer grid column 3 (inner grid)-->
                                        
                </div> <!-- .row of outer grid -->
                
                    <div class="row"><!-- begin .row 2 of outer grid-->
                        <div class="col-md-3"><!-- begin column 1 of outer grid -->
                         
                         </div><!-- end column 1 of outer grid -->
                    
                    </div><!-- end .row 2 of outer grid-->
                
            </div>` // end template
}) // @Component

export class ExtractorRoot implements OnInit {
    title = 'Gobii Web';


    private treeFileItemEvent: FileItem;
//    private selectedExportTypeEvent:GobiiExtractFilterType;
    private datasetFileItemEvents: FileItem[] = [];
    private gobiiDatasetExtracts: GobiiDataSetExtract[] = [];
    private messages: string[] = [];


    constructor(private _dtoRequestServiceExtractorFile: DtoRequestService<ExtractorInstructionFilesDTO>,
                private _dtoRequestServiceNameIds: DtoRequestService<NameId[]>,
                private _dtoRequestServiceServerConfigs: DtoRequestService<ServerConfig[]>,
                private _fileModelTreeService: FileModelTreeService) {
    }


    // ****************************************************************
    // ********************************************** SERVER SELECTION
    private selectedServerConfig: ServerConfig;
    private serverConfigList: ServerConfig[];
    private currentStatus: string;

    private initializeServerConfigs() {
        let scope$ = this;
        this._dtoRequestServiceServerConfigs.get(new DtoRequestItemServerConfigs()).subscribe(serverConfigs => {

                if (serverConfigs && ( serverConfigs.length > 0 )) {
                    scope$.serverConfigList = serverConfigs;

                    let serverCrop: String =
                        this._dtoRequestServiceServerConfigs.getGobiiCropType();

                    let gobiiVersion: string = this._dtoRequestServiceServerConfigs.getGobbiiVersion();

                    scope$.selectedServerConfig =
                        scope$.serverConfigList
                            .filter(c => {
                                    return c.crop === serverCrop;
                                }
                            )[0];

                    scope$.currentStatus = "GOBII Server " + gobiiVersion;
                    scope$.messages.push("Connected to database: " + scope$.selectedServerConfig.crop);
                    scope$.initializeContactsForSumission();
                    scope$.initializeContactsForPi();
                    scope$.initializeMapsetsForSumission();

                } else {
                    scope$.serverConfigList = [new ServerConfig("<ERROR NO SERVERS>", "<ERROR>", "<ERROR>", 0)];
                }
            },
            dtoHeaderResponse => {
                dtoHeaderResponse.statusMessages.forEach(m => scope$.messages.push("Retrieving server configs: "
                    + m.message))
            }
        )
        ;
    } // initializeServerConfigs()

    private handleServerSelected(arg) {
        this.selectedServerConfig = arg;
        // this._dtoRequestServiceNameIds
        //     .setCropType(GobiiCropType[this.selectedServerConfig.crop]);
        let currentPath = window.location.pathname;
        let currentPage: string = currentPath.substr(currentPath.lastIndexOf('/') + 1, currentPath.length);
        let newDestination = "http://"
            + this.selectedServerConfig.domain
            + ":"
            + this.selectedServerConfig.port
            + this.selectedServerConfig.contextRoot
            + currentPage;
//        console.log(newDestination);
        window.location.href = newDestination;
    } // handleServerSelected()


// ********************************************************************
// ********************************************** EXPORT TYPE SELECTION AND FLAGS


    private displayAvailableDatasets: boolean = true;
    private displaySelectorPi: boolean = true;
    private displaySelectorProject: boolean = true;
    private displaySelectorExperiment: boolean = true;
    private displaySelectorDataType: boolean = false;
    private displaySelectorPlatform: boolean = false;
    private displayIncludedDatasetsGrid: boolean = true;
    private displaySampleListTypeSelector: boolean = false;
    private displaySampleMarkerBox: boolean = false;
    private gobiiExtractFilterType: GobiiExtractFilterType;

    private handleExportTypeSelected(arg: GobiiExtractFilterType) {

        let foo: string = "foo";

        this.gobiiExtractFilterType = arg;


//        let extractorFilterItemType: FileItem = FileItem.bui(this.gobiiExtractFilterType)

        if (this.gobiiExtractFilterType === GobiiExtractFilterType.WHOLE_DATASET) {

            this.displaySelectorPi = true;
            this.displaySelectorProject = true;
            this.displaySelectorExperiment = true;
            this.displayAvailableDatasets = true;
            this.displayIncludedDatasetsGrid = true;

            this.displaySelectorDataType = false;
            this.displaySelectorPlatform = false;
            this.displaySampleListTypeSelector = false;
            this.displaySampleMarkerBox = false;


        } else if (this.gobiiExtractFilterType === GobiiExtractFilterType.BY_SAMPLE) {

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


        } else if (this.gobiiExtractFilterType === GobiiExtractFilterType.BY_MARKER) {

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


    }

// ********************************************************************
// ********************************************** SUBMISSION-USER SELECTION
    private contactNameIdListForSubmitter: NameId[];
    private selectedContactIdForSubmitter: string;

    private handleContactForSubmissionSelected(arg: NameId) {
        this.selectedContactIdForSubmitter = arg.id;

        let fileItem: FileItem = FileItem
            .build(this.gobiiExtractFilterType, ProcessType.UPDATE)
            .setEntityType(EntityType.Contacts)
            .setEntitySubType(EntitySubType.CONTACT_SUBMITED_BY)
            .setItemId(arg.id)
            .setItemName(arg.name);

        this._fileModelTreeService.put(fileItem)
            .subscribe(
                null,
                headerResponse => {
                    this.handleResponseHeader(headerResponse)
                });

    }

    private initializeContactsForSumission() {
        let scope$ = this;
        this._dtoRequestServiceNameIds.get(new DtoRequestItemNameIds(
            EntityType.Contacts)).subscribe(nameIds => {
                if (nameIds && ( nameIds.length > 0 )) {
                    scope$.contactNameIdListForSubmitter = nameIds
                    scope$.selectedContactIdForSubmitter = nameIds[0].id;
                    this.handleContactForSubmissionSelected(nameIds[0]);
                } else {
                    scope$.contactNameIdListForSubmitter = [new NameId("0", "ERROR NO USERS", EntityType.Contacts)];
                }
            },
            dtoHeaderResponse => {
                dtoHeaderResponse.statusMessages.forEach(m => scope$.messages.push("Rettrieving contacts: "
                    + m.message))
            });

    }


// ********************************************************************
// ********************************************** PI USER SELECTION
    private contactNameIdListForPi: NameId[];
    private selectedContactIdForPi: string;

    private handleContactForPiSelected(arg) {
        this.selectedContactIdForPi = arg;
        this.initializeProjectNameIds();
        //console.log("selected contact itemId:" + arg);
    }

    private initializeContactsForPi() {
        let scope$ = this;
        scope$._dtoRequestServiceNameIds.get(new DtoRequestItemNameIds(
            EntityType.Contacts,
            EntityFilter.NONE)).subscribe(nameIds => {

                if (nameIds && ( nameIds.length > 0 )) {
                    scope$.contactNameIdListForPi = nameIds;
                    scope$.selectedContactIdForPi = scope$.contactNameIdListForPi[0].id;
                } else {
                    scope$.contactNameIdListForPi = [new NameId("0", "ERROR NO USERS", EntityType.Contacts)];
                }

                scope$.initializeProjectNameIds();

            },
            dtoHeaderResponse => {
                dtoHeaderResponse.statusMessages.forEach(m => scope$.messages.push("Retrieving contacts for PIs: "
                    + m.message))
            });
    }

// ********************************************************************
// ********************************************** HAPMAP SELECTION
    private selectedExtractFormat: GobiiExtractFormat = GobiiExtractFormat.HAPMAP;

    private handleFormatSelected(arg: GobiiExtractFormat) {

        this.selectedExtractFormat = arg;

        let extractFilterTypeFileItem: FileItem = FileItem
            .build(this.gobiiExtractFilterType, ProcessType.UPDATE)
            .setExtractorItemType(ExtractorItemType.EXPORT_FORMAT)
            .setItemId(GobiiExtractFormat[arg])
            .setItemName(GobiiExtractFormat[GobiiExtractFormat[arg]]);

        this._fileModelTreeService.put(extractFilterTypeFileItem)
            .subscribe(
                null,
                headerResponse => {
                    this.handleResponseHeader(headerResponse)
                });

        //console.log("selected contact itemId:" + arg);
    }

// ********************************************************************
// ********************************************** PROJECT ID
    private projectNameIdList: NameId[];
    private selectedProjectId: string;

    private handleProjectSelected(arg) {
        this.selectedProjectId = arg;
        this.displayExperimentDetail = false;
        this.displayDataSetDetail = false;
        this.initializeExperimentNameIds();
    }

    private initializeProjectNameIds() {
        let scope$ = this;
        scope$._dtoRequestServiceNameIds.get(new DtoRequestItemNameIds(
            EntityType.Projects,
            EntityFilter.BYTYPEID,
            this.selectedContactIdForPi)).subscribe(nameIds => {

                if (nameIds && ( nameIds.length > 0 )) {
                    scope$.projectNameIdList = nameIds;
                    scope$.selectedProjectId = nameIds[0].id;
                } else {
                    scope$.projectNameIdList = [new NameId("0", "<none>", EntityType.Projects)];
                    scope$.selectedProjectId = undefined;
                }

                this.initializeExperimentNameIds();
            },
            dtoHeaderResponse => {
                dtoHeaderResponse.statusMessages.forEach(m => scope$.messages.push("Retriving project names: "
                    + m.message))
            });
    }

// ********************************************************************
// ********************************************** EXPERIMENT ID
    private displayExperimentDetail: boolean = false;

    private experimentNameIdList: NameId[];
    private selectedExperimentId: string;
    private selectedExperimentDetailId: string;

    private handleExperimentSelected(arg) {
        this.selectedExperimentId = arg;
        this.selectedExperimentDetailId = arg;
        this.displayExperimentDetail = true;

        //console.log("selected contact itemId:" + arg);
    }

    private initializeExperimentNameIds() {

        let scope$ = this;
        if (this.selectedProjectId) {
            this._dtoRequestServiceNameIds.get(new DtoRequestItemNameIds(
                EntityType.Experiments,
                EntityFilter.BYTYPEID,
                this.selectedProjectId)).subscribe(nameIds => {
                    if (nameIds && ( nameIds.length > 0 )) {
                        scope$.experimentNameIdList = nameIds;
                        scope$.selectedExperimentId = scope$.experimentNameIdList[0].id;
                    } else {
                        scope$.experimentNameIdList = [new NameId("0", "<none>", EntityType.Experiments)];
                        scope$.selectedExperimentId = undefined;
                    }
                },
                dtoHeaderResponse => {
                    dtoHeaderResponse.statusMessages.forEach(m => scope$.messages.push("Retreving experiment names: "
                        + m.message))
                });
        } else {
            scope$.experimentNameIdList = [new NameId("0", "<none>", EntityType.Experiments)];
            scope$.selectedExperimentId = undefined;
        }

    }

// ********************************************************************
// ********************************************** DATASET TYPE SELECTION
    private datasetTypeNameIdList: NameId[];
    private selectedDatasetTypeId: string;

    private handleDatasetTypeSelected(arg) {
        this.selectedDatasetTypeId = arg;
    }

    private initializeDatasetTypes() {
        let scope$ = this;
        scope$._dtoRequestServiceNameIds.get(new DtoRequestItemNameIds(
            EntityType.CvTerms,
            EntityFilter.BYTYPENAME,
            CvFilters.get(CvFilterType.DATASET_TYPE))).subscribe(nameIds => {

                if (nameIds && ( nameIds.length > 0 )) {
                    scope$.datasetTypeNameIdList = nameIds;
                    scope$.selectedDatasetTypeId = scope$.datasetTypeNameIdList[0].id;
                } else {
                    scope$.datasetTypeNameIdList = [new NameId("0", "ERROR NO DATASET TYPES", EntityType.CvTerms)];
                }
            },
            dtoHeaderResponse => {
                dtoHeaderResponse.statusMessages.forEach(m => scope$.messages.push("Retrieving DatasetTypes: "
                    + m.message))
            });
    }

// ********************************************************************
// ********************************************** PLATFORM SELECTION
    private platformsNameIdList: NameId[];
    private selectedPlatformId: string;
    private checkedPlatformId: string;

    private handlePlatformSelected(arg) {
        this.selectedPlatformId = arg.id;
    }

    private handlePlatformChecked(arg) {
        this.checkedPlatformId = arg.id;
    }

    private platformFileItemEventChange: FileItem;


    private initializePlatforms() {
        let scope$ = this;
        scope$._dtoRequestServiceNameIds.get(new DtoRequestItemNameIds(
            EntityType.Platforms,
            EntityFilter.NONE)).subscribe(nameIds => {

                if (nameIds && ( nameIds.length > 0 )) {
                    scope$.platformsNameIdList = nameIds;
                    scope$.selectedPlatformId = scope$.platformsNameIdList[0].id;
                } else {
                    scope$.platformsNameIdList = [new NameId("0", "ERROR NO PLATFORMS", EntityType.Platforms)];
                }
            },
            dtoHeaderResponse => {
                dtoHeaderResponse.statusMessages.forEach(m => scope$.messages.push("Retrieving PlatformTypes: "
                    + m.message))
            });
    }

// ********************************************************************
// ********************************************** DATASET ID
    private displayDataSetDetail: boolean = false;
    private dataSetIdToUncheck: number;

    private handleAddMessage(arg) {
        this.messages.push(arg);
    }


    private handleHeaderStatusMessage(statusMessage:HeaderStatusMessage) {

        this.handleAddMessage(statusMessage.message);
    }

    private handleResponseHeader(header: Header) {

        if (header.status !== null && header.status.statusMessages != null) {

            header.status.statusMessages.forEach(statusMessage => {
                this.handleHeaderStatusMessage(statusMessage);
            })
        }
    }

    handleStatusTreeReady(headerStatusMessage: HeaderStatusMessage) {

        this.handleFormatSelected(GobiiExtractFormat.HAPMAP);
        //this.handleContactForSubmissionSelected(this.contactNameIdListForSubmitter[0]);

    }

    private makeDatasetExtract() {

        this.gobiiDatasetExtracts.push(new GobiiDataSetExtract(GobiiFileType.GENERIC,
            false,
            Number(this.selectedDatasetId),
            this.selectedDatasetName,
            null,
            this.gobiiExtractFilterType,
            this.markerList,
            this.sampleList,
            this.uploadFileName,
            this.selectedSampleListType,
            null,
            null));

    }


    private selectedDatasetId: string;
    private selectedDatasetName: string;

    private handleCheckedDataSetItem(arg: FileItem) {


        this.selectedDatasetId = arg.getItemId();

        if (ProcessType.CREATE == arg.getProcessType()) {

            this.makeDatasetExtract();

        } else {

            let indexOfEventToRemove: number = this.datasetFileItemEvents.indexOf(arg);
            this.datasetFileItemEvents.splice(indexOfEventToRemove, 1);

            this.gobiiDatasetExtracts =
                this.gobiiDatasetExtracts
                    .filter((item: GobiiDataSetExtract) => {
                        return item.getdataSetId() != Number(arg.getItemId())
                    });
        } // if-else we're adding

        //this.treeFileItemEvent = FileItem.fromFileItem(arg);
        let fileItemEvent: FileItem = FileItem.fromFileItem(arg, this.gobiiExtractFilterType);


        this._fileModelTreeService.put(fileItemEvent).subscribe(
            null,
            headerResponse => {
                this.handleResponseHeader(headerResponse)
            });

    }

    //private datasetFileItemEventChange: FileItem;
    private changeTrigger: number = 0;

    private handleExtractDataSetUnchecked(arg: FileItem) {
        // this.changeTrigger++;
        // this.dataSetIdToUncheck = Number(arg.itemId);


        this.datasetFileItemEvents.push(arg);
        let dataSetExtractsToRemove: GobiiDataSetExtract[] = this.gobiiDatasetExtracts
            .filter(e => {
                return e.getdataSetId() === Number(arg.getItemId())
            });

        if (dataSetExtractsToRemove.length > 0) {
            let idxToRemove = this.gobiiDatasetExtracts.indexOf(dataSetExtractsToRemove[0]);

            this.gobiiDatasetExtracts.splice(idxToRemove, 1);
        }

        // this.datasetFileItemEventChange = arg;
        this.treeFileItemEvent = FileItem.fromFileItem(arg, this.gobiiExtractFilterType);

    }


// ********************************************************************
// ********************************************** MAPSET SELECTIONz
    private mapsetNameIdList: NameId[];
    private selectedMapsetId: string;
    private nullMapsetName: string;

    private handleMapsetSelected(arg: NameId) {

        if (Number(arg.id) > 0) {
            this.selectedMapsetId = arg.id;
            let fileItem: FileItem = FileItem.build(this.gobiiExtractFilterType,
                ProcessType.CREATE)
                .setEntityType(EntityType.Mapsets)
                .setCvFilterType(CvFilterType.UKNOWN)
                .setItemId(arg.id)
                .setItemName(arg.name)
                .setChecked(true)
                .setRequired(null);

            this._fileModelTreeService.put(fileItem).subscribe(
                null,
                headerResponse => {
                    this.handleResponseHeader(headerResponse)
                });

        } else {
            this.selectedMapsetId = undefined;
        }
    }

    private initializeMapsetsForSumission() {
        let scope$ = this;
        scope$.nullMapsetName = "<none>"
        this._dtoRequestServiceNameIds.get(new DtoRequestItemNameIds(
            EntityType.Mapsets)).subscribe(nameIds => {

                scope$.mapsetNameIdList = [new NameId("0", scope$.nullMapsetName, EntityType.Mapsets)]
                if (nameIds && ( nameIds.length > 0 )) {
                    scope$.mapsetNameIdList = scope$.mapsetNameIdList.concat(nameIds);
                    scope$.selectedMapsetId = nameIds[0].id;
                }
            },
            dtoHeaderResponse => {
                dtoHeaderResponse.statusMessages.forEach(m => scope$.messages.push("Rettrieving mapsets: "
                    + m.message))
            });

    }


// ********************************************************************
// ********************************************** MARKER/SAMPLE selection
    private markerList: string[] = null;
    private sampleList: string[] = null;
    private uploadFileName: string = null;

    private handleSampleMarkerListComplete(arg: SampleMarkerList) {

        let sampleMarkerList: SampleMarkerList = arg;


        if (sampleMarkerList.isArray) {
            if (this.gobiiExtractFilterType === GobiiExtractFilterType.BY_SAMPLE) {
                this.sampleList = sampleMarkerList.items;

            } else if (this.gobiiExtractFilterType === GobiiExtractFilterType.BY_MARKER) {
                this.markerList = sampleMarkerList.items;
            }
        } else {
            this.uploadFileName = sampleMarkerList.uploadFileName;
        }

        this.makeDatasetExtract();
    }


    // ********************************************************************
    // ********************************************** Sample List Type Selection
    private selectedSampleListType: GobiiSampleListType;

    private handleSampleListTypeSelected(arg: GobiiSampleListType) {
        this.selectedSampleListType = arg;
    }

    // ********************************************************************
    // ********************************************** Extract file submission
    private handleExtractSubmission() {

        let scope$ = this;

        let gobiiExtractorInstructions: GobiiExtractorInstruction[] = [];
        let gobiiDataSetExtracts: GobiiDataSetExtract[] = [];
        let mapsetIds: number[] = [];
        let submitterContactid: number = null;
        scope$._fileModelTreeService.getFileItems(scope$.gobiiExtractFilterType).subscribe(
            fileItems => {

                let submitterFileItem: FileItem = fileItems.find(item => {
                    return (item.getEntityType() === EntityType.Contacts)
                        && (item.getEntitySubType() === EntitySubType.CONTACT_SUBMITED_BY)
                });

                submitterContactid = Number(submitterFileItem.getItemId());


                mapsetIds = fileItems
                    .filter(item => {
                        return item.getEntityType() === EntityType.Mapsets
                    })
                    .map(item => {
                        return Number(item.getItemId())
                    });

                let exportFileItem: FileItem = fileItems.find(item => {
                    return item.getExtractorItemType() === ExtractorItemType.EXPORT_FORMAT
                });
                let extractFormat: GobiiExtractFormat = GobiiExtractFormat[exportFileItem.getItemId()];
                let gobiiFileType: GobiiFileType = GobiiFileType[GobiiExtractFormat[extractFormat]];

                let dataTypeFileItem: FileItem = fileItems.find(item => {
                    return item.getEntityType() === EntityType.CvTerms
                        && item.getCvFilterType() === CvFilterType.DATASET_TYPE
                });

                let datSetTypeName: string = dataTypeFileItem != null ? dataTypeFileItem.getItemName() : null;

                let platformFileItems: FileItem[] = fileItems.filter(item => {
                    return item.getEntityType() === EntityType.Platforms
                });

                let platformIds: number[] = platformFileItems.map(item => {
                    return Number(item.getItemId())
                });


                fileItems
                    .filter(item => {
                        return item.getEntityType() === EntityType.DataSets
                    })
                    .forEach(datsetFileItem => {

                        gobiiDataSetExtracts.push(new GobiiDataSetExtract(gobiiFileType,
                            false,
                            Number(datsetFileItem.getItemId()),
                            datsetFileItem.getItemName(),
                            null,
                            this.gobiiExtractFilterType,
                            this.markerList,
                            this.sampleList,
                            this.uploadFileName,
                            this.selectedSampleListType,
                            datSetTypeName,
                            platformIds));
                    });
            }
        );


        gobiiExtractorInstructions.push(
            new GobiiExtractorInstruction(
                gobiiDataSetExtracts,
                submitterContactid,
                null,
                mapsetIds)
        );


        let date: Date = new Date();
        let fileName: string = "extractor_"
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

        let extractorInstructionFilesDTORequest: ExtractorInstructionFilesDTO =
            new ExtractorInstructionFilesDTO(gobiiExtractorInstructions,
                fileName);

        let extractorInstructionFilesDTOResponse: ExtractorInstructionFilesDTO = null;

        this._dtoRequestServiceExtractorFile.post(new DtoRequestItemExtractorSubmission(extractorInstructionFilesDTORequest))
            .subscribe(extractorInstructionFilesDTO => {
                    extractorInstructionFilesDTOResponse = extractorInstructionFilesDTO;
                    scope$.messages.push("Extractor instruction file created on server: "
                        + extractorInstructionFilesDTOResponse.getInstructionFileName());
                },
                dtoHeaderResponse => {

                    scope$.handleResponseHeader(dtoHeaderResponse);
                });

    }

    ngOnInit(): any {

        this._fileModelTreeService
            .treeStateNotifications()
            .subscribe(ts => {

                if (ts.fileModelState == FileModelState.SUBMISSION_READY) {
                    //
                }

            });


        this.initializeServerConfigs();
        this.handleExportTypeSelected(GobiiExtractFilterType.WHOLE_DATASET);

    }


}

