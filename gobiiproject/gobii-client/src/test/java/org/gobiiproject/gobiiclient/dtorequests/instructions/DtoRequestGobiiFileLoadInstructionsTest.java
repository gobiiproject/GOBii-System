// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests.instructions;

import org.gobiiproject.gobiiapimodel.hateos.LinkCollection;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.restresources.UriFactory;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiiclient.core.common.ClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.headerlesscontainer.LoaderInstructionFilesDTO;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFileColumn;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiimodel.types.DataSetOrientationType;
import org.gobiiproject.gobiimodel.types.DataSetType;
import org.gobiiproject.gobiimodel.types.GobiiColumnType;

import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.types.GobiiFileType;
import org.gobiiproject.gobiimodel.utils.DateUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class DtoRequestGobiiFileLoadInstructionsTest {

    private static UriFactory uriFactory;

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(Authenticator.authenticate());
        String currentCropContextRoot = ClientContext.getInstance(null, false).getCurrentCropContextRoot();
        DtoRequestGobiiFileLoadInstructionsTest.uriFactory = new UriFactory(currentCropContextRoot);

    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }

    @Test
    public void testSendLoaderInstructionFile() throws Exception {


        LoaderInstructionFilesDTO loaderInstructionFilesDTOToSend = new LoaderInstructionFilesDTO();


        String instructionFileName = "testapp_" + DateUtils.makeDateIdString();

        String digesterInputDirectory = ClientContext
                .getInstance(null, false)
                .getFileLocationOfCurrenCrop(GobiiFileProcessDir.LOADER_INSTRUCTIONS);


        String digesterOutputDirectory =
                ClientContext.getInstance(null, false)
                        .getFileLocationOfCurrenCrop(GobiiFileProcessDir.LOADER_INTERMEDIATE_FILES);

        loaderInstructionFilesDTOToSend.setInstructionFileName(instructionFileName);

        String instructionOneTableName = "foo_table";
        Integer instructionOneDataSetId = 112;
        String gobiiCropType = ClientContext.getInstance(null, false).getCurrentClientCropType();

        GobiiLoaderInstruction gobiiLoaderInstructionOne = new GobiiLoaderInstruction();
        gobiiLoaderInstructionOne.setTable(instructionOneTableName);
        gobiiLoaderInstructionOne.setDataSetId(instructionOneDataSetId);
        gobiiLoaderInstructionOne.setGobiiCropType(gobiiCropType);


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

        PayloadEnvelope<LoaderInstructionFilesDTO> payloadEnvelope = new PayloadEnvelope<>(loaderInstructionFilesDTOToSend, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<LoaderInstructionFilesDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(DtoRequestGobiiFileLoadInstructionsTest.uriFactory.resourceColl(ServiceRequestId.URL_FILE_LOAD_INSTRUCTIONS));
        PayloadEnvelope<LoaderInstructionFilesDTO> loaderInstructionFileDTOResponseEnvelope = gobiiEnvelopeRestResource.post(LoaderInstructionFilesDTO.class,
                payloadEnvelope);


        Assert.assertNotEquals(null, loaderInstructionFileDTOResponseEnvelope);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(loaderInstructionFileDTOResponseEnvelope.getHeader()));


        LoaderInstructionFilesDTO loaderInstructionFileDTOResponse = loaderInstructionFileDTOResponseEnvelope.getPayload().getData().get(0);
        Assert.assertNotEquals(null, loaderInstructionFileDTOResponse);

        //Now re-retrieve with the link we got back
        Assert.assertNotNull(loaderInstructionFileDTOResponseEnvelope.getPayload().getLinkCollection());
        Assert.assertNotNull(loaderInstructionFileDTOResponseEnvelope.getPayload().getLinkCollection().getLinksPerDataItem());
//        Assert.assertNotNull(loaderInstructionFileDTOResponseEnvelope.getPayload().getLinkCollection().getLinksPerDataItem().get(0));

        // ************** NOW RETRIFVE THE FILE WE JUST CREATED AND MAKE SURE IT'S REALLY THERE USING THE HATEOS LINKS

        LinkCollection linkCollection = loaderInstructionFileDTOResponseEnvelope.getPayload().getLinkCollection();
        Assert.assertNotNull(linkCollection);
        Assert.assertTrue(linkCollection.getLinksPerDataItem().size() == 1);
        RestUri restUriFromLink = uriFactory.RestUriFromUri(linkCollection.getLinksPerDataItem().get(0).getHref());
        GobiiEnvelopeRestResource<LoaderInstructionFilesDTO> gobiiEnvelopeRestResourceForGet = new GobiiEnvelopeRestResource<>(restUriFromLink);
        PayloadEnvelope<LoaderInstructionFilesDTO> resultEnvelope = gobiiEnvelopeRestResourceForGet
                .get(LoaderInstructionFilesDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        LoaderInstructionFilesDTO loaderInstructionFilesDTOretrieveResponse = resultEnvelope.getPayload().getData().get(0);

//        LoaderInstructionFilesDTO loaderInstructionFilesDTOretrieve = new LoaderInstructionFilesDTO();
//        loaderInstructionFilesDTOretrieve
//                .setInstructionFileName(loaderInstructionFileDTOResponseEnvelope.getPayload().getData().get(0).getInstructionFileName());
//
//        RestUri restUriLoader = DtoRequestGobiiFileLoadInstructionsTest
//                .uriFactory.loaderInstructionsByInstructionFileName();
//        restUriLoader.setParamValue("instructionFileName", loaderInstructionFilesDTOretrieve.getInstructionFileName());
//        gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriLoader);
//        PayloadEnvelope<LoaderInstructionFilesDTO> resultEnvelope = gobiiEnvelopeRestResource
//                .get(LoaderInstructionFilesDTO.class);
//
//        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
//        LoaderInstructionFilesDTO loaderInstructionFilesDTOretrieveResponse = resultEnvelope.getPayload().getData().get(0);
        Assert.assertNotNull(loaderInstructionFilesDTOretrieveResponse.getGobiiLoaderInstructions());

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(loaderInstructionFileDTOResponseEnvelope.getHeader()));

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


        // we only set it on one, but the server should have set it for the second one
        Assert.assertTrue(2 ==
                loaderInstructionFilesDTOretrieveResponse
                        .getGobiiLoaderInstructions()
                        .stream()
                        .filter(i -> i.getGobiiCropType().equals(gobiiCropType))
                        .count()
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


        payloadEnvelope = new PayloadEnvelope<>(loaderInstructionFilesDTOToSend, GobiiProcessType.CREATE);
        gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(DtoRequestGobiiFileLoadInstructionsTest.uriFactory.resourceColl(ServiceRequestId.URL_FILE_LOAD_INSTRUCTIONS));
        loaderInstructionFileDTOResponseEnvelope = gobiiEnvelopeRestResource.post(LoaderInstructionFilesDTO.class,
                payloadEnvelope);

        Assert.assertTrue("Non-existent files are not being reported properly: there are not two ENTITY_DOES_NOT_EXIST errors",
                1 ==
                        loaderInstructionFileDTOResponseEnvelope.getHeader().getStatus()
                                .getStatusMessages()
                                .stream()
                                .filter(r ->
                                        r.getGobiiValidationStatusType()
                                                .equals(GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST))
                                .count()

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

        payloadEnvelope = new PayloadEnvelope<>(loaderInstructionFilesDTOToSend, GobiiProcessType.CREATE);
        gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(DtoRequestGobiiFileLoadInstructionsTest.uriFactory.resourceColl(ServiceRequestId.URL_FILE_LOAD_INSTRUCTIONS));
        loaderInstructionFileDTOResponseEnvelope = gobiiEnvelopeRestResource.post(LoaderInstructionFilesDTO.class,
                payloadEnvelope);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(loaderInstructionFileDTOResponseEnvelope.getHeader()));

        // ************** VERIFY THAT WE GET AN ERROR WHEN A FILE ALREADY EXISTS
        loaderInstructionFilesDTOToSend.setInstructionFileName(newInstructionFileNameNoError);
        payloadEnvelope = new PayloadEnvelope<>(loaderInstructionFilesDTOToSend, GobiiProcessType.CREATE);
        gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(DtoRequestGobiiFileLoadInstructionsTest.uriFactory.resourceColl(ServiceRequestId.URL_FILE_LOAD_INSTRUCTIONS));
        loaderInstructionFileDTOResponseEnvelope = gobiiEnvelopeRestResource.post(LoaderInstructionFilesDTO.class,
                payloadEnvelope);


        Assert.assertTrue("At least one error message should contain 'already exists'",
                loaderInstructionFileDTOResponseEnvelope.getHeader()
                        .getStatus()
                        .getStatusMessages()
                        .stream()
                        .filter(m -> m.getMessage().toLowerCase().contains("already exists"))
                        .count()
                        > 0);


        // ************** VERIFY THAT WE ERROR ON USER INPUT FILE THAT SHOULD EXISTS BUT DOESN'T EXIST

        loaderInstructionFilesDTOToSend.setInstructionFileName("testapp_" + DateUtils.makeDateIdString());

        loaderInstructionFilesDTOToSend
                .getGobiiLoaderInstructions()
                .get(0)
                .getGobiiFile()
                .setSource("foo")
                .setDestination("bar")
                .setCreateSource(false); // <== should result in validation error


        payloadEnvelope = new PayloadEnvelope<>(loaderInstructionFilesDTOToSend, GobiiProcessType.CREATE);
        gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(DtoRequestGobiiFileLoadInstructionsTest.uriFactory.resourceColl(ServiceRequestId.URL_FILE_LOAD_INSTRUCTIONS));
        PayloadEnvelope<LoaderInstructionFilesDTO> testForuserInputFileExistsCausesErrorEnvelopse = gobiiEnvelopeRestResource.post(LoaderInstructionFilesDTO.class,
                payloadEnvelope);

        Assert.assertTrue(
                1 ==
                        testForuserInputFileExistsCausesErrorEnvelopse.getHeader()
                                .getStatus()
                                .getStatusMessages()
                                .stream()
                                .filter(r ->
                                        r.getGobiiValidationStatusType()
                                                .equals(GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST))
                                .count()
        );

        // ************** VERIFY THAT WE HANDLE USER INPUT FILE ALREADY EXISTS
        // we're going to test for the existence of the previous instruction file we created;
        // that would not be the real use case; however, it is a file we created on the server
        // so it's handy way to test this functionality
        String instructionFileDirectory = ClientContext
                .getInstance(null, false)
                .getFileLocationOfCurrenCrop(GobiiFileProcessDir.LOADER_INSTRUCTIONS);
        String bogusUserInputFile = instructionFileDirectory + newInstructionFileNameNoError + ".json";

        loaderInstructionFilesDTOToSend.setInstructionFileName("testapp_" + DateUtils.makeDateIdString());
        loaderInstructionFilesDTOToSend
                .getGobiiLoaderInstructions()
                .get(0)
                .getGobiiFile()
                .setSource(bogusUserInputFile)
                .setDestination(testDestinationDirName)
                .setCreateSource(false); // <== should result in validation error

        payloadEnvelope = new PayloadEnvelope<>(loaderInstructionFilesDTOToSend, GobiiProcessType.CREATE);
        gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(DtoRequestGobiiFileLoadInstructionsTest.uriFactory.resourceColl(ServiceRequestId.URL_FILE_LOAD_INSTRUCTIONS));
        PayloadEnvelope<LoaderInstructionFilesDTO> testForuserInputFileExistsNoErrorEnvelope = gobiiEnvelopeRestResource.post(LoaderInstructionFilesDTO.class,
                payloadEnvelope);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(testForuserInputFileExistsNoErrorEnvelope.getHeader()));

    }
}
