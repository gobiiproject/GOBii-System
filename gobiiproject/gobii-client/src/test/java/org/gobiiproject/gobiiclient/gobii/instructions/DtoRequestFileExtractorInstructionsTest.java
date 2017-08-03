package org.gobiiproject.gobiiclient.gobii.instructions;

import org.gobiiproject.gobiiapimodel.hateos.LinkCollection;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.restresources.gobii.GobiiUriFactory;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContextAuth;
import org.gobiiproject.gobiiclient.core.gobii.GobiiTestConfiguration;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.gobii.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.instructions.GobiiFilePropNameId;
import org.gobiiproject.gobiimodel.headerlesscontainer.ExtractorInstructionFilesDTO;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiDataSetExtract;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiExtractorInstruction;
import org.gobiiproject.gobiimodel.types.*;
import org.gobiiproject.gobiimodel.utils.DateUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Phil on 6/15/2016.
 */
public class DtoRequestFileExtractorInstructionsTest {

    private final String INSTRUCTION_FILE_EXT = ".json";
    private static GobiiUriFactory gobiiUriFactory;


    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(GobiiClientContextAuth.authenticate());
        String currentCropContextRoot = GobiiClientContext.getInstance(null, false).getCurrentCropContextRoot();
        gobiiUriFactory = new GobiiUriFactory(currentCropContextRoot);
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(GobiiClientContextAuth.deAuthenticate());
    }


    @Test // fails on SYS_INT
    public void testSendExtractorInstructionFile() throws Exception {


        String cropTypeFromContext = GobiiClientContext.getInstance(null, false).getCurrentClientCropType();

        // ************** DEFINE DTO
        ExtractorInstructionFilesDTO extractorInstructionFilesDTOToSend = new ExtractorInstructionFilesDTO();


        String instructionFileName = "testapp_" + DateUtils.makeDateIdString();
        extractorInstructionFilesDTOToSend.setInstructionFileName(instructionFileName);


        // ************** INSTRUCTION ONE
        GobiiExtractorInstruction gobiiExtractorInstructionOne = new GobiiExtractorInstruction();
        gobiiExtractorInstructionOne.setContactId(1);
        gobiiExtractorInstructionOne.setGobiiCropType(cropTypeFromContext);

        // ************** DATA SET EXTRACT ONE
        GobiiDataSetExtract gobiiDataSetExtractOne = new GobiiDataSetExtract();
        gobiiDataSetExtractOne.setGobiiExtractFilterType(GobiiExtractFilterType.WHOLE_DATASET);
        GobiiFileType DataSetExtractOneFileType = GobiiFileType.HAPMAP;
        gobiiDataSetExtractOne.setGobiiFileType(DataSetExtractOneFileType);
        String dataSetExtractOneName = "1my_foo_Dataset1";
        gobiiDataSetExtractOne.setDataSet(new GobiiFilePropNameId(1,dataSetExtractOneName));
        gobiiDataSetExtractOne.setAccolate(true);


        // ************** DATA SET EXTRACT two
        GobiiDataSetExtract gobiiDataSetExtractTwo = new GobiiDataSetExtract();
        gobiiDataSetExtractTwo.setGobiiExtractFilterType(GobiiExtractFilterType.WHOLE_DATASET);
        GobiiFileType DataSetExtractFileTypeTwo = GobiiFileType.FLAPJACK;
        gobiiDataSetExtractTwo.setGobiiFileType(DataSetExtractFileTypeTwo);
        String DataSetExtractNameTwo = "1my_foo_Dataset2";
        gobiiDataSetExtractTwo.setAccolate(true);
        gobiiDataSetExtractTwo.setDataSet(new GobiiFilePropNameId(2,DataSetExtractNameTwo));


        gobiiExtractorInstructionOne.getDataSetExtracts().add(gobiiDataSetExtractOne);
        gobiiExtractorInstructionOne.getDataSetExtracts().add(gobiiDataSetExtractTwo);

        extractorInstructionFilesDTOToSend.getGobiiExtractorInstructions().add(gobiiExtractorInstructionOne);


        // INSTRUCTION ONE END
        // **********************************************************************


        // **********************************************************************
        // INSTRUCTION TWO BEGIN
        GobiiExtractorInstruction gobiiExtractorInstructionTwo = new GobiiExtractorInstruction();
        gobiiExtractorInstructionTwo.setContactId(1);
        // gobiiExtractorInstructionTwo.setGobiiCropType(cropTypeFromContext); // DON'T SET IT # 2

        // column one
        gobiiDataSetExtractOne = new GobiiDataSetExtract();
        gobiiDataSetExtractOne.setGobiiExtractFilterType(GobiiExtractFilterType.WHOLE_DATASET);
        gobiiDataSetExtractOne.setAccolate(true);
        gobiiDataSetExtractOne.setGobiiFileType(DataSetExtractOneFileType);
        gobiiDataSetExtractOne.setDataSet(new GobiiFilePropNameId(1,"my_foo_2Dataset"));

        // column two
        gobiiDataSetExtractTwo = new GobiiDataSetExtract();
        gobiiDataSetExtractTwo.setGobiiExtractFilterType(GobiiExtractFilterType.WHOLE_DATASET);
        gobiiDataSetExtractTwo.setAccolate(true);
        gobiiDataSetExtractTwo.setGobiiFileType(DataSetExtractFileTypeTwo);
        gobiiDataSetExtractTwo.setDataSet(new GobiiFilePropNameId(2,"my_foo_2Dataset2"));

        gobiiExtractorInstructionTwo.getDataSetExtracts().add(gobiiDataSetExtractOne);
        gobiiExtractorInstructionTwo.getDataSetExtracts().add(gobiiDataSetExtractTwo);

        extractorInstructionFilesDTOToSend.getGobiiExtractorInstructions().add(gobiiExtractorInstructionTwo);

        PayloadEnvelope<ExtractorInstructionFilesDTO> payloadEnvelope = new PayloadEnvelope<>(extractorInstructionFilesDTOToSend, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<ExtractorInstructionFilesDTO> gobiiEnvelopeRestResourceForPost = new GobiiEnvelopeRestResource<>(gobiiUriFactory
                .resourceColl(GobiiServiceRequestId.URL_FILE_EXTRACTOR_INSTRUCTIONS));
        PayloadEnvelope<ExtractorInstructionFilesDTO> extractorInstructionFileDTOResponseEnvelope = gobiiEnvelopeRestResourceForPost.post(ExtractorInstructionFilesDTO.class,
                payloadEnvelope);

        //DtoRequestFileExtractorInstructions dtoRequestFileExtractorInstructions = new DtoRequestFileExtractorInstructions();
        //ExtractorInstructionFilesDTO extractorInstructionFilesDTOResponse = dtoRequestFileExtractorInstructions.process(extractorInstructionFilesDTOToSend);


        Assert.assertNotEquals(null, extractorInstructionFileDTOResponseEnvelope);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(extractorInstructionFileDTOResponseEnvelope.getHeader()));
        // ************** NOW RETRIFVE THE FILE WE JUST CREATED AND MAKE SURE IT'S REALLY THERE, IMPLICITLY TESTING LINK

        LinkCollection linkCollection = extractorInstructionFileDTOResponseEnvelope.getPayload().getLinkCollection();
        Assert.assertNotNull(linkCollection);
        Assert.assertTrue(linkCollection.getLinksPerDataItem().size() == 1);


        RestUri restUriFromLink = gobiiUriFactory.RestUriFromUri(linkCollection.getLinksPerDataItem().get(0).getHref());
        GobiiEnvelopeRestResource<ExtractorInstructionFilesDTO> gobiiEnvelopeRestResourceForGet = new GobiiEnvelopeRestResource<>(restUriFromLink);
        PayloadEnvelope<ExtractorInstructionFilesDTO> resultEnvelope = gobiiEnvelopeRestResourceForGet
                .get(ExtractorInstructionFilesDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));

        ExtractorInstructionFilesDTO extractorInstructionFilesDTOretrieveResponse = resultEnvelope.getPayload().getData().get(0);

        for (GobiiExtractorInstruction currentExtractorInstruction : extractorInstructionFilesDTOretrieveResponse.getGobiiExtractorInstructions()) {
            Assert.assertTrue("The sent crop type of the retrieved crop types does not match",
                    currentExtractorInstruction.getGobiiCropType().equals(cropTypeFromContext));
        }

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
                        .getDataSet().getName()
                        .equals(dataSetExtractOneName)
        );


        // Test link we got from GET
        linkCollection = resultEnvelope.getPayload().getLinkCollection();
        Assert.assertNotNull(linkCollection);
        Assert.assertTrue(linkCollection.getLinksPerDataItem().size() == 1);
        RestUri newRestUriFromLink = gobiiUriFactory.RestUriFromUri(linkCollection.getLinksPerDataItem().get(0).getHref());
        gobiiEnvelopeRestResourceForGet = new GobiiEnvelopeRestResource<>(newRestUriFromLink);
        resultEnvelope = gobiiEnvelopeRestResourceForGet
                .get(ExtractorInstructionFilesDTO.class);

        ExtractorInstructionFilesDTO extractorInstructionFilesDTOFromSecondRetrieval = resultEnvelope.getPayload().getData().get(0);

        Assert.assertTrue(
                2 == extractorInstructionFilesDTOFromSecondRetrieval
                        .getGobiiExtractorInstructions()
                        .size()
        );

        Assert.assertTrue(
                extractorInstructionFilesDTOFromSecondRetrieval
                        .getGobiiExtractorInstructions()
                        .get(0)
                        .getDataSetExtracts()
                        .get(0)
                        .getDataSet().getName()
                        .equals(dataSetExtractOneName)
        );

        // ************** VERIFY THAT WE HANDLE USER INPUT FILE ALREADY EXISTS
        // we're going to test for the existence of the previous instruction file we created;
        // that would not be the real use case; however, it is a file we created on the server
        // so it's handy way to test this functionality

