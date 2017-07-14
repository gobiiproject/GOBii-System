package org.gobiiproject.gobiiclient.dtorequests.Helpers;

import org.gobiiproject.gobiiclient.dtorequests.dbops.crud.*;
import org.gobiiproject.gobiimodel.headerlesscontainer.*;
import org.gobiiproject.gobiimodel.headerlesscontainer.OrganizationDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.ProtocolDTO;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.utils.DateUtils;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by Phil on 4/27/2016.
 */
public class TestDtoFactory {

    public static EntityParamValues makeArbitraryEntityParams() {

        EntityParamValues returnVal = new EntityParamValues();


        returnVal.add("fooparam", "fooval");
        returnVal.add("barparam", "barval");
        returnVal.add("foobarparam", "foobarval");

        return returnVal;
    }

    public static EntityParamValues makeConstrainedEntityParams(List<NameIdDTO> propNames,
                                                                Integer nameStem) {

        EntityParamValues returnVal = new EntityParamValues();

        for (NameIdDTO currentPropName : propNames) {

            returnVal.add(currentPropName.getName(), "fooval " + (nameStem++));
        }

        return returnVal;
    }

    public static AnalysisDTO makePopulatedAnalysisDTO(GobiiProcessType gobiiProcessType,
                                                       Integer uniqueStem,
                                                       EntityParamValues entityParamValues) {

        AnalysisDTO returnVal = new AnalysisDTO();

        returnVal.setAnalysisName(uniqueStem + ": analysis");
        returnVal.setTimeExecuted(new Date());
        returnVal.setSourceUri(uniqueStem + ":  foo URL");
        returnVal.setAlgorithm(uniqueStem + ":  foo algorithm");
        returnVal.setSourceName(uniqueStem + ":  foo source");
        returnVal.setAnalysisDescription(uniqueStem + ":  my analysis description");
        returnVal.setProgram(uniqueStem + ":  foo program");
        returnVal.setProgramVersion(uniqueStem + ":  foo version");
        returnVal.setAnlaysisTypeId(1);
        returnVal.setStatusId(1);
        returnVal.setCreatedBy(1);
        returnVal.setModifiedBy(1);
        returnVal.setCreatedDate(new Date());
        returnVal.setModifiedDate(new Date());

        returnVal.setParameters(entityParamValues.getProperties());

        return returnVal;

    }

    public static MarkerDTO makeMarkerDTO(String markerName) throws Exception {

        MarkerDTO returnVal = new MarkerDTO();

        // required values
        returnVal.setMarkerName(markerName);
        Integer platformId = (new GlobalPkColl<DtoCrudRequestPlatformTest>())
                .getAPkVal(DtoCrudRequestPlatformTest.class,
                        GobiiEntityNameType.PLATFORMS);
        returnVal.setPlatformId(platformId);
        returnVal.setStatus(1);

        return returnVal;
    }

    public static PlatformDTO makePopulatedPlatformDTO(GobiiProcessType gobiiProcessType,
                                                       Integer uniqueStem,
                                                       EntityParamValues entityParamValues) {

        PlatformDTO returnVal = new PlatformDTO();

        String uniqueStemString = uniqueStem.toString();
        // set the plain properties
        returnVal.setStatusId(1);
        returnVal.setModifiedBy(1);
        returnVal.setModifiedDate(new Date());
        returnVal.setCreatedBy(1);
        returnVal.setCreatedDate(new Date());
        returnVal.setPlatformCode(uniqueStem + "dummy code");
        returnVal.setPlatformDescription(uniqueStem + "dummy description");
        returnVal.setPlatformName(uniqueStem + "New Platform");
        returnVal.setTypeId(1);

        returnVal.setProperties(entityParamValues.getProperties());
        return returnVal;

    }


    public static CvDTO makePopulatedCvDTO(GobiiProcessType gobiiProcessType,
                                           Integer uniqueStem) {

        CvDTO returnVal = new CvDTO();
        returnVal.setGroupId(14);
        returnVal.setTerm(UUID.randomUUID().toString());
        returnVal.setDefinition(uniqueStem + "dummy definition");
        returnVal.setRank(1);
        returnVal.setEntityStatus(1);

        return returnVal;

    }

    public static DisplayDTO makePopulatedDisplayDTO(GobiiProcessType gobiiProcessType,
                                                     Integer uniqueStem) {

        DisplayDTO returnVal = new DisplayDTO();
        returnVal.setColumnName(uniqueStem + "dummy column");
        returnVal.setCreatedBy(1);
        returnVal.setDisplayName(uniqueStem + "dummyDisplay");
        returnVal.setCreatedDate(new Date());
        returnVal.setDisplayId(1);
        returnVal.setModifiedBy(1);
        returnVal.setModifiedDate(new Date());
        returnVal.setTableName(uniqueStem + "dummy table");
        returnVal.setDisplayRank(uniqueStem);


        return returnVal;
    }

