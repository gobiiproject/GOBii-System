import {RestMethod} from "./../type-rest-method"
import {DtoHeaderAuth} from "../dto-header-auth";
import {Status} from "./status"

export class Link {

    public constructor(public href:string,
                       public description:string,
                       public allowedMethods:RestMethod[]) {
    }

    public static fromJSON(json:any):Link {

       let href:string = json.href;
        let description:string = json.description;
        let allowedMethods:RestMethod[] = [];
        json.methods.forEach(m => {allowedMethods.push(m)})

        return new Link(href,
            description,
            allowedMethods); 

    } // fromJson()
}
