package org.gobiiproject.gobiidtomapping;


import org.gobiiproject.gobiimodel.dto.container.ExtractorInstructionFilesDTO;

/**
 * Created by Phil on 4/12/2016.
 */


public interface DtoMapExtractorInstructions {

    ExtractorInstructionFilesDTO writeInstructions(ExtractorInstructionFilesDTO extractorInstructionFilesDTO);
    ExtractorInstructionFilesDTO readInstructions(ExtractorInstructionFilesDTO extractorInstructionFilesDTO);
}
