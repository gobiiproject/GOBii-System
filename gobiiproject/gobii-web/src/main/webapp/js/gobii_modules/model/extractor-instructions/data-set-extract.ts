import {GobiiFileType} from "../type-gobii-file"
import {GobiiExtractFilterType} from "../type-extractor-filter";
import {GobiiSampleListType} from "../type-extractor-sample-list";
import {NameId} from "../name-id";

export class GobiiDataSetExtract {

    public constructor(public gobiiFileType: GobiiFileType,
                       public accolate: boolean,
                       public extractDestinationDirectory: string,
                       public gobiiExtractFilterType: GobiiExtractFilterType,
                       public markerList: string[],
                       public sampleList: string[],
                       public listFileName: string,
                       public gobiiSampleListType: GobiiSampleListType,
                       public gobiiDatasetType: NameId,
                       public platformIds: number[],
                       public principleInvestigator: NameId,
                       public  project: NameId,
                       public dataSet: NameId) {

        // this.setGobiiFileType(gobiiFileType);
        // this.setAccolate(accolate);
        // this.setDataSetId(dataSetId);
        // this.setDataSetName(dataSetName);
        // this.setExtractDestinationDirectory(extractDestinationDirectory);
        // this.setGobiiFileType(gobiiExtractFilterType);
        //

    } // ctor 


    public getgobiiFileType(): GobiiFileType {
        return this.gobiiFileType;
    }

    public setgobiiFileType(value: GobiiFileType) {
        this.gobiiFileType = value;
    }

    public getaccolate(): boolean {
        return this.accolate;
    }

    public setaccolate(value: boolean) {
        this.accolate = value;
    }

    public getextractDestinationDirectory(): string {
        return this.extractDestinationDirectory;
    }

    public setextractDestinationDirectory(value: string) {
        this.extractDestinationDirectory = value;
    }

    public getgobiiExtractFilterType(): GobiiExtractFilterType {
        return this.gobiiExtractFilterType;
    }

    public setgobiiExtractFilterType(value: GobiiExtractFilterType) {
        this.gobiiExtractFilterType = value;
    }

    public getmarkerList(): string[] {
        return this.markerList;
    }

    public setmarkerList(value: string[]) {
        this.markerList = value;
    }

    public getsampleList(): string[] {
        return this.sampleList;
    }

    public setsampleList(value: string[]) {
        this.sampleList = value;
    }

    public getlistFileName(): string {
        return this.listFileName;
    }

    public setlistFileName(value: string) {
        this.listFileName = value;
    }

    public getgobiiSampleListType(): GobiiSampleListType {
        return this.gobiiSampleListType;
    }

    public setgobiiSampleListType(value: GobiiSampleListType) {
        this.gobiiSampleListType = value;
    }

    public getgobiiDatasetType(): NameId {
        return this.gobiiDatasetType;
    }

    public setgobiiDatasetType(value: NameId) {
        this.gobiiDatasetType = value;
    }

    public getplatformIds(): number[] {
        return this.platformIds;
    }

    public setplatformIds(value: number[]) {
        this.platformIds = value;
    }


    public getJson(): any {

        let returnVal: any = {};

        returnVal.gobiiFileType = this.gobiiFileType;
        returnVal.accolate = this.accolate;
        returnVal.extractDestinationDirectory = this.extractDestinationDirectory;
        returnVal.gobiiExtractFilterType = this.gobiiExtractFilterType;
        returnVal.markerList = this.markerList;
        returnVal.sampleList = this.sampleList;
        returnVal.listFileName = this.listFileName;
        returnVal.gobiiSampleListType = this.gobiiSampleListType;
        returnVal.gobiiDatasetType = this.gobiiDatasetType;
        returnVal.platformIds = this.platformIds;
        returnVal.principleInvestigator = this.principleInvestigator;
        returnVal.project = this.project;
        returnVal.dataSet = this.dataSet;

        return returnVal;
    }

    public static fromJson(json: any): GobiiDataSetExtract {

        let returnVal: GobiiDataSetExtract =
            new GobiiDataSetExtract(
                json.gobiiFileType,
                json.accolate,
                json.extractDestinationDirectory,
                json.gobiiExtractFilterType,
                json.markerList,
                json.sampleList,
                json.listFileName,
                json.gobiiSampleListType,
                json.gobiiDatasetType,
                json.platformIds,
                json.principleInvestigator,
                json.project,
                json.dataSet);

        return returnVal;
    }
}