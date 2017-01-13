package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.headerlesscontainer.ExtractorInstructionFilesDTO;


/**
 * Created by Phil on 4/12/2016.
 */
public interface ExtractorInstructionFilesService {
    ExtractorInstructionFilesDTO createInstruction(String cropType, ExtractorInstructionFilesDTO extractorInstructionFilesDTO) throws GobiiException;
    ExtractorInstructionFilesDTO getStatus(String cropType, String instructionFileName) throws GobiiException;

}
