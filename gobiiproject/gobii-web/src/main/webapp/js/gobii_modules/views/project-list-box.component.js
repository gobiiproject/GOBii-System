System.register(["@angular/core", "../services/core/dto-request.service", "../services/app/dto-request-item-project"], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
        var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
        if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
        else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
        return c > 3 && r && Object.defineProperty(target, key, r), r;
    };
    var __metadata = (this && this.__metadata) || function (k, v) {
        if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
    };
    var core_1, dto_request_service_1, dto_request_item_project_1;
    var ProjectListBoxComponent;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (dto_request_service_1_1) {
                dto_request_service_1 = dto_request_service_1_1;
            },
            function (dto_request_item_project_1_1) {
                dto_request_item_project_1 = dto_request_item_project_1_1;
            }],
        execute: function() {
            ProjectListBoxComponent = (function () {
                function ProjectListBoxComponent(_dtoRequestServiceProject) {
                    this._dtoRequestServiceProject = _dtoRequestServiceProject;
                    this.onProjectSelected = new core_1.EventEmitter();
                    this.onAddMessage = new core_1.EventEmitter();
                } // ctor
                ProjectListBoxComponent.prototype.handleProjectSelected = function (arg) {
                    var selectedProjectId = this.nameIdList[arg.srcElement.selectedIndex].id;
                    this.setProjectDetails(selectedProjectId);
                    this.onProjectSelected.emit(selectedProjectId);
                };
                ProjectListBoxComponent.prototype.handleAddMessage = function (arg) {
                    this.onAddMessage.emit(arg);
                };
                ProjectListBoxComponent.prototype.setProjectDetails = function (projectId) {
                    var scope$ = this;
                    this._dtoRequestServiceProject.getResult(new dto_request_item_project_1.DtoRequestItemProject(Number(projectId)))
                        .subscribe(function (project) {
                        if (project) {
                            scope$.project = project;
                            scope$.primaryInvestigatorId = String(project.piContact);
                            scope$.setPiName();
                        }
                    }, function (dtoHeaderResponse) {
                        dtoHeaderResponse.statusMessages.forEach(function (m) { return scope$.handleAddMessage("Retrieving project detail: "
                            + m.message); });
                    });
                };
                ProjectListBoxComponent.prototype.ngOnInit = function () {
                    //this.setList();
                };
                ProjectListBoxComponent.prototype.setPiName = function () {
                    var _this = this;
                    this.primaryInvestigatorName = undefined;
                    if (this.primaryInvestigatorId && this.nameIdListPIs) {
                        this.nameIdListPIs.forEach(function (n) {
                            if (n.id === _this.primaryInvestigatorId) {
                                _this.primaryInvestigatorName = n.name;
                            }
                        });
                    }
                };
                ProjectListBoxComponent.prototype.ngOnChanges = function (changes) {
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
                };
                ProjectListBoxComponent = __decorate([
                    core_1.Component({
                        selector: 'project-list-box',
                        inputs: ['primaryInvestigatorId', 'nameIdList', 'nameIdListPIs'],
                        outputs: ['onProjectSelected', 'onAddMessage'],
                        template: "<select name=\"projects\" \n                    (change)=\"handleProjectSelected($event)\">\n                    <option *ngFor=\"let nameId of nameIdList \" \n                    value={{nameId.id}}>{{nameId.name}}</option>\n\t\t        </select>\n                <div *ngIf=\"project\">\n                    <BR>\n                     <fieldset class=\"form-group\">\n                        <b>Name:</b> {{project.projectName}}<BR>\n                        <b>Description:</b> {{project.projectDescription}}<BR>\n                        <b>Principle Investigator:</b> {{primaryInvestigatorName}}\n                      </fieldset> \n                </div>\t\t        \n" // end template
                    }), 
                    __metadata('design:paramtypes', [dto_request_service_1.DtoRequestService])
                ], ProjectListBoxComponent);
                return ProjectListBoxComponent;
            }());
            exports_1("ProjectListBoxComponent", ProjectListBoxComponent);
        }
    }
});
//# sourceMappingURL=project-list-box.component.js.map