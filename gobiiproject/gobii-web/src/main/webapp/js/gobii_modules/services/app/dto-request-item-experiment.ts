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

        let baseUrl:string = "gobii/v1/experiments";

        let returnVal:string = baseUrl;
        if (this.experimentId) {
            returnVal = baseUrl + "/" + this.experimentId;
        }

        return returnVal;

    } // getUrl()

    private processType:ProcessType = ProcessType.READ;

    public getRequestBody():string {

        return JSON.stringify({
            "processType": ProcessType[this.processType],
            "experimentId": this.experimentId
        })
    }

    public resultFromJson(json):Experiment {

        let returnVal:Experiment = undefined;

        if (json.payload.data[0]) {
            returnVal = json.payload.data[0];
        }

        // json.payload.data.forEach(item => {
        //
        //     returnVal.push(new Experiment(item.experimentId,
        //         item.experimentName,
        //         item.experimentCode,
        //         item.experimentDataFile,
        //         item.projectId,
        //         item.platformId,
        //         item.manifestId,
        //         item.createdBy,
        //         item.createdstring,
        //         item.modifiedBy,
        //         item.modifiedstring,
        //         item.status,
        //         item.platformName
        //     ));
        // });

        return returnVal;
        //return [new NameId(1, 'foo'), new NameId(2, 'bar')];
    }


} // DtoRequestItemNameIds() 







