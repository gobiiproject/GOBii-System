import {Injectable} from "@angular/core";
import {DtoRequestItem} from "./../core/dto-request-item";
import {ProcessType} from "../../model/type-process";
import {GobiiDataSetExtract} from  "../../model/extractor-instructions/data-set-extract"
import {GobiiExtractorInstruction} from  "../../model/extractor-instructions/gobii-extractor-instruction"
import {ExtractorInstructionFilesDTO} from  "../../model/extractor-instructions/dto-extractor-instruction-files"


@Injectable()
export class DtoRequestItemExtractorSubmission implements DtoRequestItem<ExtractorInstructionFilesDTO> {

    public constructor(private extractorInstructionFilesDTO:ExtractorInstructionFilesDTO) {
        this.extractorInstructionFilesDTO = extractorInstructionFilesDTO;
    }

    public getUrl():string {
        return "extract/extractorInstructions";
    } // getUrl()

    private processType:ProcessType = ProcessType.CREATE;

    public getRequestBody():string {


        let rawJson:any = this.extractorInstructionFilesDTO.getJson();

        let returnVal:string = JSON.stringify(rawJson);

        return returnVal;
        }

    public resultFromJson(json):ExtractorInstructionFilesDTO {

        let returnVal:ExtractorInstructionFilesDTO = ExtractorInstructionFilesDTO.fromJson(json);
        console.log("*************ENTITY NAME: " + json.entityName);
        console.log(json.dtoHeaderResponse.succeeded ? "succeeded" : "error: " + json.dtoHeaderResponse.statusMessages)
        console.log(json.namesById);

        return returnVal;
    }


} // DtoRequestItemNameIds()


