///<reference path="../../../../../../typings/index.d.ts"/>

import {Component} from "@angular/core";
import {HTTP_PROVIDERS} from "@angular/http";
import {ExportFormatComponent} from "../views/export-format.component";
import {DtoRequestService} from "../services/core/dto-request.service";
import {AuthenticationService} from "../services/core/authentication.service";
import {ContactsListBoxComponent} from "../views/contacts-list-box.component";
import {ProjectListBoxComponent} from "../views/project-list-box.component";
import {ExperimentListBoxComponent} from "../views/experiment-list-box.component";
import {DataSetCheckListBoxComponent} from "../views/dataset-checklist-box.component";
import {GobiiDataSetExtract} from "../model/extractor-instructions/data-set-extract";
import {CriteriaDisplayComponent} from "../views/criteria-display.component";
import {StatusDisplayComponent} from "../views/status-display-box.component";
import {ProcessType} from "../model/type-process";
import {CheckBoxEvent} from "../model/event-checkbox";
import {ServerConfig} from "../model/server-config";
import {EntityType} from "../model/type-entity";
import {CropsListBoxComponent} from "../views/crops-list-box.component";
import {UsersListBoxComponent} from "../views/users-list-box.component";
import {NameId} from "../model/name-id";
import {GobiiFileType} from "../model/type-gobii-file";
import {ExtractorInstructionFilesDTO} from "../model/extractor-instructions/dto-extractor-instruction-files";
import {GobiiExtractorInstruction} from "../model/extractor-instructions/gobii-extractor-instruction";
import {DtoRequestItemExtractorSubmission} from "../services/app/dto-request-item-extractor-submission";
import {DtoRequestItemNameIds} from "../services/app/dto-request-item-nameids";
import {DtoRequestItemServerConfigs} from "../services/app/dto-request-item-serverconfigs";
import {GobiiCropType} from "../model/type-crop";
import * as EntityFilters from "../model/type-entity-filter";

// import { RouteConfig, ROUTER_DIRECTIVES, ROUTER_PROVIDERS } from 'angular2/router';

// GOBii Imports


