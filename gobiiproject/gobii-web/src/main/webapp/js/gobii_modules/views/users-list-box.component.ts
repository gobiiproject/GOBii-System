//import {RouteParams} from '@angular/router-deprecated';
import {Component, OnInit, EventEmitter} from "@angular/core";
import {NameId} from "../model/name-id";
import {DtoRequestService} from "../services/core/dto-request.service";


@Component({
    selector: 'users-list-box',
    inputs: ['nameIdList'],
    outputs: ['onUserSelected'],
    template: `<select name="users" (change)="handleUserSelected($event)" >
			<option *ngFor="let nameId of nameIdList " 
				value={{nameId.id}}>{{nameId.name}}</option>
		</select>
` // end template

})

export class UsersListBoxComponent implements OnInit {


    // useg
    private nameIdList:NameId[];

    private onUserSelected:EventEmitter<string> = new EventEmitter();
    private handleUserSelected(arg) {
        this.onUserSelected.emit(this.nameIdList[arg.srcElement.selectedIndex].id);
    }

    constructor(private _dtoRequestService:DtoRequestService<NameId[]>) {

    } // ctor


    ngOnInit():any {
        return null;
    }
}
