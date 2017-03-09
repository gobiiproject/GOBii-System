import {Injectable} from "@angular/core";
import {NameId} from "../../model/name-id";
import {DtoRequestItem} from "./../core/dto-request-item";
import {EntityType} from "../../model/type-entity";
import {EntityFilter} from "../../model/type-entity-filter";
import {ProcessType} from "../../model/type-process";

@Injectable()
export class DtoRequestItemNameIds implements DtoRequestItem<NameId[]> {

    public constructor(entityType:EntityType,
                       entityFilter:EntityFilter = null,
                       entityFilterValue:string = null) {
        this.entityType = entityType;
        this.entityFilter = entityFilter;
        this.entityFilterValue = entityFilterValue;
    }

    public getRequestBody():string {
        return null;
    }


    public getUrl():string {

        let baseUrl:string = "gobii/v1/names";

        let returnVal:string = baseUrl + "/" + EntityType[this.entityType].toLowerCase();

        if (this.entityFilter && (EntityFilter.NONE.valueOf() !== this.entityFilter)) {
            returnVal += "?"
                + "filterType=" + EntityFilter[this.entityFilter].toLowerCase()
                + "&"
                + "filterValue="
                + this.entityFilterValue;
        }

        return returnVal;

    } // getUrl()

    private entityType:EntityType;

    public setEntity(entityType:EntityType) {
        this.entityType = entityType;
    }

    private entityFilter:EntityFilter;

    private entityFilterValue:string;

    public resultFromJson(json):NameId[] {

        let returnVal:NameId[] = [];

        //let nameListItems:Object[] = json.payload.data;

        json.payload.data.forEach(item => {
            let currentId:string = String(item.id);
            let currentName:string = item.name;
            returnVal.push(new NameId(currentId, currentName, this.entityType));
        });

        return returnVal;
        //return [new NameId(1, 'foo'), new NameId(2, 'bar')];
    }


} // DtoRequestItemNameIds() 







