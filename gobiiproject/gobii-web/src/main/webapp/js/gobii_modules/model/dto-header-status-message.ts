

import {StatusLevel} from "./type-status-level";
export class HeaderStatusMessage {

    public constructor(public message:string,
                       public statusLevel:StatusLevel,
                       public validationStatusType:string) {

    }


    public static fromJSON(json:any):HeaderStatusMessage {

        return new HeaderStatusMessage(
            json.message,
            json.statusLevel,
            json.validationStatusType
        );

    } // fromJSON

}
