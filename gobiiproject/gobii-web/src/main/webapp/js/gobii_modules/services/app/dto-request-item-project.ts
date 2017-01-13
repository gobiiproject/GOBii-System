import {Injectable} from "@angular/core";
import {DtoRequestItem} from "./../core/dto-request-item";
import {ProcessType} from "../../model/type-process";
import {Project} from "../../model/project";

@Injectable()
export class DtoRequestItemProject implements DtoRequestItem<Project> {

    public constructor(private projectId:number) {
        this.projectId = projectId;
    }

    public getUrl():string {
        return "load/project";
    } // getUrl()

    private processType:ProcessType = ProcessType.READ;

    public getRequestBody():string {

        return JSON.stringify({
            "processType": ProcessType[this.processType],
            "projectId": this.projectId
        })
    }

    public resultFromJson(json):Project {

        let returnVal:Project;
        console.log("*************ENTITY NAME: " + json.entityName);
        console.log(json.dtoHeaderResponse.succeeded ? "succeeded" : "error: " + json.dtoHeaderResponse.statusMessages)
        console.log(json.namesById);

        returnVal = new Project(json.projectId,
            json.projectName,
            json.projectCode,
            json.projectDescription,
            json.piContact,
            json.createdBy,
            json.createdstring,
            json.modifiedBy,
            json.modifiedstring,
            json.projectStatus
        );

        return returnVal;
        //return [new NameId(1, 'foo'), new NameId(2, 'bar')];
    }


} // DtoRequestItemNameIds() 







