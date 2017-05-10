import {Component, OnInit, EventEmitter, OnChanges, SimpleChange, DoCheck, KeyValueDiffers} from "@angular/core";
import {NameId} from "../model/name-id";
import {DtoRequestService} from "../services/core/dto-request.service";
import {EntityType, EntitySubType} from "../model/type-entity";
import {CvFilterType, CvFilters} from "../model/cv-filter-type";
import {DtoRequestItemNameIds} from "../services/app/dto-request-item-nameids";
import {EntityFilter} from "../model/type-entity-filter";
import {GobiiFileItem} from "../model/gobii-file-item";
import {ProcessType} from "../model/type-process";
import {FileModelTreeService} from "../services/core/file-model-tree-service";
import {GobiiExtractFilterType} from "../model/type-extractor-filter";
import {Header} from "../model/payload/header";
import {ExtractorItemType} from "../model/file-model-node";
import {Guid} from "../model/guid";
import {NameIdService} from "../services/core/name-id-service";
import {NameIdRequestParams} from "../model/name-id-request-params";
import {HeaderStatusMessage} from "../model/dto-header-status-message";
import {Labels} from "./entity-labels";
import {GobiiUIEventOrigin} from "../model/type-event-origin";
import {NameIdLabelType} from "../model/name-id-label-type";


@Component({
    selector: 'name-id-list-box',
    inputs: ['gobiiExtractFilterType',
        'notifyOnInit',
        'nameIdRequestParams',
        'doTreeNotifications'],
    outputs: ['onNameIdSelected', 'onError'],
    template: `<select [(ngModel)]="selectedFileItemId" (change)="handleFileItemSelected($event)" >
			        <option *ngFor="let fileItem of fileItemList" 
				        [value]="fileItem.getItemId()">{{fileItem.getItemName()}}</option>
		        </select>
` // end template

})

export class NameIdListBoxComponent implements OnInit, OnChanges, DoCheck {

    //private uniqueId:string;

    differ: any;

    constructor(private _nameIdService: NameIdService,
                private _fileModelTreeService: FileModelTreeService,
                private differs: KeyValueDiffers) {

        this.differ = differs.find({}).create(null);


    } // ctor

    // private notificationSent = false;

    ngOnInit(): any {

        let scope$ = this;
        this._fileModelTreeService
            .fileItemNotifications()
            .subscribe(eventedFileItem => {
                    if (this.doTreeNotifications) {
                        this.updateSelectedItem(eventedFileItem);
                    }
                },
                responseHeader => {
                    this.handleHeaderStatus(responseHeader);
                });
    }

    private updateSelectedItem(eventedFileItem: GobiiFileItem) {

        let fileItems: GobiiFileItem[] = this.fileItemList;
        let foo: string = "foo";

        // we need to make sure that the evented item belongs to this control
        // however, the incoming event may have other properties that changed, so we
        // have to use the evented item
        if (this.fileItemList
                .find(fi => {
                    return fi.getFileItemUniqueId()
                        === eventedFileItem.getFileItemUniqueId()
                })) {

            if ((eventedFileItem.getGobiiEventOrigin() === GobiiUIEventOrigin.CRITERIA_TREE)) {
                if (this.nameIdRequestParams.getMameIdLabelType() != NameIdLabelType.UNKNOWN &&
                    (eventedFileItem.getProcessType() === ProcessType.DELETE)) {
                    this.selectedFileItemId = "0";
                } else {
                    this.selectedFileItemId = eventedFileItem.getItemId();
                }
                this.currentSelection = this.fileItemList[this.selectedFileItemId];
            }
        }
    }

    private makeFileItemFromNameId(nameId: NameId, extractorItemType: ExtractorItemType): GobiiFileItem {

        return GobiiFileItem.build(this.gobiiExtractFilterType, ProcessType.CREATE)
            .setEntityType(this.nameIdRequestParams.getEntityType())
            .setEntitySubType(this.nameIdRequestParams.getEntitySubType())
            .setCvFilterType(this.nameIdRequestParams.getCvFilterType())
            .setExtractorItemType(extractorItemType)
            .setItemName(nameId.name)
            .setItemId(nameId.id);
    }


