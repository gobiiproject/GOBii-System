package org.gobiiproject.gobiidtomapping;


import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.headerlesscontainer.LoaderInstructionFilesDTO;

import java.util.List;

/**
 * Created by Phil on 4/12/2016.
 */


public interface DtoMapLoaderInstructions {

    LoaderInstructionFilesDTO createInstruction(String cropType, LoaderInstructionFilesDTO loaderInstructionFilesDTO) throws GobiiDtoMappingException, GobiiDaoException;
    LoaderInstructionFilesDTO getInstruction(String cropType, String getInstructions) throws GobiiDtoMappingException;
}
