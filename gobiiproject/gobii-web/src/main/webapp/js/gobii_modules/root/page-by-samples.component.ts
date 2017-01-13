import {RouteParams} from '@angular/router-deprecated';
import { Component, OnInit } from '@angular/core';
//import {CORE_DIRECTIVES, FORM_DIRECTIVES, NgClass, NgStyle} from 'angular2/common';



@Component({
  selector: 'search-criteria-samples',
  template: `
			<form>
				<fieldset class="well the-fieldset">
				<legend class="the-legend">Search Criteria</legend>
				
				Samples:
				<input type="file"/>
				<BR>
				Markers:
				<input type="file/>
				
				</fieldset>
			</form>
  ` // end template
})

export class SearchCriteriaBySamplesComponent implements OnInit {
	
	constructor(
//	  private _heroService: HeroService,
//	  private _routeParams: RouteParams
	  ) {
	} // ctor
	
	ngOnInit() {
/*
		let id = +this._routeParams.get('id');
		this._heroService.getHero(id)
		  .then(hero => this.hero = hero);
*/		  
	  }

}
