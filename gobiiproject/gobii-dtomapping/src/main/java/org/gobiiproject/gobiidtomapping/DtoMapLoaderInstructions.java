package org.gobiiproject.gobiidtomapping;


import org.gobiiproject.gobiimodel.dto.container.LoaderInstructionFilesDTO;

/**
 * Created by Phil on 4/12/2016.
 */


public interface DtoMapLoaderInstructions {

    LoaderInstructionFilesDTO writeInstructions(LoaderInstructionFilesDTO loaderInstructionFilesDTO);
    LoaderInstructionFilesDTO readInstructions(LoaderInstructionFilesDTO loaderInstructionFilesDTO);
}
