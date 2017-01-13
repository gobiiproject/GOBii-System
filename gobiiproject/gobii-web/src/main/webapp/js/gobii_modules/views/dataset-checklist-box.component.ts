//import {RouteParams} from '@angular/router-deprecated';
import {Component, OnInit, OnChanges, SimpleChange, EventEmitter} from "@angular/core";
import {NameId} from "../model/name-id";
import {DtoRequestService} from "../services/core/dto-request.service";
import {DtoRequestItemNameIds} from "../services/app/dto-request-item-nameids";
import {ProcessType} from "../model/type-process";
import {EntityType} from "../model/type-entity";
import {CheckBoxEvent} from "../model/event-checkbox";
import {DtoRequestItemDataSet} from "../services/app/dto-request-item-dataset";
import {DataSet} from "../model/dataset";
import {DtoRequestItemAnalysis} from "../services/app/dto-request-item-analysis";
import {Analysis} from "../model/analysis";


@Component({
    selector: 'dataset-checklist-box',
    inputs: ['experimentId', 'checkBoxEventChange'],
    outputs: ['onItemChecked', 'onAddMessage'],
    template: `<form>
                    <div style="overflow:auto; height: 80px; border: 1px solid #336699; padding-left: 5px">
                        <div *ngFor="let checkBoxEvent of checkBoxEvents" 
                            (click)=handleItemSelected($event) 
                            (hover)=handleItemHover($event)>
                            <input  type="checkbox" 
                                (click)=handleItemChecked($event)
                                [checked]="checkBoxEvent.checked"
                                value={{checkBoxEvent.id}} 
                                name="{{checkBoxEvent.name}}">&nbsp;{{checkBoxEvent.name}}
                        </div>            
                    </div>
                </form>
                <div *ngIf="dataSet">
                    <BR>
                     <fieldset>
                        <b>Name:</b> {{dataSet.name}}<BR>
                        <b>Data Table:</b> {{dataSet.dataTable}}<BR>
                        <b>Data File:</b> {{dataSet.dataFile}}<BR>
                        <b>Quality Table:</b> {{dataSet.qualityTable}}<BR>
                        <b>Quality File:</b> {{dataSet.qualityFile}}<BR>
                        <div *ngIf="analysisNames && (analysisNames.length > 0)">
                            <b>Analyses:</b> <ul style="list-style-type:none">
                                            <li *ngFor= "let analysisName of analysisNames" >{{analysisName}}</li>
                                    </ul>
                        </div>
                        <div *ngIf="analysisTypes && (analysisTypes.length > 0)">
                            <b>Analysis Types:</b> <ul style="list-style-type:none">
                                            <li *ngFor= "let analysisType of analysisTypes" >{{analysisType}}</li>
                                    </ul>
                        </div>
                      </fieldset> 
                </div>                
` // end template

})


export class DataSetCheckListBoxComponent implements OnInit,OnChanges {

    constructor(private _dtoRequestServiceNameId:DtoRequestService<NameId[]>,
                private _dtoRequestServiceDataSetDetail:DtoRequestService<DataSet>,
                private _dtoRequestServiceAnalysisDetail:DtoRequestService<Analysis>) {

    } // ctor

    // useg
    private nameIdList:NameId[];
    private checkBoxEvents:CheckBoxEvent[] = [];
    private experimentId:string;
    private onItemChecked:EventEmitter<CheckBoxEvent> = new EventEmitter();
    private onAddMessage:EventEmitter<string> = new EventEmitter();
    private dataSet:DataSet;
    private analysisNames:string[] = [];
    private analysisTypes:string[] = [];
    private nameIdListAnalysisTypes:NameId[];

    private handleItemChecked(arg) {

        let itemToChange:CheckBoxEvent =
            this.checkBoxEvents.filter(e => {
                return e.id == arg.currentTarget.value;
            })[0];

        //let indexOfItemToChange:number = this.checkBoxEvents.indexOf(arg.currentTarget.name);
        itemToChange.processType = arg.currentTarget.checked ? ProcessType.CREATE : ProcessType.DELETE;
        itemToChange.checked = arg.currentTarget.checked;
        this.onItemChecked.emit(itemToChange);

    } // handleItemChecked()

    private handleAddMessage(arg) {
        this.onAddMessage.emit(arg);
    }

    private previousSelectedItem:any;

    private handleItemSelected(arg) {
        let selectedDataSetId:number = Number(arg.currentTarget.children[0].value);
        if (this.previousSelectedItem) {
            this.previousSelectedItem.style = ""
        }
        arg.currentTarget.style = "background-color:#b3d9ff";
        this.previousSelectedItem = arg.currentTarget;
        this.setDatasetDetails(selectedDataSetId);
    }

