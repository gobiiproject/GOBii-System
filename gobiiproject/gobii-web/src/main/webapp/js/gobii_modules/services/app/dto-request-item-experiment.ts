import {Injectable} from "@angular/core";
import {DtoRequestItem} from "./../core/dto-request-item";
import {ProcessType} from "../../model/type-process";
import {Experiment} from "../../model/experiment";

@Injectable()
export class DtoRequestItemExperiment implements DtoRequestItem<Experiment> {

    public constructor(private experimentId:number) {
        this.experimentId = experimentId;
    }

    public getUrl():string {
        return "load/experiment";
    } // getUrl()

    private processType:ProcessType = ProcessType.READ;

    public getRequestBody():string {

        return JSON.stringify({
            "processType": ProcessType[this.processType],
            "experimentId": this.experimentId
        })
    }

    public resultFromJson(json):Experiment {

        let returnVal:Experiment;
        console.log("*************ENTITY NAME: " + json.entityName);
        console.log(json.dtoHeaderResponse.succeeded ? "succeeded" : "error: " + json.dtoHeaderResponse.statusMessages)
        console.log(json.namesById);

        // let arrayOfIds = Object.keys(json.serverConfigs);
        // arrayOfIds.forEach(crop => {
        //     let currentCrop = crop;
        //     let currentDomain:string = json.serverConfigs[crop].domain;
        //     let currentContextRoot:string = json.serverConfigs[crop].contextRoot;
        //     let currentPort:number = Number(json.serverConfigs[crop].port);
        //     returnVal.push(new ServerConfig(currentCrop,
        //         currentDomain,
        //         currentContextRoot,
        //         currentPort));
        // });

        returnVal = new Experiment(json.experimentId,
            json.experimentName,
            json.experimentCode,
            json.experimentDataFile,
            json.projectId,
            json.platformId,
            json.manifestId,
            json.createdBy,
            json.createdstring,
            json.modifiedBy,
            json.modifiedstring,
            json.status,
            json.platformName
        );


        return returnVal;
        //return [new NameId(1, 'foo'), new NameId(2, 'bar')];
    }


} // DtoRequestItemNameIds() 







