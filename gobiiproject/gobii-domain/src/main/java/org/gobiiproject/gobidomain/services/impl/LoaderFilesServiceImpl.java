package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.LoaderFilesService;
import org.gobiiproject.gobiidtomapping.DtoMapLoaderFiles;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.headerlesscontainer.LoaderFilePreviewDTO;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;



/**
 * Created by Angel on 11/2016.
 */
public class LoaderFilesServiceImpl implements LoaderFilesService {

    private Logger LOGGER = LoggerFactory.getLogger(LoaderFilesServiceImpl.class);

    @Autowired
    private DtoMapLoaderFiles dtoMapLoaderFiles = null;


    @Override
    public LoaderFilePreviewDTO makeDirectory(String cropType, String directoryName) throws GobiiDomainException {

        LoaderFilePreviewDTO returnVal;

        try {
            returnVal = dtoMapLoaderFiles.makeDirectory(cropType, directoryName);
            returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);

        } catch (Exception e) {

            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);

        }

        return returnVal;
    }

    @Override
    public LoaderFilePreviewDTO getPreview(String cropType, String directoryName, String fileFormat) throws GobiiDomainException {

        LoaderFilePreviewDTO returnVal;

        try {

            returnVal = dtoMapLoaderFiles.getPreview(cropType, directoryName, fileFormat);
            returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);


        } catch (Exception e) {

            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);

        }

        return returnVal;
    }
} // LoaderFileServiceImpl
