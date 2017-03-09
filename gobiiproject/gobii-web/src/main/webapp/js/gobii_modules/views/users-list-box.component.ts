import {Component, OnInit, EventEmitter} from "@angular/core";
import {NameId} from "../model/name-id";
import {DtoRequestService} from "../services/core/dto-request.service";
import {EntityType} from "../model/type-entity";


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
    private nameIdList: NameId[];

    private onUserSelected: EventEmitter<NameId> = new EventEmitter();

    private handleUserSelected(arg) {

        let nameId: NameId = new NameId(this.nameIdList[arg.srcElement.selectedIndex].id,
            this.nameIdList[arg.srcElement.selectedIndex].name,
            EntityType.Contacts);
        this.onUserSelected.emit(nameId);
    }

    constructor(private _dtoRequestService: DtoRequestService<NameId[]>) {

    } // ctor


    ngOnInit(): any {
        return null;
    }
}
