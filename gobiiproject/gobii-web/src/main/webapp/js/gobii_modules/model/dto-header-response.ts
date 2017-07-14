import {HeaderStatusMessage} from "./dto-header-status-message"

export class DtoHeaderResponse {

    public constructor(public succeeded: boolean,
                       public statusMessages: HeaderStatusMessage[]) {
    }

    public static fromJSON(json: any): DtoHeaderResponse {

        let statusMessages: HeaderStatusMessage[] = [];
        json.dtoHeaderResponse.statusMessages.forEach(m => {
            statusMessages.push(HeaderStatusMessage.fromJSON(m));
        })

        return new DtoHeaderResponse(
            json.dtoHeaderResponse.succeeded,
            statusMessages
        ); // new

    } // fromJson()
}
