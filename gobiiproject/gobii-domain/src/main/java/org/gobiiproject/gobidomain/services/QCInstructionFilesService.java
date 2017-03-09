package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.headerlesscontainer.ExtractorInstructionFilesDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.QCInstructionsDTO;


public interface QCInstructionFilesService {
    QCInstructionsDTO createInstruction(String cropType, QCInstructionsDTO qcInstructionsDTO) throws GobiiException;

    QCInstructionsDTO getInstruction(String cropType, String instructionFileName);
}
