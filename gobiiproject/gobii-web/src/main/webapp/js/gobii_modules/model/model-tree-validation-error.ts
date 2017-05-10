import {FileModelNode} from "./file-model-node";

export class ModelTreeValidationError {

    constructor(public message: string,
                public fileModelNode: FileModelNode) {

    }

}