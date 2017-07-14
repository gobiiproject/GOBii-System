import {Injectable} from "@angular/core";
import {DtoRequestItem} from "./../core/dto-request-item";
import {ProcessType} from "../../model/type-process";
import {DataSet} from "../../model/dataset";
import {Contact} from "../../model/contact";

export enum ContactSearchType {
    UNKNOWN,
    BY_EMAIL,
    BY_USERNAME
}


@Injectable()
export class DtoRequestItemContact implements DtoRequestItem<Contact> {


    public constructor(private contactSearchType: ContactSearchType,
                       private contactSearchTerm: string) {
        this.contactSearchType = contactSearchType;
        this.contactSearchTerm = contactSearchTerm;
    }


    public getUrl(): string {

        let returnVal: string = "gobii/v1/contact-search";

        if (this.contactSearchType === ContactSearchType.BY_EMAIL) {

            returnVal += "?email=" + this.contactSearchTerm + "&lastName&firstName&userName";

        } else if (this.contactSearchType === ContactSearchType.BY_USERNAME) {
            returnVal += "?email&lastName&firstName&userName=" + this.contactSearchTerm;
        } else {
            // This should be a valid request, and the web server will give a meaningful
            // error that no search term was specified
            returnVal += "?email&lastName&firstName&userName";
        }

        return returnVal;

    } // getUrl()

    private processType: ProcessType = ProcessType.READ;

    public getRequestBody(): string {

        // return JSON.stringify({
        //     "processType": ProcessType[this.processType],
        //     "dataSetId": this.dataSetId
        // })

        // for now we are only doing a GET so we should not yet need a body
        return undefined;
    }

    public resultFromJson(json): Contact {

        let returnVal: Contact;

        if (json.payload.data[0]) {
            returnVal = new Contact(json.payload.data[0].contactId,
                json.payload.data[0].lastName,
                json.payload.data[0].firstName,
                json.payload.data[0].code,
                json.payload.data[0].email,
                //    json.payload.data[0].List<Integer> roles = new ArrayList<>(),
                //    json.payload.data[0].Integer createdBy,
                //    json.payload.data[0].Date createdDate,
                //    json.payload.data[0].Integer modifiedBy,
                //    json.payload.data[0].Date modifiedDate,
                json.payload.data[0].organizationId,
                json.payload.data[0].userName);
        }

        return returnVal;
    }


} //







