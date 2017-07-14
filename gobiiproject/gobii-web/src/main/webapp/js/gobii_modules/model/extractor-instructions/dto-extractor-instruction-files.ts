import {ProcessType} from "../../model/type-process";
import {GobiiExtractorInstruction} from "./gobii-extractor-instruction";

export class ExtractorInstructionFilesDTO {

    constructor(private gobiiExtractorInstructions:GobiiExtractorInstruction[],
                private instructionFileName:string) {

        this.gobiiExtractorInstructions = gobiiExtractorInstructions;
        this.instructionFileName = instructionFileName;

    } // ctor

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

        returnVal.instructionFileName = this.instructionFileName;
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
            json.instructionFileName
        );

        return returnVal;

    }
}
