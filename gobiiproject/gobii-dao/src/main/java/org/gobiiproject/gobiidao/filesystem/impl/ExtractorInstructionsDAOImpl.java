package org.gobiiproject.gobiidao.filesystem.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.filesystem.ExtractorInstructionsDAO;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiExtractorInstruction;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Phil on 4/12/2016.
 */
public class ExtractorInstructionsDAOImpl implements ExtractorInstructionsDAO {

    private final String LOADER_FILE_EXT = ".json";

    @Override
    public String writeInstructions(String instructionFileFqpn,
                                    List<GobiiExtractorInstruction> instructions) throws GobiiDaoException {

        String returnVal = null;

        try {

            File instructionFile = new File(instructionFileFqpn);
            if (!instructionFile.exists()) {

                String filePath = instructionFile.getAbsolutePath().
                        substring(0, instructionFile.getAbsolutePath().lastIndexOf(File.separator));

                File destinationDirectory = new File(filePath);

                if (destinationDirectory.exists()) {

                    if (destinationDirectory.isDirectory()) {

                        ObjectMapper objectMapper = new ObjectMapper();
                        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
                        objectMapper.enable(SerializationFeature.WRITE_NULL_MAP_VALUES);
                        String instructionsAsJson = objectMapper.writeValueAsString(instructions);
                        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(instructionFileFqpn));
                        bufferedWriter.write(instructionsAsJson);
                        bufferedWriter.flush();
                        bufferedWriter.close();
                    } else {
                        throw new GobiiDaoException("Path of specified instruction file name is not a directory: "
                                + destinationDirectory);
                    } // if-else directory is really a directory

                } else {
                    throw new GobiiDaoException("Path of specified instruction file directory does not exist: "
                            + destinationDirectory);

                } // if-else destination directory exists

            } else {

                throw new GobiiDaoException("The specified instruction file already exists: "
                        + instructionFileFqpn);
            } // if-else file does not arleady exist

        } catch (Exception e) {
            String message = e.getMessage() + "; fqpn: " + instructionFileFqpn;
            throw new GobiiDaoException(message);
        }

        return returnVal;

    } // writeInstructions

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
    public void makeDirectory(String pathName) throws GobiiDaoException {

        if (!doesPathExist(pathName)) {

            File pathToCreate = new File(pathName);

            if (!pathToCreate.mkdirs()) {
                throw new GobiiDaoException("Unable to create directory " + pathName);
            }

            if ((!pathToCreate.canRead()) && !(pathToCreate.setReadable(true, false))) {
                throw new GobiiDaoException("Unable to set read on directory " + pathName);
            }

            if ((!pathToCreate.canWrite()) && !(pathToCreate.setWritable(true, false))) {
                throw new GobiiDaoException("Unable to set write on directory " + pathName);
            }




        } else {
            throw new GobiiDaoException("The specified path already exists: " + pathName);
        }
    }

    @Override
    public List<GobiiExtractorInstruction> getInstructions(String instructionFileFqpn) throws GobiiDaoException {

        List<GobiiExtractorInstruction> returnVal = null;

        try {

            GobiiExtractorInstruction[] instructions = null;

            File file = new File(instructionFileFqpn);
            FileInputStream fileInputStream = new FileInputStream(file);
            org.codehaus.jackson.map.ObjectMapper objectMapper = new org.codehaus.jackson.map.ObjectMapper();
            instructions = objectMapper.readValue(fileInputStream, GobiiExtractorInstruction[].class);

            returnVal = Arrays.asList(instructions);
        } catch (Exception e) {
            String message = e.getMessage() + "; fqpn: " + instructionFileFqpn;
            throw new GobiiDaoException(message);
        }

        return returnVal;

    }

} // ExtractorInstructionsDAOImpl
