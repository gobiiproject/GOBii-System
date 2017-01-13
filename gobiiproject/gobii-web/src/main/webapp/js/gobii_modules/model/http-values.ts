import {Http, Response, Headers} from "@angular/http";
import {HeaderNames} from "./header-names";
import {GobiiCropType} from "./type-crop";

export class HttpValues {

    public static makeTokenHeaders(token:string): Headers {

        let returnVal = this.makeContentHeaders();
        returnVal.append(HeaderNames.headerToken, token);
        return returnVal;
    }
    
    public static makeContentHeaders(): Headers {
        let returnVal = new Headers();
        returnVal.append('Content-Type', 'application/json');
        returnVal.append('Accept', 'application/json');
        return returnVal;
    }
    
    public static makeLoginHeaders(userName:string,password): Headers {
        let returnVal: Headers = this.makeContentHeaders();
        returnVal.append(HeaderNames.headerUserName, userName);
        returnVal.append(HeaderNames.headerPassword, password);
        return returnVal;
    }
}