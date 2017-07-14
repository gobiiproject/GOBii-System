import {Header} from "./header"
import {Payload} from "./payload"

export class PayloadEnvelope {

    public constructor(public header:Header,
                       public payload:Payload) {
    }

    public static fromJSON(json:any):PayloadEnvelope {

        let header:Header = Header.fromJSON(json.header);
        let payload:Payload = Payload.fromJSON(json.payload);

        return new PayloadEnvelope(header, payload);

    } // fromJson()


    public static wrapSingleDTOInJSON(payLoad:any):PayloadEnvelope {

        let returnVal:any = {};


        returnVal.payload = { "data" : []};
        returnVal.payload.data.push(payLoad);


        // returnVal.processType = this.processType;
        // returnVal.instructionFileName = this.instructionFileName;
        // returnVal.gobiiCropType = this.gobiiCropType;
        // returnVal.gobiiExtractorInstructions = [];
        //
        // this.gobiiExtractorInstructions.forEach(i => {
        //     returnVal.gobiiExtractorInstructions.push(i.getJson());
        // });

        return returnVal;
    }
}
