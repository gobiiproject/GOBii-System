//import {RouteParams} from '@angular/router-deprecated';
import {Component, OnInit, SimpleChange} from "@angular/core";
import {GobiiDataSetExtract} from "../model/extractor-instructions/data-set-extract";


@Component({
    selector: 'status-display',
    inputs: ['messages'],
    //directives: [RADIO_GROUP_DIRECTIVES]
    template: `<div style="overflow:auto; height: 240px; border: 1px solid #336699; padding-left: 5px;">
                    <ol>
                    <li *ngFor="let message of messages">{{message}}</li>
                    </ol>
                </div>
` // end template

})

export class StatusDisplayComponent implements OnInit {


    // useg
    private messages:string[] = [];
    constructor() {
    } // ctor


    ngOnInit():any {
        return null;
    }

    ngOnChanges(changes:{[propName:string]:SimpleChange}) {
        this.messages= changes['messages'].currentValue;
    }

}
