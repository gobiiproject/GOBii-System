
import {Component, OnInit, EventEmitter} from "@angular/core";
import {NameId} from "../model/name-id";


@Component({
    selector: 'dataset-types-list-box',
    outputs: ['onDatasetTypeSelected'],
    inputs: ['nameIdList'],
    template: `<select name="datasetTypes" (change)="handleDatasetTypeSelected($event)" >
			<option *ngFor="let nameId of nameIdList " 
				value={{nameId.id}}>{{nameId.name}}</option>
		</select>
` // end template

})

export class DatasetTypeListBoxComponent implements OnInit {


    // useg
    private nameIdList:NameId[];

    private onDatasetTypeSelected:EventEmitter<string> = new EventEmitter();
    private handleDatasetTypeSelected(arg) {
        this.onDatasetTypeSelected.emit(this.nameIdList[arg.srcElement.selectedIndex].id);
    }

    constructor() {

    } // ctor

    ngOnInit():any {
        return null;
    }
}
