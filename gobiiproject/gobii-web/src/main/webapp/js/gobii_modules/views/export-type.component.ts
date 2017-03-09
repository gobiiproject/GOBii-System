import {Component, OnInit, EventEmitter} from '@angular/core';
import {EntityFilter} from "../model/type-entity-filter";
import {GobiiExtractFilterType} from "../model/type-extractor-filter";


@Component({
    selector: 'export-type',
    outputs: ['onExportTypeSelected'],
    template: `<label class="the-label">Extract By:&nbsp;</label>
                  <input type="radio" (change)="handleExportTypeSelected($event)" name="format" value="WHOLE_DATASET" checked="checked">Data Set&nbsp;
                  <input type="radio" (change)="handleExportTypeSelected($event)" name="format" value="BY_SAMPLE" disabled>Sample&nbsp;
                  <input type="radio" (change)="handleExportTypeSelected($event)" name="format" value="BY_MARKER" disabled>Marker&nbsp;` // end template
})

export class ExportTypeComponent implements OnInit {

    constructor(//	  private _heroService: HeroService,
        //	  private _routeParams: RouteParams
    ) {
    } // ctor

    private onExportTypeSelected: EventEmitter<GobiiExtractFilterType> = new EventEmitter();

    private handleExportTypeSelected(arg) {
        if (arg.srcElement.checked) {

            let radioValue:string = arg.srcElement.value;

            let entityFilter:GobiiExtractFilterType = GobiiExtractFilterType[radioValue];

            this.onExportTypeSelected.emit(entityFilter)
        }
    }


    ngOnInit() {
    }

}