    private initializeFileItems() {

        let scope$ = this;
        this._nameIdService.get(this.nameIdRequestParams)
            .subscribe(nameIds => {

                    this.fileItemList = [];
                    if (nameIds && ( nameIds.length > 0 )) {

                        nameIds.forEach(ni => {

                            scope$.fileItemList.push(
                                this.makeFileItemFromNameId(ni, ExtractorItemType.ENTITY)
                            );

                        });


                        if (this.nameIdRequestParams.getMameIdLabelType() != NameIdLabelType.UNKNOWN) {

                            let entityName:string = "";
                            if (scope$.nameIdRequestParams.getCvFilterType() !== CvFilterType.UNKNOWN) {
                                entityName += Labels.instance().cvFilterNodeLabels[scope$.nameIdRequestParams.getCvFilterType()];
                            } else if (scope$.nameIdRequestParams.getEntitySubType() !== EntitySubType.UNKNOWN) {
                                entityName += Labels.instance().entitySubtypeNodeLabels[scope$.nameIdRequestParams.getEntitySubType()];
                            } else {
                                entityName += Labels.instance().entityNodeLabels[scope$.nameIdRequestParams.getEntityType()];
                            }

                            let label: string = "";
                            switch( this.nameIdRequestParams.getMameIdLabelType() ) {

                                case NameIdLabelType.SELECT_A:
                                    label = "Select a " +  entityName;
                                    break;

                                // we require that these entity labels all be in the singular
                                case NameIdLabelType.ALL:
                                    label = "All " + entityName + "s";
                                    break;

                                case NameIdLabelType.NO:
                                    label = "No " +  entityName;
                                    break;

                                default:
                                    this.handleHeaderStatus(new HeaderStatusMessage("Unknown label type "
                                        + NameIdLabelType[this.nameIdRequestParams.getMameIdLabelType()],null,null))
                            }



                            let labelFileItem: GobiiFileItem = this.makeFileItemFromNameId(
                                new NameId("0", label, this.nameIdRequestParams.getEntityType()),
                                ExtractorItemType.LABEL);
                            scope$.fileItemList.unshift(labelFileItem);
                            scope$.selectedFileItemId = "0";


                        } else {
                            scope$.selectedFileItemId = scope$.fileItemList[0].getItemId();
                            //scope$.selectedFileItemId = "0";

                        }

                        scope$.currentSelection = scope$.fileItemList[0];

                        if (this.notifyOnInit
                            && ( this.nameIdRequestParams.getMameIdLabelType() === NameIdLabelType.UNKNOWN )
//                            && !this.notificationSent
//                            && scope$.fileItemList [0].getItemName() != "<none>"
                        ) {
                            this.updateTreeService(scope$.fileItemList[0]);
//                            this.notificationSent = true;
                        }
                    }
                },
                responseHeader => {
                    this.handleHeaderStatus(responseHeader);
                });

    }

    // useg
    private fileItemList: GobiiFileItem[] = [];

    private notifyOnInit: boolean = false;
    private doTreeNotifications: boolean = true;
    // DtoRequestItemNameIds expects the value to be null if it's not set (not "UNKNOWN")

    private nameIdRequestParams: NameIdRequestParams;

    private gobiiExtractFilterType: GobiiExtractFilterType = GobiiExtractFilterType.UNKNOWN;

    private selectedFileItemId: string = null;
    private onNameIdSelected: EventEmitter<NameId> = new EventEmitter();
    private onError: EventEmitter<HeaderStatusMessage> = new EventEmitter();


    private handleHeaderStatus(headerStatusMessage: HeaderStatusMessage) {

        this.onError.emit(headerStatusMessage);
    }


    private updateTreeService(eventedfileItem: GobiiFileItem) {

        this.onNameIdSelected
            .emit(new NameId(eventedfileItem.getItemId(),
                eventedfileItem.getItemName(),
                eventedfileItem.getEntityType()));

        if( eventedfileItem.getItemId() != "0") {
            if (this.doTreeNotifications) {
                this._fileModelTreeService.put(eventedfileItem)
                    .subscribe(
                        null,
                        headerResponse => {
                            this.handleHeaderStatus(headerResponse)
                        });
            }
        }

    }


    private currentSelection: GobiiFileItem = null;

    private handleFileItemSelected(arg) {

        let foo: string = "foo";


        if (this.currentSelection.getItemId() !== "0") {
            this.currentSelection.setProcessType(ProcessType.DELETE);
            this.updateTreeService(this.currentSelection);
        }


//        let gobiiFileItem: GobiiFileItem = this.fileItemList[arg.srcElement.selectedIndex]
        let gobiiFileItem: GobiiFileItem = this.fileItemList.find(fi => {
            return fi.getItemId() === this.selectedFileItemId
        });

        if (gobiiFileItem.getItemId() != "0") {
            gobiiFileItem.setProcessType(ProcessType.UPDATE);
            this.updateTreeService(gobiiFileItem);
        }

        this.currentSelection = gobiiFileItem;

    }


    ngOnChanges(changes: {[propName: string]: SimpleChange}) {

        if (changes['gobiiExtractFilterType']
            && ( changes['gobiiExtractFilterType'].currentValue != null )
            && ( changes['gobiiExtractFilterType'].currentValue != undefined )) {

            if (changes['gobiiExtractFilterType'].currentValue != changes['gobiiExtractFilterType'].previousValue) {

                //this.notificationSent = false;

                this.nameIdRequestParams.setGobiiExtractFilterType(this.gobiiExtractFilterType);

                let scope$ = this;
                this._fileModelTreeService
                    .fileItemNotifications()
                    .subscribe(fileItem => {
                        if (fileItem.getProcessType() === ProcessType.NOTIFY
                            && fileItem.getExtractorItemType() === ExtractorItemType.STATUS_DISPLAY_TREE_READY) {

                            scope$.initializeFileItems();


                        }
                    });

            } // if we have a new filter type

        } // if filter type changed


        if (changes['nameIdRequestParams']
            && ( changes['nameIdRequestParams'].currentValue != null )
            && ( changes['nameIdRequestParams'].currentValue != undefined )) {

        }

    } // ngonChanges


    // angular change detection does not do deep comparison of objects that are
    // template properties. So we need to do some specialized change detection
    // references:
    //   https://juristr.com/blog/2016/04/angular2-change-detection/
    //   https://angular.io/docs/ts/latest/api/core/index/DoCheck-class.html
    //   http://blog.angular-university.io/how-does-angular-2-change-detection-really-work/
    //   https://blog.thoughtram.io/angular/2016/02/22/angular-2-change-detection-explained.html#what-causes-change
    ngDoCheck(): void {

        var changes = this.differ.diff(this.nameIdRequestParams);

        if (changes) {
            if (this._nameIdService.validateRequest(this.nameIdRequestParams)) {
                this.initializeFileItems();
            }
        }
    }
} // class
