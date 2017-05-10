package org.gobiiproject.gobiiprocess.digester.csv;

import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFileColumn;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by jdl232 on 3/28/2017.
 */
public interface CSVFileReaderInterface {
    /**
     * Reads the input file specified by the loader instruction and creates a digest file based on the instruction. For more detailed discussions on the resulting digest file's format
     * see either the documentation of the IFLs or {@link org.gobiiproject.gobiiprocess.digester.GobiiFileReader} documentation.
     * @param loaderInstruction Singular instruction, specifying input and output directories
     * @throws IOException If an unexpected filesystem error occurs
     * @throws InterruptedException If interrupted (Signals, etc)
     */
    void processCSV(GobiiLoaderInstruction loaderInstruction) throws IOException, InterruptedException;
}
