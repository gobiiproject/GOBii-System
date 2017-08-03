// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaserb
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.gobii.dbops.readonly;

import org.apache.commons.lang.StringUtils;
import org.gobiiproject.gobiiapimodel.hateos.Link;
import org.gobiiproject.gobiiapimodel.hateos.LinkCollection;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContextAuth;
import org.gobiiproject.gobiiclient.gobii.Helpers.GlobalPkColl;
import org.gobiiproject.gobiiclient.gobii.Helpers.TestUtils;
import org.gobiiproject.gobiiclient.gobii.dbops.crud.DtoCrudRequestContactTest;
import org.gobiiproject.gobiiclient.gobii.dbops.crud.DtoCrudRequestDataSetTest;
import org.gobiiproject.gobiiclient.gobii.dbops.crud.DtoCrudRequestMapsetTest;
import org.gobiiproject.gobiiclient.gobii.dbops.crud.DtoCrudRequestProjectTest;
import org.gobiiproject.gobiiclient.gobii.dbops.crud.DtoCrudRequestProtocolTest;
import org.gobiiproject.gobiiclient.gobii.dbops.crud.DtoCrudRequestReferenceTest;
import org.gobiiproject.gobiiclient.gobii.dbops.crud.DtoCrudRequestVendorProtocolTest;
import org.gobiiproject.gobiimodel.headerlesscontainer.NameIdDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.PlatformDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.ProtocolDTO;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiFilterType;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DtoRequestNameIdListTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(GobiiClientContextAuth.authenticate());

