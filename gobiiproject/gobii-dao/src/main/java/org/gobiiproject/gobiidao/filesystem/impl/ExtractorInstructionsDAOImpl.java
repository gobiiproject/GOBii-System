package org.gobiiproject.gobiidao.filesystem.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.filesystem.ExtractorInstructionsDAO;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiDataSetExtract;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiExtractorInstruction;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.types.GobiiFileType;
import org.gobiiproject.gobiimodel.types.GobiiJobStatus;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Phil on 4/12/2016.
 * Modified by Angel 12/12/2016
 */
public class ExtractorInstructionsDAOImpl implements ExtractorInstructionsDAO {

    private final String LOADER_FILE_EXT = ".json";

    @Override
    public boolean writeInstructions(String instructionFileFqpn,
                                     List<GobiiExtractorInstruction> instructions) throws GobiiDaoException {
        boolean returnVal = false;
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

                        returnVal = true;
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
    public List<GobiiExtractorInstruction> setGobiiJobStatus(boolean applyToAll, List<GobiiExtractorInstruction> instructions, GobiiFileProcessDir gobiiFileProcessDir) {
        List<GobiiExtractorInstruction> returnVal = instructions;
        GobiiJobStatus gobiiJobStatus = getJobStatusForDirectory(gobiiFileProcessDir);
        if(applyToAll){
            for(GobiiExtractorInstruction instruction : returnVal){
                for(GobiiDataSetExtract dataSetExtract: instruction.getDataSetExtracts()){
                    dataSetExtract.setGobiiJobStatus(gobiiJobStatus);
                }
            }
        }else{ //check if the output file(s) exist in the directory specified by the *extractDestinationDirectory* field of the *DataSetExtract* item in the instruction file;
            GobiiJobStatus statusFailed = GobiiJobStatus.FAILED;
            for(GobiiExtractorInstruction instruction: returnVal){
                for(GobiiDataSetExtract dataSetExtract: instruction.getDataSetExtracts()){
                    String extractDestinationDirectory = dataSetExtract.getExtractDestinationDirectory();
                    List<String> datasetExtractFiles = getFileNamesFor("DS"+ Integer.toString(dataSetExtract.getDataSetId()), dataSetExtract.getGobiiFileType());
                        for(String s: datasetExtractFiles){
                            String currentExtractFile = extractDestinationDirectory+s;
                            if(doesPathExist(currentExtractFile))dataSetExtract.setGobiiJobStatus(gobiiJobStatus);
                            else dataSetExtract.setGobiiJobStatus(statusFailed);
                        }
                }
            }
        }
        return returnVal;
    }


    private List<String> getFileNamesFor(String fileName, GobiiFileType gobiiFileType) {
        List<String> fileNames = new ArrayList<String>();
        switch (gobiiFileType.toString().toLowerCase()) {
            case "generic":
                //fileNames.add(fileName+".txt"); to be added
                break;
            case "hapmap":
                fileNames.add(fileName+"hmp.txt");
                break;
            case "flapjack":
                fileNames.add(fileName+".map");
                fileNames.add(fileName+".genotype");
                break;
            case "vcf":
                //fileNames.add(fileName+"hmp.txt"); to be added
                break;
            default:
                throw new GobiiDaoException("Noe extension assigned for GobiiFileType: " + gobiiFileType.toString().toLowerCase());
        }
        return fileNames;
    }

    private GobiiJobStatus getJobStatusForDirectory(GobiiFileProcessDir extractorInstructions) {
        GobiiJobStatus gobiiJobStatus = null;
        switch (extractorInstructions.toString()) {
            case "EXTRACTOR_INPROGRESS":
                gobiiJobStatus = GobiiJobStatus.IN_PROGRESS;
                break;
            case "EXTRACTOR_INSTRUCTIONS":
                gobiiJobStatus = GobiiJobStatus.STARTED;
                break;
            case "EXTRACTOR_DONE":
                gobiiJobStatus = GobiiJobStatus.COMPLETED;
                break;
            default:
                gobiiJobStatus = GobiiJobStatus.FAILED;
        }
        return gobiiJobStatus;
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
