package org.gobiiproject.gobiidtomapping;


import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.headerlesscontainer.ExtractorInstructionFilesDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.QCInstructionsDTO;

public interface DtoMapQCInstructions {

    QCInstructionsDTO createInstructions(String cropType, QCInstructionsDTO qcInstructionsDTO)  throws GobiiException;

    QCInstructionsDTO getInstruction(String cropType, String instructionFileName);
}
