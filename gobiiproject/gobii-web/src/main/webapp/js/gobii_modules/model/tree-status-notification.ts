import {FileModelState} from "./file-model-tree-event";
import {DtoHeaderResponse} from "./dto-header-response";
import {ModelTreeValidationError} from "./model-tree-validation-error";
import {GobiiExtractFilterType} from "./type-extractor-filter";

export class TreeStatusNotification {

    constructor(public gobiiExractFilterType:GobiiExtractFilterType,
                public fileModelState: FileModelState,
                public modelTreeValidationErrors: ModelTreeValidationError[]) {

        if (modelTreeValidationErrors === null) {
            this.modelTreeValidationErrors = [];
        }
    }

}

