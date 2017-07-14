import {GobiiFileItem} from "./gobii-file-item";
import {FileModelNode} from "./file-model-node";

export enum FileModelState {
    UNKNOWN,
    READY,
    SUBMISSION_INCOMPLETE,
    MISMATCHED_EXTRACTOR_FILTER_TYPE,
    ERROR,
    SUBMISSION_READY
}

export class FileModelTreeEvent {

    constructor(public fileItem: GobiiFileItem,
                public fileModelNode: FileModelNode,
                public fileModelState: FileModelState,
                public message: string) {

        this.fileItem = fileItem;
        this.fileModelNode = fileModelNode;
        this.fileModelState = fileModelState;

    }

}   