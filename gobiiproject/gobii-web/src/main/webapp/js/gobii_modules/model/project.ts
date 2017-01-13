export class Project {
    
    constructor(public  projectId:number,
                public  projectName:string,
                public  projectCode:string,
                public  projectDescription:string,
                public  piContact:number,
                public   createdBy:number,
                public   createdstring:string,
                public   modifiedBy:number,
                public   modifiedstring:string,
                public   projectStatus:number) {
    }
}