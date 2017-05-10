package org.gobiiproject.gobiidtomapping;


import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.headerlesscontainer.ExtractorInstructionFilesDTO;
import org.gobiiproject.gobiimodel.types.GobiiExtractFilterType;

/**
 * Created by Phil on 4/12/2016.
 */


public interface DtoMapExtractorInstructions {

    void writeDataFile(String cropType, GobiiExtractFilterType gobiiExtractFilterType, String jobId, byte[] byteArray ) throws GobiiException;
    ExtractorInstructionFilesDTO writeInstructions(String cropType, ExtractorInstructionFilesDTO extractorInstructionFilesDTO)  throws GobiiException;
    ExtractorInstructionFilesDTO getStatus(String cropType, String instructionFileName)  throws GobiiException;
}
