package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.filesystem.LoaderInstructionsDAO;
import org.gobiiproject.gobiidtomapping.DtoMapLoaderInstructions;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.dto.container.LoaderInstructionFilesDTO;
import org.gobiiproject.gobiimodel.dto.header.DtoHeaderResponse;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFile;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.gobiiproject.gobiimodel.types.GobiiCropType;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Phil on 4/12/2016.
 */
public class DtoMapLoaderInstructionsImpl implements DtoMapLoaderInstructions {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapLoaderInstructionsImpl.class);

    private final String INSTRUCTION_FILE_EXT = ".json";

    @Autowired
    private LoaderInstructionsDAO loaderInstructionsDAO;

    private void createDirectories(String instructionFileDirectory,
                                   GobiiFile gobiiFile) throws GobiiDaoException {


        if (null != instructionFileDirectory) {
            if (!loaderInstructionsDAO.doesPathExist(instructionFileDirectory)) {
                loaderInstructionsDAO.makeDirectory(instructionFileDirectory);
            } else {
                loaderInstructionsDAO.verifyDirectoryPermissions(instructionFileDirectory);
            }
        }

        if (gobiiFile.isCreateSource()) {
            if (!loaderInstructionsDAO.doesPathExist(gobiiFile.getSource())) {
                loaderInstructionsDAO.makeDirectory(gobiiFile.getSource());
            } else {
                loaderInstructionsDAO.verifyDirectoryPermissions(gobiiFile.getSource());
            }
        }

        if (!loaderInstructionsDAO.doesPathExist(gobiiFile.getDestination())) {
            loaderInstructionsDAO.makeDirectory(gobiiFile.getDestination());
        } else {
            loaderInstructionsDAO.verifyDirectoryPermissions(gobiiFile.getDestination());
        }

    } // createDirectories()


    @Override
    public LoaderInstructionFilesDTO writeInstructions(LoaderInstructionFilesDTO loaderInstructionFilesDTO) {

        LoaderInstructionFilesDTO returnVal = loaderInstructionFilesDTO;

        try {

            ConfigSettings configSettings = new ConfigSettings();

            GobiiCropType currentGobiiCropType = loaderInstructionFilesDTO.getGobiiCropType();
            if (null == currentGobiiCropType) {
                throw new Exception("Loader instruction request does not specify a crop");
            }

            String instructionFileDirectory = configSettings
                    .getCropConfig(currentGobiiCropType)
                    .getLoaderInstructionFilesDirectory();

            String instructionFileFqpn = instructionFileDirectory
                    + loaderInstructionFilesDTO.getInstructionFileName()
                    + INSTRUCTION_FILE_EXT;


            for (GobiiLoaderInstruction currentLoaderInstruction :
                    loaderInstructionFilesDTO.getGobiiLoaderInstructions()) {


                GobiiFile currentGobiiFile = currentLoaderInstruction.getGobiiFile();

                // check that we have all required values
                boolean allValuesSpecified = true;
                if (LineUtils.isNullOrEmpty(returnVal.getInstructionFileName())) {
                    allValuesSpecified = false;
                    returnVal.getDtoHeaderResponse().addStatusMessage(DtoHeaderResponse.StatusLevel.ERROR,
                            DtoHeaderResponse.ValidationStatusType.MISSING_REQUIRED_VALUE,
                            "instruction file name is missing");
                }

                if (LineUtils.isNullOrEmpty(currentGobiiFile.getSource())) {
                    allValuesSpecified = false;
                    returnVal.getDtoHeaderResponse().addStatusMessage(DtoHeaderResponse.StatusLevel.ERROR,
                            DtoHeaderResponse.ValidationStatusType.MISSING_REQUIRED_VALUE,
                            "User file source is missing");
                }

                if (LineUtils.isNullOrEmpty(currentGobiiFile.getDestination())) {
                    allValuesSpecified = false;
                    returnVal.getDtoHeaderResponse().addStatusMessage(DtoHeaderResponse.StatusLevel.ERROR,
                            DtoHeaderResponse.ValidationStatusType.MISSING_REQUIRED_VALUE,
                            "User file destination is missing");
                }

                if (currentGobiiFile.isRequireDirectoriesToExist()) {

                    if (!loaderInstructionsDAO.doesPathExist(currentGobiiFile.getSource())) {
                        allValuesSpecified = false;
                        returnVal.getDtoHeaderResponse().addStatusMessage(DtoHeaderResponse.StatusLevel.ERROR,
                                DtoHeaderResponse.ValidationStatusType.ENTITY_DOES_NOT_EXIST,
                                "require-to-exist was set to true, but the source file path does not exist: "
                                        + currentGobiiFile.getSource());
                    }

                    if (!loaderInstructionsDAO.doesPathExist(currentGobiiFile.getDestination())) {
                        allValuesSpecified = false;
                        returnVal.getDtoHeaderResponse().addStatusMessage(DtoHeaderResponse.StatusLevel.ERROR,
                                DtoHeaderResponse.ValidationStatusType.ENTITY_DOES_NOT_EXIST,
                                "require-to-exist was set to true, but the destination file path does not exist: "
                                        + currentGobiiFile.getDestination());
                    }

                }


                // if so, proceed with processing
                if (allValuesSpecified) {


                    // "source file" is the data file the user may have already uploaded
                    if (currentGobiiFile.isCreateSource()) {

                        createDirectories(instructionFileDirectory,
                                currentGobiiFile);


                    } else {

                        // it's supposed to exist, so we check
                        if (loaderInstructionsDAO.doesPathExist(currentGobiiFile.getSource())) {

                            createDirectories(instructionFileDirectory,
                                    currentGobiiFile);
                        } else {

                            returnVal.getDtoHeaderResponse().addStatusMessage(DtoHeaderResponse.StatusLevel.ERROR,
                                    DtoHeaderResponse.ValidationStatusType.ENTITY_DOES_NOT_EXIST,
                                    "The load file was specified to exist, but does not exist: " +
                                            instructionFileFqpn);

                        } // if-else the source file exists

                    } // if-else we're creating a source file

                } // if we have all the input values we need

            } // iterate instructions/files

            if (0 ==
                    returnVal
                            .getDtoHeaderResponse()
                            .getStatusMessages()
                            .stream()
                            .filter(m -> m.getStatusLevel().equals(DtoHeaderResponse.StatusLevel.ERROR))
                            .collect(Collectors.toList())
                            .size()
                    ) {


                loaderInstructionsDAO.writeInstructions(instructionFileFqpn,
                        returnVal.getGobiiLoaderInstructions());
            }


        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }


        return returnVal;

    } // writeInstructions

    @Override
    public LoaderInstructionFilesDTO readInstructions(LoaderInstructionFilesDTO loaderInstructionFilesDTO) {

        LoaderInstructionFilesDTO returnVal = loaderInstructionFilesDTO;

        try {

            ConfigSettings configSettings = new ConfigSettings();

            String instructionFileFqpn = configSettings
                    .getCurrentCropConfig()
                    .getLoaderInstructionFilesDirectory()
                    + loaderInstructionFilesDTO.getInstructionFileName()
                    + INSTRUCTION_FILE_EXT;


            if (loaderInstructionsDAO.doesPathExist(instructionFileFqpn)) {


                List<GobiiLoaderInstruction> instructions =
                        loaderInstructionsDAO
                                .getInstructions(instructionFileFqpn);

                if (null != instructions) {
                    loaderInstructionFilesDTO.setGobiiLoaderInstructions(instructions);
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
