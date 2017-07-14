import {Links} from "./links"

export class Payload {

    public constructor(public data:Object[],
                       public links:Links) {
    }

    public static fromJSON(json:any):Payload {


        let data:Object[] = [];
        json.data.forEach(d => {
            data.push(d)
        })

        let links:Links = Links.fromJSON(json.linkCollection);

        return new Payload(data,
            links); // new

    } // fromJson()
}
