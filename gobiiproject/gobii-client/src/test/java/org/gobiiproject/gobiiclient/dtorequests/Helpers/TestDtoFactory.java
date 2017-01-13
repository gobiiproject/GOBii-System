package org.gobiiproject.gobiiclient.dtorequests.Helpers;

import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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

    public static EntityParamValues makeConstrainedEntityParams(List<String> propNames,
                                                                Integer nameStem) {

        EntityParamValues returnVal = new EntityParamValues();

        for (String currentPropName : propNames) {

            returnVal.add(currentPropName, "fooval " + (nameStem++));
        }

        return returnVal;
    }

    public static AnalysisDTO makePopulatedAnalysisDTO(DtoMetaData.ProcessType processType,
                                                       Integer uniqueStem,
                                                       EntityParamValues entityParamValues) {

        AnalysisDTO returnVal = new AnalysisDTO(processType);

        returnVal.setAnalysisName(uniqueStem + ": analysis");
        returnVal.setTimeExecuted(new Date());
        returnVal.setSourceUri(uniqueStem + ":  foo URL");
        returnVal.setAlgorithm(uniqueStem + ":  foo algorithm");
        returnVal.setSourceName(uniqueStem + ":  foo source");
        returnVal.setAnalysisDescription(uniqueStem + ":  my analysis description");
        returnVal.setProgram(uniqueStem + ":  foo program");
        returnVal.setProgramVersion(uniqueStem + ":  foo version");
        returnVal.setAnlaysisTypeId(1);
        returnVal.setStatus(1);

        returnVal.setParameters(entityParamValues.getProperties());

        return returnVal;

    }

    public static PlatformDTO makePopulatedPlatformDTO(DtoMetaData.ProcessType processType,
                                                       Integer uniqueStem,
                                                       EntityParamValues entityParamValues) {

        PlatformDTO returnVal = new PlatformDTO(processType);

        String uniqueStemString = uniqueStem.toString();
        // set the plain properties
        returnVal.setStatus(1);
        returnVal.setModifiedBy(1);
        returnVal.setModifiedDate(new Date());
        returnVal.setCreatedBy(1);
        returnVal.setCreatedDate(new Date());
        returnVal.setPlatformCode(uniqueStem +"dummy code");
        returnVal.setPlatformDescription(uniqueStem +"dummy description");
        returnVal.setPlatformName(uniqueStem +"New Platform");
        returnVal.setPlatformVendor(1);
        returnVal.setTypeId(1);

        returnVal.setProperties(entityParamValues.getProperties());
        return returnVal;

    }


