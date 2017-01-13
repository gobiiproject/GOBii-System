import {Injectable} from "@angular/core";
import {DtoRequestItem} from "./../core/dto-request-item";
import {ProcessType} from "../../model/type-process";
import {DataSet} from "../../model/dataset";

@Injectable()
export class DtoRequestItemDataSet implements DtoRequestItem<DataSet> {

    public constructor(private dataSetId:number) {
        this.dataSetId = dataSetId;
    }

    public getUrl():string {
        return "load/dataset";
    } // getUrl()

    private processType:ProcessType = ProcessType.READ;

    public getRequestBody():string {

        return JSON.stringify({
            "processType": ProcessType[this.processType],
            "dataSetId": this.dataSetId
        })
    }

    public resultFromJson(json):DataSet {

        let returnVal:DataSet;
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

        returnVal = new DataSet(json.dataSetId,
            json.name,
            json.experimentId,
            json.callingAnalysisId,
            json.dataTable,
            json.dataFile,
            json.qualityTable,
            json.qualityFile,
            json.status,
            json.typeId,
            json.analysesIds);


        return returnVal;
        //return [new NameId(1, 'foo'), new NameId(2, 'bar')];
    }


} // DtoRequestItemNameIds() 







