import {Injectable} from "@angular/core";
import {DtoRequestItem} from "./../core/dto-request-item";
import {ProcessType} from "../../model/type-process";
import {Analysis} from "../../model/analysis";

@Injectable()
export class DtoRequestItemAnalysis implements DtoRequestItem<Analysis> {

    public constructor(private analysisId:number) {
        this.analysisId = analysisId;
    }

    public getUrl():string {
        return "load/analysis";
    } // getUrl()

    private processType:ProcessType = ProcessType.READ;

    public getRequestBody():string {

        return JSON.stringify({
            "processType": ProcessType[this.processType],
            "analysisId": this.analysisId
        })
    }

    public resultFromJson(json):Analysis {

        let returnVal:Analysis;
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

        returnVal = new Analysis(json.analysisId,
            json.analysisName,
            json.analysisDescription,
            json.anlaysisTypeId,
            json.program,
            json.programVersion,
            json.algorithm,
            json.sourceName,
            json.sourceVersion,
            json.sourceUri,
            json.referenceId,
            json.timeExecuted,
            json.status
        );


        return returnVal;
        //return [new NameId(1, 'foo'), new NameId(2, 'bar')];
    }


} // DtoRequestItemNameIds() 