@Component({
    selector: 'extractor-root',
    directives: [ExportFormatComponent,
        ContactsListBoxComponent,
        ProjectListBoxComponent,
        ExperimentListBoxComponent,
        DataSetCheckListBoxComponent,
        CriteriaDisplayComponent,
        StatusDisplayComponent,
        CropsListBoxComponent,
        UsersListBoxComponent],
    styleUrls: ['/extractor-ui.css'],
    providers: [
        HTTP_PROVIDERS,
        AuthenticationService,
        DtoRequestService
    ],
    template: `
        <div class = "panel panel-default">
        
           <div class = "panel-heading">
              <h1 class = "panel-title">GOBii Extractor</h1>
           </div>
           
            <div class="container-fluid">
            
                <div class="row">
                
                    <div class="col-md-4">
                        <fieldset class="well the-fieldset">
                        <legend class="the-legend">Crop</legend>
                        <crops-list-box
                            [serverConfigList]="serverConfigList"
                            [selectedServerConfig]="selectedServerConfig"
                            (onServerSelected)="handleServerSelected($event)"></crops-list-box>
                        </fieldset>
                        
                        <fieldset class="well the-fieldset">
                        <legend class="the-legend">Submit As</legend>
                        <users-list-box
                            [nameIdList]="contactNameIdListForSubmitter"
                            (onUserSelected)="handleContactForSubmissionSelected($event)">
                        </users-list-box>
                        </fieldset>
                        
                        <div class="col-md-12">
                            <export-format (onFormatSelected)="handleFormatSelected($event)"></export-format>
                        </div>
                       
                    </div>  <!-- outer grid column 1-->
                
                
                
                    <div class="col-md-4"> 
                        <fieldset class="well the-fieldset">
                        <legend class="the-legend">Principle Investigator</legend>
                        <contacts-list-box [nameIdList]="contactNameIdListForPi" (onContactSelected)="handleContactForPiSelected($event)"></contacts-list-box>
                        </fieldset>
                        
                        <fieldset class="well the-fieldset">
                        <legend class="the-legend">Project</legend>
                        <project-list-box [primaryInvestigatorId] = "selectedContactIdForPi"
                            [nameIdList]="projectNameIdList"
                            [nameIdListPIs]="contactNameIdListForPi"
                            (onProjectSelected)="handleProjectSelected($event)"
                            (onAddMessage)="handleAddMessage($event)"></project-list-box>
                        </fieldset>
                        
                        <fieldset class="well the-fieldset">
                        <legend class="the-legend">Experiment</legend>
                        <experiment-list-box [projectId] = "selectedProjectId"
                            [nameIdList] = "experimentNameIdList"
                            (onExperimentSelected)="handleExperimentSelected($event)"
                            (onAddMessage)="handleAddMessage($event)"></experiment-list-box>
                        </fieldset>
                        
                        <fieldset class="well the-fieldset">
                        <legend class="the-legend">Data Sets</legend>
                        <dataset-checklist-box
                            [checkBoxEventChange] = "checkBoxEventChange"
                            [experimentId] = "selectedExperimentId" 
                            (onItemChecked)="handleCheckedDataSetItem($event)"
                            (onAddMessage) = "handleAddMessage($event)">
                        </dataset-checklist-box>
                        </fieldset>
                        
                    </div>  <!-- outer grid column 2-->
                    <div class="col-md-4">
                         
                            <fieldset class="well the-fieldset" style="vertical-align: bottom;">
                                <legend class="the-legend">Extract</legend>
                                <criteria-display 
                                    [dataSetCheckBoxEvents] = "dataSetCheckBoxEvents"
                                    (onItemUnChecked) = "handleExtractDataSetUnchecked($event)"></criteria-display>
                            </fieldset>
                            
                            <form>
                                <input type="button" 
                                value="Submit"
                                 [disabled]="(gobiiDatasetExtracts.length === 0)"
                                (click)="handleExtractSubmission()" >
                            </form>
                            
                            <fieldset class="well the-fieldset" style="vertical-align: bottom;">
                                <legend class="the-legend">Status</legend>
                                <status-display [messages] = "messages"></status-display>
                            </fieldset>
                                   
                    </div>  <!-- outer grid column 3 (inner grid)-->
                                        
                </div> <!-- .row of outer grid -->
                
                    <div class="row"><!-- begin .row 2 of outer grid-->
                        <div class="col-md-3"><!-- begin column 1 of outer grid -->
                         
                         </div><!-- end column 1 of outer grid -->
                    
                    </div><!-- end .row 2 of outer grid-->
                
            </div>` // end template
}) // @Component

export class ExtractorRoot {
    title = 'Gobii Web';


    private dataSetCheckBoxEvents:CheckBoxEvent[] = [];
    private gobiiDatasetExtracts:GobiiDataSetExtract[] = [];
    private messages:string[] = [];


    constructor(private _dtoRequestServiceExtractorFile:DtoRequestService<ExtractorInstructionFilesDTO>,
                private _dtoRequestServiceNameIds:DtoRequestService<NameId[]>,
                private _dtoRequestServiceServerConfigs:DtoRequestService<ServerConfig[]>) {

    }

    // ****************************************************************
    // ********************************************** SERVER SELECTION
    private selectedServerConfig:ServerConfig;
    private serverConfigList:ServerConfig[];

