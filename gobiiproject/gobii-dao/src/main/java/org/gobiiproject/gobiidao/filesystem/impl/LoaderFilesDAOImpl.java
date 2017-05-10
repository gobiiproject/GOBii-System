package org.gobiiproject.gobiidao.filesystem.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.filesystem.LoaderFilesDAO;
import org.gobiiproject.gobiidao.filesystem.LoaderInstructionsDAO;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.gobiiproject.gobiimodel.headerlesscontainer.LoaderFilePreviewDTO;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


/**
 * Created by Angel on 11/2016.
 */
public class LoaderFilesDAOImpl implements LoaderFilesDAO {

    private final String LOADER_FILE_EXT = ".json";


    @Override
    public boolean doesPathExist(String pathName) throws GobiiDaoException {
        return new File(pathName).exists();
    }


    @Override
    public void verifyDirectoryPermissions(String pathName) throws GobiiDaoException {
        File pathToCreate = new File(pathName);
        if (!pathToCreate.canRead() && !pathToCreate.setReadable(true, false)) {
            throw new GobiiDaoException("Unable to set read permissions on directory " + pathName);
        }

        if (!pathToCreate.canWrite() && !pathToCreate.setWritable(true, false)) {
            throw new GobiiDaoException("Unable to set write permissions on directory " + pathName);
        }
    }

    @Override
    public void makeDirectory(String directoryPath) throws GobiiDaoException {

        if (!doesPathExist(directoryPath)) {

            File pathToCreate = new File(directoryPath);

            if (!pathToCreate.mkdirs()) {
                throw new GobiiDaoException("Unable to create directory " + directoryPath);
            } else {
                verifyDirectoryPermissions(directoryPath);
            }

        } else {
            throw new GobiiDaoException("The specified path already exists: " + directoryPath);
        }

    }

    @Override
    public List<List<String>> getFilePreview(File file, String fileFormat) {
        List<List<String>> returnVal = new ArrayList<List<String>>();
        Scanner input = new Scanner(System.in);
        try {
            int lineCtr = 0; //count lines read
            input = new Scanner(file);

            while (input.hasNextLine() && lineCtr < 50) { //read first 50 lines only
                int ctr = 0; //count words stored
                List<String> lineRead = new ArrayList<String>();
                String line = input.nextLine();
                for (String s : line.split(getDelimiterFor(fileFormat))) {
                    if (ctr == 50) break;
                    else {
                        lineRead.add(s);
                        ctr++;
                    }
                }
                returnVal.add(lineRead);
                lineCtr++;
            }
            input.close();
        } catch (FileNotFoundException e) {
            throw new GobiiDaoException("Cannot find file. " + e.getMessage());
        }

        return returnVal;
    }

    private String getDelimiterFor(String fileFormat) {
        String delimiter;
        switch (fileFormat) {
            case "csv":
                delimiter = ",";
                break;
            case "txt":
                delimiter = "\t";
                break;
            case "vcf":
                delimiter = "\t";
                break;
            case "hmp.txt":
                delimiter = "\t";
                break;
            default:
                throw new GobiiDaoException("File Format not supported: " + fileFormat);
        }
        return delimiter;
    }


} // LoaderInstructionsDAOImpl
