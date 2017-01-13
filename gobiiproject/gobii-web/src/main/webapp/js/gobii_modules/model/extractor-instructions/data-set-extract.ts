import {GobiiFileType} from "../type-gobii-file"

export class GobiiDataSetExtract {

    constructor(private gobiiFileType:GobiiFileType,
                private accolate:boolean,
                private dataSetId:number,
                private dataSetName:string,
                private extractDestinationDirectory:string) {

        this.setGobiiFileType(gobiiFileType);
        this.setAccolate(accolate);
        this.setDataSetName(dataSetName);
        this.setDataSetId(dataSetId);

    } // ctor 

    public getGobiiFileType():GobiiFileType {
        return this.gobiiFileType;
    }

    public  setGobiiFileType(gobiiFileType:GobiiFileType):void {
        this.gobiiFileType = gobiiFileType;
    }

    public isAccolate():boolean {
        return this.accolate;
    }

    public setAccolate(accolate:boolean):void {
        this.accolate = accolate;
    }

    public getDataSetName():string {
        return this.dataSetName;
    }

    public setDataSetName(dataSetName:string):void {
        this.dataSetName = dataSetName;
    }

    public getDataSetId():number {
        return this.dataSetId;
    }

    public setDataSetId(dataSetId:number):void {
        this.dataSetId = dataSetId;
    }

    public getExtractDestinationDirectory(): string {
        return this.extractDestinationDirectory;
    }

    public sgetExtractDestinationDirectory(extractDestinationDirectory:string ){
        this.extractDestinationDirectory = extractDestinationDirectory;
    }

    public getJson():any {

        let returnVal:any = {};

        returnVal.gobiiFileType = this.gobiiFileType;
        returnVal.accolate = this.accolate;
        returnVal.dataSetName = this.dataSetName;
        returnVal.dataSetId = this.dataSetId;
        returnVal.extractDestinationDirectory = this.extractDestinationDirectory;

        return returnVal;
    }

    public static fromJson(json:any):GobiiDataSetExtract {

        let returnVal:GobiiDataSetExtract = new GobiiDataSetExtract(json.gobiiFileType,
            json.accolate,
            json.dataSetId,
            json.dataSetName,
            json.extractDestinationDirectory);

        return returnVal;
    }
}