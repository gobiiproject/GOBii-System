import {Component, OnInit, EventEmitter, OnChanges, SimpleChange} from '@angular/core';
import {GobiiExtractFormat} from "../model/type-extract-format";
import {FileModelTreeService} from "../services/core/file-model-tree-service";
import {GobiiFileItem} from "../model/gobii-file-item";
import {ProcessType} from "../model/type-process";
import {ExtractorItemType} from "../model/file-model-node";
import {GobiiExtractFilterType} from "../model/type-extractor-filter";
import {Header} from "../model/payload/header";
import {NameId} from "../model/name-id";
import {DtoRequestService} from "../services/core/dto-request.service";
import {DtoRequestItemNameIds} from "../services/app/dto-request-item-nameids";
import {EntityType} from "../model/type-entity";


@Component({
    selector: 'export-format',
    outputs: ['onFormatSelected', 'onError'],
    inputs: ['gobiiExtractFilterType'],
    //directives: [RADIO_GROUP_DIRECTIVES]
//  directives: [Alert]
    template: `<form>
                            <label class="the-legend">Select Format:&nbsp;</label>
                            <BR><input type="radio" (change)="handleFormatSelected($event)" [(ngModel)]="fileFormat" name="fileFormat" value="HAPMAP" checked="checked">
                            <label  for="HAPMAP" class="the-legend">Hapmap</label>
                            <BR><input type="radio" (change)="handleFormatSelected($event)" [(ngModel)]="fileFormat" name="fileFormat" value="FLAPJACK">
                            <label for="FLAPJACK" class="the-legend">Flapjack</label>
                            <BR><input type="radio" (change)="handleFormatSelected($event)" [(ngModel)]="fileFormat" name="fileFormat" value="META_DATA_ONLY">
                            <label  for="META_DATA_ONLY" class="the-legend">{{metaDataExtractname}}</label>
                </form>` // end template
})

export class ExportFormatComponent implements OnInit, OnChanges {

    constructor(private _fileModelTreeService: FileModelTreeService) {
    } // ctor

    // private nameIdList: NameId[];
    // private selectedNameId: string = null;
    ngOnInit() {

        // in the current version, this doesn't work: each component in the page
        // is initialized once at a time. Thus, even though the tree is being built
        // built in the tree component's constructor, nginit() here still is triggered
        // before the tree is instanced. Thus the format is not displayed in the tree because
        // the tree isn't there yet to receive the event from the dataservice. ngInit() works
        // to initialize the submitted as user in the tree is because it is calling a web service
        // with an observer: while the service is being called, the rest of the control hierarchy is
        // being built, so there is just enough enough latency in the web service call that the tree
        // is there when the fileItem is posted to the tree model service. I have providen this
        // because in the commented out code below, I make a call to the same webservice then
        // post the GobiiFileItem for the format, and lo and behold, the tree is there and gets properly
        // initialized with the format type. The only to make this is work is to post the format change
        // to the model service _after_ the tree calls oncomplete. If we want to encapsulate all the
        // service communication in the child components, the tree service will have to accommodate
        // notification events to which these components will subscribe.


        // let scope$ = this;
        // this._dtoRequestService.get(new DtoRequestItemNameIds(
        //     EntityType.Contacts,
        //     null,
        //     null)).subscribe(nameIds => {
        //         if (nameIds && ( nameIds.length > 0 )) {
        //             scope$.nameIdList = nameIds;
        //             scope$.selectedNameId = nameIds[0].id;
        //             this.updateTreeService(GobiiExtractFormat.HAPMAP);
        //             //this.updateTreeService(nameIds[0]);
        //         } else {
        //             //scope$.nameIdList = [new NameId("0", "ERROR NO " + EntityType[scope$.entityType], scope$.entityType)];
        //         }
        //     },
        //     responseHeader => {
        //         this.handleResponseHeader(responseHeader);
        //     });


        // so, for now, this dispensation solves the problem. But I suspect it only works because the
        // the tree component just happens to be the last one to be processed because it's at the end
        // of the control tree, so it's the last one to get the property binding updates. If it were
        // at the top of the control tree, we would have the reverse problem in that it would send out
        // the of TREE_READY before the sibling components had been bound to their property values,
        // and the component initialization would not work. perhaps. Tne work around for that would be that
        // in this callback, we would check that the temlate-bound parameters had values; if they did not
        // we would set a flag in this component saying, tree is ready; in ngInit, after the component properties
        // are bound, we would check whether that flag is set, and if it was, then we would send
        // the tree notification. I _think_ that would cover all the contingencies, but it's ugly.
        // I am not sure whether reactive forms would address this issue.

        //this.setDefault();
    }

    private setDefault() {
        this.updateTreeService(GobiiExtractFormat.HAPMAP);
        this.fileFormat = "HAPMAP";

    }

    private handleResponseHeader(header: Header) {

        this.onError.emit(header);
    }


    private fileFormat: string = "HAPMAP";
    private gobiiExtractFilterType: GobiiExtractFilterType;
    private onFormatSelected: EventEmitter<GobiiExtractFormat> = new EventEmitter();
    private onError: EventEmitter<Header> = new EventEmitter();

    private handleFormatSelected(arg) {
        if (arg.srcElement.checked) {

            let radioValue: string = arg.srcElement.value;
            this.selectedExtractFormat = GobiiExtractFormat[radioValue];

            this.updateTreeService(this.selectedExtractFormat);

        }
        let foo = arg;
        //this.onContactSelected.emit(this.nameIdList[arg.srcElement.selectedIndex].id);
    }

    private selectedExtractFormat: GobiiExtractFormat = GobiiExtractFormat.HAPMAP;

    private updateTreeService(arg: GobiiExtractFormat) {

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

    private metaDataExtractname: string;

    ngOnChanges(changes: {[propName: string]: SimpleChange}) {

        this._fileModelTreeService
            .fileItemNotifications()
            .subscribe(fileItem => {

                if (fileItem.getProcessType() === ProcessType.NOTIFY &&
                    ((fileItem.getExtractorItemType() === ExtractorItemType.STATUS_DISPLAY_TREE_READY)
                    || (fileItem.getExtractorItemType() === ExtractorItemType.CLEAR_TREE) )) {
                    this.setDefault();
                }
            });

        if (changes['gobiiExtractFilterType']
            && ( changes['gobiiExtractFilterType'].currentValue != null )
            && ( changes['gobiiExtractFilterType'].currentValue != undefined )) {

            if (changes['gobiiExtractFilterType'].currentValue != changes['gobiiExtractFilterType'].previousValue) {


                let labelSuffix: string = " Metadata";
                if (this.gobiiExtractFilterType === GobiiExtractFilterType.WHOLE_DATASET) {
                    this.metaDataExtractname = "Dataset" + labelSuffix;
                } else if (this.gobiiExtractFilterType === GobiiExtractFilterType.BY_MARKER) {
                    this.metaDataExtractname = "Marker" + labelSuffix;
                } else if (this.gobiiExtractFilterType === GobiiExtractFilterType.BY_SAMPLE) {
                    this.metaDataExtractname = "Sample" + labelSuffix;
                }

                this.setDefault();

            } // if we have a new filter type

        } // if filter type changed

    } // ngonChanges


}
