package org.gobiiproject.gobiidtomapping.impl.DtoMapNameIds;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.filesystem.access.InstructionFileAccess;
import org.gobiiproject.gobiidtomapping.DtoMapLoaderFiles;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.gobiiproject.gobiimodel.headerlesscontainer.LoaderFilePreviewDTO;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Angel on 11/2016.
 */
public class DtoMapLoaderFilesImpl implements DtoMapLoaderFiles {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapLoaderFilesImpl.class);

    private InstructionFileAccess<List<GobiiLoaderInstruction>> instructionFileAccess = new InstructionFileAccess<>(GobiiLoaderInstruction.class);


    public LoaderFilePreviewDTO makeDirectory(String cropType, String directoryName) throws GobiiDaoException {

        LoaderFilePreviewDTO returnVal = new LoaderFilePreviewDTO();

        if (LineUtils.isNullOrEmpty(cropType)) {
            throw new GobiiDaoException("Crop type is required");
        }
        if (LineUtils.isNullOrEmpty(directoryName)) {
            throw new GobiiDaoException("A directory name is required");
        }

        String fileCropDirectory = null;
        ConfigSettings configSettings = new ConfigSettings();
        try {
            fileCropDirectory = configSettings.getProcessingPath(cropType, GobiiFileProcessDir.RAW_USER_FILES);
        } catch (Exception e) {
            throw new GobiiDaoException("could not get processing path because of: " + e.getMessage());
        }

        if (LineUtils.isNullOrEmpty(fileCropDirectory)) {
            throw new GobiiDaoException("There is no directory defined for this configuraiton item "
                    + GobiiFileProcessDir.RAW_USER_FILES.toString());
        }


        instructionFileAccess.createDirectory(fileCropDirectory);

        String newDirectoryPath = fileCropDirectory + directoryName;
        instructionFileAccess.createDirectory(newDirectoryPath);

        returnVal.setDirectoryName(newDirectoryPath);
        returnVal.setId(1);//this is arbitrary for now

        return returnVal;

    } // createDirectories()

    public LoaderFilePreviewDTO getPreview(String cropType, String directoryName, String fileFormat) throws GobiiDaoException {

        LoaderFilePreviewDTO returnVal = new LoaderFilePreviewDTO();

        if (LineUtils.isNullOrEmpty(cropType)) {
            throw new GobiiDaoException("Crop type is required");
        }

        if (LineUtils.isNullOrEmpty(directoryName)) {
            throw new GobiiDaoException("A directory name is required");
        }

        if (LineUtils.isNullOrEmpty(fileFormat)) {
            throw new GobiiDaoException("A file format is required");
        }


        ConfigSettings configSettings = new ConfigSettings();
        String fileCropDirectory = null;
        try {
            fileCropDirectory = configSettings.getProcessingPath(cropType, GobiiFileProcessDir.RAW_USER_FILES);
        } catch (Exception e) {
            throw new GobiiDaoException("could not get processing path because of: " + e.getMessage());
        }

        if (LineUtils.isNullOrEmpty(fileCropDirectory)) {
            throw new GobiiDaoException("There is no directory defined for this configuraiton item "
                    + GobiiFileProcessDir.RAW_USER_FILES.toString());
        }

        String directoryPath = fileCropDirectory + directoryName;

        if (!instructionFileAccess.doesPathExist(directoryPath)) {
            throw new GobiiDaoException("The specified directory does not exist: " + directoryPath);
        } else {

//            returnVal = instructionFilesDAO.getPreview(directoryPath, fileFormat);

            String extension ="."+fileFormat;
            File directory = new File(directoryPath);
            File[] files = directory.listFiles();


            if(files.length==0){
                throw new GobiiDaoException("There are no files in this directory:" + directory.getName());
            }else {
                for (File file : files) {
                    if (file.getName().endsWith(extension)) {
                        if (returnVal.getFileList().isEmpty()) {//if first file in directory, get preview
                            returnVal.setFilePreview(this.getFilePreview(file, fileFormat));
                        }
                        returnVal.getFileList().add(file.getName());
                    }
                }
                if (returnVal.getFileList().isEmpty()) {//if no files are found that matches format
                    throw new GobiiDaoException("There are no files of the specified format in the directory:" + directory.getName());
                }
            }
            returnVal.setDirectoryName(directory.getAbsolutePath());
            returnVal.setId(1);//this is arbitrary for now
        }
        return returnVal;

    } // createDirectories()

    private List<List<String>> getFilePreview(File file, String fileFormat) {
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

}
