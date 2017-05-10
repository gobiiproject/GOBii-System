System.register(["@angular/core", "../../model/name-id", "./dto-request.service", "../../model/type-entity-filter", "../../model/cv-filter-type", "../../model/type-entity", "rxjs/Observable", "../app/dto-request-item-nameids"], function (exports_1, context_1) {
    "use strict";
    var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
        var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
        if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
        else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
        return c > 3 && r && Object.defineProperty(target, key, r), r;
    };
    var __metadata = (this && this.__metadata) || function (k, v) {
        if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
    };
    var __moduleName = context_1 && context_1.id;
    var core_1, name_id_1, dto_request_service_1, type_entity_filter_1, cv_filter_type_1, type_entity_1, Observable_1, dto_request_item_nameids_1, NameIdService;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (name_id_1_1) {
                name_id_1 = name_id_1_1;
            },
            function (dto_request_service_1_1) {
                dto_request_service_1 = dto_request_service_1_1;
            },
            function (type_entity_filter_1_1) {
                type_entity_filter_1 = type_entity_filter_1_1;
            },
            function (cv_filter_type_1_1) {
                cv_filter_type_1 = cv_filter_type_1_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (Observable_1_1) {
                Observable_1 = Observable_1_1;
            },
            function (dto_request_item_nameids_1_1) {
                dto_request_item_nameids_1 = dto_request_item_nameids_1_1;
            }
        ],
        execute: function () {
            NameIdService = (function () {
                function NameIdService(_dtoRequestService) {
                    this._dtoRequestService = _dtoRequestService;
                } // ctor
                NameIdService.prototype.getEntityFilterValue = function (nameIdRequestParams) {
                    var returnVal = null;
                    if (nameIdRequestParams.getEntityType() === type_entity_1.EntityType.Contacts) {
                        if (nameIdRequestParams.getEntitySubType() === type_entity_1.EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR) {
                            returnVal = "PI";
                        }
                    }
                    else if (nameIdRequestParams.getEntityType() === type_entity_1.EntityType.CvTerms) {
                        if (nameIdRequestParams.getCvFilterType() != null && nameIdRequestParams.getCvFilterType() != cv_filter_type_1.CvFilterType.UNKNOWN) {
                            returnVal = cv_filter_type_1.CvFilters.get(cv_filter_type_1.CvFilterType.DATASET_TYPE);
                        }
                    }
                    return returnVal;
                };
                NameIdService.prototype.validateRequest = function (nameIdRequestParams) {
                    var foo = "bar";
                    var returnVal = false;
                    if (nameIdRequestParams.getEntityFilter() === type_entity_filter_1.EntityFilter.NONE) {
                        nameIdRequestParams.setEntityFilterValue(null);
                        returnVal = true;
                    }
                    else if (nameIdRequestParams.getEntityFilter() === type_entity_filter_1.EntityFilter.BYTYPEID) {
                        //for filter BYTYPEID we must have a filter value specified by parent
                        returnVal = (nameIdRequestParams.getEntityFilterValue() != null);
                    }
                    else if (nameIdRequestParams.getEntityFilter() === type_entity_filter_1.EntityFilter.BYTYPENAME) {
                        //for filter BYTYPENAME we divine the typename algorityhmically for now
                        var entityFilterValue = this.getEntityFilterValue(nameIdRequestParams);
                        if (entityFilterValue) {
                            nameIdRequestParams.setEntityFilterValue(entityFilterValue);
                            returnVal = true;
                        }
                    }
                    return returnVal;
                };
                NameIdService.prototype.get = function (nameIdRequestParams) {
                    var _this = this;
                    return Observable_1.Observable.create(function (observer) {
                        _this._dtoRequestService.get(new dto_request_item_nameids_1.DtoRequestItemNameIds(nameIdRequestParams.getEntityType(), nameIdRequestParams.getEntityFilter() === type_entity_filter_1.EntityFilter.NONE ? null : nameIdRequestParams.getEntityFilter(), nameIdRequestParams.getEntityFilterValue()))
                            .subscribe(function (nameIds) {
                            var nameIdsToReturn = null;
                            if (nameIds && (nameIds.length > 0)) {
                                nameIdsToReturn = nameIds;
                            }
                            else {
                                nameIdsToReturn = [new name_id_1.NameId("0", "<none>", nameIdRequestParams.getEntityType())];
                            }
                            observer.next(nameIdsToReturn);
                            observer.complete();
                        }, function (responseHeader) {
                            responseHeader.status.statusMessages.forEach(function (headerStatusMessage) {
                                observer.error(headerStatusMessage);
                            });
                        });
                    });
                };
                return NameIdService;
            }());
            NameIdService = __decorate([
                core_1.Injectable(),
                __metadata("design:paramtypes", [dto_request_service_1.DtoRequestService])
            ], NameIdService);
            exports_1("NameIdService", NameIdService);
        }
    };
});
//# sourceMappingURL=name-id-service.js.map