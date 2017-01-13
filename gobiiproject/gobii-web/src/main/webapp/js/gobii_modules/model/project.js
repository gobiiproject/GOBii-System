System.register([], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var Project;
    return {
        setters:[],
        execute: function() {
            Project = (function () {
                function Project(projectId, projectName, projectCode, projectDescription, piContact, createdBy, createdstring, modifiedBy, modifiedstring, projectStatus) {
                    this.projectId = projectId;
                    this.projectName = projectName;
                    this.projectCode = projectCode;
                    this.projectDescription = projectDescription;
                    this.piContact = piContact;
                    this.createdBy = createdBy;
                    this.createdstring = createdstring;
                    this.modifiedBy = modifiedBy;
                    this.modifiedstring = modifiedstring;
                    this.projectStatus = projectStatus;
                }
                return Project;
            }());
            exports_1("Project", Project);
        }
    }
});
//# sourceMappingURL=project.js.map