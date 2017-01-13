import {ProcessType} from "../../model/type-process";
import {GobiiExtractorInstruction} from "./gobii-extractor-instruction";
import {GobiiCropType} from "../type-crop";

export class ExtractorInstructionFilesDTO {

    constructor(private gobiiExtractorInstructions:GobiiExtractorInstruction[],
                private instructionFileName:string,
                private processType:ProcessType,
                private gobiiCropType:GobiiCropType) {

        this.gobiiExtractorInstructions = gobiiExtractorInstructions;
        this.instructionFileName = instructionFileName;
        this.processType = processType;
        this.gobiiCropType = gobiiCropType;

    } // ctor

    public getProcessType():ProcessType {
        return this.processType;
    }

    public getGobiiCropType():GobiiCropType {
        return this.gobiiCropType;
    }

    public setProcessType(value:ProcessType) {
        this.processType = value;
    }

    public getGobiiExtractorInstructions():any {
        return this.gobiiExtractorInstructions;
    }

    public setGobiiExtractorInstructions(value:any) {
        this.gobiiExtractorInstructions = value;
    }

    public getInstructionFileName():string {
        return this.instructionFileName;
    }

    public setInstructionFileName(value:string) {
        this.instructionFileName = value;
    }


    public getJson():any {

        let returnVal:any = {};

        returnVal.processType = this.processType;
        returnVal.instructionFileName = this.instructionFileName;
        returnVal.gobiiCropType = this.gobiiCropType;
        returnVal.gobiiExtractorInstructions = [];

        this.gobiiExtractorInstructions.forEach(i => {
            returnVal.gobiiExtractorInstructions.push(i.getJson());
        });

        return returnVal;

    } // getJson()

    public static fromJson(json:any):ExtractorInstructionFilesDTO {

        let gobiiExtractorInstructions:GobiiExtractorInstruction[] = [];

        json.gobiiExtractorInstructions.forEach(i =>
            gobiiExtractorInstructions.push(GobiiExtractorInstruction.fromJson(i)));

        let returnVal:ExtractorInstructionFilesDTO = new ExtractorInstructionFilesDTO(
            gobiiExtractorInstructions,
            json.instructionFileName,
            json.processType,
            json.gobiiCropType
        );

        return returnVal;

    }
}
