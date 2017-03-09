export enum CvFilterType {UKNOWN,
ANALYSIS_TYPE,
DATASET_TYPE}
;

export class CvFilters {


    private static cvValues: Map<CvFilterType,string> = null;

    public static get(cvFilterType:CvFilterType): string {

        if ( this.cvValues === null ) {
            this.cvValues = new Map<CvFilterType,string>();
            this.cvValues[CvFilterType.ANALYSIS_TYPE] = "analysis_type";
            this.cvValues[CvFilterType.DATASET_TYPE] = "dataset_type";
        }

        return this.cvValues[cvFilterType];
    }

}

