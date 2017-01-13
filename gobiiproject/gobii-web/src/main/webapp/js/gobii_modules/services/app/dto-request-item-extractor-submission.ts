import {Injectable} from "@angular/core";
import {DtoRequestItem} from "./../core/dto-request-item";
import {ProcessType} from "../../model/type-process";
import {GobiiDataSetExtract} from  "../../model/extractor-instructions/data-set-extract"
import {GobiiExtractorInstruction} from  "../../model/extractor-instructions/gobii-extractor-instruction"
import {ExtractorInstructionFilesDTO} from  "../../model/extractor-instructions/dto-extractor-instruction-files"
import {PayloadEnvelope} from "../../model/payload/payload-envelope";


@Injectable()
export class DtoRequestItemExtractorSubmission implements DtoRequestItem<ExtractorInstructionFilesDTO> {

    public constructor(private extractorInstructionFilesDTO:ExtractorInstructionFilesDTO) {
        this.extractorInstructionFilesDTO = extractorInstructionFilesDTO;
    }

    public getUrl():string {
        return "gobii/v1/instructions/extractor";
    } // getUrl()

    private processType:ProcessType = ProcessType.CREATE;

    public getRequestBody():string {


        let rawJsonExtractorInstructionFileDTO:any = this.extractorInstructionFilesDTO.getJson();

        let payloadEnvelope = PayloadEnvelope.wrapSingleDTOInJSON(rawJsonExtractorInstructionFileDTO);

        let returnVal:string = JSON.stringify(payloadEnvelope);

        return returnVal;
    }

    public resultFromJson(json):ExtractorInstructionFilesDTO {

        let returnVal:ExtractorInstructionFilesDTO = ExtractorInstructionFilesDTO.fromJson(json.payload.data[0]);

        return returnVal;
    }


} // DtoRequestItemNameIds()


