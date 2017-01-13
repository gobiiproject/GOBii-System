export class DataSet {

    constructor(public  dataSetId:number,
                public  name:string,
                public  experimentId:number,
                public  callingAnalysisId:number,
                public  dataTable:string,
                public  dataFile:string,
                public  qualityTable:string,
                public  qualityFile:string,
                public  status:number,
                public  typeId:number,
                public analysesIds:number[]) {
    }
}