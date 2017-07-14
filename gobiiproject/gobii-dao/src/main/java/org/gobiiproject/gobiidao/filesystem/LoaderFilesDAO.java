package org.gobiiproject.gobiidao.filesystem;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.gobiiproject.gobiimodel.headerlesscontainer.LoaderFilePreviewDTO;

import java.io.File;
import java.util.List;


/**
 * Created by Angel on 11/2016.
 */
public interface LoaderFilesDAO {

    boolean doesPathExist(String pathName) throws GobiiDaoException;

    void makeDirectory(String directoryPath) throws GobiiDaoException;

    void verifyDirectoryPermissions(String pathName) throws GobiiDaoException;

    List<List<String>> getFilePreview(File file, String fileFormat);

}
