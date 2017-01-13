import {Injectable} from "@angular/core";
import {NameId} from "../../model/name-id";
import {DtoRequestItem} from "./../core/dto-request-item";
import {EntityType} from "../../model/type-entity";
import {ProcessType} from "../../model/type-process";

@Injectable()
export class DtoRequestItemNameIds implements DtoRequestItem<NameId[]> {

    public constructor(processType:ProcessType,
                       entityType:EntityType,
                       entityFilter:string = null) {
        this.processType = processType;
        this.entityType = entityType;
        this.entityFilter = entityFilter;
    }


    public getUrl():string {
        return "load/nameidlist";
    } // getUrl()

    // public getRequestBody(): string {
    //     return JSON.stringify({
    //         "processType": "READ",
    //         "dtoHeaderAuth": {"userName": null, "password": null, "token": null},
    //         "dtoHeaderResponse": {"succeeded": true, "statusMessages": []},
    //         "entityType": "DBTABLE",
    //         "entityName": "datasetnames",
    //         "namesById": {},
    //         "filter": null
    //     })
    // }

    private entityType:EntityType;

    public setEntity(entityType:EntityType) {
        this.entityType = entityType;
    }

    private processType:ProcessType = ProcessType.READ;


    private entityFilter:string;
    
    public getRequestBody():string {

        return JSON.stringify({
            "processType": ProcessType[this.processType],
            "entityType": "DBTABLE",
            "entityName": EntityType[this.entityType].toLowerCase(),
            "filter": this.entityFilter
        })
    }

    public resultFromJson(json):NameId[] {

        let returnVal:NameId[] = [];
        console.log("*************ENTITY NAME: " + json.entityName);
        console.log( json.dtoHeaderResponse.succeeded ? "succeeded" : "error: " + json.dtoHeaderResponse.statusMessages)
        console.log(json.namesById);

        let arrayOfIds = Object.keys(json.namesById);
        arrayOfIds.forEach(id => {
            let currentVal:string = json.namesById[id];
            returnVal.push(new NameId(id, currentVal));
        });

        return returnVal;
        //return [new NameId(1, 'foo'), new NameId(2, 'bar')];
    }


} // DtoRequestItemNameIds() 







