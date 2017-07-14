import {GobiiDataSetExtract} from "./data-set-extract"

export class GobiiExtractorInstruction {

    constructor(private dataSetExtracts:GobiiDataSetExtract[],
                private contactId:number,
                private contactEmail:string,
                private mapsetIds:number[]) {

        this.dataSetExtracts = dataSetExtracts;
    }

    public getDataSetExtracts():any {
        return this.dataSetExtracts;
    }

    public setDataSetExtracts(value:any) {
        this.dataSetExtracts = value;
    }

    public getContactId():Number {
        return this.contactId;
    }

    public setContactId(contactId:number):void {
        this.contactId = contactId;
    }

    public setContactEmail(contactEmail:string):void {
        this.contactEmail = contactEmail;
    }

    public getContactEmail():string {
        return this.contactEmail;
    }

    public setMapsetIds(mapsetIds:number[]) {
        this.mapsetIds = mapsetIds;
    }


    public getMapsetIds() {
        return this.mapsetIds;
    }

    public getJson():any {

        let returnVal:any = {};

        returnVal.contactId = this.contactId;
        returnVal.contactEmail = this.contactEmail;

        returnVal.mapsetIds = this.mapsetIds;

        returnVal.dataSetExtracts = [];
        this.dataSetExtracts.forEach(e => {
            returnVal.dataSetExtracts.push(e.getJson());
        });

        return returnVal;
    }

    public static fromJson(json:any):GobiiExtractorInstruction {

        let dataSetExtracts:GobiiDataSetExtract[] = [];


        json.dataSetExtracts.forEach(e => dataSetExtracts.push(GobiiDataSetExtract.fromJson(e)));

        let returnVal:GobiiExtractorInstruction = new GobiiExtractorInstruction(
            dataSetExtracts,
            json.contactId,
            json.contactEmail,
            json.mapsetIds
        );

        return returnVal;

    }


}