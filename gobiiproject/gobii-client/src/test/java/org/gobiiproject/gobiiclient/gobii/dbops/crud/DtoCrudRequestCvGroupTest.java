package org.gobiiproject.gobiiclient.gobii.dbops.crud;

import org.gobiiproject.gobiiapimodel.hateos.LinkCollection;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContextAuth;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.gobii.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.headerlesscontainer.CvDTO;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

/**
 * Created by VCalaminos on 2/1/2017.
 */
public class DtoCrudRequestCvGroupTest implements DtoCrudRequestTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(GobiiClientContextAuth.authenticate());
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(GobiiClientContextAuth.deAuthenticate());
    }


    @Test
    public void getCvByGroupId() throws Exception {

        Integer cvGroupId = 9; //platform_type

        RestUri restUriCvGroup = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_CVGROUP)
                .addUriParam("groupId")
                .setParamValue("groupId", cvGroupId.toString())
                .appendSegment(GobiiServiceRequestId.URL_CV);

        GobiiEnvelopeRestResource<CvDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriCvGroup);
        PayloadEnvelope<CvDTO> resultEnvelope = gobiiEnvelopeRestResource
                .get(CvDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        List<CvDTO> cvDTOList = resultEnvelope.getPayload().getData();
        Assert.assertNotNull(cvDTOList);
        Assert.assertTrue(cvDTOList.size() >= 0);
        Assert.assertNotNull(cvDTOList.get(0).getTerm());

        LinkCollection linkCollection = resultEnvelope.getPayload().getLinkCollection();
        Assert.assertTrue(linkCollection.getLinksPerDataItem().size() == cvDTOList.size());

        Boolean testIfExisting = false;

        for(CvDTO currentCvDTO : cvDTOList) {

            if(currentCvDTO.getTerm().toLowerCase().equals("gbs")) {
                testIfExisting = true;
            }
        }

        Assert.assertTrue(testIfExisting);
    }

    public void testEmptyResult() {}

    public void create() {}

    public void update() {}

    public void get() {}

    public void getList () {}

}
