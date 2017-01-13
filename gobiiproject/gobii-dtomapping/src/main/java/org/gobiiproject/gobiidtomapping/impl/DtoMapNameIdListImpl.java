package org.gobiiproject.gobiidtomapping.impl;

import org.apache.commons.lang.math.NumberUtils;
import org.gobiiproject.gobiidao.resultset.access.*;
import org.gobiiproject.gobiidtomapping.DtoMapNameIdList;
import org.gobiiproject.gobiimodel.dto.container.NameIdListDTO;
import org.gobiiproject.gobiimodel.dto.header.DtoHeaderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Phil on 4/6/2016.
 */
public class DtoMapNameIdListImpl implements DtoMapNameIdList {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapNameIdListImpl.class);


    @Autowired
    private RsAnalysisDao rsAnalysisDao = null;

    @Autowired
    private RsContactDao rsContactDao = null;

    @Autowired
    private RsProjectDao rsProjectDao = null;

    @Autowired
    private RsPlatformDao rsPlatformDao = null;

    @Autowired
    private RsReferenceDao rsReferenceDao = null;

    @Autowired
    private RsMapSetDao rsMapSetDao = null;

    @Autowired
    private RsMarkerGroupDao rsMarkerGroupDao = null;

    @Autowired
    private RsCvDao rsCvDao = null;

    @Autowired
    private RsExperimentDao rsExperimentDao = null;

    @Autowired
    private RsManifestDao rsManifestDao = null;

    @Autowired
    private RsDataSetDao rsDataSetDao = null;

    @Autowired
    private RsRoleDao rsRoleDao = null;

    private NameIdListDTO getNameIdListForAnalysis(NameIdListDTO nameIdListDTO) {

        NameIdListDTO returnVal = new NameIdListDTO();

        try {

            ResultSet resultSet = rsAnalysisDao.getAnalysisNames();
            Map<String, String> analysisNamesById = new HashMap<>();
            while (resultSet.next()) {

                Integer analysisId = resultSet.getInt("analysis_id");
                String name = resultSet.getString("name");
                analysisNamesById.put(analysisId.toString(), name);
            }


            returnVal.setNamesById(analysisNamesById);


        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }

        return returnVal;
    }

    private NameIdListDTO getNameIdListForAnalysisNameByTypeId(NameIdListDTO nameIdListDTO) {

        NameIdListDTO returnVal = new NameIdListDTO();

        try {

            ResultSet resultSet = rsAnalysisDao.getAnalysisNamesByTypeId(Integer.parseInt(nameIdListDTO.getFilter()));
            Map<String, String> analysisNamesById = new HashMap<>();
            while (resultSet.next()) {

                Integer analysisId = resultSet.getInt("analysis_id");
                String name = resultSet.getString("name");
                analysisNamesById.put(analysisId.toString(), name);
            }


            returnVal.setNamesById(analysisNamesById);


        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }

        return returnVal;
    }


    private NameIdListDTO getNameIdListForContacts(NameIdListDTO nameIdListDTO) {
        NameIdListDTO returnVal = nameIdListDTO;

        try {

            ResultSet contactList = rsContactDao.getContactNamesForRoleName(nameIdListDTO.getFilter());

            Map<String, String> contactNamesById = new HashMap<>();
            while (contactList.next()) {

                Integer contactId = contactList.getInt("contact_id");
                String lastName = contactList.getString("lastname");
                String firstName = contactList.getString("firstname");
                String name = lastName + ", " + firstName;
                contactNamesById.put(contactId.toString(), name);
            }

            returnVal.setNamesById(contactNamesById);

        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }


        return (returnVal);

    } // getNameIdListForContacts()

    private NameIdListDTO getNameIdListForAllContacts(NameIdListDTO nameIdListDTO) {
        NameIdListDTO returnVal = new NameIdListDTO();

        try {

            ResultSet contactList = rsContactDao.getAllContactNames();

            Map<String, String> contactNamesById = new HashMap<>();
            while (contactList.next()) {

                Integer contactId = contactList.getInt("contact_id");
                String lastName = contactList.getString("lastname");
                String firstName = contactList.getString("firstname");
                String name = lastName + ", " + firstName;
                contactNamesById.put(contactId.toString(), name);
            }

            returnVal.setNamesById(contactNamesById);

        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }


        return (returnVal);

    } // getNameIdListForContacts()

    private NameIdListDTO getNameIdListForMarkerGroups(NameIdListDTO nameIdListDTO) {

        NameIdListDTO returnVal = new NameIdListDTO();

        try {

            ResultSet resultSet = rsMarkerGroupDao.getMarkerGroupNames();
            Map<String, String> markerGroupNamesById = new HashMap<>();
            while (resultSet.next()) {

                Integer markerGroupId = resultSet.getInt("marker_group_id");
                String markerGroupName = resultSet.getString("name");
                markerGroupNamesById.put(markerGroupId.toString(), markerGroupName);
            }


            returnVal.setNamesById(markerGroupNamesById);


        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }

        return returnVal;

    }//getNameIdListForMarkerGroups

    private NameIdListDTO getNameIdListForExperiment(NameIdListDTO nameIdListDTO) {

        NameIdListDTO returnVal = new NameIdListDTO();

        try {

            ResultSet resultSet = rsExperimentDao.getExperimentNames();
            Map<String, String> experimentNamesById = new HashMap<>();
            while (resultSet.next()) {

                Integer experimentId = resultSet.getInt("experiment_id");
                String experimentName = resultSet.getString("name");
                experimentNamesById.put(experimentId.toString(), experimentName);
            }


            returnVal.setNamesById(experimentNamesById);


        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }

        return returnVal;
    }

    private NameIdListDTO getNameIdListForDataSet(NameIdListDTO nameIdListDTO) {

        NameIdListDTO returnVal = nameIdListDTO;

        try {

            ResultSet resultSet = rsDataSetDao.getDatasetNames();

            Map<String, String> datasetNamesById = makeMapOfDataSetNames(resultSet);

            returnVal.setNamesById(datasetNamesById);


        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }

        return returnVal;
    }

    private NameIdListDTO getNameIdListForPlatforms(NameIdListDTO nameIdListDTO) {

        NameIdListDTO returnVal = new NameIdListDTO();

        try {

            ResultSet resultSet = rsPlatformDao.getPlatformNames();
            Map<String, String> platformNamesById = new HashMap<>();
            while (resultSet.next()) {

                Integer platformId = resultSet.getInt("platform_id");
                String platformName = resultSet.getString("name");
                platformNamesById.put(platformId.toString(), platformName);
            }


            returnVal.setNamesById(platformNamesById);


        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }

        return returnVal;
    }

    private NameIdListDTO getNameIdListForPlatformsByTypeId(NameIdListDTO nameIdListDTO) {

        NameIdListDTO returnVal = new NameIdListDTO();

        try {

            ResultSet resultSet = rsPlatformDao.getPlatformNamesByTypeId(Integer.parseInt(nameIdListDTO.getFilter()));
            Map<String, String> platformNamesById = new HashMap<>();
            while (resultSet.next()) {

                Integer platformId = resultSet.getInt("platform_id");
                String platformName = resultSet.getString("name");
                platformNamesById.put(platformId.toString(), platformName);
            }


            returnVal.setNamesById(platformNamesById);


        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }

        return returnVal;
    }

    private NameIdListDTO getNameIdListForProjects(NameIdListDTO nameIdListDTO) {

        NameIdListDTO returnVal = new NameIdListDTO();

        try {

            ResultSet resultSet = rsProjectDao.getProjectNames();
            Map<String, String> projectNamesById = new HashMap<>();
            while (resultSet.next()) {

                Integer projectId = resultSet.getInt("project_id");
                String projectName = resultSet.getString("name");
                projectNamesById.put(projectId.toString(), projectName);
            }


            returnVal.setNamesById(projectNamesById);


        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }

        return returnVal;
    }

    private NameIdListDTO getNameIdListForReference(NameIdListDTO nameIdListDTO) {

        NameIdListDTO returnVal = new NameIdListDTO();

        try {

            ResultSet resultSet = rsReferenceDao.getReferenceNames();
            Map<String, String> referenceNamesById = new HashMap<>();
            while (resultSet.next()) {

                Integer referenceId = resultSet.getInt("reference_id");
                String referenceName = resultSet.getString("name");
                referenceNamesById.put(referenceId.toString(), referenceName);
            }


            returnVal.setNamesById(referenceNamesById);


        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }

        return returnVal;
    }

    private NameIdListDTO getNameIdListForRole(NameIdListDTO nameIdListDTO) {

        NameIdListDTO returnVal = new NameIdListDTO();

        try {

            ResultSet resultSet = rsRoleDao.getContactRoleNames();
            Map<String, String> roleNamesById = new HashMap<>();
            while (resultSet.next()) {

                Integer roleId = resultSet.getInt("role_id");
                String roleName = resultSet.getString("role_name");
                roleNamesById.put(roleId.toString(), roleName);
            }


            returnVal.setNamesById(roleNamesById);


        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }

        return returnVal;
    }

    private NameIdListDTO getNameIdListForMapByTypeId(NameIdListDTO nameIdListDTO) {

        NameIdListDTO returnVal = new NameIdListDTO();

        try {

            ResultSet resultSet = rsMapSetDao.getMapNamesByTypeId(Integer.parseInt(nameIdListDTO.getFilter()));
            Map<String, String> mapNamesById = new HashMap<>();
            while (resultSet.next()) {

                Integer mapId = resultSet.getInt("mapset_id");
                String mapName = resultSet.getString("name");
                mapNamesById.put(mapId.toString(), mapName);
            }

            returnVal.setNamesById(mapNamesById);


        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }

        return returnVal;
    }

    private NameIdListDTO getNameIdListForMap(NameIdListDTO nameIdListDTO) {

        NameIdListDTO returnVal = new NameIdListDTO();

        try {

            ResultSet resultSet = rsMapSetDao.getMapNames();
            Map<String, String> mapNamesById = new HashMap<>();
            while (resultSet.next()) {

                Integer mapId = resultSet.getInt("mapset_id");
                String mapName = resultSet.getString("name");
                mapNamesById.put(mapId.toString(), mapName);
            }

            returnVal.setNamesById(mapNamesById);


        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }

        return returnVal;
    }

    private NameIdListDTO getNameIdListForManifest(NameIdListDTO nameIdListDTO) {

        NameIdListDTO returnVal = new NameIdListDTO();

        try {

            ResultSet resultSet = rsManifestDao.getManifestNames();
            Map<String, String> manifestNamesById = new HashMap<>();
            while (resultSet.next()) {

                Integer manifestId = resultSet.getInt("manifest_id");
                String manifestName = resultSet.getString("name");
                manifestNamesById.put(manifestId.toString(), manifestName);
            }


            returnVal.setNamesById(manifestNamesById);


        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }

        return returnVal;
    }//getNameIdListForManifest

    private NameIdListDTO getNameIdListForProjectNameByContact(NameIdListDTO nameIdListDTO) {

        NameIdListDTO returnVal = nameIdListDTO;

        try {

            String filter = nameIdListDTO.getFilter();
            if (NumberUtils.isNumber(filter)) {
                ResultSet resultSet = rsProjectDao.getProjectNamesForContactId(Integer.parseInt(filter));

                Map<String, String> projectNameIdList = new HashMap<>();

                while (resultSet.next()) {
                    Integer projectId = resultSet.getInt("project_id");
                    String name = resultSet.getString("name").toString();
                    projectNameIdList.put(projectId.toString(), name);
                }

                returnVal.setNamesById(projectNameIdList);
            } else {
                nameIdListDTO.getDtoHeaderResponse()
                        .addStatusMessage(DtoHeaderResponse.StatusLevel.ERROR,
                        "Filter value is not numeric: " + filter);
            }
        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }

        return returnVal;

    } // getNameIdListForContacts()

    private NameIdListDTO getNameIdListForExperimentByProjectId(NameIdListDTO nameIdListDTO) {

        NameIdListDTO returnVal = new NameIdListDTO();

        try {

            ResultSet resultSet = rsExperimentDao.getExperimentNamesByProjectId(Integer.parseInt(nameIdListDTO.getFilter()));

            Map<String, String> experimentNameIdList = new HashMap<>();

            while (resultSet.next()) {
                Integer experimentId = resultSet.getInt("experiment_id");
                String name = resultSet.getString("name").toString();
                experimentNameIdList.put(experimentId.toString(), name);
            }

            returnVal.setNamesById(experimentNameIdList);
        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }

        return returnVal;

    } // getNameIdListForContacts()

    private NameIdListDTO getNameIdListForCvGroups(NameIdListDTO nameIdListDTO) {

        NameIdListDTO returnVal = new NameIdListDTO();

        try {

            ResultSet resultSet = rsCvDao.getCvGroups();

            Map<String, String> cvGroupTermList = new HashMap<>();

            while (resultSet.next()) {
                Integer cvId = resultSet.getInt("cv_id");
                String name = resultSet.getString("lower").toString();
                cvGroupTermList.put(cvId.toString(), name);
            }

            returnVal.setNamesById(cvGroupTermList);

        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }

        return returnVal;

    } // getNameIdListForCvTypes


    private NameIdListDTO getNameIdListForCv(NameIdListDTO nameIdListDTO) {

        NameIdListDTO returnVal = new NameIdListDTO();

        try {

            ResultSet resultSet = rsCvDao.getCvNames();

            Map<String, String> cvGroupTermList = new HashMap<>();

            while (resultSet.next()) {
                Integer cvId = resultSet.getInt("cv_id");
                String name = resultSet.getString("term").toString();
                cvGroupTermList.put(cvId.toString(), name);
            }

            returnVal.setNamesById(cvGroupTermList);
        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }

        return returnVal;

    }

    private NameIdListDTO getNameIdListForCvGroupTerms(NameIdListDTO nameIdListDTO) {

        NameIdListDTO returnVal = new NameIdListDTO();

        try {

            ResultSet resultSet = rsCvDao.getCvTermsByGroup(nameIdListDTO.getFilter());

            Map<String, String> cvGroupTermList = new HashMap<>();

            while (resultSet.next()) {
                Integer cvId = resultSet.getInt("cv_id");
                String name = resultSet.getString("term").toString();
                cvGroupTermList.put(cvId.toString(), name);
            }

            returnVal.setNamesById(cvGroupTermList);
        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }

        return returnVal;

    } // getNameIdListForCvGroupTerms()

    private Map<String, String> makeMapOfDataSetNames(ResultSet resultSet) throws Exception {

        Map<String, String> returnVal = new HashMap<>();

        while (resultSet.next()) {
            Integer dataSetId = resultSet.getInt("dataset_id");
            String dataSetName = resultSet.getString("name");
            if (resultSet.wasNull()) {
                dataSetName = "<no name>";
            }

            returnVal.put(dataSetId.toString(), dataSetName);
        }

        return returnVal;
    }

    private NameIdListDTO getNameIdListForDataSetByExperimentId(NameIdListDTO nameIdListDTO) {

        NameIdListDTO returnVal = new NameIdListDTO();

        try {

            ResultSet resultSet = rsDataSetDao.getDatasetNamesByExperimentId(Integer.parseInt(nameIdListDTO.getFilter()));
            Map<String, String> datasetNameIdList = makeMapOfDataSetNames(resultSet);

            returnVal.setNamesById(datasetNameIdList);

        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }

        return returnVal;

    } // getNameIdListForDataSet()

    @Override
    public NameIdListDTO getNameIdList(NameIdListDTO nameIdListDTO) {

        NameIdListDTO returnVal = nameIdListDTO;

        if (nameIdListDTO.getEntityType() == NameIdListDTO.EntityType.DBTABLE) {

            switch (nameIdListDTO.getEntityName().toLowerCase()) {

                case "analysis":
                    returnVal = getNameIdListForAnalysis(nameIdListDTO);
                    break;

                case "analysisnamebytypeid":
                    returnVal = getNameIdListForAnalysisNameByTypeId(nameIdListDTO);
                    break;

                case "contact":
                    returnVal = getNameIdListForContacts(nameIdListDTO);
                    break;
                case "allcontacts":
                    returnVal = getNameIdListForAllContacts(nameIdListDTO);
                    break;

                case "datasetnames":
                    returnVal = getNameIdListForDataSet(nameIdListDTO);
                    break;

                case "datasetnamesbyexperimentid":
                    returnVal = getNameIdListForDataSetByExperimentId(nameIdListDTO);
                    break;

                case "cvnames":
                    returnVal = getNameIdListForCv(nameIdListDTO);
                    break;

                case "project":
                    returnVal = getNameIdListForProjectNameByContact(nameIdListDTO);
                    break;

                case "projectnames":
                    returnVal = getNameIdListForProjects(nameIdListDTO);
                    break;

                case "platform":
                    returnVal = getNameIdListForPlatforms(nameIdListDTO);
                    break;

                case "platformbytypeid":
                    returnVal = getNameIdListForPlatformsByTypeId(nameIdListDTO);
                    break;

                case "manifest":
                    returnVal = getNameIdListForManifest(nameIdListDTO);
                    break;

                case "mapset":
                    returnVal = getNameIdListForMap(nameIdListDTO);
                    break;

                case "mapnamebytypeid":
                    returnVal = getNameIdListForMapByTypeId(nameIdListDTO);
                    break;

                case "markergroup":
                    returnVal = getNameIdListForMarkerGroups(nameIdListDTO);
                    break;

                case "experiment":
                    returnVal = getNameIdListForExperimentByProjectId(nameIdListDTO);
                    break;

                case "experimentnames":
                    returnVal = getNameIdListForExperiment(nameIdListDTO);
                    break;

                case "cvgroupterms":
                    returnVal = getNameIdListForCvGroupTerms(nameIdListDTO);
                    break;

                case "cvgroups":
                    returnVal = getNameIdListForCvGroups(nameIdListDTO);
                    break;

                case "reference":
                    returnVal = getNameIdListForReference(nameIdListDTO);
                    break;

                case "role":
                    returnVal = getNameIdListForRole(nameIdListDTO);
                    break;
                default:
                    returnVal.getDtoHeaderResponse().addStatusMessage(DtoHeaderResponse.StatusLevel.ERROR,
                            "Unsupported entity for list request: " + nameIdListDTO.getEntityName());
            }

        }

        return returnVal;

    } // getNameIdList()

} // DtoMapNameIdListImpl
