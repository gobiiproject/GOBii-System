package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.headerlesscontainer.LoaderFilePreviewDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.LoaderInstructionFilesDTO;

import java.util.List;


/**
 * Created by Angel on 11/2016.
 */
public interface LoaderFilesService {
    LoaderFilePreviewDTO makeDirectory(String cropType, String directoryName) throws GobiiDomainException;
    LoaderFilePreviewDTO getPreview(String cropType, String directoryName, String fileFormat) throws GobiiDomainException;
}
