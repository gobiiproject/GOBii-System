import {GobiiExtractFilterType} from "./type-extractor-filter";
export class FileName {

    public static makeUniqueFileId(): string {

        let date: Date = new Date();
        let returnVal: string = date.getFullYear()
            + "_"
            + ('0' + (date.getMonth() + 1)).slice(-2)
            + "_"
            + ( '0' + date.getDate()).slice(-2)
            + "_"
            + ( '0' + date.getHours()).slice(-2)
            + "_"
            + ('0' + date.getMinutes()).slice(-2)
            + "_"
            + ('0' + date.getSeconds()).slice(-2);


        return returnVal;

    };

    public static makeFileNameFromJobId(gobiiExtractFilterType: GobiiExtractFilterType, jobId: string): string {

        let returnVal: string;

        let suffix = null;

        if (gobiiExtractFilterType === GobiiExtractFilterType.BY_MARKER) {
            suffix = "_markers";
        } else {
            suffix = "_samples";
        }

        returnVal = jobId + suffix + ".txt";

        return returnVal;
    }
}   