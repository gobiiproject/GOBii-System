import {HeaderStatusMessage} from "./../dto-header-status-message"
import {DtoHeaderAuth} from "../dto-header-auth";
import {Status} from "./status"

export class Header {

    public constructor(public cropType:string,
                       public dtoHeaderAuth:DtoHeaderAuth,
                       public status:Status) {
    }

    public static fromJSON(json:any):Header {

        let cropType:string = json.cropType;
        let dtoHeaderAuth:DtoHeaderAuth = DtoHeaderAuth.fromJSON(json.dtoHeaderAuth);
        let status:Status = Status.fromJSON(json.status);

        return new Header(cropType,
            dtoHeaderAuth,
            status); // new

    } // fromJson()
}
