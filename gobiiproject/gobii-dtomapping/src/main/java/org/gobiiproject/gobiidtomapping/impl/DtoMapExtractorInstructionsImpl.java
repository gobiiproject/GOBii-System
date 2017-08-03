package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.filesystem.access.InstructionFileAccess;
import org.gobiiproject.gobiidtomapping.DtoMapContact;
import org.gobiiproject.gobiidtomapping.DtoMapExtractorInstructions;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.headerlesscontainer.ContactDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.ExtractorInstructionFilesDTO;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiDataSetExtract;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiExtractorInstruction;
import org.gobiiproject.gobiimodel.types.GobiiExtractFilterType;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.types.GobiiFileType;
import org.gobiiproject.gobiimodel.types.GobiiJobStatus;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phil on 4/12/2016.
 * Modified by Angel 12/12/2016
 */
public class DtoMapExtractorInstructionsImpl implements DtoMapExtractorInstructions {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapExtractorInstructionsImpl.class);

    private final String INSTRUCTION_FILE_EXT = ".json";
    private final String DATA_FILE_EXT = ".txt";

    @Autowired
    DtoMapContact dtoMapContact;
    private InstructionFileAccess<GobiiExtractorInstruction> instructionFileAccess = new InstructionFileAccess<>(GobiiExtractorInstruction.class);


    private String makeDestinationDirectoryName(String userEmail,
                                                GobiiExtractFilterType gobiiExtractFilterType,
                                                GobiiFileType getGobiiFileType,
                                                String parentDirectory,
                                                String jobId) {

        String returnVal;

        //$outputdir/pdg66/hapmap/whole_dataset/timestamp/<files>

        String userSegment = userEmail.substring(0, userEmail.indexOf('@'));
        String formatName = getGobiiFileType.toString().toLowerCase();


        returnVal = parentDirectory
                + userSegment
                + "/"
                + formatName
                + "/"
                + gobiiExtractFilterType.toString().toLowerCase()
                + "/"
                + jobId;

        return returnVal;
    }

    @Override
    public ExtractorInstructionFilesDTO writeInstructions(String cropType, ExtractorInstructionFilesDTO extractorInstructionFilesDTO) throws GobiiException {

        ExtractorInstructionFilesDTO returnVal = extractorInstructionFilesDTO;

        try {

            ConfigSettings configSettings = new ConfigSettings();

            String instructionFileDirectory = configSettings.getProcessingPath(cropType,
                    GobiiFileProcessDir.EXTRACTOR_INSTRUCTIONS);

            instructionFileAccess.createDirectory(instructionFileDirectory);

            String instructionFileFqpn = instructionFileDirectory
                    + extractorInstructionFilesDTO.getInstructionFileName()
                    + INSTRUCTION_FILE_EXT;


            for (GobiiExtractorInstruction currentExtractorInstruction :
                    extractorInstructionFilesDTO.getGobiiExtractorInstructions()) {

                if (LineUtils.isNullOrEmpty(returnVal.getInstructionFileName())) {
                    throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                            GobiiValidationStatusType.MISSING_REQUIRED_VALUE,
                            "instruction file name is missing");
                }


                if (LineUtils.isNullOrEmpty(currentExtractorInstruction.getGobiiCropType())) {

                    currentExtractorInstruction.setGobiiCropType(cropType);
                }

                if (null != currentExtractorInstruction.getContactId() && currentExtractorInstruction.getContactId() > 0) {

                    ContactDTO contactDTO = dtoMapContact.getContactDetails(currentExtractorInstruction.getContactId());


                    if (!LineUtils.isNullOrEmpty(contactDTO.getEmail())) {

                        currentExtractorInstruction.setContactEmail(contactDTO.getEmail());

                    } else {
                        throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                                GobiiValidationStatusType.MISSING_REQUIRED_VALUE,
                                "The contact record for contactId "
                                        + currentExtractorInstruction.getContactId()
                                        + " does not have an email address");
                    }

                } else {
                    throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                            GobiiValidationStatusType.MISSING_REQUIRED_VALUE,
                            "contactId is missing");
                }


                String extractorFileDestinationLocation = null;


                for (Integer idx = 0;
                     idx < currentExtractorInstruction.getDataSetExtracts().size();
                     idx++) {

                    GobiiDataSetExtract currentGobiiDataSetExtract = currentExtractorInstruction.getDataSetExtracts().get(idx);

                    if (currentGobiiDataSetExtract.getListFileName() != null) {

                        String presumptiveListFileFqpn = instructionFileDirectory + currentGobiiDataSetExtract.getListFileName();

                        if (this.instructionFileAccess.doesPathExist(presumptiveListFileFqpn)) {
                            currentGobiiDataSetExtract.setListFileName(presumptiveListFileFqpn);
                        } else {

                            throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                                    GobiiValidationStatusType.MISSING_REQUIRED_VALUE,
                                    "The specified list file name does not exist on the server: " + presumptiveListFileFqpn);
                        }
                    }

                    if (currentGobiiDataSetExtract.getGobiiExtractFilterType()
                            .equals(GobiiExtractFilterType.WHOLE_DATASET)) {
                        // check that we have all required values
                        if (currentGobiiDataSetExtract.getDataSet() == null) {
                            throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                                    GobiiValidationStatusType.MISSING_REQUIRED_VALUE,
                                    "DataSet is missing");
                        }

                    } else if (currentGobiiDataSetExtract.getGobiiExtractFilterType()
                            .equals(GobiiExtractFilterType.BY_SAMPLE)) {

                        if ((currentGobiiDataSetExtract.getProject() == null)
                                && (currentGobiiDataSetExtract.getPrincipleInvestigator() == null)
                                && (currentGobiiDataSetExtract.getListFileName() == null)
                                && ((currentGobiiDataSetExtract.getSampleList() == null) ||
                                (currentGobiiDataSetExtract.getSampleList().size() <= 0))) {

                            throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                                    GobiiValidationStatusType.MISSING_REQUIRED_VALUE,
                                    "The specified extract type is "
                                            + currentGobiiDataSetExtract.getGobiiExtractFilterType()
                                            + ". Please provide at least one of the following: " +
                                            "Principle Investigator, Project, Sample list, or sample file.");


                        }

                    } else if (currentGobiiDataSetExtract.getGobiiExtractFilterType()
                            .equals(GobiiExtractFilterType.BY_MARKER)) {

                        if ((currentGobiiDataSetExtract.getListFileName() == null)
                                && ((currentGobiiDataSetExtract.getMarkerList() == null) ||
                                (currentGobiiDataSetExtract.getMarkerList().size() <= 0))
                                && ((currentGobiiDataSetExtract.getMarkerGroups() == null)
                                || (currentGobiiDataSetExtract.getMarkerGroups().size() <= 0))) {

                            if (currentGobiiDataSetExtract.getPlatforms() == null ||
                                    currentGobiiDataSetExtract.getPlatforms().size() <= 0) {

                                throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                                        GobiiValidationStatusType.MISSING_REQUIRED_VALUE,
                                        "The specified extract type is "
                                                + currentGobiiDataSetExtract.getGobiiExtractFilterType()
                                                + " but no markers and no platforms are specified");
                            }
                        }

                    } else {
                        throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                                GobiiValidationStatusType.UNKNOWN_ENUM_VALUE,
                                "The specified extraction type is unknown: "
                                        + currentGobiiDataSetExtract.getGobiiExtractFilterType());
                    }

                    String extractionFileDestinationPath;

                    if(!currentExtractorInstruction.isQcCheck()) {
                        extractionFileDestinationPath = configSettings.getProcessingPath(cropType, GobiiFileProcessDir.EXTRACTOR_OUTPUT);
                    } else {
                        extractionFileDestinationPath = configSettings.getProcessingPath(cropType,GobiiFileProcessDir.QC_OUTPUT);
                    }

                    extractorFileDestinationLocation = this.makeDestinationDirectoryName(currentExtractorInstruction.getContactEmail(),
                            currentGobiiDataSetExtract.getGobiiExtractFilterType(),
                            currentGobiiDataSetExtract.getGobiiFileType(),
                            extractionFileDestinationPath,
                            extractorInstructionFilesDTO.getInstructionFileName());

                    if (currentExtractorInstruction.getDataSetExtracts().size() > 1) {
                        extractorFileDestinationLocation += "/" + idx.toString();
                    }


                    if (!instructionFileAccess.doesPathExist(extractorFileDestinationLocation)) {

                        instructionFileAccess.makeDirectory(extractorFileDestinationLocation);

                    } else {
                        instructionFileAccess.verifyDirectoryPermissions(extractorFileDestinationLocation);
                    }
                    
                    currentGobiiDataSetExtract.setExtractDestinationDirectory(extractorFileDestinationLocation);

                }
            } // iterate instructions/files

            if (!instructionFileAccess.doesPathExist(instructionFileFqpn)) {
                InstructionFileAccess<List<GobiiExtractorInstruction>> instructionFileAccess = new InstructionFileAccess<>(GobiiExtractorInstruction.class);

                if (instructionFileAccess.writeInstructions(instructionFileFqpn,
                        returnVal.getGobiiExtractorInstructions())) {

                    returnVal.setJobId(extractorInstructionFilesDTO.getInstructionFileName());
                }
            } else {
                throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.VALIDATION_NOT_UNIQUE,
                        "The specified instruction file already exists: " + instructionFileFqpn);
            }


        } catch (GobiiException e) {
            LOGGER.error("Gobii Maping Error", e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiException(e);
        }


        return returnVal;

    } // writeInstructions

    @Override
    public ExtractorInstructionFilesDTO getStatus(String cropType, String instructionFileName) throws GobiiException {

        ExtractorInstructionFilesDTO returnVal = new ExtractorInstructionFilesDTO();

        ConfigSettings configSettings = new ConfigSettings();
        try {

            String fileDirExtractorInProgressFqpn = configSettings.getProcessingPath(cropType, GobiiFileProcessDir.EXTRACTOR_INPROGRESS)
                    + instructionFileName
                    + INSTRUCTION_FILE_EXT;

            String fileDirExtractorInstructionsFqpn = configSettings.getProcessingPath(cropType, GobiiFileProcessDir.EXTRACTOR_INSTRUCTIONS)
                    + instructionFileName
                    + INSTRUCTION_FILE_EXT;

            String fileDirExtractorDoneFqpn = configSettings.getProcessingPath(cropType, GobiiFileProcessDir.EXTRACTOR_DONE)
                    + instructionFileName
                    + INSTRUCTION_FILE_EXT;

            returnVal.setJobId(instructionFileName);

            returnVal.setInstructionFileName(instructionFileName);


            List<GobiiExtractorInstruction> gobiiExtractorInstructionsWithStatus;
            if (instructionFileAccess.doesPathExist(fileDirExtractorDoneFqpn)) {
                //check if file  is already done

                List<GobiiExtractorInstruction> gobiiExtractorInstructionsFromFile = instructionFileAccess.
                        getInstructions(fileDirExtractorDoneFqpn, GobiiExtractorInstruction[].class);

                gobiiExtractorInstructionsWithStatus = setGobiiExtractorInstructionsStatus(gobiiExtractorInstructionsFromFile,
                        GobiiFileProcessDir.EXTRACTOR_DONE);

                returnVal.setGobiiExtractorInstructions(gobiiExtractorInstructionsWithStatus);

            } else if (instructionFileAccess.doesPathExist(fileDirExtractorInProgressFqpn)) {
                //check if file  is in InProgress

                List<GobiiExtractorInstruction> gobiiExtractorInstructionsFromFile = instructionFileAccess.
                        getInstructions(fileDirExtractorInProgressFqpn, GobiiExtractorInstruction[].class);

                gobiiExtractorInstructionsWithStatus = setGobiiExtractorInstructionsStatus(gobiiExtractorInstructionsFromFile,
                        GobiiFileProcessDir.EXTRACTOR_INPROGRESS);

                returnVal.setGobiiExtractorInstructions(gobiiExtractorInstructionsWithStatus);


            } else if (instructionFileAccess.doesPathExist(fileDirExtractorInstructionsFqpn)) {
                //check if file just started

                List<GobiiExtractorInstruction> gobiiExtractorInstructionsFromFile = instructionFileAccess.
                        getInstructions(fileDirExtractorInstructionsFqpn, GobiiExtractorInstruction[].class);

                gobiiExtractorInstructionsWithStatus = setGobiiExtractorInstructionsStatus(gobiiExtractorInstructionsFromFile,
                        GobiiFileProcessDir.EXTRACTOR_INSTRUCTIONS);

                returnVal.setGobiiExtractorInstructions(gobiiExtractorInstructionsWithStatus);

            }  else {
                throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                        "The specified instruction file does not exist: " +
                                instructionFileName);

            } // if-else instruction file exists

        } catch (GobiiException e) {
            LOGGER.error("Gobii Maping Error", e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiException(e);
        }

        return returnVal;

    }

    private List<GobiiExtractorInstruction> setGobiiExtractorInstructionsStatus(List<GobiiExtractorInstruction> instructions, GobiiFileProcessDir gobiiFileDir) {

        List<GobiiExtractorInstruction> returnVal;

        if (null != instructions) {

            if (gobiiFileDir.equals(GobiiFileProcessDir.EXTRACTOR_DONE)) {

                returnVal = this.setGobiiJobStatus(false, instructions, gobiiFileDir); //individually check and set status of files based on if written in the output directories
            } else {
                returnVal = this.setGobiiJobStatus(true, instructions, gobiiFileDir);
            }

        } else {

            throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                    "The instruction file exists, but could not be read in directory " + gobiiFileDir.toString());
        }

        return returnVal;
    }

    private List<GobiiExtractorInstruction> setGobiiJobStatus(boolean applyToAll, List<GobiiExtractorInstruction> instructions, GobiiFileProcessDir gobiiFileProcessDir) throws GobiiDaoException {
        List<GobiiExtractorInstruction> returnVal = instructions;

        GobiiJobStatus gobiiJobStatus;

        switch (gobiiFileProcessDir) {

            case EXTRACTOR_INPROGRESS:
                gobiiJobStatus = GobiiJobStatus.IN_PROGRESS;
                break;

            case EXTRACTOR_INSTRUCTIONS:
                gobiiJobStatus = GobiiJobStatus.STARTED;
                break;

            case EXTRACTOR_DONE:
                gobiiJobStatus = GobiiJobStatus.COMPLETED;
                break;

            default:
                gobiiJobStatus = GobiiJobStatus.FAILED;
        }

        if (applyToAll) {

            for (GobiiExtractorInstruction instruction : returnVal) {

                for (GobiiDataSetExtract dataSetExtract : instruction.getDataSetExtracts()) {

                    dataSetExtract.setGobiiJobStatus(gobiiJobStatus);
                }
            }
        } else { //check if the output file(s) exist in the directory specified by the *extractDestinationDirectory* field of the *DataSetExtract* item in the instruction file;
            GobiiJobStatus statusFailed = GobiiJobStatus.FAILED;

            for (GobiiExtractorInstruction instruction : returnVal) {

                for (GobiiDataSetExtract dataSetExtract : instruction.getDataSetExtracts()) {

                    String extractDestinationDirectory = dataSetExtract.getExtractDestinationDirectory();

                    List<String> datasetExtractFiles = new ArrayList<String>();

                    String fileName = "DS" + Integer.toString(dataSetExtract.getDataSet().getId());

                    switch (dataSetExtract.getGobiiFileType()) {
                        case GENERIC:
                            //fileNames.add(fileName+".txt"); to be added
                            break;

                        case HAPMAP:
                            datasetExtractFiles.add(fileName + ".hmp.txt");
                            break;

                        case FLAPJACK:
                            datasetExtractFiles.add(fileName + ".map");
                            datasetExtractFiles.add(fileName + ".genotype");

                            break;

                        case VCF:
                            //fileNames.add(fileName+"hmp.txt"); to be added
                            break;

                        default:
                            throw new GobiiDaoException("Noe extension assigned for GobiiFileType: " + dataSetExtract.getGobiiFileType().toString());
                    }


                    for (String currentFileName : datasetExtractFiles) {

                        String currentExtractFile = extractDestinationDirectory + "/" + currentFileName;

                        if (instructionFileAccess.doesPathExist(currentExtractFile)) dataSetExtract.setGobiiJobStatus(gobiiJobStatus);

                        else dataSetExtract.setGobiiJobStatus(statusFailed);
                    }
                }
            }
        }
        return returnVal;
    }

}
