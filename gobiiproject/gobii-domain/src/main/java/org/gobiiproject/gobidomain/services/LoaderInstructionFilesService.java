package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobiimodel.dto.container.LoaderInstructionFilesDTO;

/**
 * Created by Phil on 4/12/2016.
 */
public interface LoaderInstructionFilesService {
    LoaderInstructionFilesDTO processLoaderFileInstructions(LoaderInstructionFilesDTO loaderInstructionFilesDTO);
}
