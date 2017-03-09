package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.resultset.access.RsCvDao;
import org.gobiiproject.gobiidao.resultset.core.ParamExtractor;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidtomapping.DtoMapCv;
import org.gobiiproject.gobiidtomapping.DtoMapCvGroup;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.headerlesscontainer.CvDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 4/6/2016.
 */
public class DtoMapCvImpl implements DtoMapCv {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapCvImpl.class);


    @Autowired
    private RsCvDao rsCvDao = null;

    @Autowired
    DtoMapCvGroup dtoMapCvGroup;

    public final Integer GROUP_TYPE_SYSTEM = 2;

    @Override
    public List<CvDTO> getCvs() throws GobiiDtoMappingException {

        List<CvDTO> returnVal = new ArrayList<>();

        try {
            ResultSet resultSet = rsCvDao.getCvNames();
            while (resultSet.next()) {
                CvDTO currentCvDTO = new CvDTO();
                currentCvDTO.setTerm(resultSet.getString("term"));
                currentCvDTO.setCvId(resultSet.getInt("cv_id"));
                currentCvDTO.setGroupType(resultSet.getInt("group_type"));
                returnVal.add(currentCvDTO);
            }
        } catch (SQLException e) {
            LOGGER.error("Gobii Mapping error" ,e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }

    @Override
    public CvDTO getCvDetails(Integer cvId) throws GobiiDtoMappingException {

        CvDTO returnVal = new CvDTO();

        try {

            ResultSet resultSet = rsCvDao.getDetailsForCvId(cvId);

            if (resultSet.next()) {
                // apply cv values
                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);

            }

        } catch (SQLException e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
        }


        return returnVal;
    }


    @Override
    public CvDTO createCv(CvDTO cvDTO) throws GobiiDtoMappingException {
        CvDTO returnVal = cvDTO;

        Integer groupType = dtoMapCvGroup.getGroupTypeForGroupId(cvDTO.getGroupId());

        if(groupType.equals(GROUP_TYPE_SYSTEM)) {


            Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
            Integer cvId = rsCvDao.createCv(parameters);
            returnVal.setCvId(cvId);

        } else {

            LOGGER.error("Cannot create cv term that belongs to a system group");
            throw new GobiiDtoMappingException("Cannot create cv term that belongs to a cvgroup of type system");

        }

        return returnVal;
    }

    @Override
    public CvDTO replaceCv(Integer cvId, CvDTO cvDTO) throws GobiiDtoMappingException {

        CvDTO returnVal = cvDTO;

        CvDTO currentCvDTO = getCvDetails(cvId);

        if(currentCvDTO.getGroupType().equals(GROUP_TYPE_SYSTEM)) {

            Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
            parameters.put("cvId", cvId);
            rsCvDao.updateCv(parameters);

        } else {

            LOGGER.error("Cannot update cv term that belongs to a system group");
            throw new GobiiDtoMappingException("The specified cvId ("
                    + cvId
                    + ") belongs to a cvgroup of type system");

        }

        return returnVal;
    }

    @Override
    public CvDTO deleteCv(CvDTO cvDTO) throws GobiiDtoMappingException {

        CvDTO returnVal = cvDTO;

        if(cvDTO.getGroupType().equals(GROUP_TYPE_SYSTEM)){

            returnVal.setEntityStatus(0);
            Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
            rsCvDao.deleteCv(parameters);

            returnVal.setCvId(-1);
            returnVal.setGroupId(null);
            returnVal.setXrefId(null);
            returnVal.setTerm(null);
            returnVal.setAbbreviation(null);
            returnVal.setDefinition(null);
            returnVal.setRank(null);

        } else{

            LOGGER.error("Cannot delete cv term that belongs to a system group");
            throw new GobiiDtoMappingException("The specified cvId ("
                    + cvDTO.getCvId()
                    + ") belongs to a cvgroup of type system");

        }

        return returnVal;
    }

    @Override
    public List<CvDTO> getCvsByGroupName(String groupName) throws GobiiDtoMappingException {

        List<CvDTO> returnVal = new ArrayList<>();

        try {

            ResultSet resultSet = rsCvDao.getCvsByGroup(groupName);

            while (resultSet.next()) {

                CvDTO currentCvDTO = new CvDTO();

                ResultColumnApplicator.applyColumnValues(resultSet, currentCvDTO);
                returnVal.add(currentCvDTO);

            }


        } catch (SQLException e) {

            LOGGER.error("Gobii Mapping Error", e);
            throw  new GobiiDtoMappingException(e);
        }

        return returnVal;

    }

} // DtoMapNameIdListImpl
