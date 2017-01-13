//import {RouteParams} from '@angular/router-deprecated';
import {Component, OnInit, EventEmitter, SimpleChange} from "@angular/core";
import {ServerConfig} from "../model/server-config";



@Component({
    selector: 'crops-list-box',
    inputs: ['serverConfigList', 'selectedServerConfig'],
    outputs: ['onServerSelected'],
    template: `<select name="serverConfigs" (change)="handleServerSelected($event)" disabled="true">
			<option *ngFor="let serverConfig of serverConfigList" 
                    value={{serverConfig.domain}}
                    [attr.selected]="selectedServerConfig.crop
                    === serverConfig.crop ? true : null">
                    {{serverConfig.crop}}
			</option>
		</select>
` // end template

})

export class CropsListBoxComponent implements OnInit {


    // useg
    private serverConfigList:ServerConfig[];
    private selectedServerConfig:ServerConfig;

    private onServerSelected:EventEmitter<ServerConfig> = new EventEmitter();

    private handleServerSelected(arg) {
        this.onServerSelected.emit(this.serverConfigList[arg.srcElement.selectedIndex]);
    }

    constructor(){}

    ngOnInit():any {
        return null;
    }

    ngOnChanges(changes:{[propName:string]:SimpleChange}) {
        this.selectedServerConfig = changes['selectedServerConfig'].currentValue;

    }

}
