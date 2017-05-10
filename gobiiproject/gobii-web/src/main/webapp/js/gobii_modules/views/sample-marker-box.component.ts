import {Component, OnInit, SimpleChange, EventEmitter} from "@angular/core";
import {SampleMarkerList} from "../model/sample-marker-list";
import {HeaderStatusMessage} from "../model/dto-header-status-message";
import {GobiiExtractFilterType} from "../model/type-extractor-filter";
import {FileModelTreeService} from "../services/core/file-model-tree-service";
import {GobiiFileItem} from "../model/gobii-file-item";
import {ProcessType} from "../model/type-process";
import {ExtractorItemType} from "../model/file-model-node";
import {Labels} from "./entity-labels";

@Component({
    selector: 'sample-marker-box',
    inputs: ['gobiiExtractFilterType'],
    outputs: ['onSampleMarkerError'],
    template: `<div class="container-fluid">
            
                <div class="row">

                            <input type="radio" 
                                (click)="handleOnClickBrowse($event)" 
                                name="listType" 
                                value="itemFile"
                                [(ngModel)]="selectedListType">
                          <label class="the-legend">File:&nbsp;</label>
                            <input type="radio" 
                                (click)="handleTextBoxChanged($event)" 
                                name="listType" 
                                value="itemArray"
                                [(ngModel)]="selectedListType">
                          <label class="the-legend">List:&nbsp;</label>
                 </div>
                 
                <div class="row">
                
                    <div *ngIf="displayUploader" class="col-md-8">
                        <uploader
                        [gobiiExtractFilterType] = "gobiiExtractFilterType"
                        (onUploaderError)="handleStatusHeaderMessage($event)"></uploader>
                    </div> 
                    
                    <div *ngIf="displayListBox" class="col-md-8">
                        <text-area
                        (onTextboxDataComplete)="handleTextBoxDataSubmitted($event)"></text-area>
                    </div> 
                    <div *ngIf="displayListBox" class="col-md-4">
                          <p class="text-warning">{{maxListItems}} maximum</p>
                    </div> 
                    
                 </div>
                
                 <div>
                    <p-dialog header="{{extractTypeLabelExisting}} Already Selelected" [(visible)]="displayChoicePrompt" modal="modal" width="300" height="300" responsive="true">
                        <p>A {{extractTypeLabelExisting}} is already selected. Do you want to remove it and specify a {{extractTypeLabelProposed}} instead?</p>
                            <p-footer>
                                <div class="ui-dialog-buttonpane ui-widget-content ui-helper-clearfix">
                                    <button type="button" pButton icon="fa-close" (click)="handleUserChoice(false)" label="No"></button>
                                    <button type="button" pButton icon="fa-check" (click)="handleUserChoice(true)" label="Yes"></button>
                                </div>
                            </p-footer>
                    </p-dialog>
                  </div>
                  <div>
                    <p-dialog header="Maximum {{maxExceededTypeLabel}} Items Exceeded" [(visible)]="displayMaxItemsExceeded" modal="modal" width="300" height="300" responsive="true">
                        <p>You attempted to paste more than {{maxListItems}} {{maxExceededTypeLabel}} items; Please reduce the size of the list</p>
                    </p-dialog>
                  </div>`

})

export class SampleMarkerBoxComponent implements OnInit {

    public constructor(private _fileModelTreeService: FileModelTreeService) {

    }

    private maxListItems: number = 200;
    private displayMaxItemsExceeded: boolean = false;
    private maxExceededTypeLabel: string;

    private displayChoicePrompt: boolean = false;
    private selectedListType: string = "itemFile";

    private displayUploader: boolean = true;
    private displayListBox: boolean = false;

    private gobiiExtractFilterType: GobiiExtractFilterType = GobiiExtractFilterType.UNKNOWN;
    private onSampleMarkerError: EventEmitter<HeaderStatusMessage> = new EventEmitter();
    private onMarkerSamplesCompleted: EventEmitter<SampleMarkerList> = new EventEmitter();