    private initializeServerConfigs() {
        let scope$ = this;
        this._dtoRequestServiceServerConfigs.getResult(new DtoRequestItemServerConfigs()).subscribe(serverConfigs => {

                if (serverConfigs && ( serverConfigs.length > 0 )) {
                    scope$.serverConfigList = serverConfigs;

                    let serverCrop:GobiiCropType =
                        this._dtoRequestServiceServerConfigs.getGobiiCropType();

                    scope$.selectedServerConfig =
                        scope$.serverConfigList
                            .filter(c => {
                                    return c.crop === GobiiCropType[serverCrop];
                                }
                            )[0];

                    scope$.messages.push("Connected to database: " + scope$.selectedServerConfig.crop);
                    scope$.initializeContactsForSumission();
                    scope$.initializeContactsForPi();

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
        //     .setGobiiCropType(GobiiCropType[this.selectedServerConfig.crop]);
        let currentPath = window.location.pathname;
        let currentPage:string = currentPath.substr(currentPath.lastIndexOf('/') + 1, currentPath.length);
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
// ********************************************** SUBMISSION-USER SELECTION
    private contactNameIdListForSubmitter:NameId[];
    private selectedContactIdForSubmitter:string;

    private handleContactForSubmissionSelected(arg) {
        this.selectedContactIdForSubmitter = arg;
    }

    private initializeContactsForSumission() {
        let scope$ = this;
        this._dtoRequestServiceNameIds.getResult(new DtoRequestItemNameIds(ProcessType.READ,
            EntityType.AllContacts)).subscribe(nameIds => {
                if (nameIds && ( nameIds.length > 0 )) {
                    scope$.contactNameIdListForSubmitter = nameIds
                    scope$.selectedContactIdForSubmitter = nameIds[0].id;
                } else {
                    scope$.contactNameIdListForSubmitter = [new NameId(0, "ERROR NO USERS")];
                }
            },
            dtoHeaderResponse => {
                dtoHeaderResponse.statusMessages.forEach(m => scope$.messages.push("Rettrieving contacts: "
                    + m.message))
            });

    }


// ********************************************************************
// ********************************************** PI USER SELECTION
    private contactNameIdListForPi:NameId[];
    private selectedContactIdForPi:string;

    private handleContactForPiSelected(arg) {
        this.selectedContactIdForPi = arg;
        this.initializeProjectNameIds();
        //console.log("selected contact id:" + arg);
    }

    private initializeContactsForPi() {
        let scope$ = this;
        scope$._dtoRequestServiceNameIds.getResult(new DtoRequestItemNameIds(ProcessType.READ,
            EntityType.Contact,
            EntityFilters.ENTITY_FILTER_CONTACT_PRINICPLE_INVESTIGATOR)).subscribe(nameIds => {

                if (nameIds && ( nameIds.length > 0 )) {
                    scope$.contactNameIdListForPi = nameIds;
                    scope$.selectedContactIdForPi = scope$.contactNameIdListForPi[0].id;
                } else {
                    scope$.contactNameIdListForPi = [new NameId(0, "ERROR NO USERS")];
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
    private selectedFormatName:string = "Hapmap";

    private handleFormatSelected(arg) {
        this.selectedFormatName = arg;
        //console.log("selected contact id:" + arg);
    }

// ********************************************************************
// ********************************************** PROJECT ID
    private projectNameIdList:NameId[];
    private selectedProjectId:string;

    private handleProjectSelected(arg) {
        this.selectedProjectId = arg;
        this.displayExperimentDetail = false;
        this.displayDataSetDetail = false;
        this.initializeExperimentNameIds();
    }

    private initializeProjectNameIds() {
        let scope$ = this;
        scope$._dtoRequestServiceNameIds.getResult(new DtoRequestItemNameIds(ProcessType.READ,
            EntityType.Project,
            this.selectedContactIdForPi)).subscribe(nameIds => {

                if (nameIds && ( nameIds.length > 0 )) {
                    scope$.projectNameIdList = nameIds;
                    scope$.selectedProjectId = nameIds[0].id;
                } else {
                    scope$.projectNameIdList = [new NameId(0, "<none>")];
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
    private displayExperimentDetail:boolean = false;

    private experimentNameIdList:NameId[];
    private selectedExperimentId:string;
    private selectedExperimentDetailId:string;

    private handleExperimentSelected(arg) {
        this.selectedExperimentId = arg;
        this.selectedExperimentDetailId = arg;
        this.displayExperimentDetail = true;

        //console.log("selected contact id:" + arg);
    }

    private initializeExperimentNameIds() {

        let scope$ = this;
        if (this.selectedProjectId) {
            this._dtoRequestServiceNameIds.getResult(new DtoRequestItemNameIds(ProcessType.READ,
                EntityType.Experiment,
                this.selectedProjectId)).subscribe(nameIds => {
                    if (nameIds && ( nameIds.length > 0 )) {
                        scope$.experimentNameIdList = nameIds;
                        scope$.selectedExperimentId = scope$.experimentNameIdList[0].id;
                    } else {
                        scope$.experimentNameIdList = [new NameId(0, "<none>")];
                        scope$.selectedExperimentId = undefined;
                    }
                },
                dtoHeaderResponse => {
                    dtoHeaderResponse.statusMessages.forEach(m => scope$.messages.push("Retreving experiment names: "
                        + m.message))
                });
        } else {
            scope$.experimentNameIdList = [new NameId(0, "<none>")];
            scope$.selectedExperimentId = undefined;
        }

    }


// ********************************************************************
// ********************************************** DATASET ID
    private displayDataSetDetail:boolean = false;
    private dataSetIdToUncheck:number;

    private handleAddMessage(arg) {
        this.messages.push(arg);
    }


    private handleCheckedDataSetItem(arg:CheckBoxEvent) {


        if (ProcessType.CREATE == arg.processType) {
            this.dataSetCheckBoxEvents.push(arg);
            this.gobiiDatasetExtracts.push(new GobiiDataSetExtract(GobiiFileType.GENERIC,
                false,
                Number(arg.id),
                arg.name,
                null));

        } else {

            let indexOfEventToRemove:number = this.dataSetCheckBoxEvents.indexOf(arg);
            this.dataSetCheckBoxEvents.splice(indexOfEventToRemove, 1);

            this.gobiiDatasetExtracts =
                this.gobiiDatasetExtracts
                    .filter((item:GobiiDataSetExtract) => {
                        return item.getDataSetId() != Number(arg.id)
                    });
        } // if-else we're adding
    }

    private checkBoxEventChange:CheckBoxEvent;
    private changeTrigger:number = 0;

    private handleExtractDataSetUnchecked(arg:CheckBoxEvent) {
        // this.changeTrigger++;
        // this.dataSetIdToUncheck = Number(arg.id);

        let dataSetExtractsToRemove:GobiiDataSetExtract[] = this.gobiiDatasetExtracts
            .filter(e => {
                return e.getDataSetId() === Number(arg.id)
            });

        if (dataSetExtractsToRemove.length > 0) {
            let idxToRemove = this.gobiiDatasetExtracts.indexOf(dataSetExtractsToRemove[0]);

            this.gobiiDatasetExtracts.splice(idxToRemove, 1);
        }

        this.checkBoxEventChange = arg;
    }

    private handleExtractSubmission() {

        let gobiiExtractorInstructions:GobiiExtractorInstruction[] = [];

        let gobiiFileType:GobiiFileType = GobiiFileType[this.selectedFormatName.toUpperCase()];
        this.gobiiDatasetExtracts.forEach(e => e.setGobiiFileType(gobiiFileType));


        gobiiExtractorInstructions.push(
            new GobiiExtractorInstruction(
                this.gobiiDatasetExtracts,
                Number(this.selectedContactIdForSubmitter),
                null)
        );


        let date:Date = new Date();
        let fileName:string = "extractor_"
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
        let extractorInstructionFilesDTORequest:ExtractorInstructionFilesDTO =
            new ExtractorInstructionFilesDTO(gobiiExtractorInstructions,
                fileName,
                ProcessType.CREATE,
                GobiiCropType[this.selectedServerConfig.crop]);


        let extractorInstructionFilesDTOResponse:ExtractorInstructionFilesDTO = null;
        let scope$ = this;
        this._dtoRequestServiceExtractorFile.getResult(new DtoRequestItemExtractorSubmission(extractorInstructionFilesDTORequest))
            .subscribe(extractorInstructionFilesDTO => {
                    extractorInstructionFilesDTOResponse = extractorInstructionFilesDTO;
                    scope$.messages.push("Extractor instruction file created on server: "
                        + extractorInstructionFilesDTOResponse.getInstructionFileName());
                },
                dtoHeaderResponse => {
                    dtoHeaderResponse.statusMessages.forEach(m => scope$.messages.push("Submitting extractor instructions: "
                        + m.message))
                });

    }

    ngOnInit():any {

        this.initializeServerConfigs();

    }


}