public static CvDTO makePopulatedCvDTO(DtoMetaData.ProcessType processType,
                                       Integer uniqueStem) {

        CvDTO returnVal = new CvDTO(processType);
        returnVal.setGroup(uniqueStem+"dummy cv name");
        returnVal.setTerm(UUID.randomUUID().toString());
        returnVal.setDefinition(uniqueStem+"dummy definition");
        returnVal.setRank(1);

        return returnVal;

    }

    public static DisplayDTO makePopulatedDisplayDTO(DtoMetaData.ProcessType processType,
                                                     Integer uniqueStem) {

        DisplayDTO returnVal = new DisplayDTO(processType);
        returnVal.setColumnName(uniqueStem+"dummy column");
        returnVal.setCreatedBy(1);
        returnVal.setDisplayName(uniqueStem+"dummyDisplay");
        returnVal.setCreatedDate(new Date());
        returnVal.setDisplayId(1);
        returnVal.setModifiedBy(1);
        returnVal.setModifiedDate(new Date());
        returnVal.setTableName(uniqueStem+"dummy table");
        returnVal.setDisplayRank(uniqueStem);


        return returnVal;
}
    public static DataSetDTO makePopulatedDataSetDTO(DtoMetaData.ProcessType processType,
                                                     Integer uniqueStem,
                                                     Integer callingAnalysisId,
                                                     List<Integer> analysisIds) {

        DataSetDTO returnVal = new DataSetDTO(processType);


        // set the big-ticket items

        returnVal.getScores().add(1);
        returnVal.getScores().add(2);
        returnVal.getScores().add(3);

        // set the plain properties
        returnVal.setName(uniqueStem + ": foo name");
        returnVal.setStatus(1);
        returnVal.setCreatedBy(1);
        returnVal.setCreatedDate(new Date());
        returnVal.setDataFile(uniqueStem + ": foo file");
        returnVal.setQualityFile(uniqueStem + ": foo quality file");
        returnVal.setExperimentId(2);
        returnVal.setDataTable(uniqueStem + ": foo table");
        returnVal.setModifiedBy(1);
        returnVal.setModifiedDate(new Date());
        returnVal.setCallingAnalysisId(callingAnalysisId);
        returnVal.setAnalysesIds(analysisIds);
        returnVal.setTypeId(93);

        return returnVal;

    }


    public static MapsetDTO makePopulatedMapsetDTO(DtoMetaData.ProcessType processType,
                                                   Integer uniqueStem,
                                                   EntityParamValues entityParamValues) {

        MapsetDTO returnVal = new MapsetDTO(processType);

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
        returnVal.setReferenceId(1);
        returnVal.setStatus(1);

        returnVal.setProperties(entityParamValues.getProperties());

        return returnVal;

    }

    public static ReferenceDTO makePopulatedReferenceDTO(DtoMetaData.ProcessType processType,
                                                         Integer uniqueStem) {

        ReferenceDTO returnVal = new ReferenceDTO(processType);

        String uniqueStemString = uniqueStem.toString();
        returnVal.setName(uniqueStem + ": reference");
        returnVal.setVersion("version:" + uniqueStem);
        returnVal.setLink(uniqueStem + " link");
        returnVal.setFilePath(uniqueStem + " file path");

        return returnVal;

    }


    public static OrganizationDTO makePopulatedOrganizationDTO(DtoMetaData.ProcessType processType,
                                                       Integer uniqueStem) {

        OrganizationDTO returnVal = new OrganizationDTO(processType);

        String uniqueStemString = uniqueStem.toString();
        returnVal.setName(uniqueStem + ": reference");
        returnVal.setAddress("address:" + uniqueStem);
        returnVal.setWebsite(uniqueStem + ".com");
        returnVal.setCreatedBy(1);
        returnVal.setCreatedDate(new Date());
        returnVal.setModifiedBy(1);
        returnVal.setModifiedDate(new Date());

        return returnVal;

    }

    public static ManifestDTO makePopulatedManifestDTO(DtoMetaData.ProcessType processType,
                                                       Integer uniqueStem) {

        ManifestDTO returnVal = new ManifestDTO(processType);

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

    public static ContactDTO makePopulatedContactDTO(DtoMetaData.ProcessType processType,
                                                     Integer uniqueStem) {

        String uniqueStemString = uniqueStem.toString();
        ContactDTO returnVal = new ContactDTO(processType);
        // set the plain properties

        returnVal.setFirstName(uniqueStem + " new contact");
        returnVal.setLastName(uniqueStem + "new lastname");
        returnVal.setEmail(uniqueStem + "mail@email.com");
        returnVal.setCode(uniqueStem + "added New Code");
        returnVal.setCreatedBy(1);
        returnVal.setCreatedDate(new Date());
        returnVal.setModifiedBy(1);
        returnVal.setModifiedDate(new Date());

        returnVal.getRoles().add(1);
        returnVal.getRoles().add(2);

        return returnVal;

    }

    public static List<MarkerGroupMarkerDTO> makeMarkerGroupMarkers(List<String> markerNames,
                                                                    DtoMetaData.ProcessType processType) {

        List<MarkerGroupMarkerDTO> returnVal = new ArrayList<>();

        for(String currentMarkerName : markerNames ) {

            MarkerGroupMarkerDTO currentMarkerGroupMarker = new MarkerGroupMarkerDTO();
            currentMarkerGroupMarker.setProcessType(processType);
            currentMarkerGroupMarker.setMarkerName(currentMarkerName);
            currentMarkerGroupMarker.setFavorableAllele("G");
            returnVal.add(currentMarkerGroupMarker);

        }

        return  returnVal;

    }

    public static MarkerGroupDTO makePopulatedMarkerGroupDTO(DtoMetaData.ProcessType processType,
                                                             Integer uniqueStem,
                                                             List<MarkerGroupMarkerDTO> markerGroupMarkers) {

        MarkerGroupDTO returnVal = new MarkerGroupDTO(processType);

        returnVal.setMarkers(markerGroupMarkers);
        returnVal.setStatus(1);
        returnVal.setCode( uniqueStem + "_code");
        returnVal.setGermplasmGroup(uniqueStem + "_germplasmGroup");
        returnVal.setName(uniqueStem + "_name");

        return  returnVal;

    }

    public static PingDTO makePingDTO() throws Exception {

        PingDTO returnVal = new PingDTO();

        returnVal.getDbMetaData().add("test string 1");
        returnVal.getDbMetaData().add("test string 2");

        return returnVal;

    }

}
