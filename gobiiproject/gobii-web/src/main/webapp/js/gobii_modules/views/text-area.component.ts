import {Component, EventEmitter} from "@angular/core";

@Component({
    selector: 'text-area',
    outputs: ['onTextboxDataComplete','onTextboxClicked'],
    template: `
        <textarea ref-textarea 
        [(ngModel)]="textValue" rows="4" style="width: 100%;"
        (click)="handleTextboxClicked($event)"></textarea><br/>
        <button (click)="handleTextboxDataComplete(textarea.value)">Add To Extract</button>
        <button (click)="textValue=''">Clear</button>
        
         <!--<h2>Log <button (click)="log=''">Clear</button></h2>-->
        <!--<pre>{{log}}</pre>-->
`
})
export class TextAreaComponent {

    private textValue;
    private log: string ='';

    private logText(value: string): void {
        this.log += `Text changed to '${value}'\n`
    }

    private onTextboxClicked:EventEmitter<any> = new EventEmitter();
    private handleTextboxClicked(arg) {
        this.onTextboxClicked.emit(arg);
    }

    private onTextboxDataComplete:EventEmitter<string[]> = new EventEmitter();
    private handleTextboxDataComplete(arg) {
        let items:string[] = arg.split("\n");
        this.onTextboxDataComplete.emit(items);
        this.textValue = '';
    }

}