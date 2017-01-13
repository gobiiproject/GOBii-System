import {Injectable} from "@angular/core";
import {DtoRequestItem} from "./../core/dto-request-item";
import {ProcessType} from "../../model/type-process";
import {Cv} from "../../model/cv";

@Injectable()
export class DtoRequestItemCv implements DtoRequestItem<Cv> {

    public constructor(private cvId:number) {
        this.cvId = cvId;
    }

    public getUrl():string {
        return "load/cv";
    } // getUrl()

    private processType:ProcessType = ProcessType.READ;

    public getRequestBody():string {

        return JSON.stringify({
            "processType": ProcessType[this.processType],
            "cvId": this.cvId
        })
    }

    public resultFromJson(json):Cv {

        let returnVal:Cv;
        console.log("*************ENTITY NAME: " + json.entityName);
        console.log(json.dtoHeaderResponse.succeeded ? "succeeded" : "error: " + json.dtoHeaderResponse.statusMessages)
        console.log(json.namesById);

        returnVal = new Cv(json.cv_id,
            json.group,
            json.term,
            json.definition,
            json.rank
        );


        return returnVal;
        //return [new NameId(1, 'foo'), new NameId(2, 'bar')];
    }


} // DtoRequestItemNameIds() 







