// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.LoaderInstructionFilesDTO;
import org.gobiiproject.gobiimodel.dto.header.DtoHeaderResponse;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFileColumn;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.gobiiproject.gobiimodel.types.DataSetOrientationType;
import org.gobiiproject.gobiimodel.types.DataSetType;
import org.gobiiproject.gobiimodel.types.GobiiColumnType;
import org.gobiiproject.gobiimodel.types.GobiiCropType;
import org.gobiiproject.gobiimodel.types.GobiiFileLocationType;
import org.gobiiproject.gobiimodel.types.GobiiFileType;
import org.gobiiproject.gobiimodel.utils.DateUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.stream.Collectors;

public class DtoRequestGobiiFileLoadInstructionsTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(Authenticator.authenticate());
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }


    @Test
    public void testSendInstructionFile() throws Exception {


        LoaderInstructionFilesDTO loaderInstructionFilesDTOToSend = new LoaderInstructionFilesDTO();


        String instructionFileName = "testapp_" + DateUtils.makeDateIdString();

        String digesterInputDirectory = ClientContext
                .getInstance(null, false)
                .getFileLocationOfCurrenCrop(GobiiFileLocationType.LOADERINSTRUCTION_FILES);


        String digesterOutputDirectory =
                ClientContext.getInstance(null, false)
                        .getFileLocationOfCurrenCrop(GobiiFileLocationType.INTERMEDIATE_FILES);

        loaderInstructionFilesDTOToSend.setInstructionFileName(instructionFileName);

        String instructionOneTableName = "foo_table";
        Integer instructionOneDataSetId = 112;
        GobiiCropType gobiiCropTypeTargetOne = ClientContext.getInstance(null, false).getCurrentClientCropType();

        GobiiLoaderInstruction gobiiLoaderInstructionOne = new GobiiLoaderInstruction();
        gobiiLoaderInstructionOne.setTable(instructionOneTableName);
        gobiiLoaderInstructionOne.setDataSetId(instructionOneDataSetId);
        gobiiLoaderInstructionOne.setGobiiCropType(gobiiCropTypeTargetOne);


        // column one
        String instructionOneColumnOneName = "my_foo_column";
        DataSetType dataSetTypeTableOneColumnOne = DataSetType.IUPAC;
        String findTextTableOneColumnOne = "foo-replace-me";
        String replaceTextTextTableOneColumnOne = "bar-replace-me";
        GobiiFileColumn gobiiFileColumnOne = new GobiiFileColumn()
                .setCCoord(1)
                .setRCoord((1))
                .setGobiiColumnType(GobiiColumnType.VCF_MARKER)
                .setFilterFrom(".*")
                .setFilterTo(".*")
                .setName(instructionOneColumnOneName)
                .setFindText(findTextTableOneColumnOne)
                .setReplaceText(replaceTextTextTableOneColumnOne)
                .setDataSetType(dataSetTypeTableOneColumnOne)
                .setDataSetOrientationType(DataSetOrientationType.MARKER_FAST);

        // column two
        GobiiFileColumn gobiiFileColumnTwo = new GobiiFileColumn()
                .setCCoord(1)
                .setRCoord(1)
                .setGobiiColumnType(GobiiColumnType.CSV_COLUMN)
                .setFilterFrom(".*")
                .setFilterTo(".*")
                .setName("my_bar");

        gobiiLoaderInstructionOne.getGobiiFileColumns().add(gobiiFileColumnOne);
        gobiiLoaderInstructionOne.getGobiiFileColumns().add(gobiiFileColumnTwo);

        // GobiiFile Config
        String testSourceDirName = digesterInputDirectory + "file_one_dir";
        String testDestinationDirName = digesterOutputDirectory + "file_one_dir";
        gobiiLoaderInstructionOne.getGobiiFile().setDelimiter(",")
                .setSource("c://your-dir")
                .setDestination("c://mydir")
                .setGobiiFileType(GobiiFileType.VCF)
                .setSource(testSourceDirName)
                .setDestination(testDestinationDirName);

        // VCF Parameters
        gobiiLoaderInstructionOne.getVcfParameters()
                .setMaf(1.1f)
                .setMinDp(1.1f)
                .setMinQ(1.1f)
                .setRemoveIndels(true)
                .setToIupac(true);

        loaderInstructionFilesDTOToSend
                .getGobiiLoaderInstructions()
                .add(gobiiLoaderInstructionOne);


        // INSTRUCTION ONE END
        // **********************************************************************


        // **********************************************************************
        // INSTRUCTION TWO BEGIN
        GobiiLoaderInstruction gobiiLoaderInstructionTwo = new GobiiLoaderInstruction();

        gobiiLoaderInstructionTwo.setTable("bar_table");

        // column one
        gobiiFileColumnOne = new GobiiFileColumn();
        gobiiFileColumnOne.setCCoord(1);
        gobiiFileColumnOne.setRCoord(1);
        gobiiFileColumnOne.setGobiiColumnType(GobiiColumnType.VCF_MARKER);
        gobiiFileColumnOne.setFilterFrom(".*");
        gobiiFileColumnOne.setFilterTo(".*");
        gobiiFileColumnOne.setName("my_foobar");

        // column two
        gobiiFileColumnTwo = new GobiiFileColumn();
        gobiiFileColumnTwo.setCCoord(1);
        gobiiFileColumnTwo.setRCoord((1));
        gobiiFileColumnTwo.setGobiiColumnType(GobiiColumnType.CSV_COLUMN);
        gobiiFileColumnTwo.setFilterFrom(".*");
        gobiiFileColumnTwo.setFilterTo(".*");
        gobiiFileColumnTwo.setName("my_barfoo");

        gobiiLoaderInstructionTwo.getGobiiFileColumns().add(gobiiFileColumnTwo);
        gobiiLoaderInstructionTwo.getGobiiFileColumns().add(gobiiFileColumnTwo);

        // GobiiFile Config
        gobiiLoaderInstructionTwo.getGobiiFile().setDelimiter(",");
        gobiiLoaderInstructionTwo.getGobiiFile().setSource("c://your-bar-dir");
        gobiiLoaderInstructionTwo.getGobiiFile().setDestination("c://mybardir");
        gobiiLoaderInstructionTwo.getGobiiFile().setGobiiFileType(GobiiFileType.VCF);
        gobiiLoaderInstructionTwo.getGobiiFile().setSource(digesterInputDirectory + "file_two_dir");
        gobiiLoaderInstructionTwo.getGobiiFile().setDestination(digesterOutputDirectory + "file_two_dir");

        // VCF Parameters
        gobiiLoaderInstructionTwo.getVcfParameters().setMaf(1.1f);
        gobiiLoaderInstructionTwo.getVcfParameters().setMinDp(1.1f);
        gobiiLoaderInstructionTwo.getVcfParameters().setMinQ(1.1f);
        gobiiLoaderInstructionTwo.getVcfParameters().setRemoveIndels(true);
        gobiiLoaderInstructionTwo.getVcfParameters().setToIupac(true);

        loaderInstructionFilesDTOToSend.getGobiiLoaderInstructions().add(gobiiLoaderInstructionTwo);


        DtoRequestFileLoadInstructions dtoRequestFileLoadInstructions = new DtoRequestFileLoadInstructions();
        LoaderInstructionFilesDTO loaderInstructionFilesDTOResponse = dtoRequestFileLoadInstructions.process(loaderInstructionFilesDTOToSend);


        Assert.assertNotEquals(null, loaderInstructionFilesDTOResponse);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(loaderInstructionFilesDTOResponse));


        // ************** NOW RETRIFVE THE FILE WE JUST CREATED AND MAKE SURE IT'S REALLY THERE
        LoaderInstructionFilesDTO loaderInstructionFilesDTOretrieve = new LoaderInstructionFilesDTO();
        loaderInstructionFilesDTOretrieve.setProcessType(DtoMetaData.ProcessType.READ);
        loaderInstructionFilesDTOretrieve
                .setInstructionFileName(loaderInstructionFilesDTOResponse.getInstructionFileName());
        LoaderInstructionFilesDTO loaderInstructionFilesDTOretrieveResponse
                = dtoRequestFileLoadInstructions.process(loaderInstructionFilesDTOretrieve);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(loaderInstructionFilesDTOretrieveResponse));

        Assert.assertTrue(
                2 == loaderInstructionFilesDTOretrieveResponse
                        .getGobiiLoaderInstructions()
                        .size()
        );


        Assert.assertTrue(
                loaderInstructionFilesDTOretrieveResponse
                        .getGobiiLoaderInstructions()
                        .get(0)
                        .getDataSetId().equals(instructionOneDataSetId)
        );


        Assert.assertTrue(
                loaderInstructionFilesDTOretrieveResponse
                        .getGobiiLoaderInstructions()
                        .get(0)
                        .getGobiiCropType().equals(gobiiCropTypeTargetOne)
        );


        Assert.assertTrue(
                loaderInstructionFilesDTOretrieveResponse
                        .getGobiiLoaderInstructions()
                        .get(0)
                        .getGobiiFileColumns()
                        .get(0)
                        .getName().equals(instructionOneColumnOneName)
        );

        // ************** VERIFY THAT WE CAN MEANINGFULLY TEST FOR NON EXISTENT DIRECTORIES
        String newInstructionFileName = "testapp_" + DateUtils.makeDateIdString();
        loaderInstructionFilesDTOToSend.setInstructionFileName(newInstructionFileName);

        loaderInstructionFilesDTOToSend
                .getGobiiLoaderInstructions()
                .get(0)
                .getGobiiFile()
                .setSource("foo")
                .setDestination("bar")
                .setRequireDirectoriesToExist(true); // <== should result in validation error

        LoaderInstructionFilesDTO requiredDirectoriesResponse =
                dtoRequestFileLoadInstructions.process(loaderInstructionFilesDTOToSend);

        Assert.assertTrue(
                2 ==
                        requiredDirectoriesResponse
                                .getDtoHeaderResponse()
                                .getStatusMessages()
                                .stream()
                                .filter(r ->
                                        r.getValidationStatusType()
                                                .equals(DtoHeaderResponse.ValidationStatusType.ENTITY_DOES_NOT_EXIST))
                                .collect(Collectors.toList())
                                .size()
        );


        // ************** VERIFY THAT THE DIRECTORIES WE SHOULD HAVE CREATED DO EXIST
        String newInstructionFileNameNoError = "testapp_" + DateUtils.makeDateIdString();
        loaderInstructionFilesDTOToSend.setInstructionFileName(newInstructionFileNameNoError);
        loaderInstructionFilesDTOToSend
                .getGobiiLoaderInstructions()
                .get(0)
                .getGobiiFile()
                .setSource(testSourceDirName)
                .setDestination(testDestinationDirName)
                .setRequireDirectoriesToExist(true); // <== should result in validation error

        LoaderInstructionFilesDTO requiredDirectoriesResponseNoError =
                dtoRequestFileLoadInstructions.process(loaderInstructionFilesDTOToSend);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(requiredDirectoriesResponseNoError));


        // ************** VERIFY THAT WE GET AN ERROR WHEN A FILE ALREADY EXISTS
        loaderInstructionFilesDTOToSend.setInstructionFileName(newInstructionFileNameNoError);
        LoaderInstructionFilesDTO requiredDirectoriesResponseDuplicateNameError =
                dtoRequestFileLoadInstructions.process(loaderInstructionFilesDTOToSend);
        Assert.assertTrue(1 == requiredDirectoriesResponseDuplicateNameError
                .getDtoHeaderResponse()
                .getStatusMessages().size());

        Assert.assertTrue(requiredDirectoriesResponseDuplicateNameError
                .getDtoHeaderResponse()
                .getStatusMessages()
                .get(0)
                .getMessage().toLowerCase().contains("already exists"));


        // ************** VERIFY THAT WE ERROR ON USER INPUT FILE THAT SHOULD EXISTS BUT DOESN'T EXIST

        loaderInstructionFilesDTOToSend.setInstructionFileName("testapp_" + DateUtils.makeDateIdString());

        loaderInstructionFilesDTOToSend
                .getGobiiLoaderInstructions()
                .get(0)
                .getGobiiFile()
                .setSource("foo")
                .setDestination("bar")
                .setCreateSource(false); // <== should result in validation error


        LoaderInstructionFilesDTO testForuserInputFileExistsCausesError =
                dtoRequestFileLoadInstructions.process(loaderInstructionFilesDTOToSend);

        Assert.assertTrue(
                2 ==
                        testForuserInputFileExistsCausesError
                                .getDtoHeaderResponse()
                                .getStatusMessages()
                                .stream()
                                .filter(r ->
                                        r.getValidationStatusType()
                                                .equals(DtoHeaderResponse.ValidationStatusType.ENTITY_DOES_NOT_EXIST))
                                .collect(Collectors.toList())
                                .size()
        );

        // ************** VERIFY THAT WE HANDLE USER INPUT FILE ALREADY EXISTS
        // we're going to test for the existence of the previous instruction file we created;
        // that would not be the real use case; however, it is a file we created on the server
        // so it's handy way to test this functionality
        String instructionFileDirectory = ClientContext
                .getInstance(null, false)
                .getFileLocationOfCurrenCrop(GobiiFileLocationType.LOADERINSTRUCTION_FILES);
        String bogusUserInputFile = instructionFileDirectory + newInstructionFileNameNoError + ".json";

        loaderInstructionFilesDTOToSend.setInstructionFileName("testapp_" + DateUtils.makeDateIdString());
        loaderInstructionFilesDTOToSend
                .getGobiiLoaderInstructions()
                .get(0)
                .getGobiiFile()
                .setSource(bogusUserInputFile)
                .setDestination(testDestinationDirName)
                .setCreateSource(false); // <== should result in validation error

        LoaderInstructionFilesDTO testForuserInputFileExistsNoError =
                dtoRequestFileLoadInstructions.process(loaderInstructionFilesDTOToSend);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(testForuserInputFileExistsNoError));

    } // testGetMarkers()
}
