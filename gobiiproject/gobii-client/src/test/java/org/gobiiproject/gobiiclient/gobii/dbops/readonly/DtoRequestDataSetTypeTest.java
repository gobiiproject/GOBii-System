package org.gobiiproject.gobiiclient.gobii.dbops.readonly;

import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContextAuth;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.gobii.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.headerlesscontainer.NameIdDTO;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

/**
 * Created by VCalaminos on 2017-01-05.
 */
public class DtoRequestDataSetTypeTest {

    @BeforeClass
    public static void setUpClass() throws Exception {

        Assert.assertTrue(GobiiClientContextAuth.authenticate());

    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(GobiiClientContextAuth.deAuthenticate());
    }

    @Test
    public void testGetDataSetTypes() throws Exception {

        RestUri restUri = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_DATASETTYPES);

        GobiiEnvelopeRestResource<NameIdDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUri);
        PayloadEnvelope<NameIdDTO> resultEnvelope = gobiiEnvelopeRestResource
                .get(NameIdDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        List<NameIdDTO> nameIdDTOList = resultEnvelope.getPayload().getData();
        Assert.assertNotNull(nameIdDTOList);
        Assert.assertTrue(nameIdDTOList.size() > 0);
        Assert.assertNotNull(nameIdDTOList.get(0).getName());

        Boolean testIfExisting = false;
        for (NameIdDTO currentItem : nameIdDTOList) {

            if(currentItem.getName().toLowerCase().equals("nucleotide_2_letter")) {
                testIfExisting = true;
            }
        }

        Assert.assertTrue(testIfExisting);
    }

}
