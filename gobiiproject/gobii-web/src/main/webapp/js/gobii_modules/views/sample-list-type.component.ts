import {Component, OnInit, EventEmitter} from '@angular/core';
import {EntityFilter} from "../model/type-entity-filter";
import {GobiiExtractFilterType} from "../model/type-extractor-filter";
import {GobiiSampleListType} from "../model/type-extractor-sample-list";


@Component({
    selector: 'sample-list-type',
    outputs: ['onSampleListTypeSelected'],
    template: `<label class="the-label">Export By:</label><BR>
                  <input type="radio" (change)="handleExportTypeSelected($event)" name="format" value="GERMPLASM_NAME" checked="checked">Germplasm Name<BR>
                  <input type="radio" (change)="handleExportTypeSelected($event)" name="format" value="EXTERNAL_CODE">External Code<BR>
                  <input type="radio" (change)="handleExportTypeSelected($event)" name="format" value="DNA_SAMPLE">DNA Sample<BR>` // end template
})

export class SampleListTypeComponent implements OnInit {

    constructor(//	  private _heroService: HeroService,
        //	  private _routeParams: RouteParams
    ) {
    } // ctor

    private onSampleListTypeSelected: EventEmitter<GobiiSampleListType> = new EventEmitter();

    private handleExportTypeSelected(arg) {
        if (arg.srcElement.checked) {

            let radioValue:string = arg.srcElement.value;

            let gobiiExtractFilterType:GobiiSampleListType = GobiiSampleListType[radioValue];

            this.onSampleListTypeSelected.emit(gobiiExtractFilterType)
        }
    }


    ngOnInit() {
    }

}
