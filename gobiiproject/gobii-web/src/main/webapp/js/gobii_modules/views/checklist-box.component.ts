import {Component, OnInit, OnChanges, SimpleChange, EventEmitter, Input, DoCheck, KeyValueDiffers} from "@angular/core";
import {NameId} from "../model/name-id";
import {DtoRequestService} from "../services/core/dto-request.service";
import {ProcessType} from "../model/type-process";
import {GobiiFileItem} from "../model/gobii-file-item";
import {EntityType, EntitySubType} from "../model/type-entity";
import {GobiiExtractFilterType} from "../model/type-extractor-filter";
import {CvFilterType} from "../model/cv-filter-type";
import {EntityFilter} from "../model/type-entity-filter";
import {NameIdRequestParams} from "../model/name-id-request-params";
import {HeaderStatusMessage} from "../model/dto-header-status-message";
import {FileModelTreeService} from "../services/core/file-model-tree-service";
import {NameIdService} from "../services/core/name-id-service";
import {ExtractorItemType} from "../model/file-model-node";


@Component({
    selector: 'checklist-box',
    inputs: ['gobiiExtractFilterType',
        'nameIdRequestParams',
        'retainHistory'],
    outputs: ['onItemSelected',
        'onItemChecked',
        'onError'],
    template: `<form>
                    <div style="overflow:auto; height: 80px; border: 1px solid #336699; padding-left: 5px">
                        <div *ngFor="let fileItemEvent of fileItemEvents" 
                            (click)=handleItemSelected($event) 
                            (hover)=handleItemHover($event)>
                            <input  type="checkbox" 
                                (click)=handleItemChecked($event)
                                [checked]="fileItemEvent.getChecked()"
                                value={{fileItemEvent.getItemId()}} 
                                name="{{fileItemEvent.getItemName()}}">&nbsp;{{fileItemEvent.getItemName()}}
                        </div>            
                    </div>
                </form>` // end template

})


export class CheckListBoxComponent implements OnInit,OnChanges,DoCheck {

    differ: any;

    constructor(private _fileModelTreeService: FileModelTreeService,
                private _nameIdService: NameIdService,
                private differs: KeyValueDiffers) {

        this.differ = differs.find({}).create(null);
    } // ctor

    private retainHistory: boolean;
    private nameIdRequestParams: NameIdRequestParams;
    private gobiiExtractFilterType: GobiiExtractFilterType = GobiiExtractFilterType.UNKNOWN;
    private onError: EventEmitter<HeaderStatusMessage> = new EventEmitter();


    // useg
    private nameIdList: NameId[];
    private fileItemEvents: GobiiFileItem[] = [];
    private onItemChecked: EventEmitter<GobiiFileItem> = new EventEmitter();
    private onItemSelected: EventEmitter<GobiiFileItem> = new EventEmitter();
    private checkedFileItemHistory: GobiiFileItem[] = [];

    private handleItemChecked(arg) {

        let itemToChange: GobiiFileItem =
            this.fileItemEvents.filter(e => {
                return e.getItemId() === arg.currentTarget.value;
            })[0];

        //let indexOfItemToChange:number = this.fileItemEvents.indexOf(arg.currentTarget.name);
        itemToChange.setProcessType(arg.currentTarget.checked ? ProcessType.CREATE : ProcessType.DELETE);
        itemToChange.setChecked(arg.currentTarget.checked);

        this.updateCheckedItemHistory(itemToChange);

        this.onItemChecked.emit(itemToChange);

        this.updateTreeService(itemToChange);

    } // handleItemChecked()

    private updateCheckedItemHistory(fileItem: GobiiFileItem) {

        let historyFileItem: GobiiFileItem = this
            .checkedFileItemHistory
            .find(fi => {
                return ( fi.getEntityType() === fileItem.getEntityType()
                && fi.getItemId() === fileItem.getItemId()
                && fi.getItemName() === fileItem.getItemName())
            });


        if (fileItem.getChecked() === true) {

            if (historyFileItem == null) {
                this.checkedFileItemHistory.push(fileItem);
            }

        } else {

            if (historyFileItem != null) {
                let idx: number = this.checkedFileItemHistory.indexOf(historyFileItem);
                this.checkedFileItemHistory.splice(idx, 1);
            }
        }
    }

    private wasItemPreviouslyChecked(fileItem: GobiiFileItem): boolean {

        let checkedFileItem: GobiiFileItem = this.checkedFileItemHistory.find(fi => {
            return fi.getEntityType() === fileItem.getEntityType()
                && fi.getItemId() === fileItem.getItemId()
                && fi.getItemName() === fileItem.getItemName()
        });

        return checkedFileItem != null;
    }

    private previousSelectedItem: any;

