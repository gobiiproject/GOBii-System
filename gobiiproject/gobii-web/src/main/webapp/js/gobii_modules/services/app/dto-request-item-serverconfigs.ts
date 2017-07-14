import {Injectable} from "@angular/core";
import {NameId} from "../../model/name-id";
import {DtoRequestItem} from "./../core/dto-request-item";
import {ProcessType} from "../../model/type-process";
import {ServerConfig} from "../../model/server-config";

@Injectable()
export class DtoRequestItemServerConfigs implements DtoRequestItem<ServerConfig[]> {

    public constructor() {
    }

    public getUrl():string {
        return "gobii/v1/configsettings";
    } // getUrl()

    private processType:ProcessType = ProcessType.READ;

    public getRequestBody():string {

        return JSON.stringify({
            "processType": ProcessType[this.processType],
        })
    }

    public resultFromJson(json):ServerConfig[] {

        let returnVal:ServerConfig[] = [];

        let serverConfigs:Object = json.payload.data[0].serverConfigs;
        let arrayOfIds = Object.keys(serverConfigs);
        arrayOfIds.forEach(crop => {
            let currentCrop = crop;
            let currentDomain:string = serverConfigs[crop].domain;
            let currentContextRoot:string = serverConfigs[crop].contextRoot;
            let currentPort:number = Number(serverConfigs[crop].port);
            returnVal.push(new ServerConfig(currentCrop,
                currentDomain,
                currentContextRoot,
                currentPort));
        });

        return returnVal;
        //return [new NameId(1, 'foo'), new NameId(2, 'bar')];
    }


} // DtoRequestItemNameIds() 







