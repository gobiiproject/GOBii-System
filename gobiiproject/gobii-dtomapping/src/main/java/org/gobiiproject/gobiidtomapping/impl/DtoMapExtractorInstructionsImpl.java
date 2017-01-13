package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.filesystem.ExtractorInstructionsDAO;
import org.gobiiproject.gobiidao.resultset.access.RsContactDao;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidtomapping.DtoMapContact;
import org.gobiiproject.gobiidtomapping.DtoMapExtractorInstructions;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.dto.container.ContactDTO;
import org.gobiiproject.gobiimodel.dto.container.ExtractorInstructionFilesDTO;
import org.gobiiproject.gobiimodel.dto.header.DtoHeaderResponse;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiDataSetExtract;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiExtractorInstruction;
import org.gobiiproject.gobiimodel.types.GobiiCropType;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Phil on 4/12/2016.
 */
public class DtoMapExtractorInstructionsImpl implements DtoMapExtractorInstructions {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapExtractorInstructionsImpl.class);

    private final String INSTRUCTION_FILE_EXT = ".json";

    @Autowired
    private ExtractorInstructionsDAO extractorInstructionsDAO;

    @Autowired
    DtoMapContact dtoMapContact;

    private void createDirectories(String instructionFileDirectory) throws GobiiDaoException {


        if (null != instructionFileDirectory) {
            if (!extractorInstructionsDAO.doesPathExist(instructionFileDirectory)) {
                extractorInstructionsDAO.makeDirectory(instructionFileDirectory);
            } else {
                extractorInstructionsDAO.verifyDirectoryPermissions(instructionFileDirectory);
            }
        }

    } // createDirectories()


    @Override
    public ExtractorInstructionFilesDTO writeInstructions(ExtractorInstructionFilesDTO extractorInstructionFilesDTO) {

        ExtractorInstructionFilesDTO returnVal = extractorInstructionFilesDTO;

        try {

            ConfigSettings configSettings = new ConfigSettings();

            GobiiCropType currentGobiiCropType = extractorInstructionFilesDTO.getGobiiCropType();
            if (null == currentGobiiCropType) {
                throw new Exception("Extractor instruction request does not specify a crop");
            }

            String instructionFileDirectory = configSettings
                    .getCropConfig(currentGobiiCropType)
                    .getExtractorInstructionFilesDirectory();

            createDirectories(instructionFileDirectory);

            String instructionFileFqpn = instructionFileDirectory
                    + extractorInstructionFilesDTO.getInstructionFileName()
                    + INSTRUCTION_FILE_EXT;


            boolean allValuesSpecified = true;
            for (GobiiExtractorInstruction currentExtractorInstruction :
                    extractorInstructionFilesDTO.getGobiiExtractorInstructions()) {

                if (LineUtils.isNullOrEmpty(returnVal.getInstructionFileName())) {
                    allValuesSpecified = false;
                    returnVal.getDtoHeaderResponse().addStatusMessage(DtoHeaderResponse.StatusLevel.ERROR,
                            DtoHeaderResponse.ValidationStatusType.MISSING_REQUIRED_VALUE,
                            "instruction file name is missing");
                }

                if (null != currentExtractorInstruction.getContactId() && currentExtractorInstruction.getContactId() > 0) {
                    ContactDTO contactDTO = new ContactDTO();
                    contactDTO.setContactId(currentExtractorInstruction.getContactId());
                    contactDTO = dtoMapContact.getContactDetails(contactDTO);
                    if (!LineUtils.isNullOrEmpty(contactDTO.getEmail())) {
                        currentExtractorInstruction.setContactEmail(contactDTO.getEmail());
                    } else {
                        allValuesSpecified = false;
                        returnVal.getDtoHeaderResponse().addStatusMessage(DtoHeaderResponse.StatusLevel.ERROR,
                                DtoHeaderResponse.ValidationStatusType.MISSING_REQUIRED_VALUE,
                                "The contact record for contactId "
                                        + currentExtractorInstruction.getContactId()
                                        + " does not have an email address");
                    }

                } else {
                    allValuesSpecified = false;
                    returnVal.getDtoHeaderResponse().addStatusMessage(DtoHeaderResponse.StatusLevel.ERROR,
                            DtoHeaderResponse.ValidationStatusType.MISSING_REQUIRED_VALUE,
                            "contactId is missing");
                }

                String extractionFileDestinationPath = configSettings
                        .getCropConfig(returnVal.getGobiiCropType())
                        .getExtractorInstructionFilesOutputDirectory();


                for (GobiiDataSetExtract currentGobiiDataSetExtract :
                        currentExtractorInstruction.getDataSetExtracts()) {

                    // check that we have all required values
                    if (LineUtils.isNullOrEmpty(currentGobiiDataSetExtract.getDataSetName())) {
                        allValuesSpecified = false;
                        returnVal.getDtoHeaderResponse().addStatusMessage(DtoHeaderResponse.StatusLevel.ERROR,
                                DtoHeaderResponse.ValidationStatusType.MISSING_REQUIRED_VALUE,
                                "DataSet name is missing");
                    }

                    if (LineUtils.isNullOrEmpty(Integer.toString(currentGobiiDataSetExtract.getDataSetId()))) {
                        allValuesSpecified = false;
                        returnVal.getDtoHeaderResponse().addStatusMessage(DtoHeaderResponse.StatusLevel.ERROR,
                                DtoHeaderResponse.ValidationStatusType.MISSING_REQUIRED_VALUE,
                                "Dataset ID is missing");
                    }

                    String formatName = currentGobiiDataSetExtract.getGobiiFileType().toString().toLowerCase();
                    String dataSetId = currentGobiiDataSetExtract.getDataSetId().toString();
                    String extractorFileDestinationLocation =
                            extractionFileDestinationPath
                                    + formatName
                                    + "/"
                                    + "ds_"
                                    + dataSetId
                                    + "/";

                    if (!extractorInstructionsDAO.doesPathExist(extractorFileDestinationLocation)) {
                        extractorInstructionsDAO.makeDirectory(extractorFileDestinationLocation);
                    } else {
                        extractorInstructionsDAO.verifyDirectoryPermissions(extractorFileDestinationLocation);
                    }


                    currentGobiiDataSetExtract.setExtractDestinationDirectory(extractorFileDestinationLocation);

                }


            } // iterate instructions/files

            if (allValuesSpecified) {

                if (0 ==
                        returnVal
                                .getDtoHeaderResponse()
                                .getStatusMessages()
                                .stream()
                                .filter(m -> m.getStatusLevel().equals(DtoHeaderResponse.StatusLevel.ERROR))
                                .collect(Collectors.toList())
                                .size()
                        ) {


                    if (!extractorInstructionsDAO.doesPathExist(instructionFileFqpn)) {

                        extractorInstructionsDAO.writeInstructions(instructionFileFqpn,
                                returnVal.getGobiiExtractorInstructions());
                    } else {
                        returnVal.getDtoHeaderResponse().addStatusMessage(DtoHeaderResponse.StatusLevel.ERROR,
                                DtoHeaderResponse.ValidationStatusType.VALIDATION_NOT_UNIQUE,
                                "The specified instruction file already exists: " + instructionFileFqpn);
                    }
                }

            } // if all values were specified

        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }


        return returnVal;

    } // writeInstructions

    @Override
    public ExtractorInstructionFilesDTO readInstructions(ExtractorInstructionFilesDTO extractorInstructionFilesDTO) {

        ExtractorInstructionFilesDTO returnVal = extractorInstructionFilesDTO;

        try {

            ConfigSettings configSettings = new ConfigSettings();

            String instructionFileFqpn = configSettings
                    .getCurrentCropConfig()
                    .getExtractorInstructionFilesDirectory()
                    + extractorInstructionFilesDTO.getInstructionFileName()
                    + INSTRUCTION_FILE_EXT;


            if (extractorInstructionsDAO.doesPathExist(instructionFileFqpn)) {


                List<GobiiExtractorInstruction> instructions =
                        extractorInstructionsDAO
                                .getInstructions(instructionFileFqpn);

                if (null != instructions) {
                    extractorInstructionFilesDTO.setGobiiExtractorInstructions(instructions);
                } else {
                    returnVal.getDtoHeaderResponse()
                            .addStatusMessage(DtoHeaderResponse.StatusLevel.ERROR,
                                    DtoHeaderResponse.ValidationStatusType.ENTITY_DOES_NOT_EXIST,
                                    "The instruction file exists, but could not be read: " +
                                            instructionFileFqpn);
                }

            } else {

                returnVal.getDtoHeaderResponse()
                        .addStatusMessage(DtoHeaderResponse.StatusLevel.ERROR,
                                DtoHeaderResponse.ValidationStatusType.ENTITY_DOES_NOT_EXIST,
                                "The specified instruction file does not exist: " +
                                        instructionFileFqpn);

            } // if-else instruction file exists

        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }

        return returnVal;
    }
}
