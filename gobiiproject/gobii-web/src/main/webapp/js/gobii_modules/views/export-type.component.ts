import {Component, OnInit, EventEmitter} from '@angular/core';
import {EntityFilter} from "../model/type-entity-filter";
import {GobiiExtractFilterType} from "../model/type-extractor-filter";


// if the radios are not in a form, the BY_SAMPLE one doe snot show as being checked when it is clearly selected
@Component({
    selector: 'export-type',
    outputs: ['onExportTypeSelected'],
    template: `<fieldset class="well the-fieldset" style="width: 350px">
                  <div class="the-legend" >
                      <form>
                          <label class="the-legend">Extract By:&nbsp;</label>
                            <input type="radio" (change)="handleExportTypeSelected($event)" name="format" value="WHOLE_DATASET" checked="checked">
                            <label  for="WHOLE_DATASET" class="the-legend">Datasets</label>
                            <input type="radio" (change)="handleExportTypeSelected($event)" name="format" value="BY_SAMPLE">
                            <label for="BY_SAMPLE" class="the-legend">Samples</label>
                            <input type="radio" (change)="handleExportTypeSelected($event)" name="format" value="BY_MARKER">
                            <label  for="BY_MARKER" class="the-legend">Markers</label>
                      </form>
                  </div>
                </fieldset>` // end template
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

        //this.handleExportTypeSelected(GobiiExtractFilterType.WHOLE_DATASET);
    }

}