//        ExtractorInstructionFilesDTO testForuserInputFileExistsError =
//                dtoRequestFileExtractorInstructions.process(extractorInstructionFilesDTOToSend);

        payloadEnvelope = new PayloadEnvelope<>(extractorInstructionFilesDTOToSend, GobiiProcessType.CREATE);
        extractorInstructionFileDTOResponseEnvelope = gobiiEnvelopeRestResourceForPost.post(ExtractorInstructionFilesDTO.class,
                payloadEnvelope);


        Assert.assertTrue(1 ==
                extractorInstructionFileDTOResponseEnvelope
                        .getHeader()
                        .getStatus()
                        .getStatusMessages()
                        .stream()
                        .filter(m -> m.getGobiiValidationStatusType().equals(GobiiValidationStatusType.VALIDATION_NOT_UNIQUE))
                        .collect(Collectors.toList())
                        .size());


        //testGetExtractorInstructionStatus(

        RestUri restUriExtractorInstructionsForGetByFilename = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_FILE_EXTRACTOR_STATUS);
        restUriExtractorInstructionsForGetByFilename.setParamValue("id", extractorInstructionFilesDTOFromSecondRetrieval.getInstructionFileName());
        GobiiEnvelopeRestResource<ExtractorInstructionFilesDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriExtractorInstructionsForGetByFilename);
        PayloadEnvelope<ExtractorInstructionFilesDTO> resultEnvelopeForGetStatusByFileName = gobiiEnvelopeRestResourceForGetById
                .get(ExtractorInstructionFilesDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetStatusByFileName.getHeader()));
        ExtractorInstructionFilesDTO resultExtractorInstructionFilesDTO = resultEnvelopeForGetStatusByFileName.getPayload().getData().get(0);

        //testGetExtractorInstructionStatus
        Assert.assertTrue(doesInstructionHaveAnExtractWithStatusEqualTo(GobiiJobStatus.STARTED, resultExtractorInstructionFilesDTO));//test if status is started since file is still in directory: INSTRUCTIONS

        Path extractorInProgressFilePath = Paths.get(getFilePath(GobiiFileProcessDir.EXTRACTOR_INPROGRESS) + resultExtractorInstructionFilesDTO.getJobId() + INSTRUCTION_FILE_EXT);
        Path extractorInstructionsFilePath = Paths.get(getFilePath(GobiiFileProcessDir.EXTRACTOR_INSTRUCTIONS) + resultExtractorInstructionFilesDTO.getJobId() + INSTRUCTION_FILE_EXT);
        Path extractorDoneFilePath = Paths.get(getFilePath(GobiiFileProcessDir.EXTRACTOR_DONE) + resultExtractorInstructionFilesDTO.getJobId() + INSTRUCTION_FILE_EXT);

        //Move instruction file to directory: INPROGRESS
        Files.move(extractorInstructionsFilePath, extractorInProgressFilePath, StandardCopyOption.REPLACE_EXISTING);

        //call another get and testGetExtractorInstructionStatus after moving files
        resultEnvelopeForGetStatusByFileName = gobiiEnvelopeRestResourceForGetById.get(ExtractorInstructionFilesDTO.class);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetStatusByFileName.getHeader()));
        resultExtractorInstructionFilesDTO = resultEnvelopeForGetStatusByFileName.getPayload().getData().get(0);
        Assert.assertTrue(doesInstructionHaveAnExtractWithStatusEqualTo(GobiiJobStatus.IN_PROGRESS, resultExtractorInstructionFilesDTO));//test if status is inProgress since file was moved to directory: IN_PROGRESS

        //Move instruction file to directory: DONE
        Files.move(extractorInProgressFilePath, extractorDoneFilePath, StandardCopyOption.REPLACE_EXISTING);//call another get and testGetExtractorInstructionStatus after moving files
        createFiles(resultExtractorInstructionFilesDTO);//create sampleFiles according to the instruction extractor files
        resultEnvelopeForGetStatusByFileName = gobiiEnvelopeRestResourceForGetById.get(ExtractorInstructionFilesDTO.class);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetStatusByFileName.getHeader()));
        resultExtractorInstructionFilesDTO = resultEnvelopeForGetStatusByFileName.getPayload().getData().get(0);
        Assert.assertTrue(doesInstructionHaveAnExtractWithStatusEqualTo(GobiiJobStatus.COMPLETED, resultExtractorInstructionFilesDTO));//test if status is COMPLETED since file was in DONE and the files for each extract has been created

        deleteFiles(resultExtractorInstructionFilesDTO);//create sampleFiles according to the instruction extractor files


        //test if status is Failed since file was no longer in InProgress or in Instructions but the files for each extract has been deleted
        resultEnvelopeForGetStatusByFileName = gobiiEnvelopeRestResourceForGetById.get(ExtractorInstructionFilesDTO.class);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetStatusByFileName.getHeader()));
        resultExtractorInstructionFilesDTO = resultEnvelopeForGetStatusByFileName.getPayload().getData().get(0);
        Assert.assertTrue("Expected job status " + GobiiJobStatus.FAILED.toString() + ": got "
                + resultExtractorInstructionFilesDTO.getGobiiExtractorInstructions().get(0).getDataSetExtracts().get(0).getGobiiJobStatus().toString(),
                doesInstructionHaveAnExtractWithStatusEqualTo(GobiiJobStatus.FAILED, resultExtractorInstructionFilesDTO));
    }

    private void deleteFiles(ExtractorInstructionFilesDTO resultExtractorInstructionFilesDTO) throws Exception {
        List<GobiiExtractorInstruction> returnVal = resultExtractorInstructionFilesDTO.getGobiiExtractorInstructions();

        for (GobiiExtractorInstruction instruction : returnVal) {

            for (GobiiDataSetExtract dataSetExtract : instruction.getDataSetExtracts()) {

                String extractDestinationDirectory = dataSetExtract.getExtractDestinationDirectory() + "/";
                List<String> dataExtractFileNames = getFileNamesFor("DS" + Integer.toString(dataSetExtract.getDataSet().getId()), dataSetExtract.getGobiiFileType());

                for (String currentFileName : dataExtractFileNames) {
                    String currentExtractFile = extractDestinationDirectory + currentFileName;
                    if (new File(currentExtractFile).exists()) Files.delete(Paths.get(currentExtractFile));

                } // iterate file names

            } // iterate extracts

        } // iterate instructions
    }

    private void createFiles(ExtractorInstructionFilesDTO resultExtractorInstructionFilesDTO) throws Exception {

        List<GobiiExtractorInstruction> returnVal = resultExtractorInstructionFilesDTO.getGobiiExtractorInstructions();

        for (GobiiExtractorInstruction instruction : returnVal) {

            for (GobiiDataSetExtract dataSetExtract : instruction.getDataSetExtracts()) {

                String extractDestinationDirectory = dataSetExtract.getExtractDestinationDirectory() + "/";
                List<String> dataExtractFileNames = getFileNamesFor("DS" + Integer.toString(dataSetExtract.getDataSet().getId()), dataSetExtract.getGobiiFileType());

                for (String currentFileName : dataExtractFileNames) {

                    String currentExtractFile = extractDestinationDirectory + currentFileName;
                    if (!new File(currentExtractFile).exists()) Files.createFile(Paths.get(currentExtractFile));

                } // iterate extract file names

            } // iterate extracts

        } // iterate instructions
    }

    private String getFilePath(GobiiFileProcessDir extractorDirectory) throws Exception {
        String returnVal;

        //get intended path for the created directory
        GobiiTestConfiguration gobiiTestConfiguration = new GobiiTestConfiguration();
        String testCrop = gobiiTestConfiguration.getConfigSettings().getTestExecConfig().getTestCrop();
        returnVal = gobiiTestConfiguration.getConfigSettings().getProcessingPath(testCrop, extractorDirectory);

        //check if directory exists
        File returnValDir = new File(returnVal);
        if (!returnValDir.exists()) {//create directory
            returnValDir.mkdirs();
        }

        return returnVal;
    }

    private boolean doesInstructionHaveAnExtractWithStatusEqualTo(GobiiJobStatus status, ExtractorInstructionFilesDTO resultExtractorInstructionFilesDTO) {

        boolean returnVal = true;

        for (GobiiExtractorInstruction gobiiExtractorInstruction : resultExtractorInstructionFilesDTO.getGobiiExtractorInstructions()) {

            for (GobiiDataSetExtract gobiiDataSetExtract : gobiiExtractorInstruction.getDataSetExtracts()) {

                if (!gobiiDataSetExtract.getGobiiJobStatus().equals(status)) {

                    returnVal = false;
                    break;

                }

            } // iterate extracts

        } // iterate instructions

        return returnVal;
    }

    private List<String> getFileNamesFor(String fileName, GobiiFileType gobiiFileType) throws Exception {

        List<String> fileNames = new ArrayList<String>();

        switch (gobiiFileType) {
            case HAPMAP:
                fileNames.add(fileName + ".hmp.txt");
                break;

            case FLAPJACK:
                fileNames.add(fileName + ".map");
                fileNames.add(fileName + ".genotype");
                break;

            case VCF:
                //fileNames.add(fileName+"hmp.txt"); to be added
                break;

            case GENERIC:
                fileNames.add(fileName + ".txt"); //to be added

            default:
                throw new Exception("Unknown GobiiFileType: " + gobiiFileType.toString());

        }

        return fileNames;
    }
}

