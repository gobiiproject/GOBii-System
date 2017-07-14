package org.gobiiproject.gobiiclient.dtorequests.infrastructure;

import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiiclient.core.common.Authenticator;
import org.gobiiproject.gobiiclient.core.common.ClientContext;
import org.gobiiproject.gobiiclient.core.common.TestConfiguration;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.config.TestExecConfig;
import org.gobiiproject.gobiimodel.headerlesscontainer.*;
import org.gobiiproject.gobiimodel.utils.HelperFunctions;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.lang.reflect.Field;

/**
 * Created by VCalaminos on 3/14/2017.
 */
public class TestGobiiTestData {

    public static String FILE_PATH = "";
    private static TestExecConfig testExecConfig = null;
    private static String CONFIG_FILE_LOCATION_PROP = "cfgFqpn";

    @BeforeClass
    public static void setUpClass() throws Exception {

        testExecConfig = new TestConfiguration().getConfigSettings().getTestExecConfig();

        Assert.assertTrue(Authenticator.authenticate());
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }

    private static String processPropName(String propName) {

        char c[] = propName.toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        propName = new String(c);

        return propName;
    }

    private static void checkFkeys(NodeList fkeys, Class currentClass, Object currentDTO) throws Exception {

        if(fkeys != null && fkeys.getLength() > 0) {

            for (int i=0; i<fkeys.getLength(); i++) {

                Element currentFkeyElement = (Element) fkeys.item(i);

                String fkproperty = currentFkeyElement.getAttribute("fkproperty");

                Field field = currentClass.getDeclaredField(fkproperty);
                field.setAccessible(true);

                Element currentFkeydbPKeyElement = (Element) currentFkeyElement.getElementsByTagName("DbPKey").item(0);

                String fkeyIdFromFile = currentFkeydbPKeyElement.getTextContent();

                Assert.assertTrue(field.get(currentDTO).toString().equals(fkeyIdFromFile));
            }

        }

    }

