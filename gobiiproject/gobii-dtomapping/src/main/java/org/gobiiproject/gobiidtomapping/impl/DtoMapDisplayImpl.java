package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.resultset.access.RsDisplayDao;
import org.gobiiproject.gobiidao.resultset.core.ParamExtractor;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidtomapping.DtoMapDisplay;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.headerlesscontainer.DisplayDTO;
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
public class DtoMapDisplayImpl implements DtoMapDisplay {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapDisplayImpl.class);
    @Autowired
    private RsDisplayDao rsDisplayDao = null;

    @Override
    public List<DisplayDTO> getDisplays() throws GobiiDtoMappingException {

        List<DisplayDTO> returnVal = new ArrayList<>();

        try {
            ResultSet resultSet = rsDisplayDao.getTableDisplayNames();
            while (resultSet.next()) {
                DisplayDTO currentDisplayDTO = new DisplayDTO();
                currentDisplayDTO.setTableName(resultSet.getString("table_name"));
                currentDisplayDTO.setDisplayName(resultSet.getString("display_name"));
                currentDisplayDTO.setDisplayId(resultSet.getInt("display_id"));
                returnVal.add(currentDisplayDTO);
            }

        } catch (SQLException e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;

    }

    @Override
    public DisplayDTO getDisplayDetails(Integer displayId) throws GobiiDtoMappingException {

        DisplayDTO returnVal = new DisplayDTO();

        try {

            ResultSet resultSet = rsDisplayDao.getTableDisplayDetailByDisplayId(displayId);

            if (resultSet.next()) {
                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);
            }

        } catch (SQLException e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
        }
        return returnVal;
    }


    @Override
    public DisplayDTO createDisplay(DisplayDTO displayDTO) throws GobiiDtoMappingException {
        DisplayDTO returnVal = displayDTO;

        try {

            Map<String, Object> parameters = ParamExtractor.makeParamVals(displayDTO);
            Integer displayId = rsDisplayDao.createDisplay(parameters);
            returnVal.setDisplayId(displayId);

        } catch (Exception e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
        }


        return returnVal;
    }

    @Override
    public DisplayDTO replaceDisplay(Integer displayId, DisplayDTO displayDTO) throws GobiiDtoMappingException {

        DisplayDTO returnVal = displayDTO;

        try {

            Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
            parameters.put("displayId", displayId);
            rsDisplayDao.updateDisplay(parameters);

        } catch (Exception e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }


} // DtoMapDisplayImpl
