package org.gobiiproject.gobiiprocess;

import com.google.gson.*;
import org.apache.commons.lang.StringUtils;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContextAuth;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiimodel.headerlesscontainer.*;
import org.gobiiproject.gobiiapimodel.payload.Header;
import org.gobiiproject.gobiiapimodel.payload.HeaderStatusMessage;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiFilterType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.*;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by VCalaminos on 2/21/2017.
 */
public class GobiiTestData {



    private static void validateKeys(NodeList nodeList, XPath xPath, Document document) throws Exception {


        for(int i=0; i<nodeList.getLength(); i++) {

            String parentName = nodeList.item(i).getLocalName();

            Element element = (Element) nodeList.item(i);
            String DbPKeysurrogate = element.getAttribute("DbPKeysurrogate");

            System.out.println("\nChecking DbPKeysurrogate ("+DbPKeysurrogate+") for " + parentName +"......\n");
            Node node = nodeList.item(i);

            NodeList childNodes = node.getChildNodes();

            int count = 0;
            for (int j=0; j<childNodes.getLength(); j++) {

                if (childNodes.item(j) instanceof Element == false)
                    continue;

                System.out.println("\nRecord " + (count+1) + "....\n");

                count++;
                String childName = childNodes.item(j).getLocalName();
                Element childElement = (Element) childNodes.item(j);

                Element props = (Element) childElement.getElementsByTagName("Properties").item(0);

                String dbPkeysurrogateValue = props.getElementsByTagName(DbPKeysurrogate).item(0).getTextContent();

                System.out.println("\nChecking for value....\n");

                if(dbPkeysurrogateValue.isEmpty()) {

                    throw new Exception("DbPKeysurrogate ("+DbPKeysurrogate+") attribute for "+
                            childName+" entity cannot be empty.");

                }

                System.out.println("\nChecking for duplicates...\n");

                String expr = "count(//Entities/"+parentName+"/"+childName+"/Properties["+DbPKeysurrogate+"='"+dbPkeysurrogateValue+"'])";

                XPathExpression xPathExpressionCount = xPath.compile(expr);
                Double countDuplicate = (Double) xPathExpressionCount.evaluate(document, XPathConstants.NUMBER);


                if(countDuplicate > 1) {

                    String message = "Duplicate DbPKeysurrogate (" +
                            DbPKeysurrogate + ") value ("+ dbPkeysurrogateValue +") " +
                            "for " + childName + " entity.";


                    throw new Exception(message);

                }


                System.out.println("\nChecking for foreign keys...\n");

                Element keys = (Element) childElement.getElementsByTagName("Keys").item(0);
                NodeList fkeys = keys.getElementsByTagName("Fkey");

                for(int k = 0; k < fkeys.getLength(); k++) {

                    Element fkey = (Element) fkeys.item(k);

                    String entity = fkey.getAttribute("entity");

                    if(entity.isEmpty()) {

                        String message = "Entity attribute for Fkey of "+ childName+" ("+dbPkeysurrogateValue+") cannot be empty.";

                        throw new Exception(message);

                    }

                    NodeList fkeyDbPkey = fkey.getElementsByTagName("DbPKeySurrogate");

                    if(fkeyDbPkey.getLength() < 1) {

                        String message = "FKey property for "+ childName +" ("+ dbPkeysurrogateValue+ ") should have <DbPKeySurrogate> tag.";

                        throw new Exception(message);
                    }

                    String fkeyDbPkeyValue = fkeyDbPkey.item(0).getTextContent();

                    if(fkeyDbPkeyValue.isEmpty()) {

                        String message = "DbPKeySurrogate property for " + entity + " FKey of " + childName + " (" + dbPkeysurrogateValue + ") cannot be empty.";

                        throw new Exception(message);

                    }

                    // get parent node of fkey entity
                    XPathExpression exprParentFkey = xPath.compile("//"+entity+"/parent::*");
                    Element ancestor = (Element) exprParentFkey.evaluate(document, XPathConstants.NODE);

                    String fkeyPKey = ancestor.getAttribute("DbPKeysurrogate");

                    String exprCheckIfFKeyExists = "count(//Entities/"+ancestor.getNodeName()+"/"+entity+"/Properties["+fkeyPKey+"='"+fkeyDbPkeyValue+"'])";

                    XPathExpression xPathExpressionCountFkey = xPath.compile(exprCheckIfFKeyExists);
                    Double countIfExists = (Double) xPathExpressionCountFkey.evaluate(document, XPathConstants.NUMBER);

                    if(countIfExists < 1) {

                        String message = entity + " (" + fkeyDbPkeyValue+ ") fkey value for "
                                + childName + "(" + dbPkeysurrogateValue + ")" +
                                " doesn't exist in the file.";

                        throw new Exception(message);
                    }
                }
            }
        }

    }

    private static String processPropName(String propertyName) {

        char c[] = propertyName.toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        propertyName = new String(c);


        return propertyName;

    }

    public static <E> E processTypes (Object value, Class<E> type) throws ParseException {

        if (type.equals(Integer.class)) {

            return type.cast(Integer.parseInt(value.toString()));

        } else if (type.equals(Date.class)) {

            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

            Date date = formatter.parse(value.toString());

            return type.cast(date);

        }


        return type.cast(value);
    }

    private static void checkStatus(PayloadEnvelope payloadEnvelope) throws  Exception{

        Header header = payloadEnvelope.getHeader();
        if (!header.getStatus().isSucceeded() ||
                header
                        .getStatus()
                        .getStatusMessages()
                        .stream()
                        .filter(headerStatusMessage -> headerStatusMessage.getGobiiStatusLevel().equals(GobiiStatusLevel.VALIDATION))
                        .count() > 0) {

            System.out.println("\n***** Header errors: *****");
            String message = "";

            for (HeaderStatusMessage currentStatusMessage : header.getStatus().getStatusMessages()) {

                message = message +  "\n" + currentStatusMessage.getMessage();
            }

            throw new Exception(message);

        }

    }

    private static Map<String, Integer> getCvTermsWithId(String filterValue) throws Exception{

        Map<String, Integer> returnVal = new HashMap<>();

        RestUri namesUri = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .nameIdListByQueryParams();
        GobiiEnvelopeRestResource<NameIdDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(namesUri);
        namesUri.setParamValue("entity", GobiiEntityNameType.CVTERMS.toString().toLowerCase());
        namesUri.setParamValue("filterType", StringUtils.capitalize(GobiiFilterType.BYTYPENAME.toString().toUpperCase()));
        namesUri.setParamValue("filterValue", filterValue);

        PayloadEnvelope<NameIdDTO> resultEnvelope = gobiiEnvelopeRestResource.get(NameIdDTO.class);

        List<NameIdDTO> nameIdDTOList = resultEnvelope.getPayload().getData();

        for (int i=0; i<nameIdDTOList.size(); i++){

            NameIdDTO currentNameIdDTO = nameIdDTOList.get(i);
            returnVal.put(currentNameIdDTO.getName().toLowerCase(), currentNameIdDTO.getId());

        }

        return returnVal;

    }

    private static Integer createOrganization(Element parentElement, String entityName, NodeList fkeys, String dbPkeysurrogateValue,
                                              XPath xPath, Document document, NodeList propKeyList) throws Exception {

        Integer returnVal = null;


        System.out.println("\nChecking if " + entityName + " ("+dbPkeysurrogateValue+") already exists in the database...\n");
        /*** check if entity already exist in the database ***/

        RestUri restUriOrganization = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_ORGANIZATION);
        GobiiEnvelopeRestResource<OrganizationDTO> gobiiEnvelopeRestResourceGet = new GobiiEnvelopeRestResource<>(restUriOrganization);
        PayloadEnvelope<OrganizationDTO> resultEnvelope = gobiiEnvelopeRestResourceGet.get(OrganizationDTO.class);

        checkStatus(resultEnvelope);

        List<OrganizationDTO> organizationDTOSList = resultEnvelope.getPayload().getData();

        for (OrganizationDTO currentOrganizationDTO : organizationDTOSList) {

            if (currentOrganizationDTO.getName().equals(dbPkeysurrogateValue)) {

                System.out.println("\n" +entityName + "("+dbPkeysurrogateValue+") already exists in the database. Return current entity ID.\n");

                /*** set fkey dbpkey for entity ***/

                setFKeyDbPKeyForExistingEntity(fkeys, OrganizationDTO.class, currentOrganizationDTO);

                return currentOrganizationDTO.getId();

            }

        }


        System.out.println("\n"+entityName+"("+dbPkeysurrogateValue+") doesn't exist in the database.\nCreating new record...\n");

        OrganizationDTO newOrganizationDTO = new OrganizationDTO();

        System.out.println("Populating " +entityName+ "DTO with attributes from XML file...");