//        (new GlobalPkColl<DtoCrudRequestAnalysisTest>()).getPkVals(DtoCrudRequestAnalysisTest.class,GobiiEntityNameType.ANALYSES,10);
        (new GlobalPkColl<DtoCrudRequestProjectTest>()).getPkVals(DtoCrudRequestProjectTest.class, GobiiEntityNameType.PROJECTS, 10);
        (new GlobalPkColl<DtoCrudRequestMapsetTest>()).getPkVals(DtoCrudRequestMapsetTest.class, GobiiEntityNameType.MAPSETS, 10);
        (new GlobalPkColl<DtoCrudRequestContactTest>()).getPkVals(DtoCrudRequestContactTest.class, GobiiEntityNameType.CONTACTS, 10);
        (new GlobalPkColl<DtoCrudRequestReferenceTest>()).getPkVals(DtoCrudRequestReferenceTest.class, GobiiEntityNameType.REFERENCES, 10);
        (new GlobalPkColl<DtoCrudRequestDataSetTest>()).getPkVals(DtoCrudRequestDataSetTest.class, GobiiEntityNameType.DATASETS, 10);
        (new GlobalPkColl<DtoCrudRequestProtocolTest>()).getPkVals(DtoCrudRequestProtocolTest.class, GobiiEntityNameType.PROTOCOLS, 10);
        (new GlobalPkColl<DtoCrudRequestVendorProtocolTest>()).getPkVals(DtoCrudRequestVendorProtocolTest.class, GobiiEntityNameType.VENDORS_PROTOCOLS, 10);
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(GobiiClientContextAuth.deAuthenticate());
    }

    private void testNameRetrieval(GobiiEntityNameType gobiiEntityNameType,
                                   GobiiFilterType gobiiFilterType,
                                   String filterValue) throws Exception {
        RestUri namesUri = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .nameIdListByQueryParams();
        GobiiEnvelopeRestResource<NameIdDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(namesUri);
        namesUri.setParamValue("entity", gobiiEntityNameType.toString().toLowerCase());

        if (GobiiFilterType.NONE != gobiiFilterType) {
            namesUri.setParamValue("filterType", StringUtils.capitalize(gobiiFilterType.toString().toUpperCase()));
            namesUri.setParamValue("filterValue", filterValue);
        }


        PayloadEnvelope<NameIdDTO> resultEnvelope = gobiiEnvelopeRestResource
                .get(NameIdDTO.class);

        String assertionErrorStem = "Error testing name-id retrieval of entity "
                + gobiiEntityNameType.toString();

        if (GobiiFilterType.NONE != gobiiFilterType) {

            assertionErrorStem += " with filter type "
                    + gobiiFilterType.toString()
                    + " and filter value "
                    + filterValue;
        }

        assertionErrorStem += ": ";

        Assert.assertFalse(assertionErrorStem,
                TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));

        List<NameIdDTO> NameIdDTOList = resultEnvelope.getPayload().getData();
        Assert.assertNotNull(assertionErrorStem
                        + "null name id list",
                NameIdDTOList);

        Assert.assertTrue(assertionErrorStem
                        + "empty name id list",
                NameIdDTOList.size() > 0);

        Assert.assertNotNull(assertionErrorStem
                        + "null name",
                NameIdDTOList.get(0).getName());

        Assert.assertNotNull(assertionErrorStem
                        + "null ",
                NameIdDTOList.get(0).getId());

        Assert.assertTrue(assertionErrorStem
                        + "id <= 0",
                NameIdDTOList.get(0).getId() > 0);

    }


    @Test
    public void testGetAnalysisNames() throws Exception {

        testNameRetrieval(GobiiEntityNameType.ANALYSES, GobiiFilterType.NONE, null);

    } // testGetAnalysisNames()

    @Test
    public void testGetAnalysisNamesByTypeId() throws Exception {

        testNameRetrieval(GobiiEntityNameType.ANALYSES, GobiiFilterType.BYTYPEID, "1");
    }

    @Test
    public void testGetNamesWithBadEntityValue() throws Exception {

        RestUri namesUri = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .nameIdListByQueryParams();
        GobiiEnvelopeRestResource<NameIdDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(namesUri);

        namesUri.setParamValue("entity", "foo");
        PayloadEnvelope<NameIdDTO> resultEnvelope = gobiiEnvelopeRestResource
                .get(NameIdDTO.class);

        Assert.assertTrue("There should be exactly one error for bad entity type",
                1 == resultEnvelope
                        .getHeader()
                        .getStatus()
                        .getStatusMessages()
                        .stream()
                        .filter(m -> m.getMessage().toLowerCase().contains("unsupported entity for list request"))
                        .count());

    }

    @Test
    public void testGetAnalysisNamesByTypeIdErrorBadFilterType() throws Exception {


        RestUri namesUri = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .nameIdListByQueryParams();
        GobiiEnvelopeRestResource<NameIdDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(namesUri);

        namesUri.setParamValue("entity", GobiiEntityNameType.ANALYSES.toString().toLowerCase());
        namesUri.setParamValue("filterType", "foo");
        namesUri.setParamValue("filterValue", "33");
        PayloadEnvelope<NameIdDTO> resultEnvelope = gobiiEnvelopeRestResource
                .get(NameIdDTO.class);

        Assert.assertTrue("There should be exactly one error for the unsupported filter type",
                1 == resultEnvelope
                        .getHeader()
                        .getStatus()
                        .getStatusMessages()
                        .stream()
                        .filter(m -> m.getMessage().toLowerCase().contains("unsupported filter"))
                        .count());

    }

    @Test
    public void testGetAnalysisNamesByTypeIdErrorEmptyFilterValue() throws Exception {


        RestUri namesUri = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .nameIdListByQueryParams();
        GobiiEnvelopeRestResource<NameIdDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(namesUri);

        namesUri.setParamValue("entity", GobiiEntityNameType.ANALYSES.toString().toLowerCase());
        namesUri.setParamValue("filterType", StringUtils.capitalize(GobiiFilterType.BYTYPEID.toString().toUpperCase()));
        // normally would also specify "filterValue" here

        PayloadEnvelope<NameIdDTO> resultEnvelope = gobiiEnvelopeRestResource
                .get(NameIdDTO.class);

        Assert.assertTrue("There should be exactly one error for filter value not supplied",
                1 == resultEnvelope
                        .getHeader()
                        .getStatus()
                        .getStatusMessages()
                        .stream()
                        .filter(m -> m.getMessage().toLowerCase().contains("a value was not supplied for filter"))
                        .count());

    }


    @Test
    public void testGetContactsByIdForContactType() throws Exception {

        testNameRetrieval(GobiiEntityNameType.CONTACTS, GobiiFilterType.BYTYPENAME, "Admin");

    } // testGetMarkers()

    @Test
    public void testGetContactNames() throws Exception {


        testNameRetrieval(GobiiEntityNameType.CONTACTS, GobiiFilterType.NONE, null);


    } // testGetMarkers()

    @Test
    public void testGetProjectNames() throws Exception {


        testNameRetrieval(GobiiEntityNameType.PROJECTS, GobiiFilterType.NONE, null);

    } // testGetMarkers()


    @Test
    public void testGetProjectNamesByContactId() throws Exception {


        testNameRetrieval(GobiiEntityNameType.PROJECTS, GobiiFilterType.BYTYPEID, "1");
    }

    @Test
    public void testGetExperimentNamesByProjectId() throws Exception {


        testNameRetrieval(GobiiEntityNameType.EXPERIMENTS, GobiiFilterType.BYTYPEID, "1");

    }

    @Test
    public void testGetExperimentNames() throws Exception {


        testNameRetrieval(GobiiEntityNameType.EXPERIMENTS, GobiiFilterType.NONE, null);
    }

    @Test
    public void testGetCvTermsByGroup() throws Exception {


        testNameRetrieval(GobiiEntityNameType.CVTERMS, GobiiFilterType.BYTYPENAME, "status");
    }

    @Test
    public void testGetPlatformNames() throws Exception {


        //testNameRetrieval(GobiiEntityNameType.PLATFORMS, GobiiFilterType.NONE, null);
        RestUri namesUri = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .nameIdListByQueryParams();
        GobiiEnvelopeRestResource<NameIdDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(namesUri);
        namesUri.setParamValue("entity", GobiiEntityNameType.PLATFORMS.toString().toLowerCase());

        PayloadEnvelope<NameIdDTO> resultEnvelope = gobiiEnvelopeRestResource
                .get(NameIdDTO.class);

        List<NameIdDTO> nameIdDTOList = resultEnvelope.getPayload().getData();

        String assertionErrorStem = "Error retrieving Platform Names: ";

        Assert.assertFalse(assertionErrorStem,
                TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));

        Assert.assertNotNull(assertionErrorStem
                        + "null name id list",
                nameIdDTOList);

        Assert.assertTrue(assertionErrorStem
                        + "empty name id list",
                nameIdDTOList.size() > 0);

        Assert.assertNotNull(assertionErrorStem
                        + "null name",
                nameIdDTOList.get(0).getName());

        Assert.assertNotNull(assertionErrorStem
                        + "null ",
                nameIdDTOList.get(0).getId());

        Assert.assertTrue(assertionErrorStem
                        + "id <= 0",
                nameIdDTOList.get(0).getId() > 0);


        // verify that we can retrieve platofrmDtos from the links we got for the platform name IDs
        LinkCollection linkCollection = resultEnvelope.getPayload().getLinkCollection();
        Assert.assertTrue(linkCollection.getLinksPerDataItem().size() == nameIdDTOList.size());

        List<Integer> itemsToTest = new ArrayList<>();
        if (nameIdDTOList.size() > 50) {
            itemsToTest = TestUtils.makeListOfIntegersInRange(10, nameIdDTOList.size());

        } else {
            for (int idx = 0; idx < nameIdDTOList.size(); idx++) {
                itemsToTest.add(idx);
            }
        }

        for (Integer currentIdx : itemsToTest) {

            NameIdDTO currentPlatformNameDto = nameIdDTOList.get(currentIdx);

            Link currentLink = linkCollection.getLinksPerDataItem().get(currentIdx);
            RestUri restUriPlatformForGetById = GobiiClientContext.getInstance(null, false)
                    .getUriFactory()
                    .RestUriFromUri(currentLink.getHref());
            GobiiEnvelopeRestResource<PlatformDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriPlatformForGetById);
            PayloadEnvelope<PlatformDTO> resultEnvelopeForGetByID = gobiiEnvelopeRestResourceForGetById
                    .get(PlatformDTO.class);
            Assert.assertNotNull(resultEnvelopeForGetByID);
            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetByID.getHeader()));
            PlatformDTO platformDTOFromLink = resultEnvelopeForGetByID.getPayload().getData().get(0);

            Assert.assertTrue(currentPlatformNameDto.getName().equals(platformDTOFromLink.getPlatformName()));
            Assert.assertTrue(currentPlatformNameDto.getId().equals(platformDTOFromLink.getPlatformId()));
        }


    } // testGetMarkers()

    @Test
    public void testGetPlatformNamesByTypeId() throws Exception {

        testNameRetrieval(GobiiEntityNameType.PLATFORMS, GobiiFilterType.BYTYPEID, "1");

    } // testGetMarkers()


    @Ignore
    public void testGetMarkerGroupNames() throws Exception {

        testNameRetrieval(GobiiEntityNameType.MARKERGROUPS, GobiiFilterType.NONE, null);

    } // testGetMarkerGroupNames()

    @Test
    public void testGetReferenceNames() throws Exception {


        testNameRetrieval(GobiiEntityNameType.REFERENCES, GobiiFilterType.NONE, null);

    } // testGetMarkers()

    @Test
    public void testGetMapNames() throws Exception {


        testNameRetrieval(GobiiEntityNameType.MAPSETS, GobiiFilterType.NONE, null);

    } // testGetMarkers()

    @Test
    public void testGetMapsSetNamesByType() throws Exception {
        testNameRetrieval(GobiiEntityNameType.MAPSETS, GobiiFilterType.BYTYPEID, "1");

    } // testGetMarkers()


    @Test
    public void testGetCvTypes() throws Exception {


        testNameRetrieval(GobiiEntityNameType.CVGROUPS, GobiiFilterType.NONE, null);

    } // testGetMarkers()

    @Test
    public void testGetCvNames() throws Exception {


        testNameRetrieval(GobiiEntityNameType.CVTERMS, GobiiFilterType.NONE, null);

    } // testGetMarkers()

    @Test
    public void testGetRoles() throws Exception {


        testNameRetrieval(GobiiEntityNameType.ROLES, GobiiFilterType.NONE, null);
    } // testGetMarkers()


    @Test
    public void testGetManifestNames() throws Exception {

        testNameRetrieval(GobiiEntityNameType.MANIFESTS, GobiiFilterType.NONE, null);

    }

    @Test
    public void testGetOrganizationNames() throws Exception {


        testNameRetrieval(GobiiEntityNameType.ORGANIZATIONS, GobiiFilterType.NONE, null);

    }

    @Test
    public void testGetDataSetNamesByExperimentId() throws Exception {


        testNameRetrieval(GobiiEntityNameType.DATASETS, GobiiFilterType.BYTYPEID, "1");

    }

    @Test
    public void testGetDataSetNames() throws Exception {


        testNameRetrieval(GobiiEntityNameType.DATASETS, GobiiFilterType.NONE, null);
    }

    @Test
    public void testGetProtocols() throws Exception {

        testNameRetrieval(GobiiEntityNameType.PROTOCOLS, GobiiFilterType.NONE, null);
    }

    @Test
    public void testGetProtocolVendors() throws Exception {
        testNameRetrieval(GobiiEntityNameType.VENDORS_PROTOCOLS, GobiiFilterType.NONE, null);
    }

    @Test
    public void testGetProtocolVendorsByProtocolId() throws Exception {
        Integer protocolId = (new GlobalPkColl<DtoCrudRequestProtocolTest>()
                .getAPkVal(DtoCrudRequestProtocolTest.class, GobiiEntityNameType.PROTOCOLS));

        testNameRetrieval(GobiiEntityNameType.VENDORS_PROTOCOLS, GobiiFilterType.BYTYPEID, protocolId.toString());
    }


    @Test
    public void testGetProtocolsByPlatformId() throws Exception {

        RestUri restUriProtocol = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_PROTOCOL);
        GobiiEnvelopeRestResource<ProtocolDTO> restResource = new GobiiEnvelopeRestResource<>(restUriProtocol);
        PayloadEnvelope<ProtocolDTO> resultEnvelope = restResource
                .get(ProtocolDTO.class);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        List<ProtocolDTO> protocolDTOList = resultEnvelope.getPayload().getData();

        Integer platformId = null;
        for (Integer idx = 0; (platformId == null) && (idx < protocolDTOList.size()); idx++) {

            ProtocolDTO currentProtocolDTO = protocolDTOList.get(idx);
            if( ( currentProtocolDTO.getPlatformId() != null ) &&
                    currentProtocolDTO.getPlatformId() > 0 ) {
                platformId = currentProtocolDTO.getPlatformId();
            }
        }

        Assert.assertNotNull(platformId);


        testNameRetrieval(GobiiEntityNameType.PROTOCOLS, GobiiFilterType.BYTYPEID, platformId.toString());
    }
}
