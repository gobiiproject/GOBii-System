package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.FilesService;
import org.gobiiproject.gobiidao.filesystem.access.InstructionFileAccess;
import org.gobiiproject.gobiidtomapping.DtoMapExtractorInstructions;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiDataSetExtract;
import org.gobiiproject.gobiimodel.headerlesscontainer.ExtractorInstructionFilesDTO;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;


/**
 * Created by Angel on 6/8/2016.
 */
public class FileServiceImpl implements FilesService {

    private Logger LOGGER = LoggerFactory.getLogger(FileServiceImpl.class);


    InstructionFileAccess<Void> instructionFileAccess = new InstructionFileAccess<>(Void.class);

    @Autowired
    DtoMapExtractorInstructions extractorInstructions;


    private String getFilePath(String cropType, String jobId, GobiiFileProcessDir gobiiFileProcessDir) throws Exception {

        String returnVal = null;

        ConfigSettings configSettings = new ConfigSettings();

        if (gobiiFileProcessDir.equals(GobiiFileProcessDir.EXTRACTOR_OUTPUT)) {

            ExtractorInstructionFilesDTO extractorInstructionFilesDTO = extractorInstructions.getStatus(cropType, jobId);
            if (extractorInstructionFilesDTO
                    .getGobiiExtractorInstructions().size() > 0
                    && extractorInstructionFilesDTO
                    .getGobiiExtractorInstructions()
                    .get(0)
                    .getDataSetExtracts().size() > 0) {

                GobiiDataSetExtract gobiiDataSetExtract = extractorInstructionFilesDTO
                        .getGobiiExtractorInstructions()
                        .get(0)
                        .getDataSetExtracts()
                        .get(0);

                returnVal = gobiiDataSetExtract.getExtractDestinationDirectory();


            } else {
                throw new GobiiDomainException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.NONE, "There is no instruction for the job ");
            }

        } else {
            returnVal = configSettings.getProcessingPath(cropType,
                    gobiiFileProcessDir);
        }

        return returnVal;
    }


    @Override
    public void writeFile(String cropType,
                          String jobId,
                          String fileName,
                          GobiiFileProcessDir gobiiFileProcessDir,
                          byte[] byteArray) throws GobiiException, Exception {

        String path = this.getFilePath(cropType, jobId, gobiiFileProcessDir);
        String fqpn = instructionFileAccess.makeFileName(path, fileName);
        instructionFileAccess.writeFile(fqpn,byteArray);
    }

    @Override
    public File readFile(String cropType,
                         String gobiiJobId,
                         String fileName,
                         GobiiFileProcessDir gobiiFileProcessDir) throws GobiiException, Exception {

        File returnVal;


        String path = this.getFilePath(cropType, gobiiJobId, gobiiFileProcessDir);

        String fqpn = instructionFileAccess.makeFileName(path, fileName);

        returnVal = instructionFileAccess.readFile(fqpn);

        return returnVal;
    }


} // ExtractorInstructionFileServiceImpl
