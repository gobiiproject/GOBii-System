package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.ExtractorInstructionFilesDTO;
import org.gobiiproject.gobiimodel.dto.header.DtoHeaderResponse;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiDataSetExtract;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiExtractorInstruction;
import org.gobiiproject.gobiimodel.types.GobiiFileLocationType;
import org.gobiiproject.gobiimodel.types.GobiiFileType;
import org.gobiiproject.gobiimodel.utils.DateUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.stream.Collectors;

/**
 * Created by Phil on 6/15/2016.
 */
public class DtoRequestFileExtractorInstructionsTest {

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


        // ************** DEFINE DTO
        ExtractorInstructionFilesDTO extractorInstructionFilesDTOToSend = new ExtractorInstructionFilesDTO();


        String instructionFileName = "testapp_" + DateUtils.makeDateIdString();
        extractorInstructionFilesDTOToSend.setInstructionFileName(instructionFileName);



        // ************** INSTRUCTION ONE
        GobiiExtractorInstruction gobiiExtractorInstructionOne = new GobiiExtractorInstruction();
        gobiiExtractorInstructionOne.setContactId(1);

        // ************** DATA SET EXTRACT ONE
        GobiiDataSetExtract gobiiDataSetExtractOne = new GobiiDataSetExtract();
        GobiiFileType DataSetExtractOneFileType = GobiiFileType.GENERIC;
        gobiiDataSetExtractOne.setGobiiFileType(DataSetExtractOneFileType);
        String DataSetExtractOneName = "my_foo_Dataset";
        gobiiDataSetExtractOne.setDataSetName(DataSetExtractOneName);
        gobiiDataSetExtractOne.setAccolate(true);
        gobiiDataSetExtractOne.setDataSetId(1);


        // ************** DATA SET EXTRACT two
        GobiiDataSetExtract gobiiDataSetExtractTwo = new GobiiDataSetExtract();
        GobiiFileType DataSetExtractFileTypeTwo = GobiiFileType.GENERIC;
        gobiiDataSetExtractTwo.setGobiiFileType(DataSetExtractFileTypeTwo);
        String DataSetExtractNameTwo = "my_foo_Dataset2";
        gobiiDataSetExtractTwo.setDataSetName(DataSetExtractNameTwo);
        gobiiDataSetExtractTwo.setAccolate(true);
        gobiiDataSetExtractTwo.setDataSetId(1);


        gobiiExtractorInstructionOne.getDataSetExtracts().add(gobiiDataSetExtractOne);
        gobiiExtractorInstructionOne.getDataSetExtracts().add(gobiiDataSetExtractTwo);

        extractorInstructionFilesDTOToSend.getGobiiExtractorInstructions().add(gobiiExtractorInstructionOne);


        // INSTRUCTION ONE END
        // **********************************************************************


        // **********************************************************************
        // INSTRUCTION TWO BEGIN
        GobiiExtractorInstruction gobiiExtractorInstructionTwo = new GobiiExtractorInstruction();
        gobiiExtractorInstructionTwo.setContactId(1);

        // column one
        gobiiDataSetExtractOne = new GobiiDataSetExtract();
        gobiiDataSetExtractOne.setDataSetName("my_foo_2Dataset");
        gobiiDataSetExtractOne.setAccolate(true);
        gobiiDataSetExtractOne.setGobiiFileType(DataSetExtractOneFileType);
        gobiiDataSetExtractOne.setDataSetId(1);

        // column two
        gobiiDataSetExtractTwo = new GobiiDataSetExtract();
        gobiiDataSetExtractTwo.setDataSetName("my_foo_2Dataset2");
        gobiiDataSetExtractTwo.setAccolate(true);
        gobiiDataSetExtractTwo.setGobiiFileType(DataSetExtractFileTypeTwo);
        gobiiDataSetExtractTwo.setDataSetId(1);

        gobiiExtractorInstructionTwo.getDataSetExtracts().add(gobiiDataSetExtractOne);
        gobiiExtractorInstructionTwo.getDataSetExtracts().add(gobiiDataSetExtractTwo);

        extractorInstructionFilesDTOToSend.getGobiiExtractorInstructions().add(gobiiExtractorInstructionTwo);


        DtoRequestFileExtractorInstructions dtoRequestFileExtractorInstructions = new DtoRequestFileExtractorInstructions();
        ExtractorInstructionFilesDTO extractorInstructionFilesDTOResponse = dtoRequestFileExtractorInstructions.process(extractorInstructionFilesDTOToSend);


        Assert.assertNotEquals(null, extractorInstructionFilesDTOResponse);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(extractorInstructionFilesDTOResponse));


        // ************** NOW RETRIFVE THE FILE WE JUST CREATED AND MAKE SURE IT'S REALLY THERE
        ExtractorInstructionFilesDTO extractorInstructionFilesDTOretrieve = new ExtractorInstructionFilesDTO();
        extractorInstructionFilesDTOretrieve.setProcessType(DtoMetaData.ProcessType.READ);
        extractorInstructionFilesDTOretrieve
                .setInstructionFileName(extractorInstructionFilesDTOResponse.getInstructionFileName());
        ExtractorInstructionFilesDTO extractorInstructionFilesDTOretrieveResponse
                = dtoRequestFileExtractorInstructions.process(extractorInstructionFilesDTOretrieve);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(extractorInstructionFilesDTOretrieveResponse));

        Assert.assertTrue(
                2 == extractorInstructionFilesDTOretrieveResponse
                        .getGobiiExtractorInstructions()
                        .size()
        );

        Assert.assertTrue(
                extractorInstructionFilesDTOretrieveResponse
                        .getGobiiExtractorInstructions()
                        .get(0)
                        .getDataSetExtracts()
                        .get(0)
                        .getDataSetName()
                        .equals(DataSetExtractOneName)
        );


           // ************** VERIFY THAT WE HANDLE USER INPUT FILE ALREADY EXISTS
        // we're going to test for the existence of the previous instruction file we created;
        // that would not be the real use case; however, it is a file we created on the server
        // so it's handy way to test this functionality
        ExtractorInstructionFilesDTO testForuserInputFileExistsError =
                dtoRequestFileExtractorInstructions.process(extractorInstructionFilesDTOToSend);

        Assert.assertTrue(1 ==
        testForuserInputFileExistsError
                .getDtoHeaderResponse()
                .getStatusMessages()
                .stream()
                .filter(m -> m.getValidationStatusType().equals(DtoHeaderResponse.ValidationStatusType.VALIDATION_NOT_UNIQUE))
                .collect(Collectors.toList())
                .size() );


    } // testGetMarkers()

}
