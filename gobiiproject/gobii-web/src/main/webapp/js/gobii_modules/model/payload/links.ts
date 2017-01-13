import {Link} from "./link"

export class Links {

    public constructor(public exploreLinksPerDataItem:Link[],
                       public linksPerDataItem:Link[]) {
    }

    public static fromJSON(json:any):Links {

        let exploreLinksPerDataItem:Link[] = [];
        json.exploreLinksPerDataItem.forEach(l => {
            exploreLinksPerDataItem.push(Link.fromJSON(l))
        })

        let linksPerDataItem:Link[] = [];
        json.linksPerDataItem.forEach(l => {
            linksPerDataItem.push(Link.fromJSON(l))
        });

        return new Links(exploreLinksPerDataItem,
            linksPerDataItem); // new

    } // fromJson()
}
