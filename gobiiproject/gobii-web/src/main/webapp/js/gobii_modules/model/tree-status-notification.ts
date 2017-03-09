import {FileModelState} from "./file-model-tree-event";
import {DtoHeaderResponse} from "./dto-header-response";

export class TreeStatusNotification {

    constructor(public fileModelState: FileModelState,
                public dtoHeaderResponse: DtoHeaderResponse) {

    }

}

