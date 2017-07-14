import {HeaderStatusMessage} from "./../dto-header-status-message"

export class Status {

    public constructor(public succeeded:boolean,
                       public statusMessages:HeaderStatusMessage[]) {}

    public static fromJSON(json:any):Status {

        let succeeded:boolean = json.succeeded;

        let statusMessages:HeaderStatusMessage[] = [];
        json.statusMessages.forEach(m => {
            statusMessages.push(HeaderStatusMessage.fromJSON(m));
        })

        return new Status(
            succeeded,
            statusMessages
        ); // new
        
    } // fromJson()
}
