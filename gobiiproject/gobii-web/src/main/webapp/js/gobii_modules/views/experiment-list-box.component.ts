//import {RouteParams} from '@angular/router-deprecated';
import {Component, OnInit, OnChanges, SimpleChange, EventEmitter} from "@angular/core";
import {NameId} from "../model/name-id";
import {DtoRequestService} from "../services/core/dto-request.service";
import {DtoRequestItemNameIds} from "../services/app/dto-request-item-nameids";
import {ProcessType} from "../model/type-process";
import {EntityType} from "../model/type-entity";
import {DtoRequestItemExperiment} from "../services/app/dto-request-item-experiment";
import {Experiment} from "../model/experiment"



@Component({
    selector: 'experiment-list-box',
    inputs: ['projectId','nameIdList'],
    outputs: ['onExperimentSelected','onAddMessage'],
    template: `<select name="experiment" 
                    (change)="handleExperimentSelected($event)">
                    <option *ngFor="let nameId of nameIdList " 
                    value={{nameId.id}}>{{nameId.name}}</option>
		        </select>
                <div *ngIf="experiment">
    		        <BR>
                     <fieldset>
                        <b>Name:</b> {{experiment.experimentName}}<BR>
                        <b>Platform:</b> {{experiment.platformName}}<BR>
                        <b>Code:</b> {{experiment.experimentCode}}<BR>
                      </fieldset> 
                </div>		        
` // end template

})

export class ExperimentListBoxComponent implements OnInit,OnChanges {


    // useg
    private nameIdList:NameId[];
    private projectId:string;
    private experiment:Experiment;
    private onExperimentSelected:EventEmitter<string> = new EventEmitter();
    private onAddMessage:EventEmitter<string> = new EventEmitter();

    private handleExperimentSelected(arg) {
        this.onExperimentSelected.emit(this.nameIdList[arg.srcElement.selectedIndex].id);
    }

    constructor(private _dtoRequestServiceNameIds:DtoRequestService<NameId[]>,
                private _dtoRequestServiceExperiment:DtoRequestService<Experiment>) {
    } // ctor


    private handleAddMessage(arg) {
        this.onAddMessage.emit(arg);
    }


    private setExperimentDetail(experimentId:string):void {

        let scope$ = this;
        scope$._dtoRequestServiceExperiment.getResult(new DtoRequestItemExperiment(Number(experimentId))).subscribe(experiment => {
                if (experiment) {
                    scope$.experiment = experiment
                }
            },
            dtoHeaderResponse => {
                dtoHeaderResponse.statusMessages.forEach(m => scope$.handleAddMessage("Retrieving experiment detail: " 
                + m.message))
            });
    } // setList()


    ngOnInit():any {

        //this.setList();
    }

    ngOnChanges(changes:{[propName:string]:SimpleChange}) {

        if( changes['projectId'] && changes['projectId'].currentValue ) {
            this.projectId = changes['projectId'].currentValue;
        }
        if( changes['nameIdList']  ) {
            if(changes['nameIdList'].currentValue) {
                this.nameIdList = changes['nameIdList'].currentValue;
                this.setExperimentDetail(this.nameIdList[0].id);
            }
        }
        //this.setList();
    }
}