    private handleItemSelected(arg) {

        if (this.previousSelectedItem) {
            this.previousSelectedItem.style = ""
        }
        arg.currentTarget.style = "background-color:#b3d9ff";
        this.previousSelectedItem = arg.currentTarget;

        let idValue: string = arg.currentTarget.children[0].value;
        let selectedFileItem: GobiiFileItem =
            this.fileItemEvents.filter(e => {
                return e.getItemId() === idValue;
            })[0];


        // let fileItemEvent: GobiiFileItem = GobiiFileItem.build(
        //     GobiiExtractFilterType.UNKNOWN,
        //     ProcessType.READ)
        //     .setEntityType(this.nameIdRequestParams.getEntityType())
        //     .setCvFilterType(CvFilterType.UNKNOWN)
        //     .setItemId(arg.currentTarget.children[0].value)
        //     .setItemName(arg.currentTarget.children[0].name)
        //     .setChecked(false)
        //     .setRequired(false);

        if (selectedFileItem) {
            this.onItemSelected.emit(selectedFileItem);
        }

    }

    private initializeNameIds() {
        let scope$ = this;
        this._nameIdService.get(this.nameIdRequestParams)
            .subscribe(nameIds => {
                    if (nameIds && ( nameIds.length > 0 )) {
                        scope$.nameIdList = nameIds;
                        scope$.setList(scope$.nameIdList);
                    }
                },
                responseHeader => {
                    this.handleHeaderStatus(responseHeader);
                });
    }


    private updateTreeService(fileItem: GobiiFileItem) {

        this._fileModelTreeService.put(fileItem)
            .subscribe(
                null,
                headerResponse => {
                    this.handleHeaderStatus(headerResponse)
                });
    }

    private handleHeaderStatus(headerStatusMessage: HeaderStatusMessage) {

        this.onError.emit(headerStatusMessage);
    }


    public setList(nameIdList: NameId[]): void {

        // we can get this event whenver the item is clicked, not necessarily when the checkbox
        let scope$ = this;

        scope$.nameIdList = nameIdList;

        if (scope$.nameIdList && ( scope$.nameIdList.length > 0 )) {

            scope$.fileItemEvents = [];
            if (!scope$.retainHistory) {
                scope$.checkedFileItemHistory = [];
            }
            scope$.nameIdList.forEach(n => {
                let currentFileItem: GobiiFileItem =
                    GobiiFileItem.build(
                        this.gobiiExtractFilterType,
                        ProcessType.CREATE)
                        .setExtractorItemType(ExtractorItemType.ENTITY)
                        .setEntityType(this.nameIdRequestParams.getEntityType())
                        .setCvFilterType(CvFilterType.UNKNOWN)
                        .setItemId(n.id)
                        .setItemName(n.name)
                        .setChecked(false)
                        .setRequired(false);


                if (scope$.wasItemPreviouslyChecked(currentFileItem)) {
                    currentFileItem.setChecked(true);
                }

                scope$.fileItemEvents.push(currentFileItem);
            });

        } else {
            scope$.fileItemEvents = [];
            // scope$.nameIdList = [new NameId("0", "<none>", this.entityType)];
        }
    } // setList()


    ngOnInit(): any {

        this._fileModelTreeService
            .fileItemNotifications()
            .subscribe(eventedFileItem => {

                    if (eventedFileItem) {
                        let itemToChange: GobiiFileItem =
                            this.fileItemEvents.find(e => {
                                return e.getEntityType() == eventedFileItem.getEntityType()
                                    && e.getItemName() == eventedFileItem.getItemName()
                            });

                        //let indexOfItemToChange:number = this.fileItemEvents.indexOf(arg.currentTarget.name);
                        if (itemToChange) {
                            itemToChange.setProcessType(eventedFileItem.getProcessType());
                            itemToChange.setChecked(eventedFileItem.getChecked());
                            this.updateCheckedItemHistory(itemToChange);
                        }
                    }
                },
                responseHeader => {
                    this.handleHeaderStatus(responseHeader);
                });

        if (this._nameIdService.validateRequest(this.nameIdRequestParams)) {
            this.initializeNameIds();
        }
    }

    private resetList() {
        if (this._nameIdService.validateRequest(this.nameIdRequestParams)) {
            this.initializeNameIds();
        } else {
            this.setList([]);
        }
    }

    ngOnChanges(changes: {[propName: string]: SimpleChange}) {


        if (changes['gobiiExtractFilterType']
            && ( changes['gobiiExtractFilterType'].currentValue != null )
            && ( changes['gobiiExtractFilterType'].currentValue != undefined )) {

            if (changes['gobiiExtractFilterType'].currentValue != changes['gobiiExtractFilterType'].previousValue) {


                this.nameIdRequestParams.setGobiiExtractFilterType(this.gobiiExtractFilterType);
                this.resetList();

                this._fileModelTreeService
                    .fileItemNotifications()
                    .subscribe(fileItem => {
                        if (fileItem.getProcessType() === ProcessType.NOTIFY
                            && fileItem.getExtractorItemType() === ExtractorItemType.STATUS_DISPLAY_TREE_READY) {

                            this.resetList();

                        }
                    });

            } // if we have a new filter type

        } // if filter type changed

    }

    ngDoCheck(): void {

        var changes = this.differ.diff(this.nameIdRequestParams);

        if (changes) {

            this.resetList();
        }
    }

}
