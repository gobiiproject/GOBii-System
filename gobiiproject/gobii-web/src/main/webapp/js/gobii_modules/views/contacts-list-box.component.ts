//import {RouteParams} from '@angular/router-deprecated';
import {Component, OnInit, EventEmitter} from "@angular/core";
import {NameId} from "../model/name-id";


@Component({
    selector: 'contacts-list-box',
    outputs: ['onContactSelected'],
    inputs: ['nameIdList'],
    template: `<select name="principleInvestigators" (change)="handleContactSelected($event)" >
			<option *ngFor="let nameId of nameIdList " 
				value={{nameId.id}}>{{nameId.name}}</option>
		</select>
` // end template

})

export class ContactsListBoxComponent implements OnInit {


    // useg
    private nameIdList:NameId[];

    private onContactSelected:EventEmitter<string> = new EventEmitter();

    private handleContactSelected(arg) {
        this.onContactSelected.emit(this.nameIdList[arg.srcElement.selectedIndex].id);
    }

    constructor() {

    } // ctor

    ngOnInit():any {
        return null;
    }
}
