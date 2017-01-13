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
        let baseUrl:string = "gobii/v1/datasets";

        let returnVal:string  = baseUrl;
        if( this.dataSetId ) {
            returnVal = baseUrl + "/" + this.dataSetId;
        }

        return returnVal;
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
        // console.log("*************ENTITY NAME: " + json.entityName);
        // console.log(json.dtoHeaderResponse.succeeded ? "succeeded" : "error: " + json.dtoHeaderResponse.statusMessages)
        // console.log(json.namesById);
        //
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

        if( json.payload.data[0]) {
            returnVal = new DataSet(json.payload.data[0].dataSetId,
                json.payload.data[0].name,
                json.payload.data[0].experimentId,
                json.payload.data[0].callingAnalysisId,
                json.payload.data[0].dataTable,
                json.payload.data[0].dataFile,
                json.payload.data[0].qualityTable,
                json.payload.data[0].qualityFile,
                json.payload.data[0].status,
                json.payload.data[0].typeId,
                json.payload.data[0].analysesIds);
        }

        return returnVal;
        //return [new NameId(1, 'foo'), new NameId(2, 'bar')];
    }


} // DtoRequestItemNameIds() 







