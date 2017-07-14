///<reference path="../../../../../../typings/index.d.ts"/>
import {Component, OnInit} from "@angular/core";
import {DtoRequestService} from "../services/core/dto-request.service";
import {GobiiDataSetExtract} from "../model/extractor-instructions/data-set-extract";
import {ProcessType} from "../model/type-process";
import {GobiiFileItem} from "../model/gobii-file-item";
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
import {NameIdRequestParams} from "../model/name-id-request-params";
import {FileName} from "../model/file_name";
import {Labels} from "../views/entity-labels";
import {TreeStatusNotification} from "../model/tree-status-notification";
import {Contact} from "../model/contact";
import {DtoRequestItemContact, ContactSearchType} from "../services/app/dto-request-item-contact";
import {AuthenticationService} from "../services/core/authentication.service";
import {FileItem} from "ng2-file-upload";
import {isNullOrUndefined} from "util";
import {NameIdLabelType} from "../model/name-id-label-type";
import {StatusLevel} from "../model/type-status-level";

// import { RouteConfig, ROUTER_DIRECTIVES, ROUTER_PROVIDERS } from 'angular2/router';

// GOBii Imports


@Component({
    selector: 'extractor-root',
    styleUrls: ['/extractor-ui.css'],
    template: `<div class = "panel panel-default">
        
           <div class = "panel-heading">
                <img src="images/gobii_logo.png" alt="GOBii Project"/>

                    <div class="panel panel-primary">
                      <div class="panel-heading">
                        <h3 class="panel-title">Connected to {{currentStatus}}</h3>
                      </div>
                      <div class="panel-body">
                    
                    <div class="col-md-1">
                    
                        <crops-list-box
                            [serverConfigList]="serverConfigList"
                            [selectedServerConfig]="selectedServerConfig"
                            (onServerSelected)="handleServerSelected($event)"></crops-list-box>
                     </div>
                                                
                    <div class="col-md-5">
                       <export-type
                        (onExportTypeSelected)="handleExportTypeSelected($event)"></export-type>
                     </div>
                    

                        <div class="col-md-4">
                        <div class = "well">
                            <table>
                                <tr>
                                    <td colspan="2">
                                        <export-format
                                         [gobiiExtractFilterType] = "gobiiExtractFilterType"
                                         (onFormatSelected)="handleFormatSelected($event)">
                                          </export-format>
                              </td>
                              <td style="vertical-align: top;">
                                     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
                                     <name-id-list-box
                                        [gobiiExtractFilterType] = "gobiiExtractFilterType"
                                        [nameIdRequestParams]="nameIdRequestParamsMapsets"
                                        (onError) = "handleHeaderStatusMessage($event)">
                                    </name-id-list-box>
                              </td>
                              </tr>
                              </table>
                              
                        </div>
                        </div>
    
                     
                    <div class="col-md-2">
                       <p style="text-align: right; font-weight: bold;">{{loggedInUser}}</p>
                     </div>
                     
                    </div> <!-- panel body -->
                    </div> <!-- panel primary -->

           </div>
           
            <div class="container-fluid">
            
                <div class="row">
                
                    <div class="col-md-4">
                    
                    <div class="panel panel-primary">
                      <div class="panel-heading">
                        <h3 class="panel-title">Filters</h3>
                      </div>
                      <div class="panel-body">

                          <div *ngIf="displaySelectorPi">
                            <label class="the-label">Principle Investigator:</label><BR>
                            <name-id-list-box
                                [gobiiExtractFilterType] = "gobiiExtractFilterType"
                                [nameIdRequestParams]="nameIdRequestParamsContactsPi"
                                [notifyOnInit]="!doPrincipleInvestigatorTreeNotifications"
                                [doTreeNotifications] = "doPrincipleInvestigatorTreeNotifications"
                                (onNameIdSelected)="handleContactForPiSelected($event)"
                                (onError) = "handleHeaderStatusMessage($event)">
                            </name-id-list-box>
                            
                        </div>
                        
                        <div *ngIf="displaySelectorProject" >
                            <BR>
                            <BR>
                            <label class="the-label">Project:</label><BR>
                            <project-list-box [primaryInvestigatorId] = "selectedContactIdForPi"
                                [gobiiExtractFilterType] = "gobiiExtractFilterType"
                                [reinitProjectList] = "reinitProjectList"
                                (onProjectSelected)="handleProjectSelected($event)"
                                (onAddHeaderStatus)="handleHeaderStatusMessage($event)"></project-list-box>
                        </div>

                        <div *ngIf="displaySelectorDataType" >
                            <BR>
                            <BR>
                            <label class="the-label">Dataset Types:</label><BR>
                            <name-id-list-box
                                [gobiiExtractFilterType] = "gobiiExtractFilterType"
                                [notifyOnInit]="false"
                                [nameIdRequestParams]="nameIdRequestParamsDatasetType"
                                (onError) = "handleHeaderStatusMessage($event)">
                            </name-id-list-box>
                        </div>

                        
                        <div *ngIf="displaySelectorExperiment">
                            <BR>
                            <BR>
                            <label class="the-label">Experiment:</label><BR>
                               <name-id-list-box
                                [gobiiExtractFilterType] = "gobiiExtractFilterType"
                                [notifyOnInit]="true"
                                [doTreeNotifications]= "false"
                                [nameIdRequestParams]="nameIdRequestParamsExperiments"
                                (onNameIdSelected) = "handleExperimentSelected($event)"
                                (onError) = "handleHeaderStatusMessage($event)">
                            </name-id-list-box>
                            
                        </div>

                        <div *ngIf="displaySelectorPlatform">
                            <BR>
                            <BR>
                            <label class="the-label">Platforms:</label><BR>
                            <checklist-box
                                [nameIdRequestParams] = "nameIdRequestParamsPlatforms"
                                [gobiiExtractFilterType] = "gobiiExtractFilterType"
                                [retainHistory] = "false"
                                (onAddStatusMessage) = "handleHeaderStatusMessage($event)">
                            </checklist-box>
                         </div>


                        <div *ngIf="displayAvailableDatasets">
                            <BR>
                            <BR>
                            <label class="the-label">Data Sets</label><BR>
                            <dataset-checklist-box
                                [gobiiExtractFilterType] = "gobiiExtractFilterType"
                                [experimentId] = "selectedExperimentId" 
                                (onAddStatusMessage) = "handleHeaderStatusMessage($event)">
                            </dataset-checklist-box>
                        </div>
                    </div> <!-- panel body -->
                    </div> <!-- panel primary -->
                       

                        <div *ngIf="displaySampleListTypeSelector">
                    <div class="panel panel-primary">
                      <div class="panel-heading">
                        <h3 class="panel-title">Included Samples</h3>
                      </div>
                      <div class="panel-body">
                                <sample-list-type
                                    [gobiiExtractFilterType] = "gobiiExtractFilterType"
                                    (onHeaderStatusMessage)="handleHeaderStatusMessage($event)">
                                 </sample-list-type>
                                <hr style="width: 100%; color: black; height: 1px; background-color:black;" />
                                <sample-marker-box 
                                    [gobiiExtractFilterType] = "gobiiExtractFilterType"
                                    (onSampleMarkerError)="handleHeaderStatusMessage($event)">
                                </sample-marker-box>
                    </div> <!-- panel body -->
                    </div> <!-- panel primary -->
                        </div>
                        
                        <div *ngIf="displaySampleMarkerBox">
                    <div class="panel panel-primary">
                      <div class="panel-heading">
                        <h3 class="panel-title">Included Markers</h3>
                      </div>
                      <div class="panel-body">
                                <sample-marker-box 
                                    [gobiiExtractFilterType] = "gobiiExtractFilterType"
                                    (onSampleMarkerError)="handleHeaderStatusMessage($event)">
                                </sample-marker-box>
                    </div> <!-- panel body -->
                    </div> <!-- panel primary -->
                        </div>
                       
                    </div>  <!-- outer grid column 1-->
                
                
                
                    <div class="col-md-4"> 

                    <div class="panel panel-primary">
                      <div class="panel-heading">
                        <h3 class="panel-title">Extraction Criteria</h3>
                      </div>
                      <div class="panel-body">
                            <status-display-tree
                                [fileItemEventChange] = "treeFileItemEvent"
                                [gobiiExtractFilterTypeEvent] = "gobiiExtractFilterType"
                                (onAddMessage)="handleHeaderStatusMessage($event)"
                                (onTreeReady)="handleStatusTreeReady($event)">
                            </status-display-tree>
                            
                            <BR>
                            
                            <button type="submit"
                            [class]="submitButtonStyle"
                            (mouseenter)="handleOnMouseOverSubmit($event,true)"
                            (mouseleave)="handleOnMouseOverSubmit($event,false)"
                            (click)="handleExtractSubmission()">Submit</button>
                               
                            <button type="clear"
                            [class]="clearButtonStyle"
                            (click)="handleClearTree()">Clear</button>
                               
                    </div> <!-- panel body -->
                    </div> <!-- panel primary -->

                    </div>  <!-- outer grid column 2-->
                    
                    
                    <div class="col-md-4">

                            
                        <div>
                    <div class="panel panel-primary">
                      <div class="panel-heading">
                        <h3 class="panel-title">Status Messages</h3>
                      </div>
                      <div class="panel-body">
                                <status-display [messages] = "messages"></status-display>
                            <BR>
                            <button type="clear"
                            class="btn btn-primary"
                            (click)="handleClearMessages()">Clear</button>
                    </div> <!-- panel body -->

                    </div> <!-- panel primary -->
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


    // *** You cannot use an Enum directly as a template type parameter, so we need
    //     to assign them to properties
    private nameIdRequestParamsContactsPi: NameIdRequestParams;
    private nameIdRequestParamsExperiments: NameIdRequestParams;
    private nameIdRequestParamsMapsets: NameIdRequestParams;
    private nameIdRequestParamsDatasetType: NameIdRequestParams;
    private nameIdRequestParamsPlatforms: NameIdRequestParams;


    // ************************************************************************

    private treeFileItemEvent: GobiiFileItem;
//    private selectedExportTypeEvent:GobiiExtractFilterType;
    private datasetFileItemEvents: GobiiFileItem[] = [];
    private gobiiDatasetExtracts: GobiiDataSetExtract[] = [];

    private criteriaInvalid: boolean = true;

    private loggedInUser: string = null;


    private messages: string[] = [];


    constructor(private _dtoRequestServiceExtractorFile: DtoRequestService<ExtractorInstructionFilesDTO>,
                private _dtoRequestServiceNameIds: DtoRequestService<NameId[]>,
                private _dtoRequestServiceContact: DtoRequestService<Contact>,
                private _authenticationService: AuthenticationService,
                private _dtoRequestServiceServerConfigs: DtoRequestService<ServerConfig[]>,
                private _fileModelTreeService: FileModelTreeService) {

        this.nameIdRequestParamsContactsPi = NameIdRequestParams
            .build("Contact-PI",
                GobiiExtractFilterType.WHOLE_DATASET,
                EntityType.Contacts)
            .setEntitySubType(EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR);

        this.nameIdRequestParamsExperiments = NameIdRequestParams
            .build("Experiments",
                GobiiExtractFilterType.WHOLE_DATASET,
                EntityType.Experiments)
            .setEntityFilter(EntityFilter.BYTYPEID);

        this.nameIdRequestParamsDatasetType = NameIdRequestParams
            .build("Cv-DataType",
                GobiiExtractFilterType.WHOLE_DATASET,
                EntityType.CvTerms)
            .setCvFilterType(CvFilterType.DATASET_TYPE)
            .setEntityFilter(EntityFilter.BYTYPENAME)
            .setEntityFilterValue(CvFilters.get(CvFilterType.DATASET_TYPE))
            .setMameIdLabelType(NameIdLabelType.SELECT_A);


        this.nameIdRequestParamsMapsets = NameIdRequestParams
            .build("Mapsets",
                GobiiExtractFilterType.WHOLE_DATASET,
                EntityType.Mapsets)
            .setMameIdLabelType(NameIdLabelType.NO);

        this.nameIdRequestParamsPlatforms = NameIdRequestParams
            .build("Platforms",
                GobiiExtractFilterType.WHOLE_DATASET,
                EntityType.Platforms);

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

                    scope$.initializeSubmissionContact();
                    scope$.currentStatus = "GOBII Server " + gobiiVersion;
                    scope$.handleAddMessage("Connected to crop config: " + scope$.selectedServerConfig.crop);

                } else {
                    scope$.serverConfigList = [new ServerConfig("<ERROR NO SERVERS>", "<ERROR>", "<ERROR>", 0)];
                }
            },
            dtoHeaderResponse => {
                dtoHeaderResponse.statusMessages.forEach(m => scope$.handleAddMessage("Retrieving server configs: "
                    + m.message))
            }
        )
        ;
    } // initializeServerConfigs()

    private initializeSubmissionContact() {


        this.loggedInUser = this._authenticationService.getUserName();
        let scope$ = this;
        scope$._dtoRequestServiceContact.get(new DtoRequestItemContact(
            ContactSearchType.BY_USERNAME,
            this.loggedInUser)).subscribe(contact => {

                if (contact && contact.contactId && contact.contactId > 0) {

                    //loggedInUser
                    scope$._fileModelTreeService.put(
                        GobiiFileItem.build(scope$.gobiiExtractFilterType, ProcessType.CREATE)
                            .setEntityType(EntityType.Contacts)
                            .setEntitySubType(EntitySubType.CONTACT_SUBMITED_BY)
                            .setCvFilterType(CvFilterType.UNKNOWN)
                            .setExtractorItemType(ExtractorItemType.ENTITY)
                            .setItemName(contact.email)
                            .setItemId(contact.contactId.toLocaleString())).subscribe(
                        null,
                        headerStatusMessage => {
                            this.handleHeaderStatusMessage(headerStatusMessage)
                        }
                    );

                } else {
                    scope$.handleAddMessage("There is no contact associated with user " + this.loggedInUser);
                }

            },
            dtoHeaderResponse => {
                dtoHeaderResponse.statusMessages.forEach(m => scope$.handleAddMessage("Retrieving contacts for submission: "
                    + m.message))
            });

        //   _dtoRequestServiceContact
    }

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
    private doPrincipleInvestigatorTreeNotifications: boolean = false;
    private displaySelectorProject: boolean = true;
    private displaySelectorExperiment: boolean = true;
    private displaySelectorDataType: boolean = false;
    private displaySelectorPlatform: boolean = false;
    private displayIncludedDatasetsGrid: boolean = true;
    private displaySampleListTypeSelector: boolean = false;
    private displaySampleMarkerBox: boolean = false;
    private reinitProjectList: boolean = false;
    private gobiiExtractFilterType: GobiiExtractFilterType;

    private handleExportTypeSelected(arg: GobiiExtractFilterType) {

        let foo: string = "foo";


        this._fileModelTreeService
            .fileItemNotifications()
            .subscribe(fileItem => {
                if (fileItem.getProcessType() === ProcessType.NOTIFY
                    && fileItem.getExtractorItemType() === ExtractorItemType.STATUS_DISPLAY_TREE_READY) {

                    let jobId: string = FileName.makeUniqueFileId();

                    this._fileModelTreeService
                        .put(GobiiFileItem
                            .build(arg, ProcessType.CREATE)
                            .setExtractorItemType(ExtractorItemType.JOB_ID)
                            .setItemId(jobId)
                            .setItemName(jobId))
                        .subscribe(
                            fmte => {
                                this._fileModelTreeService
                                    .getTreeState(this.gobiiExtractFilterType)
                                    .subscribe(
                                        ts => {
                                            this.handleTreeStatusChanged(ts)
                                        },
                                        hsm => {
                                            this.handleHeaderStatusMessage(hsm)
                                        }
                                    )
                            },
                            headerStatusMessage => {
                                this.handleHeaderStatusMessage(headerStatusMessage)
                            }
                        );
                }
            });

        this.gobiiExtractFilterType = arg;

//        let extractorFilterItemType: GobiiFileItem = GobiiFileItem.bui(this.gobiiExtractFilterType)

        if (this.gobiiExtractFilterType === GobiiExtractFilterType.WHOLE_DATASET) {

            this.doPrincipleInvestigatorTreeNotifications = false;
            this.nameIdRequestParamsContactsPi.setMameIdLabelType(NameIdLabelType.UNKNOWN);
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


        } else if (this.gobiiExtractFilterType === GobiiExtractFilterType.BY_SAMPLE) {

//            this.initializePlatforms();

            this.displaySelectorPi = true;
            this.doPrincipleInvestigatorTreeNotifications = true;
            this.nameIdRequestParamsContactsPi.setMameIdLabelType(NameIdLabelType.ALL);
            this.displaySelectorProject = true;
            this.displaySelectorDataType = true;
            this.displaySelectorPlatform = true;
            this.displaySampleListTypeSelector = true;

            this.displaySelectorExperiment = false;
            this.displayAvailableDatasets = false;
            this.displayIncludedDatasetsGrid = false;
            this.displaySampleMarkerBox = false;

            this.reinitProjectList = true;

        } else if (this.gobiiExtractFilterType === GobiiExtractFilterType.BY_MARKER) {

//            this.initializePlatforms();

            this.displaySelectorDataType = true;
            this.displaySelectorPlatform = true;
            this.displaySampleMarkerBox = true;

            this.displaySelectorPi = false;
            this.doPrincipleInvestigatorTreeNotifications = false;
            this.nameIdRequestParamsContactsPi.setMameIdLabelType(NameIdLabelType.UNKNOWN);
            this.displaySelectorProject = false;
            this.displaySelectorExperiment = false;
            this.displayAvailableDatasets = false;
            this.displayIncludedDatasetsGrid = false;
            this.displaySampleListTypeSelector = false;

            this.reinitProjectList = false;


        }

        this.initializeSubmissionContact();

        //changing modes will have nuked the submit as item in the tree, so we need to re-event (sic.) it:


    }


// ********************************************************************
// ********************************************** PI USER SELECTION
    private selectedContactIdForPi: string;

    private handleContactForPiSelected(arg) {
        this.selectedContactIdForPi = arg.id;
        //console.log("selected contact itemId:" + arg);
    }

// ********************************************************************
// ********************************************** HAPMAP SELECTION
    private selectedExtractFormat: GobiiExtractFormat = GobiiExtractFormat.HAPMAP;

    private handleFormatSelected(arg: GobiiExtractFormat) {

        this.selectedExtractFormat = arg;

        let extractFilterTypeFileItem: GobiiFileItem = GobiiFileItem
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
    private selectedProjectId: string;

    private handleProjectSelected(arg) {
        this.selectedProjectId = arg;
        this.displayExperimentDetail = false;
        this.displayDataSetDetail = false;
        this.nameIdRequestParamsExperiments.setEntityFilterValue(this.selectedProjectId);
    }


// ********************************************************************
// ********************************************** EXPERIMENT ID
    private displayExperimentDetail: boolean = false;

    private experimentNameIdList: NameId[];
    private selectedExperimentId: string;
    private selectedExperimentDetailId: string;

    private handleExperimentSelected(arg: NameId) {
        this.selectedExperimentId = arg.id;
        this.selectedExperimentDetailId = arg.id;
        this.displayExperimentDetail = true;

        //console.log("selected contact itemId:" + arg);
    }


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
    private displayDataSetDetail: boolean = false;
    private dataSetIdToUncheck: number;

    private handleAddMessage(arg) {
        this.messages.unshift(arg);
    }

    private handleClearMessages() {
        this.messages = [];
    }

    private handleHeaderStatusMessage(statusMessage: HeaderStatusMessage) {

        if (!statusMessage.statusLevel || statusMessage.statusLevel != StatusLevel.WARNING) {
            this.handleAddMessage(statusMessage.message);
        } else {
            console.log(statusMessage.message);
        }
    }

    private handleResponseHeader(header: Header) {

        if (header.status !== null && header.status.statusMessages != null) {

            header.status.statusMessages.forEach(statusMessage => {
                this.handleHeaderStatusMessage(statusMessage);
            })
        }
    }

    handleStatusTreeReady(headerStatusMessage: HeaderStatusMessage) {

        //this.handleFormatSelected(GobiiExtractFormat.HAPMAP);

    }


// ********************************************************************
// ********************************************** MARKER/SAMPLE selection
    // ********************************************************************
    // ********************************************** Extract file submission
    private treeStatusNotification: TreeStatusNotification = null;
    private submitButtonStyleDefault = "btn btn-primary";
    private buttonStyleSubmitReady = "btn btn-success";
    private buttonStyleSubmitNotReady = "btn btn-warning";
    private submitButtonStyle = this.buttonStyleSubmitNotReady;
    private clearButtonStyle = this.submitButtonStyleDefault;

    private handleTreeStatusChanged(treeStatusNotification: TreeStatusNotification) {

        if (treeStatusNotification.gobiiExractFilterType === this.gobiiExtractFilterType) {
            this.treeStatusNotification = treeStatusNotification;
            this.setSubmitButtonState();
        } // does the filter type match
    }


    private setSubmitButtonState(): boolean {

        let returnVal: boolean = false;

        if (this.treeStatusNotification.fileModelState == FileModelState.SUBMISSION_READY) {
            this.submitButtonStyle = this.buttonStyleSubmitReady;
            returnVal = true;
        } else {
            this.submitButtonStyle = this.buttonStyleSubmitNotReady;
            returnVal = false;

        }

        return returnVal;
    }

    private handleOnMouseOverSubmit(arg, isEnter) {

        // this.criteriaInvalid = true;

        if (isEnter) {

            this.setSubmitButtonState()

            this.treeStatusNotification.modelTreeValidationErrors.forEach(mtv => {

                let currentMessage: string;

                if (mtv.fileModelNode.getItemType() === ExtractorItemType.ENTITY) {
                    currentMessage = mtv.fileModelNode.getEntityName();

                } else {
                    currentMessage = Labels.instance().treeExtractorTypeLabels[mtv.fileModelNode.getItemType()];
                }

                currentMessage += ": " + mtv.message;

                this.handleAddMessage(currentMessage);

            });
        }


        // else {
        //     this.submitButtonStyle = this.submitButtonStyleDefault;
        // }

        //#eee


        let foo: string = "foo";
    }

    private handleClearTree() {

        this._fileModelTreeService.put(GobiiFileItem
            .build(this.gobiiExtractFilterType, ProcessType.NOTIFY)
            .setExtractorItemType(ExtractorItemType.CLEAR_TREE)).subscribe(
            null,
            headerResponse => {
                this.handleResponseHeader(headerResponse)
            }
        );
    }

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
    private eliminateDuplicateEntities(extractorItemType: ExtractorItemType,
                                       entityType: EntityType,
                                       fileItems: GobiiFileItem[]): GobiiFileItem[] {

        let returnVal: GobiiFileItem[] = [];

        if (fileItems
                .filter(fi => {
                    return fi.getExtractorItemType() === extractorItemType
                    && fi.getEntityType() === entityType
                })
                .length == fileItems.length) {

            fileItems.forEach(ifi => {

                if (returnVal.filter(rfi => {
                        return rfi.getItemId() === ifi.getItemId()
                    }).length === 0) {

                    returnVal.push(ifi);
                } else {
                    let message: string = "A duplicate ";
                    message += ExtractorItemType[extractorItemType];
                    message += " (" + EntityType[entityType] +") ";
                    message += "item was found; ";
                    if (ifi.getItemName()) {
                        message += "name: " + ifi.getItemName() + "; "
                    }

                    if (ifi.getItemId()) {
                        message += "id: " + ifi.getItemId();
                    }

                    this.handleHeaderStatusMessage(new HeaderStatusMessage(message,
                        StatusLevel.WARNING,
                        null));
                }
            });

        } else {

            this.handleHeaderStatusMessage(new HeaderStatusMessage(
                "The elimination array contains mixed entities",
                StatusLevel.WARNING,
                null));

        }


        return returnVal;
    }

    private handleExtractSubmission() {


        if (this.setSubmitButtonState()) {

            let scope$ = this;

            let gobiiExtractorInstructions: GobiiExtractorInstruction[] = [];
            let gobiiDataSetExtracts: GobiiDataSetExtract[] = [];
            let mapsetIds: number[] = [];
            let submitterContactid: number = null;
            let jobId: string = null;
            let markerFileName: string = null;
            let sampleFileName: string = null;
            let sampleListType: GobiiSampleListType;
            scope$._fileModelTreeService.getFileItems(scope$.gobiiExtractFilterType).subscribe(
                fileItems => {

                    // ******** JOB ID
                    let fileItemJobId: GobiiFileItem = fileItems.find(item => {
                        return item.getExtractorItemType() === ExtractorItemType.JOB_ID
                    });

                    if (fileItemJobId != null) {
                        jobId = fileItemJobId.getItemId();
                    }

                    // ******** MARKER FILE
                    let fileItemMarkerFile: GobiiFileItem = fileItems.find(item => {
                        return item.getExtractorItemType() === ExtractorItemType.MARKER_FILE
                    });

                    if (fileItemMarkerFile != null) {
                        markerFileName = fileItemMarkerFile.getItemId();
                    }

                    // ******** SAMPLE FILE
                    let fileItemSampleFile: GobiiFileItem = fileItems.find(item => {
                        return item.getExtractorItemType() === ExtractorItemType.SAMPLE_FILE
                    });

                    if (fileItemSampleFile != null) {
                        sampleFileName = fileItemSampleFile.getItemId();
                    }

                    // ******** SUBMITTER CONTACT
                    let submitterFileItem: GobiiFileItem = fileItems.find(item => {
                        return (item.getEntityType() === EntityType.Contacts)
                            && (item.getEntitySubType() === EntitySubType.CONTACT_SUBMITED_BY)
                    });

                    submitterContactid = Number(submitterFileItem.getItemId());


                    // ******** MAPSET IDs
                    let mapsetFileItems: GobiiFileItem[] = fileItems
                        .filter(item => {
                            return item.getEntityType() === EntityType.Mapsets
                        });
                    mapsetFileItems = this.eliminateDuplicateEntities( ExtractorItemType.ENTITY,
                        EntityType.Mapsets,
                        mapsetFileItems);
                    mapsetIds = mapsetFileItems
                        .map(item => {
                            return Number(item.getItemId())
                        });

                    // ******** EXPORT FORMAT
                    let exportFileItem: GobiiFileItem = fileItems.find(item => {
                        return item.getExtractorItemType() === ExtractorItemType.EXPORT_FORMAT
                    });

                    // these probably should be just one enum
                    let gobiiFileType: GobiiFileType = null;
                    let extractFormat: GobiiExtractFormat = GobiiExtractFormat[exportFileItem.getItemId()];
                    if (extractFormat === GobiiExtractFormat.FLAPJACK) {
                        gobiiFileType = GobiiFileType.FLAPJACK;
                    } else if (extractFormat === GobiiExtractFormat.HAPMAP) {
                        gobiiFileType = GobiiFileType.HAPMAP;
                    } else if (extractFormat === GobiiExtractFormat.META_DATA_ONLY) {
                        gobiiFileType = GobiiFileType.META_DATA;
                    }


                    // ******** DATA SET TYPE
                    let dataTypeFileItem: GobiiFileItem = fileItems.find(item => {
                        return item.getEntityType() === EntityType.CvTerms
                            && item.getCvFilterType() === CvFilterType.DATASET_TYPE
                    });
                    let datasetType: NameId = dataTypeFileItem != null ? new NameId(dataTypeFileItem.getItemId(),
                            dataTypeFileItem.getItemName(), EntityType.CvTerms) : null;


                    // ******** PRINCIPLE INVESTIGATOR CONCEPT
                    let principleInvestigatorFileItem: GobiiFileItem = fileItems.find(item => {
                        return item.getEntityType() === EntityType.Contacts
                            && item.getEntitySubType() === EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR
                    });
                    let principleInvestigator: NameId = principleInvestigatorFileItem != null ? new NameId(principleInvestigatorFileItem.getItemId(),
                            principleInvestigatorFileItem.getItemName(), EntityType.Contacts) : null;


                    // ******** PROJECT
                    let projectFileItem: GobiiFileItem = fileItems.find(item => {
                        return item.getEntityType() === EntityType.Projects
                    });
                    let project: NameId = projectFileItem != null ? new NameId(projectFileItem.getItemId(),
                            projectFileItem.getItemName(), EntityType.Projects) : null;


                    // ******** PLATFORMS
                    let platformFileItems: GobiiFileItem[] = fileItems.filter(item => {
                        return item.getEntityType() === EntityType.Platforms
                    });

                    platformFileItems = this.eliminateDuplicateEntities(ExtractorItemType.ENTITY,
                        EntityType.Platforms,
                        platformFileItems);

                    let platformIds: number[] = platformFileItems.map(item => {
                        return Number(item.getItemId())
                    });

                    // ******** MARKERS
                    let markerListItems: GobiiFileItem[] =
                        fileItems
                            .filter(fi => {
                                return fi.getExtractorItemType() === ExtractorItemType.MARKER_LIST_ITEM
                            });

                    markerListItems = this.eliminateDuplicateEntities( ExtractorItemType.MARKER_LIST_ITEM,
                        EntityType.UNKNOWN,
                        markerListItems);
                    let markerList: string[] = markerListItems
                        .map(mi => {
                            return mi.getItemId()
                        });


                    // ******** SAMPLES
                    let sampleListItems: GobiiFileItem[] =
                        fileItems
                            .filter(fi => {
                                return fi.getExtractorItemType() === ExtractorItemType.SAMPLE_LIST_ITEM
                            });

                    sampleListItems = this.eliminateDuplicateEntities(ExtractorItemType.SAMPLE_LIST_ITEM,
                        EntityType.UNKNOWN,
                        sampleListItems);
                    let sampleList: string[] = sampleListItems
                        .map(mi => {
                            return mi.getItemId()
                        });


                    let sampleListTypeFileItem: GobiiFileItem = fileItems.find(item => {
                        return item.getExtractorItemType() === ExtractorItemType.SAMPLE_LIST_TYPE;
                    });

                    if (sampleListTypeFileItem != null) {
                        sampleListType = GobiiSampleListType[sampleListTypeFileItem.getItemId()];
                    }

                    if (this.gobiiExtractFilterType === GobiiExtractFilterType.WHOLE_DATASET) {

                        let dataSetItems: GobiiFileItem[] = fileItems
                            .filter(item => {
                                return item.getEntityType() === EntityType.DataSets
                            });

                        dataSetItems = this.eliminateDuplicateEntities( ExtractorItemType.ENTITY,
                            EntityType.DataSets,
                            dataSetItems);

                        dataSetItems.forEach(datsetFileItem => {

                            let dataSet: NameId = new NameId(datsetFileItem.getItemId(),
                                datsetFileItem.getItemName(), EntityType.CvTerms);


                            gobiiDataSetExtracts.push(new GobiiDataSetExtract(gobiiFileType,
                                false,
                                null,
                                this.gobiiExtractFilterType,
                                null,
                                null,
                                markerFileName,
                                null,
                                datasetType,
                                platformIds,
                                null,
                                null,
                                dataSet));
                        });
                    } else if (this.gobiiExtractFilterType === GobiiExtractFilterType.BY_MARKER) {
                        gobiiDataSetExtracts.push(new GobiiDataSetExtract(gobiiFileType,
                            false,
                            null,
                            this.gobiiExtractFilterType,
                            markerList,
                            null,
                            markerFileName,
                            null,
                            datasetType,
                            platformIds,
                            null,
                            null,
                            null));
                    } else if (this.gobiiExtractFilterType === GobiiExtractFilterType.BY_SAMPLE) {
                        gobiiDataSetExtracts.push(new GobiiDataSetExtract(gobiiFileType,
                            false,
                            null,
                            this.gobiiExtractFilterType,
                            null,
                            sampleList,
                            sampleFileName,
                            sampleListType,
                            datasetType,
                            platformIds,
                            principleInvestigator,
                            project,
                            null));
                    } else {
                        this.handleAddMessage("Unhandled extract filter type: " + GobiiExtractFilterType[this.gobiiExtractFilterType]);
                    }
                }
            );


            gobiiExtractorInstructions.push(
                new GobiiExtractorInstruction(
                    gobiiDataSetExtracts,
                    submitterContactid,
                    null,
                    mapsetIds)
            );


            let fileName: string = jobId;

            let extractorInstructionFilesDTORequest: ExtractorInstructionFilesDTO =
                new ExtractorInstructionFilesDTO(gobiiExtractorInstructions,
                    fileName);

            let extractorInstructionFilesDTOResponse: ExtractorInstructionFilesDTO = null;

            this._dtoRequestServiceExtractorFile.post(new DtoRequestItemExtractorSubmission(extractorInstructionFilesDTORequest))
                .subscribe(extractorInstructionFilesDTO => {
                        extractorInstructionFilesDTOResponse = extractorInstructionFilesDTO;
                        scope$.handleAddMessage("Extractor instruction file created on server: "
                            + extractorInstructionFilesDTOResponse.getInstructionFileName());

                        let newJobId: string = FileName.makeUniqueFileId();
                        this._fileModelTreeService
                            .put(GobiiFileItem
                                .build(this.gobiiExtractFilterType, ProcessType.CREATE)
                                .setExtractorItemType(ExtractorItemType.JOB_ID)
                                .setItemId(newJobId)
                                .setItemName(newJobId))
                            .subscribe(
                                e => {

                                    this.handleClearTree();
                                },
                                headerStatusMessage => {
                                    this.handleHeaderStatusMessage(headerStatusMessage)
                                }
                            );
                    },
                    headerResponse => {

                        scope$.handleResponseHeader(headerResponse);
                    });

        } // if submission state is READY

    }

    ngOnInit(): any {

        this._fileModelTreeService
            .treeStateNotifications()
            .subscribe(ts => {

                this.handleTreeStatusChanged(ts);
            });

        this.initializeServerConfigs();
        this.handleExportTypeSelected(GobiiExtractFilterType.WHOLE_DATASET);

    } // ngOnInit()


}

