System.register(["@angular/core", "../../model/name-id", "../../model/type-entity", "../../model/type-entity-filter"], function(exports_1, context_1) {
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
    var core_1, name_id_1, type_entity_1, type_entity_filter_1;
    var DtoRequestItemNameIds;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (name_id_1_1) {
                name_id_1 = name_id_1_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (type_entity_filter_1_1) {
                type_entity_filter_1 = type_entity_filter_1_1;
            }],
        execute: function() {
            DtoRequestItemNameIds = (function () {
                function DtoRequestItemNameIds(entityType, entityFilter, entityFilterValue) {
                    if (entityFilter === void 0) { entityFilter = null; }
                    if (entityFilterValue === void 0) { entityFilterValue = null; }
                    this.entityType = entityType;
                    this.entityFilter = entityFilter;
                    this.entityFilterValue = entityFilterValue;
                }
                DtoRequestItemNameIds.prototype.getRequestBody = function () {
                    return null;
                };
                DtoRequestItemNameIds.prototype.getUrl = function () {
                    var baseUrl = "gobii/v1/names";
                    var returnVal = baseUrl + "/" + type_entity_1.EntityType[this.entityType].toLowerCase();
                    if (this.entityFilter && (type_entity_filter_1.EntityFilter.NONE != this.entityFilter)) {
                        returnVal += "?"
                            + "filterType=" + type_entity_filter_1.EntityFilter[this.entityFilter].toLowerCase()
                            + "&"
                            + "filterValue="
                            + this.entityFilterValue;
                    }
                    return returnVal;
                }; // getUrl()
                DtoRequestItemNameIds.prototype.setEntity = function (entityType) {
                    this.entityType = entityType;
                };
                DtoRequestItemNameIds.prototype.resultFromJson = function (json) {
                    var returnVal = [];
                    //let nameListItems:Object[] = json.payload.data;
                    json.payload.data.forEach(function (item) {
                        var currentId = item.id;
                        var currentName = item.name;
                        returnVal.push(new name_id_1.NameId(currentId, currentName));
                    });
                    return returnVal;
                    //return [new NameId(1, 'foo'), new NameId(2, 'bar')];
                };
                DtoRequestItemNameIds = __decorate([
                    core_1.Injectable(), 
                    __metadata('design:paramtypes', [Number, Number, String])
                ], DtoRequestItemNameIds);
                return DtoRequestItemNameIds;
            }());
            exports_1("DtoRequestItemNameIds", DtoRequestItemNameIds); // DtoRequestItemNameIds() 
        }
    }
});
//# sourceMappingURL=dto-request-item-nameids.js.map