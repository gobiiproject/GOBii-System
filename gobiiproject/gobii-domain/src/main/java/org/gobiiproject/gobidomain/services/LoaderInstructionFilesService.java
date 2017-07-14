package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.headerlesscontainer.LoaderInstructionFilesDTO;

import java.util.List;

/**
 * Created by Phil on 4/12/2016.
 */
public interface LoaderInstructionFilesService {
    LoaderInstructionFilesDTO createInstruction(String cropType, LoaderInstructionFilesDTO loaderInstructionFilesDTO) throws GobiiDomainException;
    LoaderInstructionFilesDTO getInstruction(String cropType, String instructionFileName) throws GobiiDomainException;
}
