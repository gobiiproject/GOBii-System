import {RouteParams} from '@angular/router-deprecated';
import {Component, OnInit,EventEmitter} from '@angular/core';


@Component({
    selector: 'export-format',
    outputs: ['onFormatSelected'],
//  inputs: ['hero'],
    //directives: [RADIO_GROUP_DIRECTIVES]
//  directives: [Alert]
    template: `
		<form>
			<fieldset class="well the-fieldset">
			<legend class="the-legend">Export Format</legend>
              <input type="radio" (change)="handleContactSelected($event)" name="format" value="Hapmap" checked="checked">Hapmap<br>
              <input type="radio" (change)="handleContactSelected($event)" name="format" value="FlapJack">FlapJack<br>
              <input type="radio" (change)="handleContactSelected($event)" name="format" value="VCF" disabled="true">VCF<br>
              <input type="radio" (change)="handleContactSelected($event)" name="format" value="HDF5" disabled="true">HDF5<br>
              <input type="radio" (change)="handleContactSelected($event)" name="format" value="PLINK CSV" disabled="true">PLINK CSV<br>
			</fieldset>
			
		</form>
	` // end template
})

export class ExportFormatComponent implements OnInit {

    constructor(//	  private _heroService: HeroService,
                //	  private _routeParams: RouteParams
    ) {
    } // ctor

    private onFormatSelected:EventEmitter<string> = new EventEmitter();
    private handleContactSelected(arg) {
        if( arg.srcElement.checked ) {
        
            this.onFormatSelected.emit(arg.srcElement.value)
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
