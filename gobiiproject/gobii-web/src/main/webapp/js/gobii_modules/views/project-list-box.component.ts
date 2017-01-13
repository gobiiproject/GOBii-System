//import {RouteParams} from '@angular/router-deprecated';
import {Component, OnInit, OnChanges, SimpleChange, EventEmitter} from "@angular/core";
import {NameId} from "../model/name-id";
import {DtoRequestService} from "../services/core/dto-request.service";
import {Project} from "../model/project";
import {DtoRequestItemProject} from "../services/app/dto-request-item-project";

@Component({
    selector: 'project-list-box',
    inputs: ['primaryInvestigatorId', 'nameIdList','nameIdListPIs'],
    outputs: ['onProjectSelected', 'onAddMessage'],
    template: `<select name="projects" 
                    (change)="handleProjectSelected($event)">
                    <option *ngFor="let nameId of nameIdList " 
                    value={{nameId.id}}>{{nameId.name}}</option>
		        </select>
                <div *ngIf="project">
                    <BR>
                     <fieldset class="form-group">
                        <b>Name:</b> {{project.projectName}}<BR>
                        <b>Description:</b> {{project.projectDescription}}<BR>
                        <b>Principle Investigator:</b> {{primaryInvestigatorName}}
                      </fieldset> 
                </div>		        
` // end template

})

export class ProjectListBoxComponent implements OnInit,OnChanges {


    // useg    privatre
    private project:Project;
    private nameIdList:NameId[];
    private nameIdListPIs:NameId[];
    private primaryInvestigatorId:string;
    private primaryInvestigatorName:string;
    private onProjectSelected:EventEmitter<string> = new EventEmitter();
    private onAddMessage:EventEmitter<string> = new EventEmitter();

    private handleProjectSelected(arg) {
        let selectedProjectId = this.nameIdList[arg.srcElement.selectedIndex].id;
        this.setProjectDetails(selectedProjectId);
        this.onProjectSelected.emit(selectedProjectId);
    }

    private handleAddMessage(arg) {
        this.onAddMessage.emit(arg);
    }


    constructor(private _dtoRequestServiceProject:DtoRequestService<Project>) {


    } // ctor

    private setProjectDetails(projectId:string):void {
        let scope$ = this;
        this._dtoRequestServiceProject.getResult(new DtoRequestItemProject(Number(projectId)))
            .subscribe(project => {
                    if (project) {
                        scope$.project = project;
                        scope$.primaryInvestigatorId = String(project.piContact);
                        scope$.setPiName();
                    }
                },
                dtoHeaderResponse => {
                    dtoHeaderResponse.statusMessages.forEach(m => scope$.handleAddMessage(
                        "Retrieving project detail: " 
                        + m.message))
                });
    }

    ngOnInit():any {

        //this.setList();
    }

    private setPiName() {

        this.primaryInvestigatorName = undefined;
        if( this.primaryInvestigatorId && this.nameIdListPIs) {
            this.nameIdListPIs.forEach(n => {
                if(n.id === this.primaryInvestigatorId) {
                    this.primaryInvestigatorName = n.name;

                }
            })
        }
    }

    ngOnChanges(changes:{[propName:string]:SimpleChange}) {

        if (changes['primaryInvestigatorId'] && changes['primaryInvestigatorId'].currentValue) {
            this.primaryInvestigatorId = changes['primaryInvestigatorId'].currentValue;
            
        }

        if (changes['nameIdList']) {
            if (changes['nameIdList'].currentValue) {
                this.nameIdList = changes['nameIdList'].currentValue;
                this.setProjectDetails(this.nameIdList[0].id);
            }
        }

        if (changes['nameIdListPIs']) {
            if (changes['nameIdListPIs'].currentValue) {
                this.nameIdListPIs = changes['nameIdListPIs'].currentValue;
            }
        }

        //

    }
}