    private extractTypeLabelExisting: string;
    private extractTypeLabelProposed: string;

    // private handleUserSelected(arg) {
    //     this.onUserSelected.emit(this.nameIdList[arg.srcElement.selectedIndex].id);
    // }
    //
    // constructor(private _dtoRequestService:DtoRequestService<NameId[]>) {
    //
    // } // ctor

    private handleTextBoxDataSubmitted(items: string[]) {

        if (items.length <= this.maxListItems) {

            let listItemType: ExtractorItemType =
                this.gobiiExtractFilterType === GobiiExtractFilterType.BY_MARKER ?
                    ExtractorItemType.MARKER_LIST_ITEM : ExtractorItemType.SAMPLE_LIST_ITEM;

            items.forEach(listItem => {

                if (listItem && listItem !== "") {

                    this._fileModelTreeService
                        .put(GobiiFileItem.build(this.gobiiExtractFilterType, ProcessType.CREATE)
                            .setExtractorItemType(listItemType)
                            .setItemId(listItem)
                            .setItemName(listItem))
                        .subscribe(null, headerStatusMessage => {
                            this.handleStatusHeaderMessage(headerStatusMessage)
                        });
                }
            });

        } else {

            if (this.gobiiExtractFilterType === GobiiExtractFilterType.BY_MARKER) {
                this.maxExceededTypeLabel = Labels.instance().treeExtractorTypeLabels[ExtractorItemType.MARKER_LIST_ITEM];
            } else if (this.gobiiExtractFilterType === GobiiExtractFilterType.BY_SAMPLE) {
                this.maxExceededTypeLabel = Labels.instance().treeExtractorTypeLabels[ExtractorItemType.SAMPLE_LIST_ITEM];
            } else {
                this.handleStatusHeaderMessage(new HeaderStatusMessage("This control does not handle the currently selected item type: "
                    + GobiiExtractFilterType[this.gobiiExtractFilterType]
                    ,null,null))
            }

            this.displayMaxItemsExceeded = true;
        }
    }


    private currentFileItems: GobiiFileItem[] = [];

    handleSampleMarkerChoicesExist(): boolean {

        let returnVal: boolean = false;

        this._fileModelTreeService.getFileItems(this.gobiiExtractFilterType).subscribe(
            fileItems => {

                let extractorItemTypeListToFind: ExtractorItemType = ExtractorItemType.UNKNOWN;
                let extractorItemTypeFileToFind: ExtractorItemType = ExtractorItemType.UNKNOWN;

                if (this.gobiiExtractFilterType === GobiiExtractFilterType.BY_SAMPLE) {
                    extractorItemTypeListToFind = ExtractorItemType.SAMPLE_LIST_ITEM;
                    extractorItemTypeFileToFind = ExtractorItemType.SAMPLE_FILE;
                } else if (this.gobiiExtractFilterType === GobiiExtractFilterType.BY_MARKER) {
                    extractorItemTypeListToFind = ExtractorItemType.MARKER_LIST_ITEM;
                    extractorItemTypeFileToFind = ExtractorItemType.MARKER_FILE;
                }

                this.currentFileItems = fileItems.filter(item => {
                    return ( ( item.getExtractorItemType() === extractorItemTypeListToFind ) ||
                    (item.getExtractorItemType() === extractorItemTypeFileToFind) )
                });

                if (this.currentFileItems.length > 0) {

                    this.extractTypeLabelExisting = Labels.instance().treeExtractorTypeLabels[this.currentFileItems[0].getExtractorItemType()];

                    if (this.currentFileItems[0].getExtractorItemType() === ExtractorItemType.SAMPLE_LIST_ITEM) {

                        this.extractTypeLabelProposed = Labels.instance().treeExtractorTypeLabels[ExtractorItemType.SAMPLE_FILE];

                    } else if (this.currentFileItems[0].getExtractorItemType() === ExtractorItemType.MARKER_LIST_ITEM) {

                        this.extractTypeLabelProposed = Labels.instance().treeExtractorTypeLabels[ExtractorItemType.MARKER_FILE];

                    } else if (this.currentFileItems[0].getExtractorItemType() === ExtractorItemType.SAMPLE_FILE) {

                        this.extractTypeLabelProposed = Labels.instance().treeExtractorTypeLabels[ExtractorItemType.SAMPLE_LIST_ITEM];

                    } else if (this.currentFileItems[0].getExtractorItemType() === ExtractorItemType.MARKER_FILE) {

                        this.extractTypeLabelProposed = Labels.instance().treeExtractorTypeLabels[ExtractorItemType.MARKER_LIST_ITEM];
                    }

                    this.displayChoicePrompt = true;
                    returnVal = true;
                    // it does not seem that the PrimeNG dialog really blocks in the usual sense; 
                    // so we have to chain what we do next off of the click events on the dialog.
                    // see handleUserChoice() 

                } else {

                }
            },
            hsm => {
                this.handleStatusHeaderMessage(hsm)
            }
        );

        // if (event.currentTarget.defaultValue === "itemArray") {
        //
        // } else if (event.currentTarget.defaultValue == "itemFile") {
        //
        // }

        return returnVal;

    }

