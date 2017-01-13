System.register(["@angular/core", "../services/core/dto-request.service", "../services/app/dto-request-item-experiment"], function(exports_1, context_1) {
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
    var core_1, dto_request_service_1, dto_request_item_experiment_1;
    var ExperimentListBoxComponent;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (dto_request_service_1_1) {
                dto_request_service_1 = dto_request_service_1_1;
            },
            function (dto_request_item_experiment_1_1) {
                dto_request_item_experiment_1 = dto_request_item_experiment_1_1;
            }],
        execute: function() {
            ExperimentListBoxComponent = (function () {
                function ExperimentListBoxComponent(_dtoRequestServiceNameIds, _dtoRequestServiceExperiment) {
                    this._dtoRequestServiceNameIds = _dtoRequestServiceNameIds;
                    this._dtoRequestServiceExperiment = _dtoRequestServiceExperiment;
                    this.onExperimentSelected = new core_1.EventEmitter();
                    this.onAddMessage = new core_1.EventEmitter();
                } // ctor
                ExperimentListBoxComponent.prototype.handleExperimentSelected = function (arg) {
                    this.onExperimentSelected.emit(this.nameIdList[arg.srcElement.selectedIndex].id);
                };
                ExperimentListBoxComponent.prototype.handleAddMessage = function (arg) {
                    this.onAddMessage.emit(arg);
                };
                ExperimentListBoxComponent.prototype.setExperimentDetail = function (experimentId) {
                    var scope$ = this;
                    scope$._dtoRequestServiceExperiment.getResult(new dto_request_item_experiment_1.DtoRequestItemExperiment(Number(experimentId))).subscribe(function (experiment) {
                        if (experiment) {
                            scope$.experiment = experiment;
                        }
                    }, function (dtoHeaderResponse) {
                        dtoHeaderResponse.statusMessages.forEach(function (m) { return scope$.handleAddMessage("Retrieving experiment detail: "
                            + m.message); });
                    });
                }; // setList()
                ExperimentListBoxComponent.prototype.ngOnInit = function () {
                    //this.setList();
                };
                ExperimentListBoxComponent.prototype.ngOnChanges = function (changes) {
                    if (changes['projectId'] && changes['projectId'].currentValue) {
                        this.projectId = changes['projectId'].currentValue;
                    }
                    if (changes['nameIdList']) {
                        if (changes['nameIdList'].currentValue) {
                            this.nameIdList = changes['nameIdList'].currentValue;
                            this.setExperimentDetail(this.nameIdList[0].id);
                        }
                    }
                    //this.setList();
                };
                ExperimentListBoxComponent = __decorate([
                    core_1.Component({
                        selector: 'experiment-list-box',
                        inputs: ['projectId', 'nameIdList'],
                        outputs: ['onExperimentSelected', 'onAddMessage'],
                        template: "<select name=\"experiment\" \n                    (change)=\"handleExperimentSelected($event)\">\n                    <option *ngFor=\"let nameId of nameIdList \" \n                    value={{nameId.id}}>{{nameId.name}}</option>\n\t\t        </select>\n                <div *ngIf=\"experiment\">\n    \t\t        <BR>\n                     <fieldset>\n                        <b>Name:</b> {{experiment.experimentName}}<BR>\n                        <b>Platform:</b> {{experiment.platformName}}<BR>\n                        <b>Code:</b> {{experiment.experimentCode}}<BR>\n                      </fieldset> \n                </div>\t\t        \n" // end template
                    }), 
                    __metadata('design:paramtypes', [dto_request_service_1.DtoRequestService, dto_request_service_1.DtoRequestService])
                ], ExperimentListBoxComponent);
                return ExperimentListBoxComponent;
            }());
            exports_1("ExperimentListBoxComponent", ExperimentListBoxComponent);
        }
    }
});
//# sourceMappingURL=experiment-list-box.component.js.map