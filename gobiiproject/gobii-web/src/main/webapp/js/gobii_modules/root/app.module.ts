import {NgModule} from "@angular/core";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HttpModule} from "@angular/http";
import {BrowserModule} from "@angular/platform-browser";

import {ExportFormatComponent} from "../views/export-format.component";
import {ContactsListBoxComponent} from "../views/contacts-list-box.component";
import {ProjectListBoxComponent} from "../views/project-list-box.component";
import {ExperimentListBoxComponent} from "../views/experiment-list-box.component";
import {DataSetCheckListBoxComponent} from "../views/dataset-checklist-box.component";
import {MapsetsListBoxComponent} from "../views/mapsets-list-box.component";
import {CriteriaDisplayComponent} from "../views/criteria-display.component";
import {StatusDisplayComponent} from "../views/status-display-box.component";
import {CropsListBoxComponent} from "../views/crops-list-box.component";
import {UsersListBoxComponent} from "../views/users-list-box.component";
import {ExportTypeComponent} from "../views/export-type.component";
import {DatasetTypeListBoxComponent} from "../views/dataset-types-list-box.component";
import {CheckListBoxComponent} from "../views/checklist-box.component";
import {SampleMarkerBoxComponent} from "../views/sample-marker-box.component";
import {FileSelectDirective, FileDropDirective} from "ng2-file-upload";
import {ExtractorRoot} from "./app.extractorroot";
import {DtoRequestService} from "../services/core/dto-request.service";
import {AuthenticationService} from "../services/core/authentication.service";
import {TextAreaComponent} from "../views/text-area.component";
import {UploaderComponent} from "../views/uploader.component";
import {SampleListTypeComponent} from "../views/sample-list-type.component";
import {TreeModule, SharedModule, TreeNode} from 'primeng/primeng';
import {StatusDisplayTreeComponent} from "../views/status-display-tree.component";
import {FileModelTreeService} from "../services/core/file-model-tree-service";


@NgModule({
    imports: [BrowserModule,
        HttpModule,
        FormsModule,
        ReactiveFormsModule,
        TreeModule,
        SharedModule],
    declarations: [ExtractorRoot,
        ExportFormatComponent,
        ContactsListBoxComponent,
        ProjectListBoxComponent,
        ExperimentListBoxComponent,
        DataSetCheckListBoxComponent,
        MapsetsListBoxComponent,
        CriteriaDisplayComponent,
        StatusDisplayComponent,
        CropsListBoxComponent,
        UsersListBoxComponent,
        ExportTypeComponent,
        DatasetTypeListBoxComponent,
        CheckListBoxComponent,
        SampleMarkerBoxComponent,
        FileSelectDirective,
        FileDropDirective,
        TextAreaComponent,
        UploaderComponent,
        SampleListTypeComponent,
        StatusDisplayTreeComponent],
    providers: [AuthenticationService,
        DtoRequestService,
        FileModelTreeService],
    bootstrap: [ExtractorRoot]
})

export class AppModule {
}
