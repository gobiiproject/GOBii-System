package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.QCInstructionFilesService;
import org.gobiiproject.gobiidtomapping.DtoMapQCInstructions;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.headerlesscontainer.QCInstructionsDTO;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Created by Angel on 6/8/2016.
 */
public class QCInstructionFilesServiceImpl implements QCInstructionFilesService {

    private Logger LOGGER = LoggerFactory.getLogger(QCInstructionFilesServiceImpl.class);

    @Autowired
    private DtoMapQCInstructions dtoMapQCInstructions = null;

    @Override
    public QCInstructionsDTO createInstruction(String cropType, QCInstructionsDTO qcInstructionsDTO)
            throws GobiiException {
        QCInstructionsDTO returnVal;

        returnVal = dtoMapQCInstructions.createInstructions(cropType, qcInstructionsDTO);

        returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);

        return returnVal;
    }

    @Override
    public QCInstructionsDTO getInstruction(String cropType, String instructionFileName) {

        QCInstructionsDTO returnVal;

        try {
            returnVal = dtoMapQCInstructions.getInstruction(cropType, instructionFileName);

            returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);


        } catch (Exception e) {

            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);

        }

        return returnVal;
    }

}
