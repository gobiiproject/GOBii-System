package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsPlatformDao;
import org.gobiiproject.gobiidao.resultset.access.RsProjectDao;
import org.gobiiproject.gobiidao.resultset.core.ParamExtractor;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidao.resultset.core.listquery.DtoListQueryColl;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiidtomapping.DtoMapProject;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.core.EntityProperties;
import org.gobiiproject.gobiimodel.headerlesscontainer.PlatformDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.ProjectDTO;
import org.gobiiproject.gobiimodel.dto.container.EntityPropertyDTO;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 4/6/2016.
 */
public class DtoMapProjectImpl implements DtoMapProject {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapNameIdListImpl.class);

    @Autowired
    private RsProjectDao rsProjectDao;

    @Autowired
    private DtoListQueryColl dtoListQueryColl;


    @SuppressWarnings("unchecked")
    @Override
    public List<ProjectDTO> getProjects() throws GobiiDtoMappingException {

        List<ProjectDTO> returnVal = new ArrayList<>();

        try {

            returnVal = (List<ProjectDTO>) dtoListQueryColl.getList(ListSqlId.QUERY_ID_PROJECT_ALL,null);


        } catch (Exception e) {
            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }

    public ProjectDTO getProjectDetails(Integer projectId) throws GobiiDtoMappingException {


        ProjectDTO returnVal = new ProjectDTO();

        try {

            ResultSet resultSet = rsProjectDao.getProjectDetailsForProjectId(projectId);

            boolean retrievedOneRecord = false;
            while (resultSet.next()) {

                if (true == retrievedOneRecord) {
                    throw (new GobiiDtoMappingException(GobiiStatusLevel.VALIDATION,
                            GobiiValidationStatusType.VALIDATION_NOT_UNIQUE,
                            "There are more than one project records for project id: " + projectId));
                }


                retrievedOneRecord = true;

                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);

            }


            addPropertiesToProject(returnVal);

        } catch (Exception e) {
            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiDtoMappingException(e);
        }


        return returnVal;
    }

    private void addPropertiesToProject(ProjectDTO projectDTO) throws GobiiDaoException {

        try {
            ResultSet propertyResultSet = rsProjectDao.getPropertiesForProject(projectDTO.getProjectId());
            List<EntityPropertyDTO> projectProperties =
                    EntityProperties.resultSetToProperties(projectDTO.getProjectId(), propertyResultSet);

            projectDTO.setProperties(projectProperties);
        } catch( SQLException e) {
            throw new GobiiDtoMappingException(e);
        }

    }

    private void validateProjectRequest(ProjectDTO projectDTO) throws GobiiDtoMappingException {


        String projectName = projectDTO.getProjectName();
        Integer piContactId = projectDTO.getPiContact();
        ResultSet resultSetExistingProject =
                rsProjectDao.getProjectsByNameAndPiContact(projectName, piContactId);

        try {

            if (resultSetExistingProject.next()) {
                throw new GobiiDtoMappingException(GobiiStatusLevel.VALIDATION,
                        GobiiValidationStatusType.VALIDATION_COMPOUND_UNIQUE,
                        "A project with name " + projectName + " and contact id " + piContactId + "already exists");
            }
        } catch (SQLException e) {
            throw new GobiiDtoMappingException(e);
        }

    }

    @Override
    public ProjectDTO createProject(ProjectDTO projectDTO) throws GobiiDtoMappingException {

        ProjectDTO returnVal = projectDTO;


        // validate that the dto does not specify a project that already exists
        if (null != returnVal.getProjectId() && returnVal.getProjectId() > 0) {
            try {
                ResultSet resultSet = rsProjectDao.getProjectDetailsForProjectId(returnVal.getProjectId());
                if (resultSet.next()) {
                    throw (new GobiiDtoMappingException(GobiiStatusLevel.VALIDATION,
                            GobiiValidationStatusType.ENTITY_ALREADY_EXISTS,
                            "A record already exists for this project id: " + returnVal.getProjectId()));
                }
            } catch (SQLException e) {
                throw new GobiiDtoMappingException(e);
            }
        }

        validateProjectRequest(returnVal);

        Map<String, Object> parameters = ParamExtractor.makeParamVals(projectDTO);

        Integer projectId = rsProjectDao.createProject(parameters);
        returnVal.setProjectId(projectId);

        List<EntityPropertyDTO> projectProperties = projectDTO.getProperties();

        upsertProjectProperties(projectId, projectProperties);

        return returnVal;
    }

    private void upsertProjectProperties(Integer projectId, List<EntityPropertyDTO> projectProperties) throws GobiiDaoException {

        for (EntityPropertyDTO currentProperty : projectProperties) {

            Map<String, Object> spParamsParameters =
                    EntityProperties.propertiesToParams(projectId, currentProperty);

            Integer propertyId = rsProjectDao.createUpdateProjectProperty(spParamsParameters);
            currentProperty.setEntityIdId(projectId);
            currentProperty.setPropertyId(propertyId);
        }

    }

    @Override
    public ProjectDTO replaceProject(Integer projectId, ProjectDTO projectDTO) throws GobiiDtoMappingException {

        ProjectDTO returnVal = projectDTO;

        try {

            Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
            parameters.put("projectId", projectId);
            rsProjectDao.updateProject(parameters);

            List<EntityPropertyDTO> projectProperties = projectDTO.getProperties();

            upsertProjectProperties(projectId, projectProperties);


        } catch (Exception e) {
            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;

    }
}