    public static String getFolderNameWithTimestamp(String folderName) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String returnVal = folderName.replaceAll(" ", "_") + DateUtils.makeDateIdString();


        return returnVal;
    }

    public static DataSetDTO makePopulatedDataSetDTO(Integer uniqueStem,
                                                     Integer callingAnalysisId,
                                                     List<Integer> analysisIds) throws Exception {

        DataSetDTO returnVal = new DataSetDTO();


        // set the big-ticket items

        Integer experimentId = (new GlobalPkColl<DtoCrudRequestExperimentTest>().getAPkVal(DtoCrudRequestExperimentTest.class, GobiiEntityNameType.EXPERIMENTS));


        returnVal.getScores().add(1);
        returnVal.getScores().add(2);
        returnVal.getScores().add(3);

        // set the plain properties
        returnVal.setName(uniqueStem + ": foo name");
        returnVal.setStatusId(1);
        returnVal.setCreatedBy(1);
        returnVal.setCreatedDate(new Date());
        returnVal.setDataFile(uniqueStem + ": foo file");
        returnVal.setQualityFile(uniqueStem + ": foo quality file");
        returnVal.setExperimentId(experimentId);
        returnVal.setDataTable(uniqueStem + ": foo table");
        returnVal.setModifiedBy(1);
        returnVal.setModifiedDate(new Date());
        returnVal.setCallingAnalysisId(callingAnalysisId);
        for (Integer currentAnalysisId : analysisIds) {
            returnVal.getAnalysesIds().add(currentAnalysisId);
        }
        returnVal.setTypeId(93);

        return returnVal;

    }


    public static MapsetDTO makePopulatedMapsetDTO(GobiiProcessType gobiiProcessType,
                                                   Integer uniqueStem,
                                                   EntityParamValues entityParamValues) {

        MapsetDTO returnVal = new MapsetDTO();

        String uniqueStemString = uniqueStem.toString();
        // set the plain properties
        returnVal.setName(uniqueStem + "dummy name");
        returnVal.setCode(uniqueStem + "add dummy code");
        returnVal.setCreatedBy(1);
        returnVal.setCreatedDate(new Date());
        returnVal.setDescription(uniqueStem + "dummy description");
        returnVal.setMapType(1);
        returnVal.setModifiedBy(1);
        returnVal.setModifiedDate(new Date());
        returnVal.setReferenceId(GlobalPkValues.getInstance().getAPkVal(GobiiEntityNameType.REFERENCES));
        returnVal.setStatusId(1);

        returnVal.setProperties(entityParamValues.getProperties());

        return returnVal;

    }

    public static ReferenceDTO makePopulatedReferenceDTO(GobiiProcessType gobiiProcessType,
                                                         Integer uniqueStem) {

        ReferenceDTO returnVal = new ReferenceDTO();

        String uniqueStemString = uniqueStem.toString();
        returnVal.setName(uniqueStem + ": reference");
        returnVal.setVersion("version:" + uniqueStem);
        returnVal.setLink(uniqueStem + " link");
        returnVal.setFilePath(uniqueStem + " file path");

        return returnVal;

    }


    public static OrganizationDTO makePopulatedOrganizationDTO(GobiiProcessType gobiiProcessType,
                                                               Integer uniqueStem) {

        OrganizationDTO returnVal = new OrganizationDTO();

        String uniqueStemString = UUID.randomUUID().toString();
        returnVal.setName(uniqueStemString + ": organization");
        returnVal.setAddress("address:" + uniqueStem);
        returnVal.setWebsite(uniqueStem + ".com");
        returnVal.setCreatedBy(1);
        returnVal.setCreatedDate(new Date());
        returnVal.setModifiedBy(1);
        returnVal.setModifiedDate(new Date());
        returnVal.setStatusId(1);

        return returnVal;

    }

    public static ProtocolDTO makePopulatedProtocolDTO(GobiiProcessType gobiiProcessType,
                                                       Integer uniqueStem) throws Exception {

        ProtocolDTO returnVal = new ProtocolDTO();

        Integer platformId = (new GlobalPkColl<DtoCrudRequestPlatformTest>())
                .getAPkVal(DtoCrudRequestPlatformTest.class,
                        GobiiEntityNameType.PLATFORMS);

        String uniqueStemString = UUID.randomUUID().toString();
        returnVal.setName(uniqueStemString + ": protocol");
        returnVal.setDescription(uniqueStemString + ": dummy description");
        returnVal.setTypeId(1);
        returnVal.setCreatedBy(1);
        returnVal.setCreatedDate(new Date());
        returnVal.setModifiedBy(1);
        returnVal.setModifiedDate(new Date());
        returnVal.setPlatformId(platformId);
        returnVal.setStatus(1);

        return returnVal;

    }

    public static ManifestDTO makePopulatedManifestDTO(GobiiProcessType gobiiProcessType,
                                                       Integer uniqueStem) {

        ManifestDTO returnVal = new ManifestDTO();

        String uniqueStemString = uniqueStem.toString();
        returnVal.setName(uniqueStem + ": reference");
        returnVal.setCode("version:" + uniqueStem);
        returnVal.setFilePath(uniqueStem + " file path");
        returnVal.setCreatedBy(1);
        returnVal.setCreatedDate(new Date());
        returnVal.setModifiedBy(1);
        returnVal.setModifiedDate(new Date());

        return returnVal;

    }

    public static ContactDTO makePopulatedContactDTO(GobiiProcessType gobiiProcessType,
                                                     String uniqueStem) throws Exception {

        String uniqueStemString = uniqueStem.toString();
        ContactDTO returnVal = new ContactDTO();
        // set the plain properties

        Integer organizationId = (new GlobalPkColl<DtoCrudRequestOrganizationTest>()).getAPkVal(DtoCrudRequestOrganizationTest.class,
                GobiiEntityNameType.ORGANIZATIONS);


        returnVal.setFirstName(uniqueStem + " new contact");
        returnVal.setLastName(uniqueStem + "new lastname");
        returnVal.setEmail(uniqueStem + "mail@email.com");
        returnVal.setCode(uniqueStem + "added New Code");
        returnVal.setCreatedBy(1);
        returnVal.setCreatedDate(new Date());
        returnVal.setModifiedBy(1);
        returnVal.setModifiedDate(new Date());
        returnVal.setOrganizationId(organizationId);
        returnVal.getRoles().add(1);
        returnVal.getRoles().add(2);
        returnVal.setUserName(uniqueStem + "new username");

        return returnVal;

    }

    public static List<MarkerGroupMarkerDTO> makeMarkerGroupMarkers(List<String> markerNames,
                                                                    GobiiProcessType gobiiProcessType) {

        List<MarkerGroupMarkerDTO> returnVal = new ArrayList<>();

        for (String currentMarkerName : markerNames) {

            MarkerGroupMarkerDTO currentMarkerGroupMarker = new MarkerGroupMarkerDTO();
            currentMarkerGroupMarker.setGobiiProcessType(gobiiProcessType);
            currentMarkerGroupMarker.setMarkerName(currentMarkerName);
            currentMarkerGroupMarker.setFavorableAllele("G");
            returnVal.add(currentMarkerGroupMarker);

        }

        return returnVal;

    }

    public static MarkerGroupDTO makePopulatedMarkerGroupDTO(GobiiProcessType gobiiProcessType,
                                                             Integer uniqueStem,
                                                             List<MarkerGroupMarkerDTO> markerGroupMarkers) {

        MarkerGroupDTO returnVal = new MarkerGroupDTO();

        returnVal.setMarkers(markerGroupMarkers);
        returnVal.setStatusId(1);
        returnVal.setCode(uniqueStem + "_code");
        returnVal.setGermplasmGroup(uniqueStem + "_germplasmGroup");
        returnVal.setName(uniqueStem + "_name");

        return returnVal;

    }

    public static PingDTO makePingDTO() throws Exception {

        PingDTO returnVal = new PingDTO();

        returnVal.getDbMetaData().add("test string 1");
        returnVal.getDbMetaData().add("test string 2");

        return returnVal;

    }
    public static QCInstructionsDTO makePopulatedQCInstructionsDTO() throws Exception {

        QCInstructionsDTO returnVal = new QCInstructionsDTO();
        Integer contactId = (new GlobalPkColl<DtoCrudRequestContactTest>()).getAPkVal(DtoCrudRequestContactTest.class,
                GobiiEntityNameType.CONTACTS);
        Integer datasetId = (new GlobalPkColl<DtoCrudRequestDataSetTest>()).getAPkVal(DtoCrudRequestDataSetTest.class,
                GobiiEntityNameType.DATASETS);
                returnVal.setContactId(contactId);
        returnVal.setDatasetId(datasetId);
        returnVal.setDataFileDirectory("E:/Gobii/dummyPath");
        returnVal.setDataFileName(getFolderNameWithTimestamp("qcDataFile"));
        returnVal.setQualityFileName("qualityFileName");

        return returnVal;

    }
}
