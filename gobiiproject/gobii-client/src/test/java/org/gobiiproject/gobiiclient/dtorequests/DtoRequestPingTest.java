// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.CropConfig;
import org.gobiiproject.gobiimodel.config.CropDbConfig;
import org.gobiiproject.gobiimodel.dto.container.PingDTO;
import org.gobiiproject.gobiimodel.dto.types.ControllerType;
import org.gobiiproject.gobiimodel.types.GobiiCropType;
import org.gobiiproject.gobiimodel.types.GobiiDbType;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DtoRequestPingTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(Authenticator.authenticate());
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }


    @Ignore
    public void testGetPingFromExtractController() throws Exception {

        PingDTO pingDTORequest = TestDtoFactory.makePingDTO();
        pingDTORequest.setControllerType(ControllerType.EXTRACTOR);

        DtoRequestPing dtoRequestPing = new DtoRequestPing();
        PingDTO pingDTOResponse = dtoRequestPing.process(pingDTORequest);

        Assert.assertNotEquals(null, pingDTOResponse);
        Assert.assertNotEquals(null, pingDTOResponse.getDbMetaData());
        Assert.assertNotEquals(null, pingDTOResponse.getPingResponses());
        Assert.assertTrue(pingDTOResponse.getPingResponses().size()
                >= pingDTORequest.getDbMetaData().size());

    } // testGetMarkers()


    @Test
    public void testGetPingFromLoadController() throws Exception {

        PingDTO pingDTORequest = TestDtoFactory.makePingDTO();
        pingDTORequest.setControllerType(ControllerType.LOADER);

        DtoRequestPing dtoRequestPing = new DtoRequestPing();
        PingDTO pingDTOResponse = dtoRequestPing.process(pingDTORequest);

        Assert.assertNotEquals(null, pingDTOResponse);
        Assert.assertNotEquals(null, pingDTOResponse.getDbMetaData());
        Assert.assertNotEquals(null, pingDTOResponse.getPingResponses());
        Assert.assertTrue(pingDTOResponse.getPingResponses().size()
                >= pingDTORequest.getDbMetaData().size());

    } // testGetMarkers()


}
