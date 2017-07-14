import {NgModule} from "@angular/core";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HttpModule} from "@angular/http";
import {BrowserModule} from "@angular/platform-browser";

import {ExportFormatComponent} from "../views/export-format.component";
import {ProjectListBoxComponent} from "../views/project-list-box.component";
import {DataSetCheckListBoxComponent} from "../views/dataset-checklist-box.component";
import {StatusDisplayComponent} from "../views/status-display-box.component";
import {CropsListBoxComponent} from "../views/crops-list-box.component";
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
import {TreeModule, SharedModule, TreeNode, Dialog} from 'primeng/primeng';
import {StatusDisplayTreeComponent} from "../views/status-display-tree.component";
import {FileModelTreeService} from "../services/core/file-model-tree-service";
import {NameIdListBoxComponent} from "../views/name-id-list-box.component";
import {NameIdService} from "../services/core/name-id-service";
import {AppComponent} from "./app.component";
import {LoginComponent} from "../views/login.component";
import {routing} from "./app.routing";
import {APP_BASE_HREF} from "@angular/common";
import {AuthGuard} from "../services/core/auth.guard";
import {Button} from "primeng/components/button/button";

@NgModule({
    imports: [BrowserModule,
        HttpModule,
        FormsModule,
        ReactiveFormsModule,
        TreeModule,
        SharedModule,
        routing],
    declarations: [
        AppComponent,
        ExtractorRoot,
        LoginComponent,
        ExportFormatComponent,
        ProjectListBoxComponent,
        DataSetCheckListBoxComponent,
        StatusDisplayComponent,
        CropsListBoxComponent,
        ExportTypeComponent,
        DatasetTypeListBoxComponent,
        CheckListBoxComponent,
        SampleMarkerBoxComponent,
        FileSelectDirective,
        FileDropDirective,
        TextAreaComponent,
        UploaderComponent,
        SampleListTypeComponent,
        NameIdListBoxComponent,
        StatusDisplayTreeComponent,
        Dialog,
        Button],
    providers: [AuthGuard,
        AuthenticationService,
        DtoRequestService,
        FileModelTreeService,
        NameIdService,
        {provide: APP_BASE_HREF, useValue: './'}],
    bootstrap: [AppComponent]
})

export class AppModule {
}
