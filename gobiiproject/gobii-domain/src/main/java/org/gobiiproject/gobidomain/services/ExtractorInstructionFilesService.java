package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.headerlesscontainer.ExtractorInstructionFilesDTO;
import org.gobiiproject.gobiimodel.types.GobiiExtractFilterType;


/**
 * Created by Phil on 4/12/2016.
 */
public interface ExtractorInstructionFilesService {

    ExtractorInstructionFilesDTO createInstruction(String cropType, ExtractorInstructionFilesDTO extractorInstructionFilesDTO) throws GobiiDomainException;
    ExtractorInstructionFilesDTO getStatus(String cropType, String instructionFileName) throws GobiiDomainException;

}
