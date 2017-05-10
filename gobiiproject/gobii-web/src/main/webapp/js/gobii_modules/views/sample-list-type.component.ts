import {Component, OnInit, EventEmitter, OnChanges, SimpleChanges, SimpleChange} from '@angular/core';
import {EntityFilter} from "../model/type-entity-filter";
import {GobiiExtractFilterType} from "../model/type-extractor-filter";
import {GobiiSampleListType} from "../model/type-extractor-sample-list";
import {FileModelTreeService} from "../services/core/file-model-tree-service";
import {HeaderStatusMessage} from "../model/dto-header-status-message";
import {GobiiFileItem} from "../model/gobii-file-item";
import {ProcessType} from "../model/type-process";
import {ExtractorItemType} from "../model/file-model-node";


@Component({
    selector: 'sample-list-type',
    inputs: ['gobiiExtractFilterType'],
    outputs: ['onHeaderStatusMessage'],
    template: `<form>
                            <label class="the-legend">List Item Type:&nbsp;</label>
                            <BR><input type="radio" (change)="handleExportTypeSelected($event)" [(ngModel)]="listType" name="listType" value="GERMPLASM_NAME" checked="checked">
                            <label  for="GERMPLASM_NAME" class="the-legend">Germplasm Name</label>
                            <BR><input type="radio" (change)="handleExportTypeSelected($event)" [(ngModel)]="listType" name="listType" value="EXTERNAL_CODE">
                            <label for="EXTERNAL_CODE" class="the-legend">External Code</label>
                            <BR><input type="radio" (change)="handleExportTypeSelected($event)" [(ngModel)]="listType" name="listType" value="DNA_SAMPLE">
                            <label  for="DNA_SAMPLE" class="the-legend">DNA Sample</label>
                </form>` // end template
})

export class SampleListTypeComponent implements OnInit, OnChanges {

    constructor(private _fileModelTreeService: FileModelTreeService) {
    } // ctor

    private onHeaderStatusMessage: EventEmitter<HeaderStatusMessage> = new EventEmitter();
    private gobiiExtractFilterType: GobiiExtractFilterType = GobiiExtractFilterType.UNKNOWN;
    private listType: string = "GERMPLASM_NAME";

    private handleExportTypeSelected(arg) {
        if (arg.srcElement.checked) {

            let radioValue: string = arg.srcElement.value;

            let gobiiSampleListType: GobiiSampleListType = GobiiSampleListType[radioValue];

            this.submitSampleListTypeToService(gobiiSampleListType);

        }
    }

    private submitSampleListTypeToService(gobiiSampleListType: GobiiSampleListType) {


        this._fileModelTreeService
            .put(GobiiFileItem.build(this.gobiiExtractFilterType, ProcessType.CREATE)
                .setExtractorItemType(ExtractorItemType.SAMPLE_LIST_TYPE)
                .setItemName(GobiiSampleListType[gobiiSampleListType])
                .setItemId(GobiiSampleListType[gobiiSampleListType]))
            .subscribe(null,
                he => {
                    this.onHeaderStatusMessage.emit(he)
                });
    }


    private setDefault() {
        this.listType = "GERMPLASM_NAME";
        this.submitSampleListTypeToService(GobiiSampleListType.GERMPLASM_NAME);
    }


    ngOnInit(): any {

        this._fileModelTreeService
            .fileItemNotifications()
            .subscribe(fileItem => {

                if (fileItem.getGobiiExtractFilterType() === GobiiExtractFilterType.BY_SAMPLE
                    && fileItem.getProcessType() === ProcessType.NOTIFY
                    && (( fileItem.getExtractorItemType() === ExtractorItemType.STATUS_DISPLAY_TREE_READY )
                    || (fileItem.getExtractorItemType() === ExtractorItemType.CLEAR_TREE))) {

                    this.setDefault();
                }
            });

        return null;
    }


    ngOnChanges(changes: {[propName: string]: SimpleChange}) {

        if (changes['gobiiExtractFilterType']
            && ( changes['gobiiExtractFilterType'].currentValue != null )
            && ( changes['gobiiExtractFilterType'].currentValue != undefined )) {

                this.setDefault();
        }
    }
}
