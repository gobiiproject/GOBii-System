package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.resultset.access.RsCvGroupDao;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidtomapping.DtoMapCvGroup;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.headerlesscontainer.CvDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.CvGroupDTO;
import org.gobiiproject.gobiimodel.types.GobiiCvGroupType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by VCalaminos on 2/1/2017.
 */
public class DtoMapCvGroupImpl implements DtoMapCvGroup {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapCvGroupImpl.class);

    @Autowired
    private RsCvGroupDao rsCvGroupDao;

    @Transactional
    @Override
    public List<CvDTO> getCvsForGroup(Integer groupId) throws GobiiDtoMappingException {

        List<CvDTO> returnVal = new ArrayList<>();

        try {

            ResultSet resultSet = rsCvGroupDao.getCvsByGroupId(groupId);

            while (resultSet.next()) {

                CvDTO currentCvDTO = new CvDTO();

                ResultColumnApplicator.applyColumnValues(resultSet, currentCvDTO);
                returnVal.add(currentCvDTO);

            }


        } catch (SQLException e) {

            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;

    }

    public List<CvGroupDTO> getCvGroupsForType(GobiiCvGroupType gobiiCvGroupType) throws GobiiDtoMappingException {

        List<CvGroupDTO> returnVal = new ArrayList<>();

        try {

            ResultSet resultSet = rsCvGroupDao.getCvGroupsForType(gobiiCvGroupType.getGroupTypeId());

            while (resultSet.next()) {

                CvGroupDTO currentCvGroupDTO = new CvGroupDTO();

                ResultColumnApplicator.applyColumnValues(resultSet, currentCvGroupDTO);
                returnVal.add(currentCvGroupDTO);

            }


        } catch (SQLException e) {

            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;

    }

    public Integer getGroupTypeForGroupId(Integer groupId) throws GobiiDtoMappingException {

        Integer returnVal = null;

        try {

            ResultSet resultSet = rsCvGroupDao.getGroupTypeForGroupId(groupId);

            if (resultSet.next()) {

                returnVal = resultSet.getInt("group_type");

            }


        } catch (SQLException e) {

            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);

        }

        return returnVal;

    }


    public CvGroupDTO getUserCvByGroupName(String groupName) throws GobiiDtoMappingException {

        CvGroupDTO returnVal = new CvGroupDTO();

        try {

            ResultSet resultSet = rsCvGroupDao.getUserCvGroupByName(groupName);

            if (resultSet.next()) {

                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);
            }


        } catch (SQLException e) {

            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;

    }

    public CvGroupDTO getCvGroup(Integer cvGroupId) throws GobiiDtoMappingException {
        CvGroupDTO returnVal = new CvGroupDTO();

        try {

            ResultSet resultSet = rsCvGroupDao.getCvGroupById(cvGroupId);

            if (resultSet.next()) {

                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);
            }


        } catch (SQLException e) {

            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;

    }
}
