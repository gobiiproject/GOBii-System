System.register(["@angular/core", "../../model/type-process", "../../model/contact"], function (exports_1, context_1) {
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
    var core_1, type_process_1, contact_1, ContactSearchType, DtoRequestItemContact;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (contact_1_1) {
                contact_1 = contact_1_1;
            }
        ],
        execute: function () {
            (function (ContactSearchType) {
                ContactSearchType[ContactSearchType["UNKNOWN"] = 0] = "UNKNOWN";
                ContactSearchType[ContactSearchType["BY_EMAIL"] = 1] = "BY_EMAIL";
                ContactSearchType[ContactSearchType["BY_USERNAME"] = 2] = "BY_USERNAME";
            })(ContactSearchType || (ContactSearchType = {}));
            exports_1("ContactSearchType", ContactSearchType);
            DtoRequestItemContact = (function () {
                function DtoRequestItemContact(contactSearchType, contactSearchTerm) {
                    this.contactSearchType = contactSearchType;
                    this.contactSearchTerm = contactSearchTerm;
                    this.processType = type_process_1.ProcessType.READ;
                    this.contactSearchType = contactSearchType;
                    this.contactSearchTerm = contactSearchTerm;
                }
                DtoRequestItemContact.prototype.getUrl = function () {
                    var returnVal = "gobii/v1/contact-search";
                    if (this.contactSearchType === ContactSearchType.BY_EMAIL) {
                        returnVal += "?email=" + this.contactSearchTerm + "&lastName&firstName&userName";
                    }
                    else if (this.contactSearchType === ContactSearchType.BY_USERNAME) {
                        returnVal += "?email&lastName&firstName&userName=" + this.contactSearchTerm;
                    }
                    else {
                        // This should be a valid request, and the web server will give a meaningful
                        // error that no search term was specified
                        returnVal += "?email&lastName&firstName&userName";
                    }
                    return returnVal;
                }; // getUrl()
                DtoRequestItemContact.prototype.getRequestBody = function () {
                    // return JSON.stringify({
                    //     "processType": ProcessType[this.processType],
                    //     "dataSetId": this.dataSetId
                    // })
                    // for now we are only doing a GET so we should not yet need a body
                    return undefined;
                };
                DtoRequestItemContact.prototype.resultFromJson = function (json) {
                    var returnVal;
                    if (json.payload.data[0]) {
                        returnVal = new contact_1.Contact(json.payload.data[0].contactId, json.payload.data[0].lastName, json.payload.data[0].firstName, json.payload.data[0].code, json.payload.data[0].email, 
                        //    json.payload.data[0].List<Integer> roles = new ArrayList<>(),
                        //    json.payload.data[0].Integer createdBy,
                        //    json.payload.data[0].Date createdDate,
                        //    json.payload.data[0].Integer modifiedBy,
                        //    json.payload.data[0].Date modifiedDate,
                        json.payload.data[0].organizationId, json.payload.data[0].userName);
                    }
                    return returnVal;
                };
                return DtoRequestItemContact;
            }()); //
            DtoRequestItemContact = __decorate([
                core_1.Injectable(),
                __metadata("design:paramtypes", [Number, String])
            ], DtoRequestItemContact);
            exports_1("DtoRequestItemContact", DtoRequestItemContact);
        }
    };
});
//# sourceMappingURL=dto-request-item-contact.js.map