export class Experiment {

    constructor(public experimentId:number,
                public experimentName:string,
                public experimentCode:string,
                public experimentDataFile:string,
                public projectId:number,
                public platformId:number,
                public manifestId:number,
                public createdBy:number,
                public createdstring:string,
                public modifiedBy:number,
                public modifiedstring:string,
                public status:number,
                public platformName) {
    }
}