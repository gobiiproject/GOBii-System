package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobiimodel.dto.container.ExtractorInstructionFilesDTO;

/**
 * Created by Phil on 4/12/2016.
 */
public interface ExtractorInstructionFilesService {
    ExtractorInstructionFilesDTO processExtractorFileInstructions(ExtractorInstructionFilesDTO extractorInstructionFilesDTO);
}
