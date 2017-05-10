// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests.instructions;

import org.apache.commons.io.FileUtils;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiiclient.core.common.ClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.core.common.Authenticator;
import org.gobiiproject.gobiiclient.core.common.TestConfiguration;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.headerlesscontainer.LoaderFilePreviewDTO;
import org.gobiiproject.gobiimodel.types.*;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class DtoRequestLoaderFilePreviewTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(Authenticator.authenticate());

    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }

    @Test
    public void testCreateDirectory() throws Exception {
        LoaderFilePreviewDTO loaderFilePreviewDTO = new LoaderFilePreviewDTO();

        RestUri previewTestUri = ClientContext
                .getInstance(null,false)
                .getUriFactory()
                .resourceByUriIdParam(ServiceRequestId.URL_FILE_LOAD);

        String folderName = TestDtoFactory.getFolderNameWithTimestamp("Loader File Preview Test");
        previewTestUri.setParamValue("id", folderName );
        GobiiEnvelopeRestResource<LoaderFilePreviewDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(previewTestUri);
        PayloadEnvelope<LoaderFilePreviewDTO> resultEnvelope = gobiiEnvelopeRestResource.put(LoaderFilePreviewDTO.class,
                new PayloadEnvelope<>(loaderFilePreviewDTO, GobiiProcessType.CREATE));



        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        Assert.assertTrue("No directory name DTO received", resultEnvelope.getPayload().getData().size() > 0);
        LoaderFilePreviewDTO  resultLoaderFilePreviewDTO = resultEnvelope.getPayload().getData().get(0);
        Assert.assertNotNull(resultLoaderFilePreviewDTO.getDirectoryName());



        PayloadEnvelope<LoaderFilePreviewDTO> resultEnvelopeSecondRequest = gobiiEnvelopeRestResource.put(LoaderFilePreviewDTO.class,
                new PayloadEnvelope<>(loaderFilePreviewDTO, GobiiProcessType.CREATE));

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeSecondRequest.getHeader()));


    }

    //Fails on SYS_INT due to the fact that it physically copies files; this test
    // mechanism does not work unless the unit tests run on the same system as the
    // as the web server.
    @Ignore
    public void testGetFilePreview() throws Exception {
        //Create newFolder
        LoaderFilePreviewDTO loaderFileCreateDTO = new LoaderFilePreviewDTO();
        RestUri previewTestUriCreate = ClientContext
                .getInstance(null,false)
                .getUriFactory()
                .resourceByUriIdParam(ServiceRequestId.URL_FILE_LOAD);
        previewTestUriCreate.setParamValue("id", TestDtoFactory.getFolderNameWithTimestamp("Loader File Preview Test"));
        GobiiEnvelopeRestResource<LoaderFilePreviewDTO> gobiiEnvelopeRestResourceCreate = new GobiiEnvelopeRestResource<>(previewTestUriCreate);
        PayloadEnvelope<LoaderFilePreviewDTO> resultEnvelopeCreate = gobiiEnvelopeRestResourceCreate.put(LoaderFilePreviewDTO.class,
                new PayloadEnvelope<>(loaderFileCreateDTO, GobiiProcessType.CREATE));


        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeCreate.getHeader()));
        Assert.assertTrue("No directory name DTO received", resultEnvelopeCreate.getPayload().getData().size() > 0);
        LoaderFilePreviewDTO  resultLoaderFilePreviewDTOCreated = resultEnvelopeCreate.getPayload().getData().get(0);
        Assert.assertNotNull(resultLoaderFilePreviewDTOCreated.getDirectoryName());

        //get intended path for the created directory
        TestConfiguration testConfiguration = new TestConfiguration();
        String testCrop = testConfiguration.getConfigSettings().getTestExecConfig().getTestCrop();
        String destinationDirectory = testConfiguration.getConfigSettings().getProcessingPath(testCrop, GobiiFileProcessDir.RAW_USER_FILES);
        String createdFileDirectory = destinationDirectory + new File(resultLoaderFilePreviewDTOCreated.getDirectoryName()).getName();

        //copyContentsFromCreatedFolder
        File resourcesDirectory = new File("src/test/resources/datasets");
        File dst = new File(resultLoaderFilePreviewDTOCreated.getDirectoryName());

        FileUtils.copyDirectory(resourcesDirectory, dst);

        //retrieve contents from created name
        RestUri previewTestUri = ClientContext
                .getInstance(null,false)
                .getUriFactory()
                .fileLoaderPreview();
        previewTestUri.setParamValue("directoryName", dst.getName());
        previewTestUri.setParamValue("fileFormat", "txt");

        GobiiEnvelopeRestResource<LoaderFilePreviewDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(previewTestUri);
        PayloadEnvelope<LoaderFilePreviewDTO> resultEnvelope = gobiiEnvelopeRestResource.get(LoaderFilePreviewDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        Assert.assertTrue("No file preview DTO received", resultEnvelopeCreate.getPayload().getData().size() > 0);
        LoaderFilePreviewDTO resultLoaderFilePreviewDTO = resultEnvelope.getPayload().getData().get(0);
        Assert.assertNotNull(resultLoaderFilePreviewDTO.getDirectoryName());
        Assert.assertTrue(resultLoaderFilePreviewDTO.getDirectoryName().replace("C:","").equals(createdFileDirectory.replaceAll("/","\\\\"))); // because the getAbsolutePath function in files returns a windows format path to the file
        Assert.assertNotNull(resultLoaderFilePreviewDTO.getFileList().get(0));

        //compare results read to file
        Assert.assertTrue(checkPreviewFileMatch(resultLoaderFilePreviewDTO.getFilePreview(), resourcesDirectory,resultLoaderFilePreviewDTO.getFileList().get(0)));

        /** TEST hmp.txt format **/

        //Create newFolder
        LoaderFilePreviewDTO loaderFileCreateDTOHmp = new LoaderFilePreviewDTO();
        RestUri previewTestUriCreateHmp = ClientContext
                .getInstance(null,false)
                .getUriFactory()
                .resourceByUriIdParam(ServiceRequestId.URL_FILE_LOAD);
        previewTestUriCreateHmp.setParamValue("id", TestDtoFactory.getFolderNameWithTimestamp("Loader File Preview Test"));
        GobiiEnvelopeRestResource<LoaderFilePreviewDTO> gobiiEnvelopeRestResourceCreateHmp = new GobiiEnvelopeRestResource<>(previewTestUriCreateHmp);
        PayloadEnvelope<LoaderFilePreviewDTO> resultEnvelopeCreateHmp = gobiiEnvelopeRestResourceCreateHmp.put(LoaderFilePreviewDTO.class,
                new PayloadEnvelope<>(loaderFileCreateDTOHmp, GobiiProcessType.CREATE));


        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeCreateHmp.getHeader()));
        Assert.assertTrue("No directory name DTO received", resultEnvelopeCreateHmp.getPayload().getData().size() > 0);
        LoaderFilePreviewDTO  resultLoaderFilePreviewDTOCreatedHmp = resultEnvelopeCreateHmp.getPayload().getData().get(0);
        Assert.assertNotNull(resultLoaderFilePreviewDTOCreatedHmp.getDirectoryName());

        //get intended path for the created directory
        TestConfiguration testConfigurationHmp = new TestConfiguration();
        String testCropHmp = testConfigurationHmp.getConfigSettings().getTestExecConfig().getTestCrop();
        String destinationDirectoryHmp = testConfigurationHmp.getConfigSettings().getProcessingPath(testCropHmp, GobiiFileProcessDir.RAW_USER_FILES);
        String createdFileDirectoryHmp = destinationDirectoryHmp + new File(resultLoaderFilePreviewDTOCreatedHmp.getDirectoryName()).getName();

        File resourceDirectoryHmp = new File("src/test/resources/hmp_dataset");
        File dstHmp = new File(resultLoaderFilePreviewDTOCreatedHmp.getDirectoryName());

        FileUtils.copyDirectory(resourceDirectoryHmp, dstHmp);

        RestUri previewTestUriHmp = ClientContext
                .getInstance(null,false)
                .getUriFactory()
                .fileLoaderPreview();
        previewTestUriHmp.setParamValue("directoryName", dstHmp.getName());
        previewTestUriHmp.setParamValue("fileFormat", "hmp.txt");

        GobiiEnvelopeRestResource<LoaderFilePreviewDTO> gobiiEnvelopeRestResourceHmp = new GobiiEnvelopeRestResource<>(previewTestUriHmp);
        PayloadEnvelope<LoaderFilePreviewDTO> resultEnvelopeHmp = gobiiEnvelopeRestResourceHmp.get(LoaderFilePreviewDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeHmp.getHeader()));
        Assert.assertTrue("No file preview DTO received", resultEnvelopeCreateHmp.getPayload().getData().size() > 0);
        LoaderFilePreviewDTO resultLoaderFilePreviewDTOhmp = resultEnvelopeHmp.getPayload().getData().get(0);
        Assert.assertNotNull(resultLoaderFilePreviewDTOhmp.getDirectoryName());
        Assert.assertTrue(resultLoaderFilePreviewDTOhmp.getDirectoryName().replace("C:","").equals(createdFileDirectoryHmp.replaceAll("/","\\\\"))); // because the getAbsolutePath function in files returns a windows format path to the file
        Assert.assertNotNull(resultLoaderFilePreviewDTOhmp.getFileList().get(0));

        Assert.assertTrue(checkPreviewFileMatch(resultLoaderFilePreviewDTOhmp.getFilePreview(), resourceDirectoryHmp, resultLoaderFilePreviewDTOhmp.getFileList().get(0)));

    }

    public static File getFileOfType(String filePath, File resourcesDirectory) {

        File newFile = new File(filePath);
        for(File f: resourcesDirectory.listFiles()){
            if(f.getName().equals(newFile.getName())){
                return f;
            }
        }
        return null;
    }

    public static boolean checkPreviewFileMatch(List<List<String>> previewFileItems, File resourcesDirectory, String filePath) throws FileNotFoundException {

        Scanner input = new Scanner(System.in);

            int lineCtr = 0; //count lines read
            input = new Scanner(getFileOfType(filePath, resourcesDirectory));
            while (lineCtr<50) { //read first 50 lines only
                int ctr=0; //count words stored
                String line = input.nextLine();
                for(String s: line.split("\t")){
                    if(ctr==50) break;
                    else{
                        if(!previewFileItems.get(lineCtr).get(ctr).equals(s)) return false;
                        ctr++;
                    }
                }
                lineCtr++;
            }
            input.close();


        return true;
    }
}
