import {ProcessType} from "./type-process";

export class CheckBoxEvent {
    constructor(public processType:ProcessType,
                public id:string,
                public name:string,
                public checked:boolean) {

        this.processType = processType;
        this.id = id;
        this.name = name;
    }
} // CheckBoxEvent()
