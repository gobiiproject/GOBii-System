export class Analysis {

    constructor(public analysisId:number,
                public analysisName:string,
                public analysisDescription:string,
                public anlaysisTypeId:number,
                public program:string,
                public programVersion:string,
                public algorithm:string,
                public sourceName:string,
                public sourceVersion:string,
                public sourceUri:string,
                public referenceId:number,
                public timeExecuted:string,
                public status:number) {

    }

}