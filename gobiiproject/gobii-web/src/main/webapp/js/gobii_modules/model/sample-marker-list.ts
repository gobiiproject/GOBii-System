import {EntityFilter} from "./type-entity-filter";
export class SampleMarkerList {

    constructor(public  isArray: boolean,
                public  items: string[],
                public  uploadFileName: string) {
    }
}