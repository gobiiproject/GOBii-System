package org.gobiiproject.gobiidao.filesystem;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiimodel.dto.instructions.GobiiQCComplete;
import org.gobiiproject.gobiimodel.headerlesscontainer.QCInstructionsDTO;

public interface QCInstructionsDAO {

    boolean writeInstructions(String instructionFileFqpn,
                              QCInstructionsDTO instructions) throws GobiiDaoException;

    boolean doesPathExist(String pathName) throws GobiiDaoException;

    void verifyDirectoryPermissions(String pathName) throws GobiiDaoException;

    void makeDirectory(String pathName) throws GobiiDaoException;

    QCInstructionsDTO getInstructions(String instructionFileFqpn);
}
