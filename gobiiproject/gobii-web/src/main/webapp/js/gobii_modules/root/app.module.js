System.register(["@angular/core", "@angular/forms", "@angular/http", "@angular/platform-browser", "../views/export-format.component", "../views/project-list-box.component", "../views/dataset-checklist-box.component", "../views/status-display-box.component", "../views/crops-list-box.component", "../views/export-type.component", "../views/dataset-types-list-box.component", "../views/checklist-box.component", "../views/sample-marker-box.component", "ng2-file-upload", "./app.extractorroot", "../services/core/dto-request.service", "../services/core/authentication.service", "../views/text-area.component", "../views/uploader.component", "../views/sample-list-type.component", "primeng/primeng", "../views/status-display-tree.component", "../services/core/file-model-tree-service", "../views/name-id-list-box.component", "../services/core/name-id-service", "./app.component", "../views/login.component", "./app.routing", "@angular/common", "../services/core/auth.guard", "primeng/components/button/button"], function (exports_1, context_1) {
    "use strict";
    var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
        var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
        if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
        else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
        return c > 3 && r && Object.defineProperty(target, key, r), r;
    };
    var __moduleName = context_1 && context_1.id;
    var core_1, forms_1, http_1, platform_browser_1, export_format_component_1, project_list_box_component_1, dataset_checklist_box_component_1, status_display_box_component_1, crops_list_box_component_1, export_type_component_1, dataset_types_list_box_component_1, checklist_box_component_1, sample_marker_box_component_1, ng2_file_upload_1, app_extractorroot_1, dto_request_service_1, authentication_service_1, text_area_component_1, uploader_component_1, sample_list_type_component_1, primeng_1, status_display_tree_component_1, file_model_tree_service_1, name_id_list_box_component_1, name_id_service_1, app_component_1, login_component_1, app_routing_1, common_1, auth_guard_1, button_1, AppModule;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (forms_1_1) {
                forms_1 = forms_1_1;
            },
            function (http_1_1) {
                http_1 = http_1_1;
            },
            function (platform_browser_1_1) {
                platform_browser_1 = platform_browser_1_1;
            },
            function (export_format_component_1_1) {
                export_format_component_1 = export_format_component_1_1;
            },
            function (project_list_box_component_1_1) {
                project_list_box_component_1 = project_list_box_component_1_1;
            },
            function (dataset_checklist_box_component_1_1) {
                dataset_checklist_box_component_1 = dataset_checklist_box_component_1_1;
            },
            function (status_display_box_component_1_1) {
                status_display_box_component_1 = status_display_box_component_1_1;
            },
            function (crops_list_box_component_1_1) {
                crops_list_box_component_1 = crops_list_box_component_1_1;
            },
            function (export_type_component_1_1) {
                export_type_component_1 = export_type_component_1_1;
            },
            function (dataset_types_list_box_component_1_1) {
                dataset_types_list_box_component_1 = dataset_types_list_box_component_1_1;
            },
            function (checklist_box_component_1_1) {
                checklist_box_component_1 = checklist_box_component_1_1;
            },
            function (sample_marker_box_component_1_1) {
                sample_marker_box_component_1 = sample_marker_box_component_1_1;
            },
            function (ng2_file_upload_1_1) {
                ng2_file_upload_1 = ng2_file_upload_1_1;
            },
            function (app_extractorroot_1_1) {
                app_extractorroot_1 = app_extractorroot_1_1;
            },
            function (dto_request_service_1_1) {
                dto_request_service_1 = dto_request_service_1_1;
            },
            function (authentication_service_1_1) {
                authentication_service_1 = authentication_service_1_1;
            },
            function (text_area_component_1_1) {
                text_area_component_1 = text_area_component_1_1;
            },
            function (uploader_component_1_1) {
                uploader_component_1 = uploader_component_1_1;
            },
            function (sample_list_type_component_1_1) {
                sample_list_type_component_1 = sample_list_type_component_1_1;
            },
            function (primeng_1_1) {
                primeng_1 = primeng_1_1;
            },
            function (status_display_tree_component_1_1) {
                status_display_tree_component_1 = status_display_tree_component_1_1;
            },
            function (file_model_tree_service_1_1) {
                file_model_tree_service_1 = file_model_tree_service_1_1;
            },
            function (name_id_list_box_component_1_1) {
                name_id_list_box_component_1 = name_id_list_box_component_1_1;
            },
            function (name_id_service_1_1) {
                name_id_service_1 = name_id_service_1_1;
            },
            function (app_component_1_1) {
                app_component_1 = app_component_1_1;
            },
            function (login_component_1_1) {
                login_component_1 = login_component_1_1;
            },
            function (app_routing_1_1) {
                app_routing_1 = app_routing_1_1;
            },
            function (common_1_1) {
                common_1 = common_1_1;
            },
            function (auth_guard_1_1) {
                auth_guard_1 = auth_guard_1_1;
            },
            function (button_1_1) {
                button_1 = button_1_1;
            }
        ],
        execute: function () {
            AppModule = (function () {
                function AppModule() {
                }
                return AppModule;
            }());
            AppModule = __decorate([
                core_1.NgModule({
                    imports: [platform_browser_1.BrowserModule,
                        http_1.HttpModule,
                        forms_1.FormsModule,
                        forms_1.ReactiveFormsModule,
                        primeng_1.TreeModule,
                        primeng_1.SharedModule,
                        app_routing_1.routing],
                    declarations: [
                        app_component_1.AppComponent,
                        app_extractorroot_1.ExtractorRoot,
                        login_component_1.LoginComponent,
                        export_format_component_1.ExportFormatComponent,
                        project_list_box_component_1.ProjectListBoxComponent,
                        dataset_checklist_box_component_1.DataSetCheckListBoxComponent,
                        status_display_box_component_1.StatusDisplayComponent,
                        crops_list_box_component_1.CropsListBoxComponent,
                        export_type_component_1.ExportTypeComponent,
                        dataset_types_list_box_component_1.DatasetTypeListBoxComponent,
                        checklist_box_component_1.CheckListBoxComponent,
                        sample_marker_box_component_1.SampleMarkerBoxComponent,
                        ng2_file_upload_1.FileSelectDirective,
                        ng2_file_upload_1.FileDropDirective,
                        text_area_component_1.TextAreaComponent,
                        uploader_component_1.UploaderComponent,
                        sample_list_type_component_1.SampleListTypeComponent,
                        name_id_list_box_component_1.NameIdListBoxComponent,
                        status_display_tree_component_1.StatusDisplayTreeComponent,
                        primeng_1.Dialog,
                        button_1.Button
                    ],
                    providers: [auth_guard_1.AuthGuard,
                        authentication_service_1.AuthenticationService,
                        dto_request_service_1.DtoRequestService,
                        file_model_tree_service_1.FileModelTreeService,
                        name_id_service_1.NameIdService,
                        { provide: common_1.APP_BASE_HREF, useValue: './' }],
                    bootstrap: [app_component_1.AppComponent]
                })
            ], AppModule);
            exports_1("AppModule", AppModule);
        }
    };
});
//# sourceMappingURL=app.module.js.map