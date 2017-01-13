package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.services.LoaderInstructionFilesService;
import org.gobiiproject.gobiidtomapping.DtoMapLoaderInstructions;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.LoaderInstructionFilesDTO;
import org.gobiiproject.gobiimodel.dto.header.DtoHeaderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Created by Phil on 4/12/2016.
 */
public class LoaderInstructionFileServiceImpl implements LoaderInstructionFilesService {

    private Logger LOGGER = LoggerFactory.getLogger(LoaderInstructionFileServiceImpl.class);

    @Autowired
    private DtoMapLoaderInstructions dtoMapLoaderInstructions = null;


    @Override
    public LoaderInstructionFilesDTO processLoaderFileInstructions(LoaderInstructionFilesDTO loaderInstructionFilesDTO) {

        LoaderInstructionFilesDTO returnVal = loaderInstructionFilesDTO;

        try {

            switch (returnVal.getProcessType()) {

                case CREATE:
                    returnVal = dtoMapLoaderInstructions.writeInstructions(loaderInstructionFilesDTO);
                    break;

                case READ:
                    returnVal = dtoMapLoaderInstructions.readInstructions(loaderInstructionFilesDTO);
                    break;

                default:
                    returnVal.getDtoHeaderResponse().addStatusMessage(DtoHeaderResponse.StatusLevel.ERROR,
                            DtoHeaderResponse.ValidationStatusType.BAD_REQUEST,
                            "Unsupported proces type " + returnVal.getProcessType().toString());

            } // switch

        } catch (Exception e) {

            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii service error", e);
        }

        return returnVal;

    } // processLoaderFileInstructions

} // LoaderInstructionFileServiceImpl