    private void organizationTest(String currentElementId, String dbPkeySurrogateName, String dbPkeySurrogateValue, NodeList fkeys) throws Exception{

        RestUri restUriOrganizationForGetById = ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(ServiceRequestId.URL_ORGANIZATION);
        restUriOrganizationForGetById.setParamValue("id", currentElementId);

        GobiiEnvelopeRestResource<OrganizationDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriOrganizationForGetById);
        PayloadEnvelope<OrganizationDTO> resultEnvelopeForGetById = gobiiEnvelopeRestResourceForGetById.get(OrganizationDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetById.getHeader()));
        OrganizationDTO organizationDTO  = resultEnvelopeForGetById.getPayload().getData().get(0);
        Assert.assertTrue(organizationDTO.getOrganizationId() > 0);
        Assert.assertNotNull(organizationDTO.getName());

        dbPkeySurrogateName = processPropName(dbPkeySurrogateName);

        Field field = OrganizationDTO.class.getDeclaredField(dbPkeySurrogateName);
        field.setAccessible(true);

        Assert.assertTrue((field.get(organizationDTO).toString()).equals(dbPkeySurrogateValue));

        /** check foreign keys **/
        checkFkeys(fkeys, OrganizationDTO.class, organizationDTO);

    }

    private void contactTest(String currentElementId, String dbPkeySurrogateName, String dbPkeySurrogateValue, NodeList fkeys) throws Exception{

        RestUri restUriContactForGetById = ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(ServiceRequestId.URL_CONTACTS);
        restUriContactForGetById.setParamValue("id", currentElementId);

        GobiiEnvelopeRestResource<ContactDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriContactForGetById);
        PayloadEnvelope<ContactDTO> resultEnvelopeForGetById = gobiiEnvelopeRestResourceForGetById.get(ContactDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetById.getHeader()));
        ContactDTO contactDTO  = resultEnvelopeForGetById.getPayload().getData().get(0);
        Assert.assertTrue(contactDTO.getContactId() > 0);
        Assert.assertNotNull(contactDTO.getEmail());

        dbPkeySurrogateName = processPropName(dbPkeySurrogateName);

        Field field = ContactDTO.class.getDeclaredField(dbPkeySurrogateName);
        field.setAccessible(true);

        Assert.assertTrue((field.get(contactDTO).toString()).equals(dbPkeySurrogateValue));

        /** check foreign keys **/
        checkFkeys(fkeys, ContactDTO.class, contactDTO);

    }

    private void platformTest(String currentElementId, String dbPkeySurrogateName, String dbPkeySurrogateValue, NodeList fkeys) throws Exception{

        RestUri restUriPlatformForGetById = ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(ServiceRequestId.URL_PLATFORM);
        restUriPlatformForGetById.setParamValue("id", currentElementId);

        GobiiEnvelopeRestResource<PlatformDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriPlatformForGetById);
        PayloadEnvelope<PlatformDTO> resultEnvelopeForGetById = gobiiEnvelopeRestResourceForGetById.get(PlatformDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetById.getHeader()));
        PlatformDTO platformDTO  = resultEnvelopeForGetById.getPayload().getData().get(0);
        Assert.assertTrue(platformDTO.getPlatformId() > 0);
        Assert.assertNotNull(platformDTO.getPlatformName());

        dbPkeySurrogateName = processPropName(dbPkeySurrogateName);

        Field field = PlatformDTO.class.getDeclaredField(dbPkeySurrogateName);
        field.setAccessible(true);

        Assert.assertTrue((field.get(platformDTO).toString()).equals(dbPkeySurrogateValue));

        /** check foreign keys **/
        checkFkeys(fkeys, PlatformDTO.class, platformDTO);

    }

    private void protocolTest(String currentElementId, String dbPkeySurrogateName, String dbPkeySurrogateValue, NodeList fkeys) throws Exception{

        RestUri restUriProtocolForGetById = ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(ServiceRequestId.URL_PROTOCOL);
        restUriProtocolForGetById.setParamValue("id", currentElementId);

        GobiiEnvelopeRestResource<ProtocolDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriProtocolForGetById);
        PayloadEnvelope<ProtocolDTO> resultEnvelopeForGetById = gobiiEnvelopeRestResourceForGetById.get(ProtocolDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetById.getHeader()));
        ProtocolDTO protocolDTO  = resultEnvelopeForGetById.getPayload().getData().get(0);
        Assert.assertTrue(protocolDTO.getPlatformId() > 0);
        Assert.assertNotNull(protocolDTO.getName());

        dbPkeySurrogateName = processPropName(dbPkeySurrogateName);

        Field field = ProtocolDTO.class.getDeclaredField(dbPkeySurrogateName);
        field.setAccessible(true);

        Assert.assertTrue((field.get(protocolDTO).toString()).equals(dbPkeySurrogateValue));


        /** check foreign keys **/
        checkFkeys(fkeys, ProtocolDTO.class, protocolDTO);
    }

    private void referenceTest(String currentElementId, String dbPkeySurrogateName, String dbPkeySurrogateValue, NodeList fkeys) throws Exception{

        RestUri restUriReferenceForGetById = ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(ServiceRequestId.URL_REFERENCE);
        restUriReferenceForGetById.setParamValue("id", currentElementId);

        GobiiEnvelopeRestResource<ReferenceDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriReferenceForGetById);
        PayloadEnvelope<ReferenceDTO> resultEnvelopeForGetById = gobiiEnvelopeRestResourceForGetById.get(ReferenceDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetById.getHeader()));
        ReferenceDTO referenceDTO  = resultEnvelopeForGetById.getPayload().getData().get(0);
        Assert.assertTrue(referenceDTO.getReferenceId() > 0);
        Assert.assertNotNull(referenceDTO.getName());

        dbPkeySurrogateName = processPropName(dbPkeySurrogateName);

        Field field = ReferenceDTO.class.getDeclaredField(dbPkeySurrogateName);
        field.setAccessible(true);

        Assert.assertTrue((field.get(referenceDTO).toString()).equals(dbPkeySurrogateValue));


        /** check foreign keys **/
        checkFkeys(fkeys, ReferenceDTO.class, referenceDTO);
    }

    private void mapsetTest(String currentElementId, String dbPkeySurrogateName, String dbPkeySurrogateValue, NodeList fkeys) throws Exception{

        RestUri restUriMapsetForGetById = ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(ServiceRequestId.URL_MAPSET);
        restUriMapsetForGetById.setParamValue("id", currentElementId);

        GobiiEnvelopeRestResource<MapsetDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriMapsetForGetById);
        PayloadEnvelope<MapsetDTO> resultEnvelopeForGetById = gobiiEnvelopeRestResourceForGetById.get(MapsetDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetById.getHeader()));
        MapsetDTO mapsetDTO  = resultEnvelopeForGetById.getPayload().getData().get(0);
        Assert.assertTrue(mapsetDTO.getMapsetId() > 0);
        Assert.assertNotNull(mapsetDTO.getName());

        dbPkeySurrogateName = processPropName(dbPkeySurrogateName);

        Field field = MapsetDTO.class.getDeclaredField(dbPkeySurrogateName);
        field.setAccessible(true);

        Assert.assertTrue((field.get(mapsetDTO).toString()).equals(dbPkeySurrogateValue));


        /** check foreign keys **/
        checkFkeys(fkeys, MapsetDTO.class, mapsetDTO);
    }

    private void projectTest(String currentElementId, String dbPkeySurrogateName, String dbPkeySurrogateValue, NodeList fkeys) throws Exception{

        RestUri restUriProjectForGetById = ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(ServiceRequestId.URL_PROJECTS);
        restUriProjectForGetById.setParamValue("id", currentElementId);

        GobiiEnvelopeRestResource<ProjectDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriProjectForGetById);
        PayloadEnvelope<ProjectDTO> resultEnvelopeForGetById = gobiiEnvelopeRestResourceForGetById.get(ProjectDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetById.getHeader()));
        ProjectDTO projectDTO  = resultEnvelopeForGetById.getPayload().getData().get(0);
        Assert.assertTrue(projectDTO.getProjectId() > 0);
        Assert.assertNotNull(projectDTO.getProjectName());

        dbPkeySurrogateName = processPropName(dbPkeySurrogateName);

        Field field = ProjectDTO.class.getDeclaredField(dbPkeySurrogateName);
        field.setAccessible(true);

        Assert.assertTrue((field.get(projectDTO).toString()).equals(dbPkeySurrogateValue));


        /** check foreign keys **/
        checkFkeys(fkeys, ProjectDTO.class, projectDTO);
    }

    private void manifestTest(String currentElementId, String dbPkeySurrogateName, String dbPkeySurrogateValue, NodeList fkeys) throws Exception{

        RestUri restUriManifestForGetById = ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(ServiceRequestId.URL_MANIFEST);
        restUriManifestForGetById.setParamValue("id", currentElementId);

        GobiiEnvelopeRestResource<ManifestDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriManifestForGetById);
        PayloadEnvelope<ManifestDTO> resultEnvelopeForGetById = gobiiEnvelopeRestResourceForGetById.get(ManifestDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetById.getHeader()));
        ManifestDTO manifestDTO  = resultEnvelopeForGetById.getPayload().getData().get(0);
        Assert.assertTrue(manifestDTO.getManifestId() > 0);
        Assert.assertNotNull(manifestDTO.getName());

        dbPkeySurrogateName = processPropName(dbPkeySurrogateName);

        Field field = ManifestDTO.class.getDeclaredField(dbPkeySurrogateName);
        field.setAccessible(true);

        Assert.assertTrue((field.get(manifestDTO).toString()).equals(dbPkeySurrogateValue));


        /** check foreign keys **/
        checkFkeys(fkeys, ManifestDTO.class, manifestDTO);
    }

    private void experimentTest(String currentElementId, String dbPkeySurrogateName, String dbPkeySurrogateValue, NodeList fkeys) throws Exception{

        RestUri restUriExperimentForGetById = ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(ServiceRequestId.URL_EXPERIMENTS);
        restUriExperimentForGetById.setParamValue("id", currentElementId);

        GobiiEnvelopeRestResource<ExperimentDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriExperimentForGetById);
        PayloadEnvelope<ExperimentDTO> resultEnvelopeForGetById = gobiiEnvelopeRestResourceForGetById.get(ExperimentDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetById.getHeader()));
        ExperimentDTO experimentDTO  = resultEnvelopeForGetById.getPayload().getData().get(0);
        Assert.assertTrue(experimentDTO.getExperimentId() > 0);
        Assert.assertNotNull(experimentDTO.getExperimentName());

        dbPkeySurrogateName = processPropName(dbPkeySurrogateName);

        Field field = ExperimentDTO.class.getDeclaredField(dbPkeySurrogateName);
        field.setAccessible(true);

        Assert.assertTrue((field.get(experimentDTO).toString()).equals(dbPkeySurrogateValue));


        /** check foreign keys **/
        checkFkeys(fkeys, ExperimentDTO.class, experimentDTO);
    }

    private void analysisTest(String currentElementId, String dbPkeySurrogateName, String dbPkeySurrogateValue, NodeList fkeys) throws Exception{

        RestUri restUriAnalysisForGetById = ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(ServiceRequestId.URL_ANALYSIS);
        restUriAnalysisForGetById.setParamValue("id", currentElementId);

        GobiiEnvelopeRestResource<AnalysisDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriAnalysisForGetById);
        PayloadEnvelope<AnalysisDTO> resultEnvelopeForGetById = gobiiEnvelopeRestResourceForGetById.get(AnalysisDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetById.getHeader()));
        AnalysisDTO analysisDTO  = resultEnvelopeForGetById.getPayload().getData().get(0);
        Assert.assertTrue(analysisDTO.getAnalysisId() > 0);
        Assert.assertNotNull(analysisDTO.getAnalysisName());

        dbPkeySurrogateName = processPropName(dbPkeySurrogateName);

        Field field = AnalysisDTO.class.getDeclaredField(dbPkeySurrogateName);
        field.setAccessible(true);

        Assert.assertTrue((field.get(analysisDTO).toString()).equals(dbPkeySurrogateValue));


        /** check foreign keys **/
        checkFkeys(fkeys, AnalysisDTO.class, analysisDTO);
    }

    private void datasetTest(String currentElementId, String dbPkeySurrogateName, String dbPkeySurrogateValue, NodeList fkeys) throws Exception{

        RestUri restUriDatasetForGetById = ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(ServiceRequestId.URL_DATASETS);
        restUriDatasetForGetById.setParamValue("id", currentElementId);

        GobiiEnvelopeRestResource<DataSetDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriDatasetForGetById);
        PayloadEnvelope<DataSetDTO> resultEnvelopeForGetById = gobiiEnvelopeRestResourceForGetById.get(DataSetDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetById.getHeader()));
        DataSetDTO dataSetDTO  = resultEnvelopeForGetById.getPayload().getData().get(0);
        Assert.assertTrue(dataSetDTO.getDataSetId() > 0);
        Assert.assertNotNull(dataSetDTO.getName());

        dbPkeySurrogateName = processPropName(dbPkeySurrogateName);

        Field field = DataSetDTO.class.getDeclaredField(dbPkeySurrogateName);
        field.setAccessible(true);

        Assert.assertTrue((field.get(dataSetDTO).toString()).equals(dbPkeySurrogateValue));


        /** check foreign keys **/
        checkFkeys(fkeys, DataSetDTO.class, dataSetDTO);
    }


    private String makeCommandLine() {

        String configFileLocation = System.getProperty(CONFIG_FILE_LOCATION_PROP);
        String returnVal;

        returnVal = testExecConfig.getConfigUtilCommandlineStem();

        configFileLocation = configFileLocation.replace("\\", "\\\\");

        returnVal = returnVal.replaceFirst("java", "java -DcfgFqpn="+configFileLocation+" ");

        ClassLoader classLoader = getClass().getClassLoader();

        File xmlFile = new File(classLoader.getResource("unit_test_data.xml").getFile());

        FILE_PATH = xmlFile.getAbsolutePath();

        returnVal = returnVal + "\\gobiitestdata.jar " + FILE_PATH;

        return returnVal;
    }

    @Test
    public void testGobiiTestData() throws Exception {
        String commandline = makeCommandLine();

        boolean succeeded = HelperFunctions.tryExec(commandline, "output.txt", "error.txt");

        /******************** CHECK IF ENTITIES ARE CREATED ****************/

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);

        File fXmlFile = new File(FILE_PATH);
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(fXmlFile);

        XPath xPath = XPathFactory.newInstance().newXPath();

        String getAllNodesExpr = "//*[local-name() = 'Keys']";

        XPathExpression xPathExpression = xPath.compile(getAllNodesExpr);

        NodeList nodeList = (NodeList) xPathExpression.evaluate(document, XPathConstants.NODESET);

        for(int i=0; i<nodeList.getLength(); i++) {

            Element element = (Element) nodeList.item(i);
            Element parentElement = (Element) element.getParentNode();
            Element rootElement = (Element) parentElement.getParentNode();

            String parentLocalName = parentElement.getLocalName();
            String dbPkeySurrogateName = rootElement.getAttribute("DbPKeysurrogate");

            Element props = (Element) parentElement.getElementsByTagName("Properties").item(0);

            String dbPkeySurrogateValue = props.getElementsByTagName(dbPkeySurrogateName).item(0).getTextContent();

            Element dbPkey = (Element) element.getElementsByTagName("DbPKey").item(0);

            String currentElementId = dbPkey.getTextContent();

            NodeList fkeys = element.getElementsByTagName("Fkey");

            checkEntities(parentLocalName, currentElementId, dbPkeySurrogateName, dbPkeySurrogateValue, fkeys);
        }


        /*** delete error.txt and output.txt ***/

        File errorFile = new File("error.txt");
        if (errorFile.exists()) {
            errorFile.delete();
        }

        File outputFile = new File("output.txt");
        if(outputFile.exists()) {
            outputFile.delete();
        }
        
    }

    public void checkEntities(String entityName, String currentElementId, String dbPkeySurrogateName, String dbPkeySurrogateValue, NodeList fkeys) throws Exception{


        switch (entityName) {

            case "Organization" :

                organizationTest(currentElementId, dbPkeySurrogateName, dbPkeySurrogateValue, fkeys);

                break;

            case "Contact":

                contactTest(currentElementId, dbPkeySurrogateName, dbPkeySurrogateValue, fkeys);

                break;

            case "Platform":

                platformTest(currentElementId, dbPkeySurrogateName, dbPkeySurrogateValue, fkeys);

                break;

            case "Protocol":

                protocolTest(currentElementId, dbPkeySurrogateName, dbPkeySurrogateValue, fkeys);

                break;

            case "VendorProtocol":

                break;

            case "Reference":

                referenceTest(currentElementId, dbPkeySurrogateName, dbPkeySurrogateValue, fkeys);

                break;

            case "Mapset":

                mapsetTest(currentElementId, dbPkeySurrogateName, dbPkeySurrogateValue, fkeys);

                break;

            case "Project":

                projectTest(currentElementId, dbPkeySurrogateName, dbPkeySurrogateValue, fkeys);

                break;

            case "Manifest":

                manifestTest(currentElementId, dbPkeySurrogateName, dbPkeySurrogateValue, fkeys);

                break;

            case "Experiment":

                experimentTest(currentElementId, dbPkeySurrogateName, dbPkeySurrogateValue, fkeys);

                break;

            case "Analysis":

                analysisTest(currentElementId, dbPkeySurrogateName, dbPkeySurrogateValue, fkeys);

                break;

            case "Dataset":

                datasetTest(currentElementId, dbPkeySurrogateName, dbPkeySurrogateValue, fkeys);

                break;

            default:
                break;


        }

    }

}
