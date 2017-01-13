import {Injectable} from "@angular/core";
import {NameId} from "../../model/name-id";
import {DtoRequestItem} from "./../core/dto-request-item";
import {ProcessType} from "../../model/type-process";
import {ServerConfig} from "../../model/server-config";
import {GobiiCropType} from "../../model/type-crop"

@Injectable()
export class DtoRequestItemServerConfigs implements DtoRequestItem<ServerConfig[]> {

    public constructor() {
    }

    public getUrl():string {
        return "load/configsettings";
    } // getUrl()

    private processType:ProcessType = ProcessType.READ;

    public getRequestBody():string {

        return JSON.stringify({
            "processType": ProcessType[this.processType],
        })
    }

    public resultFromJson(json):ServerConfig[] {

        let returnVal:ServerConfig[] = [];
        console.log("*************ENTITY NAME: " + json.entityName);
        console.log(json.dtoHeaderResponse.succeeded ? "succeeded" : "error: " + json.dtoHeaderResponse.statusMessages)
        console.log(json.namesById);

        let arrayOfIds = Object.keys(json.serverConfigs);
        arrayOfIds.forEach(crop => {
            let currentCrop = crop;
            let currentDomain:string = json.serverConfigs[crop].domain;
            let currentContextRoot:string = json.serverConfigs[crop].contextRoot;
            let currentPort:number = Number(json.serverConfigs[crop].port);
            returnVal.push(new ServerConfig(currentCrop,
                currentDomain,
                currentContextRoot,
                currentPort));
        });

        return returnVal;
        //return [new NameId(1, 'foo'), new NameId(2, 'bar')];
    }


} // DtoRequestItemNameIds() 







