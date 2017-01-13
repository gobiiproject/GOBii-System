System.register(["@angular/core", "../services/core/dto-request.service"], function(exports_1, context_1) {
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
    var core_1, dto_request_service_1;
    var UsersListBoxComponent;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (dto_request_service_1_1) {
                dto_request_service_1 = dto_request_service_1_1;
            }],
        execute: function() {
            UsersListBoxComponent = (function () {
                function UsersListBoxComponent(_dtoRequestService) {
                    this._dtoRequestService = _dtoRequestService;
                    this.onUserSelected = new core_1.EventEmitter();
                } // ctor
                UsersListBoxComponent.prototype.handleUserSelected = function (arg) {
                    this.onUserSelected.emit(this.nameIdList[arg.srcElement.selectedIndex].id);
                };
                UsersListBoxComponent.prototype.ngOnInit = function () {
                    return null;
                };
                UsersListBoxComponent = __decorate([
                    core_1.Component({
                        selector: 'users-list-box',
                        inputs: ['nameIdList'],
                        outputs: ['onUserSelected'],
                        template: "<select name=\"users\" (change)=\"handleUserSelected($event)\" >\n\t\t\t<option *ngFor=\"let nameId of nameIdList \" \n\t\t\t\tvalue={{nameId.id}}>{{nameId.name}}</option>\n\t\t</select>\n" // end template
                    }), 
                    __metadata('design:paramtypes', [dto_request_service_1.DtoRequestService])
                ], UsersListBoxComponent);
                return UsersListBoxComponent;
            }());
            exports_1("UsersListBoxComponent", UsersListBoxComponent);
        }
    }
});
//# sourceMappingURL=users-list-box.component.js.map