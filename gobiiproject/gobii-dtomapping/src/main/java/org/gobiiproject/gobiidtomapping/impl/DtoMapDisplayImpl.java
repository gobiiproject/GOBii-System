package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsDisplayDao;
import org.gobiiproject.gobiidao.resultset.core.ParamExtractor;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidtomapping.DtoMapDisplay;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.container.DisplayDTO;
import org.gobiiproject.gobiimodel.entity.TableColDisplay;
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
    public DisplayDTO getDisplayDetails(DisplayDTO displayDTO) throws GobiiDtoMappingException {

        DisplayDTO returnVal = displayDTO;

        try {

            ResultSet resultSet = rsDisplayDao.getTableDisplayDetailByDisplayId(returnVal.getDisplayId());

            if (resultSet.next()) {
                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);
            }

            if (displayDTO.isIncludeDetailsList()) {

                ResultSet tableColResultSet = rsDisplayDao.getTableDisplayNames();
                String currentTableName = "";
                while (tableColResultSet.next()) {

                    String newTableName = tableColResultSet.getString("table_name");

                    if (!currentTableName.equals(newTableName)) {
                        currentTableName = newTableName; //set table name if first table name frm query
                        returnVal.getTableNamesWithColDisplay().put(currentTableName, new ArrayList<>());
                    }

                    TableColDisplay  currentTableColDisplay = new TableColDisplay();
                    currentTableColDisplay.setDisplayId(tableColResultSet.getInt("display_id"));
                    currentTableColDisplay.setColumnName(tableColResultSet.getString("column_name"));
                    currentTableColDisplay.setDisplayName(tableColResultSet.getString("display_name"));
                    currentTableColDisplay.setRank(tableColResultSet.getInt("rank"));
                    returnVal.getTableNamesWithColDisplay().get(currentTableName).add(currentTableColDisplay);

                }
            }


        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }
        return returnVal;
    }


    @Override
    public DisplayDTO createDisplay(DisplayDTO displayDTO) throws GobiiDtoMappingException {
        DisplayDTO returnVal = displayDTO;

        try {

            Map<String, Object> parameters = ParamExtractor.makeParamVals(displayDTO);
            Integer contactId = rsDisplayDao.createDisplay(parameters);
            returnVal.setDisplayId(contactId);

        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }


        return returnVal;
    }

    @Override
    public DisplayDTO updateDisplay(DisplayDTO displayDTO) throws GobiiDtoMappingException {

        DisplayDTO returnVal = displayDTO;

        try {

            Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
            rsDisplayDao.updateDisplay(parameters);

        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }

        return returnVal;
    }

} // DtoMapNameIdListImpl
