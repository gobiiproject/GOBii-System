System.register(["./gobii-extractor-instruction"], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var gobii_extractor_instruction_1;
    var ExtractorInstructionFilesDTO;
    return {
        setters:[
            function (gobii_extractor_instruction_1_1) {
                gobii_extractor_instruction_1 = gobii_extractor_instruction_1_1;
            }],
        execute: function() {
            ExtractorInstructionFilesDTO = (function () {
                function ExtractorInstructionFilesDTO(gobiiExtractorInstructions, instructionFileName, processType, gobiiCropType) {
                    this.gobiiExtractorInstructions = gobiiExtractorInstructions;
                    this.instructionFileName = instructionFileName;
                    this.processType = processType;
                    this.gobiiCropType = gobiiCropType;
                    this.gobiiExtractorInstructions = gobiiExtractorInstructions;
                    this.instructionFileName = instructionFileName;
                    this.processType = processType;
                    this.gobiiCropType = gobiiCropType;
                } // ctor
                ExtractorInstructionFilesDTO.prototype.getProcessType = function () {
                    return this.processType;
                };
                ExtractorInstructionFilesDTO.prototype.getGobiiCropType = function () {
                    return this.gobiiCropType;
                };
                ExtractorInstructionFilesDTO.prototype.setProcessType = function (value) {
                    this.processType = value;
                };
                ExtractorInstructionFilesDTO.prototype.getGobiiExtractorInstructions = function () {
                    return this.gobiiExtractorInstructions;
                };
                ExtractorInstructionFilesDTO.prototype.setGobiiExtractorInstructions = function (value) {
                    this.gobiiExtractorInstructions = value;
                };
                ExtractorInstructionFilesDTO.prototype.getInstructionFileName = function () {
                    return this.instructionFileName;
                };
                ExtractorInstructionFilesDTO.prototype.setInstructionFileName = function (value) {
                    this.instructionFileName = value;
                };
                ExtractorInstructionFilesDTO.prototype.getJson = function () {
                    var returnVal = {};
                    returnVal.processType = this.processType;
                    returnVal.instructionFileName = this.instructionFileName;
                    returnVal.gobiiCropType = this.gobiiCropType;
                    returnVal.gobiiExtractorInstructions = [];
                    this.gobiiExtractorInstructions.forEach(function (i) {
                        returnVal.gobiiExtractorInstructions.push(i.getJson());
                    });
                    return returnVal;
                }; // getJson()
                ExtractorInstructionFilesDTO.fromJson = function (json) {
                    var gobiiExtractorInstructions = [];
                    json.gobiiExtractorInstructions.forEach(function (i) {
                        return gobiiExtractorInstructions.push(gobii_extractor_instruction_1.GobiiExtractorInstruction.fromJson(i));
                    });
                    var returnVal = new ExtractorInstructionFilesDTO(gobiiExtractorInstructions, json.instructionFileName, json.processType, json.gobiiCropType);
                    return returnVal;
                };
                return ExtractorInstructionFilesDTO;
            }());
            exports_1("ExtractorInstructionFilesDTO", ExtractorInstructionFilesDTO);
        }
    }
});
//# sourceMappingURL=dto-extractor-instruction-files.js.map