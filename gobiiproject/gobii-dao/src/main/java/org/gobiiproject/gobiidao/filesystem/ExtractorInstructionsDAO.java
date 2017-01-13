package org.gobiiproject.gobiidao.filesystem;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiExtractorInstruction;

import java.util.List;

/**
 * Created by Phil on 4/12/2016.
 */
public interface ExtractorInstructionsDAO {

    String writeInstructions(String instructionFileFqpn,
                             List<GobiiExtractorInstruction> instructions) throws GobiiDaoException;

    List<GobiiExtractorInstruction> getInstructions(String instructionFileFqpn) throws GobiiDaoException;

    boolean doesPathExist(String pathName) throws GobiiDaoException;

    void verifyDirectoryPermissions(String pathName) throws GobiiDaoException;

    void makeDirectory(String pathName) throws GobiiDaoException;

}
