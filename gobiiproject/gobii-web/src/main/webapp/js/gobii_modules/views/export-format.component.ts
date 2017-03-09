import {Component, OnInit,EventEmitter} from '@angular/core';
import {GobiiExtractFormat} from "../model/type-extract-format";


@Component({
    selector: 'export-format',
    outputs: ['onFormatSelected'],
//  inputs: ['hero'],
    //directives: [RADIO_GROUP_DIRECTIVES]
//  directives: [Alert]
    template: `
    		  <label class="the-label">Select Format:</label><BR>
              &nbsp;&nbsp;&nbsp;<input type="radio" (change)="handleContactSelected($event)" name="format" value="HAPMAP" checked="checked">Hapmap<br>
              &nbsp;&nbsp;&nbsp;<input type="radio" (change)="handleContactSelected($event)" name="format" value="FLAPJACK">FlapJack<br>
              &nbsp;&nbsp;&nbsp;<input type="radio" (change)="handleContactSelected($event)" name="format" value="META_DATA_ONLY">Dataset Metadata Only<br>
	` // end template
})

export class ExportFormatComponent implements OnInit {

    constructor(//	  private _heroService: HeroService,
                //	  private _routeParams: RouteParams
    ) {
    } // ctor

    private onFormatSelected:EventEmitter<GobiiExtractFormat> = new EventEmitter();
    private handleContactSelected(arg) {
        if( arg.srcElement.checked ) {

            let radioValue:string = arg.srcElement.value;

            let gobiiExportFormat:GobiiExtractFormat = GobiiExtractFormat[radioValue];

            this.onFormatSelected.emit(gobiiExportFormat)
        }
        let foo = arg;
        //this.onContactSelected.emit(this.nameIdList[arg.srcElement.selectedIndex].id);
    }


    ngOnInit() {
        /*
         let id = +this._routeParams.get('id');
         this._heroService.getHero(id)
         .then(hero => this.hero = hero);
         */
    }

}
