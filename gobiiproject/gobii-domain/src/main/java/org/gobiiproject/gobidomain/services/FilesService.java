package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.headerlesscontainer.ExtractorInstructionFilesDTO;
import org.gobiiproject.gobiimodel.types.GobiiExtractFilterType;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;

import java.io.File;


/**
 * Created by Phil on 4/12/2016.
 */
public interface FilesService {

    void writeFile(String cropType,
                   String jobId,
                   String fileName,
                   GobiiFileProcessDir gobiiFileProcessDir,
                   byte[] byteArray) throws GobiiException, Exception;

    File readFile(String cropType,
                  String gobiiJobId,
                  String fileName,
                  GobiiFileProcessDir gobiiFileProcessDir) throws GobiiException, Exception;
}
