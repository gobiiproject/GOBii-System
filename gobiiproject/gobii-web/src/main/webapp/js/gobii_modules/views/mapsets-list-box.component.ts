import {Component, OnInit, EventEmitter} from "@angular/core";
import {NameId} from "../model/name-id";
import {FileModelTreeService} from "../services/core/file-model-tree-service";


@Component({
    selector: 'mapsets-list-box',
    outputs: ['onMapsetSelected'],
    inputs: ['nameIdList'],
    template: `
            <label class="the-label">Add Mapset Info</label><BR>
            <select name="mapsets" (change)="handleMapsetSelected($event)" >
			<option *ngFor="let nameId of nameIdList " 
				value={{nameId.id}}>{{nameId.name}}</option>
		</select>
` // end template

})

export class MapsetsListBoxComponent implements OnInit {


    // useg
    private nameIdList:NameId[];

    private onMapsetSelected:EventEmitter<NameId> = new EventEmitter();

    private handleMapsetSelected(arg) {
        this.onMapsetSelected.emit(this.nameIdList[arg.srcElement.selectedIndex]);

    }

    constructor(private _fileModelTreeService: FileModelTreeService) {

    } // ctor

    ngOnInit():any {
        return null;
    }
}