    private setList():void {

        // we can get this event whenver the item is clicked, not necessarily when the checkbox
        let scope$ = this;

        scope$.nameIdList = [];
        scope$.checkBoxEvents = [];
        scope$.setDatasetDetails(undefined);

        if( scope$.experimentId) {

            this._dtoRequestServiceNameId.getResult(new DtoRequestItemNameIds(ProcessType.READ,
                EntityType.DataSetNamesByExperimentId,
                this.experimentId)).subscribe(nameIds => {
                    if (nameIds && ( nameIds.length > 0 )) {

                        scope$.nameIdList = nameIds;
                        scope$.checkBoxEvents = [];
                        scope$.nameIdList.forEach(n => {
                            scope$.checkBoxEvents.push(new CheckBoxEvent(
                                ProcessType.CREATE,
                                n.id,
                                n.name,
                                false
                            ));
                        });

                        scope$.setDatasetDetails(scope$.nameIdList[0].id);

                    } else {
                        scope$.nameIdList = [new NameId(0, "<none>")];
                        scope$.setDatasetDetails(undefined);
                    }
                },
                dtoHeaderResponse => {
                    dtoHeaderResponse.statusMessages.forEach(m => scope$.handleAddMessage("Retrieving dataset names by experiment id: "
                        + ": "
                        + m.message))
                });

        } // if we have an experiment id

    } // setList()

    private setDatasetDetails(dataSetId:number):void {

        if( dataSetId ) {
            let scope$ = this;
            scope$._dtoRequestServiceDataSetDetail.getResult(new DtoRequestItemDataSet(dataSetId))
                .subscribe(dataSet => {

                        if (dataSet) {

                            scope$.dataSet = dataSet;
                            scope$.analysisNames = [];
                            scope$.analysisTypes = [];

                            scope$.dataSet.analysesIds.forEach(
                                analysisId => {
                                    let currentAnalysisId:number = analysisId;
                                    if (currentAnalysisId) {
                                        scope$._dtoRequestServiceAnalysisDetail
                                            .getResult(new DtoRequestItemAnalysis(currentAnalysisId))
                                            .subscribe(analysis => {
                                                    scope$.analysisNames.push(analysis.analysisName);
                                                    if (analysis.anlaysisTypeId && scope$.nameIdListAnalysisTypes) {

                                                        scope$
                                                            .nameIdListAnalysisTypes
                                                            .forEach(t => {
                                                                if (Number(t.id) === analysis.anlaysisTypeId) {
                                                                    scope$.analysisTypes.push(t.name);
                                                                }
                                                            });


                                                    } // if we have an analysis type id
                                                },
                                                dtoHeaderResponse => {
                                                    dtoHeaderResponse.statusMessages.forEach(m => scope$.handleAddMessage(m.message));
                                                });
                                    }
                                }
                            );
                        }
                    },
                    dtoHeaderResponse => {
                        dtoHeaderResponse.statusMessages.forEach(m => scope$.handleAddMessage(m.message));
                    });
        } else {

            this.dataSet = undefined;
            this.analysisNames = [];
            this.analysisTypes = [];

        } // if else we got a dataset id
    } // setList()


    ngOnInit():any {

        let scope$ = this;
        scope$._dtoRequestServiceNameId
            .getResult(new DtoRequestItemNameIds(ProcessType.READ, EntityType.CvGroupTerms, "analysis_type"))
            .subscribe(nameIdList => {
                    scope$.nameIdListAnalysisTypes = nameIdList;
                    if (this.experimentId) {
                        this.setList();
                    }
                },
                dtoHeaderResponse => {
                    dtoHeaderResponse.statusMessages.forEach(m => scope$.handleAddMessage(m.message));
                });

    }

    private itemChangedEvent:CheckBoxEvent;

    ngOnChanges(changes:{[propName:string]:SimpleChange}) {

        if (changes['experimentId']) {
            this.experimentId = changes['experimentId'].currentValue;
            this.setList();
        }

        if (changes['checkBoxEventChange'] && changes['checkBoxEventChange'].currentValue) {

            this.itemChangedEvent = changes['checkBoxEventChange'].currentValue;

            if( this.itemChangedEvent  ) {
                let itemToChange:CheckBoxEvent =
                    this.checkBoxEvents.filter(e => {
                        return e.id == changes['checkBoxEventChange'].currentValue.id;
                    })[0];

                //let indexOfItemToChange:number = this.checkBoxEvents.indexOf(arg.currentTarget.name);
                if(itemToChange) {
                    itemToChange.processType = changes['checkBoxEventChange'].currentValue.processType;
                    itemToChange.checked = changes['checkBoxEventChange'].currentValue.checked;
                }
            }
        }
    }
}
