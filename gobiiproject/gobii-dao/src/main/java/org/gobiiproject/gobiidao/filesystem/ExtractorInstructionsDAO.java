package org.gobiiproject.gobiidao.filesystem;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiExtractorInstruction;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;

import java.util.List;

/**
 * Created by Phil on 4/12/2016.
 */
public interface ExtractorInstructionsDAO {

    boolean writeInstructions(String instructionFileFqpn,
                              List<GobiiExtractorInstruction> instructions) throws GobiiDaoException;

    List<GobiiExtractorInstruction> getGobiiExtractorInstructionsFromFile(String instructionFileFqpn) throws GobiiDaoException;

    boolean doesPathExist(String pathName) throws GobiiDaoException;

    void verifyDirectoryPermissions(String pathName) throws GobiiDaoException;

    void makeDirectory(String pathName) throws GobiiDaoException;

    List<GobiiExtractorInstruction> setGobiiJobStatus(boolean applyToAll, List<GobiiExtractorInstruction> instructions, GobiiFileProcessDir extractorInstructions) throws GobiiDaoException;

}
