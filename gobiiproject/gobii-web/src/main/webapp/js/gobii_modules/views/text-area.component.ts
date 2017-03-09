import {Component, EventEmitter} from "@angular/core";

@Component({
    selector: 'text-area',
    outputs: ['onTextboxDataComplete'],
    template: `
        <textarea ref-textarea [(ngModel)]="textValue" rows="4" style="width: 100%;"></textarea><br/>
        <button (click)="handleTextboxDataComplete(textarea.value)">Add To Extract</button>
        <button (click)="textValue=''">Clear</button>
        
         <!--<h2>Log <button (click)="log=''">Clear</button></h2>-->
        <!--<pre>{{log}}</pre>-->
`
})
export class TextAreaComponent {

    private textValue = "initial value";
    private log: string ='';

    private logText(value: string): void {
        this.log += `Text changed to '${value}'\n`
    }

    private onTextboxDataComplete:EventEmitter<string[]> = new EventEmitter();
    private handleTextboxDataComplete(arg) {
        let items:string[] = arg.split("\n");
        this.onTextboxDataComplete.emit(items);
    }

}