    handleUserChoice(userChoice: boolean) {

        this.displayChoicePrompt = false;

        if (this.currentFileItems.length > 0 && userChoice === true) {

            // based on what _was_ the current item, we now make the current selection the other one
            if (this.currentFileItems[0].getExtractorItemType() === ExtractorItemType.MARKER_LIST_ITEM
                || this.currentFileItems[0].getExtractorItemType() === ExtractorItemType.SAMPLE_LIST_ITEM) {

                this.displayListBox = false;
                this.displayUploader = true;

                this.selectedListType = "itemFile";

            } else if (this.currentFileItems[0].getExtractorItemType() === ExtractorItemType.MARKER_FILE
                || this.currentFileItems[0].getExtractorItemType() === ExtractorItemType.SAMPLE_FILE) {

                this.displayListBox = true;
                this.displayUploader = false;

                this.selectedListType = "itemArray";

            }

            this.currentFileItems.forEach(currentFileItem => {

                currentFileItem.setProcessType(ProcessType.DELETE);
                this._fileModelTreeService
                    .put(currentFileItem)
                    .subscribe(fmte => {

                    }, headerStatusMessage => {
                        this.handleStatusHeaderMessage(headerStatusMessage)
                    });
            });
        } else {
            // we leave things as they are; hwoever, because the user clicked a radio button,
            // we have to reset it to match the currently diusplayed list selector
            if (this.selectedListType === "itemFile") {

                this.displayListBox = true;
                this.displayUploader = false;

                this.selectedListType = "itemArray"

            } else if (this.selectedListType === "itemArray") {

                this.displayListBox = false;
                this.displayUploader = true;

                this.selectedListType = "itemFile"

            }

        } // if-else user answered "yes"
    }

    private handleTextBoxChanged(event) {

        // if there is no existing selected list or file, then this is just a simple setting
        if (this.handleSampleMarkerChoicesExist() === false) {

            this.displayListBox = true;
            this.displayUploader = false;

            // this.displayListBox = true;
            // this.displayUploader = false;

        }
    }

    private handleOnClickBrowse($event) {

        if (this.handleSampleMarkerChoicesExist() === false) {

            this.displayListBox = false;
            this.displayUploader = true;

            // this.displayListBox = false;
            // this.displayUploader = true;

        }
    }

    private handleStatusHeaderMessage(statusMessage: HeaderStatusMessage) {

        this.onSampleMarkerError.emit(statusMessage);
    }

    ngOnInit(): any {

//        this.extractTypeLabel = Labels.instance().extractorFilterTypeLabels[this.gobiiExtractFilterType];
        return null;
    }

}