        for (int j=0; j<propKeyList.getLength(); j++) {

            Element propKey = (Element) propKeyList.item(j);

            String propKeyLocalName = propKey.getLocalName();

            propKeyLocalName = processPropName(propKeyLocalName);

            Field field = OrganizationDTO.class.getDeclaredField(propKeyLocalName);
            field.setAccessible(true);
            field.set(newOrganizationDTO, processTypes(propKey.getTextContent(), field.getType()));
        }

        newOrganizationDTO.setCreatedBy(1);
        newOrganizationDTO.setCreatedDate(new Date());
        newOrganizationDTO.setModifiedBy(1);
        newOrganizationDTO.setModifiedDate(new Date());
        newOrganizationDTO.setStatusId(1);

        setFKeyDbPKeyForNewEntity(fkeys, OrganizationDTO.class, newOrganizationDTO, parentElement, dbPkeysurrogateValue, document, xPath);

        System.out.println("Calling the web service...\n");

        /*** create organization ***/

        PayloadEnvelope<OrganizationDTO> payloadEnvelope = new PayloadEnvelope<>(newOrganizationDTO, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<OrganizationDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_ORGANIZATION));
        PayloadEnvelope<OrganizationDTO> organizationDTOResponseEnvelope = gobiiEnvelopeRestResource.post(OrganizationDTO.class,
                payloadEnvelope);

        checkStatus(organizationDTOResponseEnvelope);

        OrganizationDTO organizationDTOResponse = organizationDTOResponseEnvelope.getPayload().getData().get(0);

        returnVal = organizationDTOResponse.getOrganizationId();

        System.out.println(entityName + "("+dbPkeysurrogateValue+") is successfully created!\n");

        return  returnVal;

    }

    private static Integer createContact(Element parentElement, String entityName, NodeList fkeys, String dbPkeysurrogateValue,
                                         XPath xPath, Document document, NodeList propKeyList) throws Exception {


        Integer returnVal = null;


        System.out.println("\nChecking if " + entityName + " ("+dbPkeysurrogateValue+") already exists in the database...\n");
        /*** check if entity already exist in the database ***/

        RestUri restUriContact = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .contactsByQueryParams();
        restUriContact.setParamValue("email", dbPkeysurrogateValue);
        GobiiEnvelopeRestResource<ContactDTO> gobiiEnvelopeRestResourceGet = new GobiiEnvelopeRestResource<>(restUriContact);
        PayloadEnvelope<ContactDTO> resultEnvelope = gobiiEnvelopeRestResourceGet.get(ContactDTO.class);

        checkStatus(resultEnvelope);

        if (resultEnvelope.getPayload().getData().size() > 0) {

            ContactDTO currentContactDTO = resultEnvelope.getPayload().getData().get(0);

            System.out.println("\n" +entityName + "("+dbPkeysurrogateValue+") already exists in the database. Return current entity ID.\n");

            /*** set fkey dbpkey for entity ***/

            setFKeyDbPKeyForExistingEntity(fkeys, ContactDTO.class, currentContactDTO);

            return currentContactDTO.getId();

        }


        System.out.println("\n"+entityName+"("+dbPkeysurrogateValue+") doesn't exist in the database.\nCreating new record...\n");

        ContactDTO newContactDTO = new ContactDTO();

        System.out.println("Populating " +entityName+ "DTO with attributes from XML file...");

        // get roles
        RestUri rolesUri = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .nameIdListByQueryParams();

        GobiiEnvelopeRestResource<NameIdDTO> gobiiEnvelopeNameResource = new GobiiEnvelopeRestResource<>(rolesUri);
        rolesUri.setParamValue("entity", GobiiEntityNameType.ROLES.toString().toLowerCase());

        PayloadEnvelope<NameIdDTO> resultEnvelopeRoles = gobiiEnvelopeNameResource.get(NameIdDTO.class);

        List<NameIdDTO> nameIdDTOList = resultEnvelopeRoles.getPayload().getData();

        Map<String, Integer> rolesMap = new HashMap<>();

        for(int i=0; i<nameIdDTOList.size(); i++) {

            NameIdDTO currentNameIdDTO = nameIdDTOList.get(i);
            rolesMap.put(currentNameIdDTO.getName(), currentNameIdDTO.getId());

        }

        for (int j=0; j<propKeyList.getLength(); j++) {

            Element propKey = (Element) propKeyList.item(j);

            String propKeyLocalName = propKey.getLocalName();

            if (propKeyLocalName.equals("Roles")) {continue;}

            if(propKeyLocalName.equals("Role")) {

                Integer roleId = rolesMap.get(propKey.getTextContent());

                newContactDTO.getRoles().add(roleId);

            } else {

                propKeyLocalName = processPropName(propKeyLocalName);

                Field field = ContactDTO.class.getDeclaredField(propKeyLocalName);
                field.setAccessible(true);
                field.set(newContactDTO, processTypes(propKey.getTextContent(), field.getType()));

            }

        }

        newContactDTO.setCreatedBy(1);
        newContactDTO.setCreatedDate(new Date());
        newContactDTO.setModifiedBy(1);
        newContactDTO.setModifiedDate(new Date());

        /** check roles **/
        if (newContactDTO.getRoles().size() <= 0) {

            throw new Exception("Roles are required to create a contact. Please add role/s for Contact ("+newContactDTO.getEmail()+").");

        }

        setFKeyDbPKeyForNewEntity(fkeys, ContactDTO.class, newContactDTO, parentElement, dbPkeysurrogateValue, document, xPath);

        System.out.println("Calling the web service...\n");

        /*** create contact ***/

        PayloadEnvelope<ContactDTO> payloadEnvelopeContact = new PayloadEnvelope<>(newContactDTO, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<ContactDTO> gobiiEnvelopeRestResourceContact = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                    .getUriFactory()
                    .resourceColl(GobiiServiceRequestId.URL_CONTACTS));
        PayloadEnvelope<ContactDTO> contactDTOResponseEnvelope = gobiiEnvelopeRestResourceContact.post(ContactDTO.class,
                payloadEnvelopeContact);

        checkStatus(contactDTOResponseEnvelope);

        ContactDTO contactDTOResponse = contactDTOResponseEnvelope.getPayload().getData().get(0);

        returnVal = contactDTOResponse.getContactId();

        System.out.println(entityName + "("+dbPkeysurrogateValue+") is successfully created!\n");

        return returnVal;


    }

    private static Integer createPlatform(Element parentElement, String entityName, NodeList fkeys, String dbPkeysurrogateValue,
                                          XPath xPath, Document document, NodeList propKeyList) throws Exception {

        Integer returnVal = null;

        System.out.println("\nChecking if " + entityName + " ("+dbPkeysurrogateValue+") already exists in the database...\n");
        /*** check if entity already exist in the database ***/

        RestUri restUriPlatform = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_PLATFORM);
        GobiiEnvelopeRestResource<PlatformDTO> gobiiEnvelopeRestResourceGet = new GobiiEnvelopeRestResource<>(restUriPlatform);
        PayloadEnvelope<PlatformDTO> resultEnvelope = gobiiEnvelopeRestResourceGet.get(PlatformDTO.class);

        checkStatus(resultEnvelope);

        List<PlatformDTO> platformDTOSList = resultEnvelope.getPayload().getData();

        for (PlatformDTO currentPlatformDTO : platformDTOSList) {

            if (currentPlatformDTO.getPlatformName().equals(dbPkeysurrogateValue)) {

                System.out.println("\n" +entityName + "("+dbPkeysurrogateValue+") already exists in the database. Return current entity ID.\n");

                /*** set fkey dbpkey for entity ***/

                setFKeyDbPKeyForExistingEntity(fkeys, PlatformDTO.class, currentPlatformDTO);

                return currentPlatformDTO.getId();

            }

        }

        System.out.println("\n"+entityName+"("+dbPkeysurrogateValue+") doesn't exist in the database.\nCreating new record...\n");

        PlatformDTO newPlatformDTO = new PlatformDTO();

        System.out.println("Populating " +entityName+ "DTO with attributes from XML file...");

        Element propertiesElement = null;

        /*** get cv's from platform_type group ***/

        Map<String, Integer> platformTypeMap = getCvTermsWithId("platform_type");

        for (int j=0; j<propKeyList.getLength(); j++) {

            Element propKey = (Element) propKeyList.item(j);

            String propKeyLocalName = propKey.getLocalName();

            propKeyLocalName = processPropName(propKeyLocalName);

            if (propKeyLocalName.equals("properties")) {

                propertiesElement = propKey;
                continue;
            }

            if (propKeyLocalName.equals("typeId")) {

                Integer typeId = platformTypeMap.get(propKey.getTextContent().toLowerCase());

                newPlatformDTO.setTypeId(typeId);
                continue;
            }

            if(propKey.getParentNode().equals(propertiesElement)) { // add to properties attribute of platform

                EntityPropertyDTO entityPropertyDTO = new EntityPropertyDTO();

                entityPropertyDTO.setPropertyName(propKeyLocalName);
                entityPropertyDTO.setPropertyValue(propKey.getTextContent());

                newPlatformDTO.getProperties().add(entityPropertyDTO);
            }
            else {

                Field field = PlatformDTO.class.getDeclaredField(propKeyLocalName);
                field.setAccessible(true);
                field.set(newPlatformDTO, processTypes(propKey.getTextContent(), field.getType()));

            }

        }

        newPlatformDTO.setCreatedDate(new Date());
        newPlatformDTO.setCreatedBy(1);
        newPlatformDTO.setModifiedDate(new Date());
        newPlatformDTO.setModifiedBy(1);

        setFKeyDbPKeyForNewEntity(fkeys, PlatformDTO.class, newPlatformDTO, parentElement, dbPkeysurrogateValue, document, xPath);


        System.out.println("Calling the web service...\n");

        /*** create platform ***/

        PayloadEnvelope<PlatformDTO> payloadEnvelopePlatform = new PayloadEnvelope<>(newPlatformDTO, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<PlatformDTO> gobiiEnvelopeRestResourcePlatform = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                        .getUriFactory()
                        .resourceColl(GobiiServiceRequestId.URL_PLATFORM));
        PayloadEnvelope<PlatformDTO> platformDTOResponseEnvelope = gobiiEnvelopeRestResourcePlatform.post(PlatformDTO.class,
                payloadEnvelopePlatform);


        checkStatus(platformDTOResponseEnvelope);

        PlatformDTO platformDTOResponse = platformDTOResponseEnvelope.getPayload().getData().get(0);

        returnVal = platformDTOResponse.getPlatformId();

        System.out.println(entityName + "("+dbPkeysurrogateValue+") is successfully created!\n");

        return returnVal;

    }

    private static Integer createProtocol(Element parentElement, String entityName, NodeList fkeys, String dbPkeysurrogateValue,
                                          XPath xPath, Document document, NodeList propKeyList) throws Exception {

        Integer returnVal = null;


        System.out.println("\nChecking if " + entityName + " ("+dbPkeysurrogateValue+") already exists in the database...\n");
        /*** check if entity already exist in the database ***/

        RestUri restUriProtocol = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_PROTOCOL);
        GobiiEnvelopeRestResource<ProtocolDTO> gobiiEnvelopeRestResourceGet = new GobiiEnvelopeRestResource<>(restUriProtocol);
        PayloadEnvelope<ProtocolDTO> resultEnvelope = gobiiEnvelopeRestResourceGet.get(ProtocolDTO.class);

        checkStatus(resultEnvelope);

        List<ProtocolDTO> protocolDTOSList = resultEnvelope.getPayload().getData();

        for (ProtocolDTO currentProtocolDTO : protocolDTOSList) {

            if (currentProtocolDTO.getName().equals(dbPkeysurrogateValue)) {

                System.out.println("\n" +entityName + "("+dbPkeysurrogateValue+") already exists in the database. Return current entity ID.\n");

                /*** set fkey dbpkey for entity ***/

                setFKeyDbPKeyForExistingEntity(fkeys, ProtocolDTO.class, currentProtocolDTO);

                return currentProtocolDTO.getId();

            }

        }


        System.out.println("\n"+entityName+"("+dbPkeysurrogateValue+") doesn't exist in the database.\nCreating new record...\n");

        ProtocolDTO newProtocolDTO = new ProtocolDTO();

        System.out.println("Populating " +entityName+ "DTO with attributes from XML file...");

        Element propsElement = null;

        for (int j=0; j<propKeyList.getLength(); j++) {

            Element propKey = (Element) propKeyList.item(j);

            String propKeyLocalName = propKey.getLocalName();

            propKeyLocalName = processPropName(propKeyLocalName);

            if(propKeyLocalName.equals("vendorProtocols")) {continue;}

            if(propKeyLocalName.equals("props")) {

                propsElement = propKey;
                continue;
            }

            if(propKey.getParentNode().equals(propsElement)) {

                EntityPropertyDTO entityPropertyDTO = new EntityPropertyDTO();

                entityPropertyDTO.setPropertyName(propKeyLocalName);
                entityPropertyDTO.setPropertyValue(propKey.getTextContent());

                newProtocolDTO.getProps().add(entityPropertyDTO);
            }
            else {

                Field field = ProtocolDTO.class.getDeclaredField(propKeyLocalName);
                field.setAccessible(true);
                field.set(newProtocolDTO, processTypes(propKey.getTextContent(), field.getType()));
            }
        }

        newProtocolDTO.setCreatedDate(new Date());
        newProtocolDTO.setCreatedBy(1);
        newProtocolDTO.setModifiedDate(new Date());
        newProtocolDTO.setModifiedBy(1);


        setFKeyDbPKeyForNewEntity(fkeys, ProtocolDTO.class, newProtocolDTO, parentElement, dbPkeysurrogateValue, document, xPath);


        System.out.println("Calling the web service...\n");

        /*** create protocol ***/

        PayloadEnvelope<ProtocolDTO> payloadEnvelopeProtocol = new PayloadEnvelope<>(newProtocolDTO, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<ProtocolDTO> gobiiEnvelopeRestResourceProtocol = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                        .getUriFactory()
                        .resourceColl(GobiiServiceRequestId.URL_PROTOCOL));
        PayloadEnvelope<ProtocolDTO> protocolDTOResponseEnvelope = gobiiEnvelopeRestResourceProtocol.post(ProtocolDTO.class,
                payloadEnvelopeProtocol);


        checkStatus(protocolDTOResponseEnvelope);

        ProtocolDTO protocolDTOResponse = protocolDTOResponseEnvelope.getPayload().getData().get(0);

        returnVal = protocolDTOResponse.getProtocolId();

        System.out.println(entityName + "("+dbPkeysurrogateValue+") is successfully created!\n");

        return returnVal;

    }

    private static Integer createVendorProtocol(Element parentElement, String entityName, NodeList fkeys, String dbPkeysurrogateValue,
                                                XPath xPath, Document document, NodeList propKeyList) throws Exception {

        Integer returnVal = null;

        VendorProtocolDTO newVendorProtocolDTO = new VendorProtocolDTO();

        for (int j=0; j<propKeyList.getLength(); j++) {

            Element propKey = (Element) propKeyList.item(j);

            String propKeyLocalName = propKey.getLocalName();

            propKeyLocalName = processPropName(propKeyLocalName);

            Field field = VendorProtocolDTO.class.getDeclaredField(propKeyLocalName);
            field.setAccessible(true);
            field.set(newVendorProtocolDTO, processTypes(propKey.getTextContent(), field.getType()));

        }

        setFKeyDbPKeyForNewEntity(fkeys, VendorProtocolDTO.class, newVendorProtocolDTO, parentElement, dbPkeysurrogateValue, document, xPath);

        System.out.println("\nChecking if " + entityName + " ("+dbPkeysurrogateValue+") already exists in the database...\n");
        /*** check if entity already exist in the database ***/

        RestUri restUriOrganizationForGetById = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_ORGANIZATION);
        restUriOrganizationForGetById.setParamValue("id", newVendorProtocolDTO.getOrganizationId().toString());
        GobiiEnvelopeRestResource<OrganizationDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriOrganizationForGetById);
        PayloadEnvelope<OrganizationDTO> resultEnvelopeForGetById = gobiiEnvelopeRestResourceForGetById.get(OrganizationDTO.class);

        checkStatus(resultEnvelopeForGetById);

        OrganizationDTO currentOrganizationDTO = resultEnvelopeForGetById.getPayload().getData().get(0);

        for (VendorProtocolDTO vendorProtocolDTO : currentOrganizationDTO.getVendorProtocols()) {

            if (vendorProtocolDTO.getProtocolId().equals(newVendorProtocolDTO.getProtocolId())) {

                System.out.println("\n" +entityName + "("+dbPkeysurrogateValue+") already exists in the database. Return current entity ID.\n");

                return vendorProtocolDTO.getId();

            }

        }

        System.out.println("\n"+entityName+"("+dbPkeysurrogateValue+") doesn't exist in the database.\nCreating new record...\n");
        System.out.println("Populating " +entityName+ "DTO with attributes from XML file...");


        System.out.println("Calling the web service...\n");

        /*** create vendor protcol ***/
        // get organization/vendor

        RestUri restUriForGetOrganizationById = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_ORGANIZATION);
        restUriForGetOrganizationById.setParamValue("id", newVendorProtocolDTO.getOrganizationId().toString());
        GobiiEnvelopeRestResource<OrganizationDTO> gobiiEnvelopeRestResourceForGetOrganizationById =
                new GobiiEnvelopeRestResource<>(restUriForGetOrganizationById);
        PayloadEnvelope<OrganizationDTO> resultEnvelopeForGetOrganizationByID = gobiiEnvelopeRestResourceForGetOrganizationById
                .get(OrganizationDTO.class);
        OrganizationDTO organizationDTO = resultEnvelopeForGetOrganizationByID.getPayload().getData().get(0);
        organizationDTO.getVendorProtocols().add(newVendorProtocolDTO);

        RestUri restUriProtocoLVendor = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .childResourceByUriIdParam(GobiiServiceRequestId.URL_PROTOCOL,
                        GobiiServiceRequestId.URL_VENDORS);
        restUriProtocoLVendor.setParamValue("id", newVendorProtocolDTO.getProtocolId().toString());
        GobiiEnvelopeRestResource<OrganizationDTO> protocolVendorResource =
                new GobiiEnvelopeRestResource<>(restUriProtocoLVendor);
        PayloadEnvelope<OrganizationDTO> vendorPayloadEnvelope =
                new PayloadEnvelope<>(organizationDTO, GobiiProcessType.CREATE);
        PayloadEnvelope<OrganizationDTO> protocolVendorResult =
                protocolVendorResource.post(OrganizationDTO.class, vendorPayloadEnvelope);

        checkStatus(protocolVendorResult);

        OrganizationDTO vendorResult = protocolVendorResult.getPayload().getData().get(0);

        for (VendorProtocolDTO vendorProtocolDTO : vendorResult.getVendorProtocols()) {

            if (vendorProtocolDTO.getName().equals(newVendorProtocolDTO.getName())) {

                returnVal = vendorProtocolDTO.getId();
                break;

            }

        }

        System.out.println(entityName + "("+dbPkeysurrogateValue+") is successfully created!\n");

        return returnVal;

    }

    private static Integer createReference(Element parentElement, String entityName, NodeList fkeys, String dbPkeysurrogateValue,
                                          XPath xPath, Document document, NodeList propKeyList) throws Exception {

        Integer returnVal = null;


        System.out.println("\nChecking if " + entityName + " ("+dbPkeysurrogateValue+") already exists in the database...\n");
        /*** check if entity already exist in the database ***/

        RestUri restUriReference = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_REFERENCE);
        GobiiEnvelopeRestResource<ReferenceDTO> gobiiEnvelopeRestResourceGet = new GobiiEnvelopeRestResource<>(restUriReference);
        PayloadEnvelope<ReferenceDTO> resultEnvelope = gobiiEnvelopeRestResourceGet.get(ReferenceDTO.class);

        checkStatus(resultEnvelope);

        List<ReferenceDTO> referenceDTOSList = resultEnvelope.getPayload().getData();

        for (ReferenceDTO currentReferenceDTO : referenceDTOSList) {

            if (currentReferenceDTO.getName().equals(dbPkeysurrogateValue)) {

                System.out.println("\n" +entityName + "("+dbPkeysurrogateValue+") already exists in the database. Return current entity ID.\n");

                /*** set fkey dbpkey for entity ***/

                setFKeyDbPKeyForExistingEntity(fkeys, ReferenceDTO.class, currentReferenceDTO);

                return currentReferenceDTO.getId();

            }

        }

        System.out.println("\n"+entityName+"("+dbPkeysurrogateValue+") doesn't exist in the database.\nCreating new record...\n");

        ReferenceDTO newReferenceDTO = new ReferenceDTO();

        System.out.println("Populating " +entityName+ "DTO with attributes from XML file...");

        for (int j=0; j<propKeyList.getLength(); j++) {

            Element propKey = (Element) propKeyList.item(j);

            String propKeyLocalName = propKey.getLocalName();

            propKeyLocalName = processPropName(propKeyLocalName);

            Field field = ReferenceDTO.class.getDeclaredField(propKeyLocalName);
            field.setAccessible(true);
            field.set(newReferenceDTO, processTypes(propKey.getTextContent(), field.getType()));

        }

        newReferenceDTO.setCreatedDate(new Date());
        newReferenceDTO.setCreatedBy(1);
        newReferenceDTO.setModifiedDate(new Date());
        newReferenceDTO.setModifiedBy(1);

        setFKeyDbPKeyForNewEntity(fkeys, ReferenceDTO.class, newReferenceDTO, parentElement, dbPkeysurrogateValue, document, xPath);


        System.out.println("Calling the web service...\n");

        /*** create reference ***/

        PayloadEnvelope<ReferenceDTO> payloadEnvelopeReference = new PayloadEnvelope<>(newReferenceDTO, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<ReferenceDTO> gobiiEnvelopeRestResourceReference = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                        .getUriFactory()
                        .resourceColl(GobiiServiceRequestId.URL_REFERENCE));
        PayloadEnvelope<ReferenceDTO> referenceDTOResponseEnvelope = gobiiEnvelopeRestResourceReference.post(ReferenceDTO.class,
                payloadEnvelopeReference);


        checkStatus(referenceDTOResponseEnvelope);

        ReferenceDTO referenceDTOResponse = referenceDTOResponseEnvelope.getPayload().getData().get(0);

        returnVal = referenceDTOResponse.getReferenceId();

        System.out.println(entityName + "("+dbPkeysurrogateValue+") is successfully created!\n");

        return  returnVal;

    }

    private static Integer createMapset(Element parentElement, String entityName, NodeList fkeys, String dbPkeysurrogateValue,
                                           XPath xPath, Document document, NodeList propKeyList) throws Exception {

        Integer returnVal = null;


        System.out.println("\nChecking if " + entityName + " ("+dbPkeysurrogateValue+") already exists in the database...\n");
        /*** check if entity already exist in the database ***/

        RestUri restUriMapset = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_MAPSET);
        GobiiEnvelopeRestResource<MapsetDTO> gobiiEnvelopeRestResourceGet = new GobiiEnvelopeRestResource<>(restUriMapset);
        PayloadEnvelope<MapsetDTO> resultEnvelope = gobiiEnvelopeRestResourceGet.get(MapsetDTO.class);

        checkStatus(resultEnvelope);

        List<MapsetDTO> mapsetDTOSList = resultEnvelope.getPayload().getData();

        for (MapsetDTO currentMapsetDTO : mapsetDTOSList) {

            if (currentMapsetDTO.getName().equals(dbPkeysurrogateValue)) {

                System.out.println("\n" +entityName + "("+dbPkeysurrogateValue+") already exists in the database. Return current entity ID.\n");

                /*** set fkey dbpkey for entity ***/

                setFKeyDbPKeyForExistingEntity(fkeys, MapsetDTO.class, currentMapsetDTO);

                return currentMapsetDTO.getId();

            }

        }

        System.out.println("\n"+entityName+"("+dbPkeysurrogateValue+") doesn't exist in the database.\nCreating new record...\n");

        MapsetDTO newMapsetDTO = new MapsetDTO();

        System.out.println("Populating " +entityName+ "DTO with attributes from XML file...");

        Element propertiesElement = null;

        /*** get cv's from mapset_type group ***/

        Map<String, Integer> mapsetTypeMap = getCvTermsWithId("mapset_type");

        for (int j=0; j<propKeyList.getLength(); j++) {

            Element propKey = (Element) propKeyList.item(j);

            String propKeyLocalName = propKey.getLocalName();

            propKeyLocalName = processPropName(propKeyLocalName);

            if (propKeyLocalName.equals("properties")) {

                propertiesElement = propKey;
                continue;

            }

            if (propKeyLocalName.equals("mapType")) {

                Integer typeId = mapsetTypeMap.get(propKey.getTextContent().toLowerCase());

                newMapsetDTO.setMapType(typeId);
                continue;

            }

            if (propKey.getParentNode().equals(propertiesElement)) {

                EntityPropertyDTO entityPropertyDTO = new EntityPropertyDTO();

                entityPropertyDTO.setPropertyName(propKeyLocalName);
                entityPropertyDTO.setPropertyValue(propKey.getTextContent());

                newMapsetDTO.getProperties().add(entityPropertyDTO);

            }
            else {

                Field field = MapsetDTO.class.getDeclaredField(propKeyLocalName);
                field.setAccessible(true);
                field.set(newMapsetDTO, processTypes(propKey.getTextContent(), field.getType()));

            }
        }

        newMapsetDTO.setCreatedDate(new Date());
        newMapsetDTO.setCreatedBy(1);
        newMapsetDTO.setModifiedDate(new Date());
        newMapsetDTO.setModifiedBy(1);


        setFKeyDbPKeyForNewEntity(fkeys, MapsetDTO.class, newMapsetDTO, parentElement, dbPkeysurrogateValue, document, xPath);


        System.out.println("Calling the web service...\n");

        /*** create mapset ***/

        PayloadEnvelope<MapsetDTO> payloadEnvelopeMapset = new PayloadEnvelope<>(newMapsetDTO, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<MapsetDTO> gobiiEnvelopeRestResourceMapset = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                        .getUriFactory()
                        .resourceColl(GobiiServiceRequestId.URL_MAPSET));
        PayloadEnvelope<MapsetDTO> mapsetDTOResponseEnvelope = gobiiEnvelopeRestResourceMapset.post(MapsetDTO.class,
                payloadEnvelopeMapset);


        checkStatus(mapsetDTOResponseEnvelope);

        MapsetDTO mapsetDTOResponse = mapsetDTOResponseEnvelope.getPayload().getData().get(0);

        returnVal = mapsetDTOResponse.getMapsetId();

        System.out.println(entityName + "("+dbPkeysurrogateValue+") is successfully created!\n");

        return returnVal;

    }

    private static Integer createProject(Element parentElement, String entityName, NodeList fkeys, String dbPkeysurrogateValue,
                                        XPath xPath, Document document, NodeList propKeyList) throws Exception {

        Integer returnVal = null;


        System.out.println("\nChecking if " + entityName + " ("+dbPkeysurrogateValue+") already exists in the database...\n");
        /*** check if entity already exist in the database ***/

        RestUri restUriProject = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_PROJECTS);
        GobiiEnvelopeRestResource<ProjectDTO> gobiiEnvelopeRestResourceGet = new GobiiEnvelopeRestResource<>(restUriProject);
        PayloadEnvelope<ProjectDTO> resultEnvelope = gobiiEnvelopeRestResourceGet.get(ProjectDTO.class);

        checkStatus(resultEnvelope);

        List<ProjectDTO> projectDTOSList = resultEnvelope.getPayload().getData();

        for (ProjectDTO currentProjectDTO : projectDTOSList) {

            if (currentProjectDTO.getProjectName().equals(dbPkeysurrogateValue)) {

                System.out.println("\n" +entityName + "("+dbPkeysurrogateValue+") already exists in the database. Return current entity ID.\n");

                /*** set fkey dbpkey for entity ***/

                setFKeyDbPKeyForExistingEntity(fkeys, ProjectDTO.class, currentProjectDTO);

                return currentProjectDTO.getId();

            }

        }

        System.out.println("\n"+entityName+"("+dbPkeysurrogateValue+") doesn't exist in the database.\nCreating new record...\n");

        ProjectDTO newProjectDTO = new ProjectDTO();

        System.out.println("Populating " +entityName+ "DTO with attributes from XML file...");

        Element propertiesElement = null;

        Integer propertyCount = 0;

        for (int j=0; j<propKeyList.getLength(); j++) {

            Element propKey = (Element) propKeyList.item(j);

            String propKeyLocalName = propKey.getLocalName();

            propKeyLocalName = processPropName(propKeyLocalName);

            if (propKeyLocalName.equals("properties")) {

                propertiesElement = propKey;
                continue;

            }

            if (propKey.getParentNode().equals(propertiesElement)) {


            } else {

                Field field = ProjectDTO.class.getDeclaredField(propKeyLocalName);
                field.setAccessible(true);
                field.set(newProjectDTO, processTypes(propKey.getTextContent(), field.getType()));

            }

        }

        newProjectDTO.setCreatedDate(new Date());
        newProjectDTO.setCreatedBy(1);
        newProjectDTO.setModifiedDate(new Date());
        newProjectDTO.setModifiedBy(1);


        setFKeyDbPKeyForNewEntity(fkeys, ProjectDTO.class, newProjectDTO, parentElement, dbPkeysurrogateValue, document, xPath);

        System.out.println("Calling the web service...\n");


        newProjectDTO.getProperties().add(new EntityPropertyDTO(null, null, "division", "foo division"));
        newProjectDTO.getProperties().add(new EntityPropertyDTO(null, null, "study_name", "foo study name"));
        newProjectDTO.getProperties().add(new EntityPropertyDTO(null, null, "genotyping_purpose", "foo purpose"));

        /*** create project ***/

        RestUri projectsUri = GobiiClientContext.getInstance(null, false).getUriFactory().resourceColl(GobiiServiceRequestId.URL_PROJECTS);
        GobiiEnvelopeRestResource<ProjectDTO> gobiiEnvelopeRestResourceForProjects = new GobiiEnvelopeRestResource<>(projectsUri);
        PayloadEnvelope<ProjectDTO> payloadEnvelope = new PayloadEnvelope<>(newProjectDTO, GobiiProcessType.CREATE);
        PayloadEnvelope<ProjectDTO> projectDTOResponseEnvelope = gobiiEnvelopeRestResourceForProjects.post(ProjectDTO.class, payloadEnvelope);

        checkStatus(projectDTOResponseEnvelope);

        ProjectDTO projectDTOResponse = projectDTOResponseEnvelope.getPayload().getData().get(0);

        returnVal = projectDTOResponse.getProjectId();

        System.out.println(entityName + "("+dbPkeysurrogateValue+") is successfully created!\n");

        return returnVal;

    }

    private static Integer createManifest(Element parentElement, String entityName, NodeList fkeys, String dbPkeysurrogateValue,
                                        XPath xPath, Document document, NodeList propKeyList) throws Exception {

        Integer returnVal = null;


        System.out.println("\nChecking if " + entityName + " ("+dbPkeysurrogateValue+") already exists in the database...\n");
        /*** check if entity already exist in the database ***/

        RestUri restUriManifest = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_MANIFEST);
        GobiiEnvelopeRestResource<ManifestDTO> gobiiEnvelopeRestResourceGet = new GobiiEnvelopeRestResource<>(restUriManifest);
        PayloadEnvelope<ManifestDTO> resultEnvelope = gobiiEnvelopeRestResourceGet.get(ManifestDTO.class);

        checkStatus(resultEnvelope);

        List<ManifestDTO> manifestDTOSList = resultEnvelope.getPayload().getData();

        for (ManifestDTO currentManifestDTO : manifestDTOSList) {

            if (currentManifestDTO.getName().equals(dbPkeysurrogateValue)) {

                System.out.println("\n" +entityName + "("+dbPkeysurrogateValue+") already exists in the database. Return current entity ID.\n");

                /*** set fkey dbpkey for entity ***/

                setFKeyDbPKeyForExistingEntity(fkeys, ManifestDTO.class, currentManifestDTO);

                return currentManifestDTO.getId();

            }

        }


        System.out.println("\n"+entityName+"("+dbPkeysurrogateValue+") doesn't exist in the database.\nCreating new record...\n");

        ManifestDTO newManifestDTO = new ManifestDTO();

        System.out.println("Populating " +entityName+ "DTO with attributes from XML file...");

        for (int j=0; j<propKeyList.getLength(); j++) {

            Element propKey = (Element) propKeyList.item(j);

            String propKeyLocalName = propKey.getLocalName();

            propKeyLocalName = processPropName(propKeyLocalName);

            Field field = ManifestDTO.class.getDeclaredField(propKeyLocalName);
            field.setAccessible(true);
            field.set(newManifestDTO, processTypes(propKey.getTextContent(), field.getType()));

        }

        newManifestDTO.setCreatedDate(new Date());
        newManifestDTO.setCreatedBy(1);
        newManifestDTO.setModifiedDate(new Date());
        newManifestDTO.setModifiedBy(1);


        setFKeyDbPKeyForNewEntity(fkeys, ManifestDTO.class, newManifestDTO, parentElement, dbPkeysurrogateValue, document, xPath);


        System.out.println("Calling the web service...\n");

        /*** create manifest ***/


        PayloadEnvelope<ManifestDTO> payloadEnvelopeManifest = new PayloadEnvelope<>(newManifestDTO, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<ManifestDTO> gobiiEnvelopeRestResourceManifest = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                        .getUriFactory()
                        .resourceColl(GobiiServiceRequestId.URL_MANIFEST));
        PayloadEnvelope<ManifestDTO> manifestDTOResponseEnvelope = gobiiEnvelopeRestResourceManifest.post(ManifestDTO.class,
                payloadEnvelopeManifest);


        checkStatus(manifestDTOResponseEnvelope);

        ManifestDTO manifestDTOResponse = manifestDTOResponseEnvelope.getPayload().getData().get(0);

        returnVal = manifestDTOResponse.getManifestId();

        System.out.println(entityName + "("+dbPkeysurrogateValue+") is successfully created!\n");

        return returnVal;


    }

    private static Integer createExperiment(Element parentElement, String entityName, NodeList fkeys, String dbPkeysurrogateValue,
                                        XPath xPath, Document document, NodeList propKeyList) throws Exception {

        Integer returnVal = null;


        System.out.println("\nChecking if " + entityName + " ("+dbPkeysurrogateValue+") already exists in the database...\n");
        /*** check if entity already exist in the database ***/

        RestUri restUriExperiment = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_EXPERIMENTS);
        GobiiEnvelopeRestResource<ExperimentDTO> gobiiEnvelopeRestResourceGet = new GobiiEnvelopeRestResource<>(restUriExperiment);
        PayloadEnvelope<ExperimentDTO> resultEnvelope = gobiiEnvelopeRestResourceGet.get(ExperimentDTO.class);

        checkStatus(resultEnvelope);

        List<ExperimentDTO> experimentDTOSList = resultEnvelope.getPayload().getData();

        for (ExperimentDTO currentExperimentDTO : experimentDTOSList) {

            if (currentExperimentDTO.getExperimentName().equals(dbPkeysurrogateValue)) {

                System.out.println("\n" +entityName + "("+dbPkeysurrogateValue+") already exists in the database. Return current entity ID.\n");

                /*** set fkey dbpkey for entity ***/

                setFKeyDbPKeyForExistingEntity(fkeys, ExperimentDTO.class, currentExperimentDTO);

                return currentExperimentDTO.getId();

            }

        }


        System.out.println("\n"+entityName+"("+dbPkeysurrogateValue+") doesn't exist in the database.\nCreating new record...\n");

        ExperimentDTO newExperimentDTO = new ExperimentDTO();

        System.out.println("Populating " +entityName+ "DTO with attributes from XML file...");

        for (int j=0; j<propKeyList.getLength(); j++) {

            Element propKey = (Element) propKeyList.item(j);

            String propKeyLocalName = propKey.getLocalName();

            propKeyLocalName = processPropName(propKeyLocalName);

            Field field = ExperimentDTO.class.getDeclaredField(propKeyLocalName);
            field.setAccessible(true);
            field.set(newExperimentDTO, processTypes(propKey.getTextContent(), field.getType()));

        }
        newExperimentDTO.setCreatedDate(new Date());
        newExperimentDTO.setCreatedBy(1);
        newExperimentDTO.setModifiedDate(new Date());
        newExperimentDTO.setModifiedBy(1);


        setFKeyDbPKeyForNewEntity(fkeys, ExperimentDTO.class, newExperimentDTO, parentElement, dbPkeysurrogateValue, document, xPath);


        System.out.println("Calling the web service...\n");

        /*** create experiment ***/

        PayloadEnvelope<ExperimentDTO> payloadEnvelopeExperiment = new PayloadEnvelope<>(newExperimentDTO, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<ExperimentDTO> gobiiEnvelopeRestResourceExperiment = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                        .getUriFactory()
                        .resourceColl(GobiiServiceRequestId.URL_EXPERIMENTS));
        PayloadEnvelope<ExperimentDTO> experimentDTOResponseEnvelope = gobiiEnvelopeRestResourceExperiment.post(ExperimentDTO.class,
                payloadEnvelopeExperiment);


        checkStatus(experimentDTOResponseEnvelope);

        ExperimentDTO experimentDTOResponse = experimentDTOResponseEnvelope.getPayload().getData().get(0);

        returnVal = experimentDTOResponse.getExperimentId();

        System.out.println(entityName + "("+dbPkeysurrogateValue+") is successfully created!\n");

        return returnVal;

    }

    private static Integer createAnalysis(Element parentElement, String entityName, NodeList fkeys, String dbPkeysurrogateValue,
                                        XPath xPath, Document document, NodeList propKeyList) throws Exception {

        Integer returnVal = null;


        System.out.println("\nChecking if " + entityName + " ("+dbPkeysurrogateValue+") already exists in the database...\n");
        /*** check if entity already exist in the database ***/

        RestUri restUriAnalysis = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_ANALYSIS);
        GobiiEnvelopeRestResource<AnalysisDTO> gobiiEnvelopeRestResourceGet = new GobiiEnvelopeRestResource<>(restUriAnalysis);
        PayloadEnvelope<AnalysisDTO> resultEnvelope = gobiiEnvelopeRestResourceGet.get(AnalysisDTO.class);

        checkStatus(resultEnvelope);

        List<AnalysisDTO> analysisDTOSList = resultEnvelope.getPayload().getData();

        for (AnalysisDTO currentAnalysisDTO : analysisDTOSList) {

            if (currentAnalysisDTO.getAnalysisName().equals(dbPkeysurrogateValue)) {

                System.out.println("\n" +entityName + "("+dbPkeysurrogateValue+") already exists in the database. Return current entity ID.\n");

                /*** set fkey dbpkey for entity ***/

                setFKeyDbPKeyForExistingEntity(fkeys, AnalysisDTO.class, currentAnalysisDTO);

                return currentAnalysisDTO.getId();

            }

        }


        System.out.println("\n"+entityName+"("+dbPkeysurrogateValue+") doesn't exist in the database.\nCreating new record...\n");

        AnalysisDTO newAnalysisDTO = new AnalysisDTO();

        System.out.println("Populating " +entityName+ "DTO with attributes from XML file...");

        Element paramElement = null;

        /*** get cv's from analysis_type group ***/

        Map<String, Integer> analysisTypeMap = getCvTermsWithId("analysis_type");

        for (int j=0; j<propKeyList.getLength(); j++) {

            Element propKey = (Element) propKeyList.item(j);

            String propKeyLocalName = propKey.getLocalName();

            propKeyLocalName = processPropName(propKeyLocalName);

            if(propKeyLocalName.equals("parameters")){

                paramElement = propKey;
                continue;
            }

            if (propKeyLocalName.equals("anlaysisTypeId")) {

                Integer typeId = analysisTypeMap.get(propKey.getTextContent().toLowerCase());

                newAnalysisDTO.setAnlaysisTypeId(typeId);
                continue;
            }

            if (propKey.getParentNode().equals(paramElement)) {

                EntityPropertyDTO entityPropertyDTO = new EntityPropertyDTO();

                entityPropertyDTO.setPropertyName(propKeyLocalName);
                entityPropertyDTO.setPropertyValue(propKey.getTextContent());

                newAnalysisDTO.getParameters().add(entityPropertyDTO);
            }
            else {

                Field field = AnalysisDTO.class.getDeclaredField(propKeyLocalName);
                field.setAccessible(true);
                field.set(newAnalysisDTO, processTypes(propKey.getTextContent(), field.getType()));

            }
        }

        newAnalysisDTO.setCreatedDate(new Date());
        newAnalysisDTO.setCreatedBy(1);
        newAnalysisDTO.setModifiedDate(new Date());
        newAnalysisDTO.setModifiedBy(1);


        setFKeyDbPKeyForNewEntity(fkeys, AnalysisDTO.class, newAnalysisDTO, parentElement, dbPkeysurrogateValue, document, xPath);


        System.out.println("Calling the web service...\n");

        /*** create analysis ***/

        PayloadEnvelope<AnalysisDTO> payloadEnvelopeAnalysis = new PayloadEnvelope<>(newAnalysisDTO, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<AnalysisDTO> gobiiEnvelopeRestResourceAnalysis = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                        .getUriFactory()
                        .resourceColl(GobiiServiceRequestId.URL_ANALYSIS));
        PayloadEnvelope<AnalysisDTO> analysisDTOResponseEnvelope = gobiiEnvelopeRestResourceAnalysis.post(AnalysisDTO.class,
                payloadEnvelopeAnalysis);


        checkStatus(analysisDTOResponseEnvelope);

        AnalysisDTO analysisDTOResponse = analysisDTOResponseEnvelope.getPayload().getData().get(0);

        returnVal = analysisDTOResponse.getAnalysisId();

        System.out.println(entityName + "("+dbPkeysurrogateValue+") is successfully created!\n");

        return returnVal;



    }

    private static Integer createDataset(Element parentElement, String entityName, NodeList fkeys, String dbPkeysurrogateValue,
                                        XPath xPath, Document document, NodeList propKeyList) throws Exception {

        Integer returnVal = null;


        System.out.println("\nChecking if " + entityName + " ("+dbPkeysurrogateValue+") already exists in the database...\n");
        /*** check if entity already exist in the database ***/

        RestUri restUriDataset = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_DATASETS);
        GobiiEnvelopeRestResource<DataSetDTO> gobiiEnvelopeRestResourceGet = new GobiiEnvelopeRestResource<>(restUriDataset);
        PayloadEnvelope<DataSetDTO> resultEnvelope = gobiiEnvelopeRestResourceGet.get(DataSetDTO.class);

        checkStatus(resultEnvelope);

        List<DataSetDTO> datasetDTOSList = resultEnvelope.getPayload().getData();

        for (DataSetDTO currentDatasetDTO : datasetDTOSList) {

            if (currentDatasetDTO.getName().equals(dbPkeysurrogateValue)) {

                System.out.println("\n" +entityName + "("+dbPkeysurrogateValue+") already exists in the database. Return current entity ID.\n");

                /*** set fkey dbpkey for entity ***/

                setFKeyDbPKeyForExistingEntity(fkeys, DataSetDTO.class, currentDatasetDTO);

                return currentDatasetDTO.getId();

            }

        }


        System.out.println("\n"+entityName+"("+dbPkeysurrogateValue+") doesn't exist in the database.\nCreating new record...\n");

        DataSetDTO newDataSetDTO = new DataSetDTO();

        System.out.println("Populating " +entityName+ "DTO with attributes from XML file...");

        /*** get cv's from dataset_type group ***/

        Map<String, Integer> datasetTypeMap = getCvTermsWithId("dataset_type");


        for (int j=0; j<propKeyList.getLength(); j++) {

            Element propKey = (Element) propKeyList.item(j);

            String propKeyLocalName = propKey.getLocalName();

            propKeyLocalName = processPropName(propKeyLocalName);

            if(propKeyLocalName.equals("analysesIds")) {continue;}

            if(propKeyLocalName.equals("analysisId")) {

                newDataSetDTO.getAnalysesIds().add(Integer.parseInt(propKey.getTextContent()));
                continue;
            }

            if (propKeyLocalName.equals("typeId")) {

                Integer typeId = datasetTypeMap.get(propKey.getTextContent().toLowerCase());

                newDataSetDTO.setTypeId(typeId);
                continue;
            }


            if(propKeyLocalName.equals("scores")) {continue;}

            if(propKeyLocalName.equals("score")) {

                newDataSetDTO.getScores().add(Integer.parseInt(propKey.getTextContent()));
                continue;
            }

            Field field = DataSetDTO.class.getDeclaredField(propKeyLocalName);
            field.setAccessible(true);
            field.set(newDataSetDTO, processTypes(propKey.getTextContent(), field.getType()));

        }

        newDataSetDTO.setCreatedDate(new Date());
        newDataSetDTO.setCreatedBy(1);
        newDataSetDTO.setModifiedDate(new Date());
        newDataSetDTO.setModifiedBy(1);


        setFKeyDbPKeyForNewEntity(fkeys, DataSetDTO.class, newDataSetDTO, parentElement, dbPkeysurrogateValue, document, xPath);


        System.out.println("Calling the web service...\n");

        /*** create dataset ***/

        PayloadEnvelope<DataSetDTO> payloadEnvelopeDataSet = new PayloadEnvelope<>(newDataSetDTO, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<DataSetDTO> gobiiEnvelopeRestResourceDataSet = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                        .getUriFactory()
                        .resourceColl(GobiiServiceRequestId.URL_DATASETS));
        PayloadEnvelope<DataSetDTO> dataSetDTOResponseEnvelope = gobiiEnvelopeRestResourceDataSet.post(DataSetDTO.class,
                payloadEnvelopeDataSet);


        checkStatus(dataSetDTOResponseEnvelope);

        DataSetDTO dataSetDTOResponse = dataSetDTOResponseEnvelope.getPayload().getData().get(0);

        returnVal = dataSetDTOResponse.getDataSetId();

        System.out.println(entityName + "("+dbPkeysurrogateValue+") is successfully created!\n");

        return returnVal;


    }

    private static Integer createEntity(Element parentElement, String entityName, NodeList fKeys, String dbPkeysurrogateValue, XPath xPath, Document document) throws Exception {

        Integer returnVal = null;


        Element props = (Element) parentElement.getElementsByTagName("Properties").item(0);
        NodeList propKeyList = props.getElementsByTagName("*");

        GobiiClientContextAuth.authenticate();

        switch (entityName) {

            case "Organization" :

                returnVal = createOrganization(parentElement, entityName, fKeys, dbPkeysurrogateValue, xPath, document, propKeyList);

                break;

            case "Contact":

                returnVal = createContact(parentElement, entityName, fKeys, dbPkeysurrogateValue, xPath, document, propKeyList);

                break;

            case "Platform":

                returnVal = createPlatform(parentElement, entityName, fKeys, dbPkeysurrogateValue, xPath, document, propKeyList);

                break;

            case "Protocol":

                returnVal = createProtocol(parentElement, entityName, fKeys, dbPkeysurrogateValue, xPath, document, propKeyList);

                break;

            case "VendorProtocol":

                returnVal = createVendorProtocol(parentElement, entityName, fKeys, dbPkeysurrogateValue, xPath, document, propKeyList);

                break;

            case "Reference":

                returnVal = createReference(parentElement, entityName, fKeys, dbPkeysurrogateValue, xPath, document, propKeyList);

                break;

            case "Mapset":

                returnVal = createMapset(parentElement, entityName, fKeys, dbPkeysurrogateValue, xPath, document, propKeyList);

                break;

            case "Project":

                returnVal = createProject(parentElement, entityName, fKeys, dbPkeysurrogateValue, xPath, document, propKeyList);

                break;

            case "Manifest":

                returnVal = createManifest(parentElement, entityName, fKeys, dbPkeysurrogateValue, xPath, document, propKeyList);

                break;

            case "Experiment":

                returnVal = createExperiment(parentElement, entityName, fKeys, dbPkeysurrogateValue, xPath, document, propKeyList);

                break;

            case "Analysis":

                returnVal = createAnalysis(parentElement, entityName, fKeys, dbPkeysurrogateValue, xPath, document, propKeyList);

                break;

            case "Dataset":

                returnVal = createDataset(parentElement, entityName, fKeys, dbPkeysurrogateValue, xPath, document, propKeyList);

                break;

            default:
                break;


        }


        return returnVal;

    }


    private static void setFKeyDbPKeyForNewEntity(NodeList fkeys, Class currentClass, Object currentDTO, Element parentElement, String dbPkeysurrogateValue,
                                      Document document, XPath xPath) throws Exception {

        if(fkeys != null && fkeys.getLength() > 0) {

            for (int i=0; i<fkeys.getLength(); i++) {

                Element currentFkeyElement = (Element) fkeys.item(i);

                String fkproperty = currentFkeyElement.getAttribute("fkproperty");

                Integer fKeyDbPkey = getFKeyDbPKey(currentFkeyElement, parentElement, dbPkeysurrogateValue, document, xPath);

                Field field = currentClass.getDeclaredField(fkproperty);
                field.setAccessible(true);
                field.set(currentDTO, fKeyDbPkey);

            }

        }

    }


    private static void setFKeyDbPKeyForExistingEntity(NodeList fkeys, Class currentClass, Object currentDTO) throws Exception {

        if(fkeys != null && fkeys.getLength() > 0) {

            for (int i=0; i<fkeys.getLength(); i++) {

                Element currentFkeyElement = (Element) fkeys.item(i);

                String fkproperty = currentFkeyElement.getAttribute("fkproperty");

                Field field = currentClass.getDeclaredField(fkproperty);
                field.setAccessible(true);

                Element currentFkeydbPKeyElement = (Element) currentFkeyElement.getElementsByTagName("DbPKey").item(0);

                currentFkeydbPKeyElement.setTextContent(field.get(currentDTO).toString());

            }

        }

    }


    private static Integer getFKeyDbPKey(Element currentFkeyElement, Element parentElement, String dbPkeysurrogateValue, Document document, XPath xPath) throws Exception {


        String entity = currentFkeyElement.getAttribute("entity");

        String fKeyDbPkeyValue = currentFkeyElement.getElementsByTagName("DbPKeySurrogate").item(0).getTextContent();


        System.out.println("\nWriting "+ entity+ " (" + fKeyDbPkeyValue+ ") FkeyDbPkey for " + parentElement.getLocalName()+
                "  (" +dbPkeysurrogateValue+ " ) to file...\n");



        XPathExpression exprParentFkey = xPath.compile("//"+entity+"/parent::*");
        Element ancestor = (Element) exprParentFkey.evaluate(document, XPathConstants.NODE);

        String fkeyPKey = ancestor.getAttribute("DbPKeysurrogate");

        String exprCheckIfFKeyExists = "//Entities/"+ancestor.getNodeName()+"/"+entity+"/Properties["+fkeyPKey+"='"+fKeyDbPkeyValue+"']";

        XPathExpression xPathExprNodeFKey = xPath.compile(exprCheckIfFKeyExists);
        Element nodeFKey = (Element) xPathExprNodeFKey.evaluate(document, XPathConstants.NODE);

        Element parentNode = (Element) nodeFKey.getParentNode();

        String dbPkeyValue =  ((Element) parentNode.getElementsByTagName("Keys").item(0)).getElementsByTagName("DbPKey").item(0).getTextContent();

        // set to <FKey><DbPkey></DbPkey></Fkey>

        Element currentFkeydbPKeyElement = (Element) currentFkeyElement.getElementsByTagName("DbPKey").item(0);

        currentFkeydbPKeyElement.setTextContent(dbPkeyValue);

        return Integer.parseInt(dbPkeyValue);


    }

    private static void writePkValues(NodeList nodeList, XPath xPath, Document document) throws Exception{

        for (int i=0; i<nodeList.getLength(); i++) {

            Element element = (Element) nodeList.item(i);

            Element parentElement = (Element) element.getParentNode();

            String parentLocalName = parentElement.getLocalName();

            Element rootElement = (Element) parentElement.getParentNode();

            String DBPKeysurrogateName = rootElement.getAttribute("DbPKeysurrogate");

            Element props = (Element) parentElement.getElementsByTagName("Properties").item(0);

            String dbPkeysurrogateValue = props.getElementsByTagName(DBPKeysurrogateName).item(0).getTextContent();

            Element dbPKey = (Element) element.getElementsByTagName("DbPKey").item(0);

            NodeList fkeys = element.getElementsByTagName("Fkey");

            Integer returnEntityId = createEntity(parentElement, parentLocalName, fkeys, dbPkeysurrogateValue, xPath, document);

            System.out.println("\nWriting DbPKey for "+parentLocalName+" ("+dbPkeysurrogateValue+") " +
                    " to file...\n");

            dbPKey.setTextContent(returnEntityId.toString());
        }

    }

    private static void getEntities(XPath xPath, Document document, File fXmlFile) throws Exception{

        /****** get nodes with no FKey dependencies to update DbPKey ******/

        String expr = "//*[local-name() = 'Keys' and not(descendant::*[local-name() = 'Fkey'])]";

        XPathExpression xPathExpression = xPath.compile(expr);

        NodeList nodeList = (NodeList) xPathExpression.evaluate(document, XPathConstants.NODESET);

        writePkValues(nodeList, xPath, document);

        writeToFile(document, fXmlFile);


        /***** update DbPKey of elements with FKey dependencies ********/

        String constantStr = "//*[local-name() = 'Keys' and descendant::*[local-name() = 'Fkey'] and parent::*[local-name() = ";
        List<String> entityList = new ArrayList<>();

        entityList.add("Contact");
        entityList.add("Protocol");
        entityList.add("Project");
        entityList.add("VendorProtocol");
        entityList.add("Experiment");
        entityList.add("Dataset");

        for (int i=0; i<entityList.size(); i++) {

            expr = constantStr + "'"+entityList.get(i)+"']]";
            xPathExpression = xPath.compile(expr);
            nodeList = (NodeList) xPathExpression.evaluate(document, XPathConstants.NODESET);
            writePkValues(nodeList, xPath, document);

        }

        writeToFile(document, fXmlFile);

    }

    private static void writeToFile(Document document, File fXmlFile) throws Exception{

        // Get file ready to write
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        StreamResult result = new StreamResult(new FileWriter(fXmlFile));
        transformer.transform(new DOMSource(document), result);

        // Write file out
        result.getWriter().flush();

    }

    private static void parseScenarios(NodeList nodeList, XPath xPath, Document document, File fXmlFile) throws  Exception{

        JsonParser parser = new JsonParser();

        for(int i=0; i<nodeList.getLength(); i++) {

            Element currentElement = (Element) nodeList.item(i);

            String scenarioName = currentElement.getElementsByTagName("Name").item(0).getTextContent();

            System.out.println("Parsing scenario: " + scenarioName);

            String dataExpr = "//Scenario[Name='"+scenarioName+"']/Files/Data";

            XPathExpression xPathExpressionData = xPath.compile(dataExpr);

            String sourcePath = (String) xPathExpressionData.evaluate(document, XPathConstants.STRING);

            boolean writeSourcePath = true;

            String fileExpr = "//Scenario[Name='"+scenarioName+"']/Files/Instruction";

            XPathExpression xPathExpressionFiles = xPath.compile(fileExpr);

            String instructionFilePath = (String) xPathExpressionFiles.evaluate(document, XPathConstants.STRING);

            Object obj;

            if (!new File(instructionFilePath).exists()){

                ClassLoader classLoader = GobiiTestData.class.getClassLoader();

                if(classLoader.getResourceAsStream(instructionFilePath) == null){

                    throw new Exception(" Instruction file template " + instructionFilePath + " not found.");
                }

                InputStream inputStream = classLoader.getResourceAsStream(instructionFilePath);
                StringBuilder sb = new StringBuilder();

                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                obj = parser.parse(sb.toString());

            } else {

                 obj = parser.parse(new FileReader(instructionFilePath));

            }

            JsonArray jsonArray = (JsonArray) obj;

            NodeList dbPkeys = currentElement.getElementsByTagName("DbFkey");

            for(int j=0; j<dbPkeys.getLength(); j++) {

                Element currentDbPkeyElement = (Element) dbPkeys.item(j);

                String entityName = currentDbPkeyElement.getAttribute("entity");

                Element dbPkeySurrogateElement = (Element) currentDbPkeyElement.getElementsByTagName("DbPKeySurrogate").item(0);

                String dbPkeySurrogateValue = dbPkeySurrogateElement.getTextContent();

                // get DbPKeysurrogate attribute of entity (ie Contacts - Email)

                String expr = "//" + entityName + "s/@DbPKeysurrogate";

                XPathExpression xPathExpression = xPath.compile(expr);

                String refAttr = (String) xPathExpression.evaluate(document, XPathConstants.STRING);

                // check if entity with the specify dbPkeysurrogate value exist in the file
                expr = "count(//Entities/"+entityName+"s/"+entityName+"/Properties["+refAttr+"='"+dbPkeySurrogateValue+"'])";

                xPathExpression = xPath.compile(expr);

                Double count = (Double) xPathExpression.evaluate(document, XPathConstants.NUMBER);

                if(count <= 0) {

                    throw new Exception(entityName + ": " + dbPkeySurrogateValue + " does not exist in the file.");
                }

                expr = "//"+entityName+"[Properties/"+refAttr+"='"+dbPkeySurrogateValue+"']/Keys/DbPKey";

                xPathExpression = xPath.compile(expr);

                Element currentEntity = (Element) xPathExpression.evaluate(document, XPathConstants.NODE);

                if(currentEntity.getTextContent().isEmpty()) {

                    throw new Exception("The primary DB key of " + entityName + ": " + dbPkeySurrogateValue + " is not written in the file");
                }

                Element dbPkeyElement = (Element) currentDbPkeyElement.getElementsByTagName("DbPKey").item(0);

                String currentEntityId = currentEntity.getTextContent();

                dbPkeyElement.setTextContent(currentEntityId);
                writeToFile(document, fXmlFile);

                //write to instruction file

                if(entityName.equals("Dataset")){

                    entityName = "dataSet";

                } else{
                    entityName = entityName.toLowerCase();
                }

                for (int k = 0; k < jsonArray.size(); k++) {
                    JsonObject object = (JsonObject) jsonArray.get(k);

                    if(entityName.equals("contact")){

                        if(object.has("contactId")){
                            object.addProperty("contactId", currentEntityId);
                        }

                        if(object.has("contactEmail")){
                            object.addProperty("contactEmail", dbPkeySurrogateValue);
                        }

                        continue;
                    }

                    if(entityName.equals("dataSet")) {
                        if (object.has("dataSetId")) {
                            object.addProperty("dataSetId", currentEntityId);
                        }
                    }

                    JsonObject tempObject = (JsonObject) object.get(entityName);

                    tempObject.addProperty("name", dbPkeySurrogateValue);
                    tempObject.addProperty("id", currentEntityId);

                    object.add(entityName, tempObject);

                    if(writeSourcePath) {

                        JsonObject gobiiFileObject = (JsonObject) object.get("gobiiFile");
                        gobiiFileObject.addProperty("source", sourcePath);

                        object.add("gobiiFile", gobiiFileObject);

                    }

                    jsonArray.set(k, object);
                }
            }

            // update instruction file
            System.out.println("\nWriting instruction file for " +scenarioName + "\n");
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String prettyJsonString = gson.toJson(jsonArray);

            if (new File(instructionFilePath).exists()) {

                FileWriter instructionFileWriter = new FileWriter(instructionFilePath);
                instructionFileWriter.write(prettyJsonString);
                instructionFileWriter.close();

            }
        }
    }


    public static void main(String[] args) throws Exception{

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);

        File fXmlFile;
        if(args.length != 1) {
            ClassLoader classLoader = GobiiTestData.class.getClassLoader();
            fXmlFile = new File(classLoader.getResource("test_profiles/codominant_test.xml").getFile());
        } else {
            fXmlFile = new File(args[0]);
        }



        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(fXmlFile);

        XPath xPath = XPathFactory.newInstance().newXPath();

        //check if all DbPKeysurrogate value is unique for each entity
        String getAllNotesExpr = "//Entities/*";

        XPathExpression xPathExpression = xPath.compile(getAllNotesExpr);

        NodeList nodeList = (NodeList) xPathExpression.evaluate(document, XPathConstants.NODESET);

        validateKeys(nodeList, xPath, document);

        System.out.println("\nFile passed key checks...\n");

        getEntities(xPath, document, fXmlFile);

        System.out.println("\n\n\nSuccessfully saved DbPKeys to file\n");

        System.out.println("\nParsing Scenarios...\n");

        String getAllScenarios = "//Scenarios/*";

        xPathExpression = xPath.compile(getAllScenarios);

        nodeList = (NodeList) xPathExpression.evaluate(document, XPathConstants.NODESET);

        parseScenarios(nodeList, xPath, document, fXmlFile);

    }

}
