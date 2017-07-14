package org.gobiiproject.gobiiclient.dtorequests.instructions;

import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.restresources.UriFactory;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiiclient.core.common.ClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.core.common.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.headerlesscontainer.QCInstructionsDTO;
import org.gobiiproject.gobiimodel.types.*;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class DtoRequestFileQCInstructionsTest {

    private static UriFactory uriFactory;


    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(Authenticator.authenticate());
        String currentCropContextRoot = ClientContext.getInstance(null, false).getCurrentCropContextRoot();
        uriFactory = new UriFactory(currentCropContextRoot);
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }


    //@Ignore
    @Test
    public void create() throws Exception {

        QCInstructionsDTO qcInstructionsDTO = TestDtoFactory.makePopulatedQCInstructionsDTO();

        qcInstructionsDTO.setGobiiJobStatus(GobiiJobStatus.COMPLETED); // set qc job status to completed, to write instructions

        PayloadEnvelope<QCInstructionsDTO> payloadEnvelope = new PayloadEnvelope<>(qcInstructionsDTO, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<QCInstructionsDTO> restResourceForPost = new GobiiEnvelopeRestResource<>(uriFactory
                .resourceColl(ServiceRequestId.URL_FILE_QC_INSTRUCTIONS));
        PayloadEnvelope<QCInstructionsDTO> qcInstructionFileDTOResponseEnvelope = restResourceForPost.post(QCInstructionsDTO.class,
                payloadEnvelope);

        Assert.assertNotEquals(null, qcInstructionFileDTOResponseEnvelope.getPayload().getData().get(0));
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(qcInstructionFileDTOResponseEnvelope.getHeader()));

        //get intended path for the created directory and check if file has been created
        /*TestConfiguration testConfiguration = new TestConfiguration();
        String testCrop = testConfiguration.getConfigSettings().getTestExecConfig().getTestCrop();
        String destinationDirectory = testConfiguration.getConfigSettings().getProcessingPath(testCrop, GobiiFileProcessDir.QC_NOTIFICATIONS);
        String createdFile = destinationDirectory +  qcInstructionsDTO.getGobiiQCComplete().getDataFileName()+".json";
        Assert.assertTrue(new File(createdFile).exists());*/

        RestUri qcGetUri = ClientContext
                .getInstance(null,false)
                .getUriFactory()
                .resourceByUriIdParam(ServiceRequestId.URL_FILE_QC_INSTRUCTIONS);
        qcGetUri.setParamValue("id", qcInstructionsDTO.getDataFileName());

        GobiiEnvelopeRestResource<QCInstructionsDTO> restResource = new GobiiEnvelopeRestResource<>(qcGetUri);
        PayloadEnvelope<QCInstructionsDTO> resultEnvelope = restResource.get(QCInstructionsDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        Assert.assertTrue("No qc DTO received", resultEnvelope.getPayload().getData().size() > 0);
        QCInstructionsDTO resultDTO = resultEnvelope.getPayload().getData().get(0);
        Assert.assertNotNull(resultDTO.getDataFileName());
        Assert.assertTrue(resultDTO.getDataFileName().equals(qcInstructionsDTO.getDataFileName()));

    }
}
