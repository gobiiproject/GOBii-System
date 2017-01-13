package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.entity.access.MarkerEntityDao;
import org.gobiiproject.gobiidao.resultset.access.RsMarkerDao;
import org.gobiiproject.gobiidao.resultset.core.ParamExtractor;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidao.resultset.core.listquery.DtoListQueryColl;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiidtomapping.DtoMapMarker;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.container.MarkerGroupDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.MarkerDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.MarkerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 3/29/2016.
 */
public class DtoMapMarkerImpl implements DtoMapMarker {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapMarkerImpl.class);

    @Autowired
    RsMarkerDao rsMarkerDao = null;

    @Autowired
    private DtoListQueryColl dtoListQueryColl;

    @SuppressWarnings("unchecked")
    @Override
    public List<MarkerDTO> getMarkers() throws GobiiDtoMappingException {

        List<MarkerDTO> returnVal;

        try {

            returnVal = (List<MarkerDTO>) dtoListQueryColl.getList(ListSqlId.QUERY_ID_MARKER_ALL,null);

        } catch (Exception e) {
            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }

    @Transactional
    @Override
    public MarkerDTO getMarkerDetails(Integer markerId) throws GobiiDtoMappingException {

        MarkerDTO returnVal = new MarkerDTO();


        try {
            ResultSet resultSet = rsMarkerDao.getMarkerDetailsByMarkerId(markerId);

            if (resultSet.next()) {

                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);
            }
        } catch(SQLException e) {
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;

    }

    @Override
    public MarkerDTO createMarker(MarkerDTO markerDTO) throws GobiiDtoMappingException {

        MarkerDTO returnVal = markerDTO;


        Map<String, Object> parameters = ParamExtractor.makeParamVals(markerDTO);
        Integer markerId = rsMarkerDao.createMarker(parameters);
        returnVal.setMarkerId(markerId);

        return returnVal;
    }

    @Override
    public MarkerDTO replaceMarker(Integer markerId, MarkerDTO markerDTO) throws GobiiDtoMappingException {

        MarkerDTO returnVal = markerDTO;


        Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
        parameters.put("markerId", markerId);
        rsMarkerDao.updateMarker(parameters);


        return returnVal;
    }


    @Override
    public List<MarkerDTO> getMarkersByName(String markerName) throws GobiiDtoMappingException {

        List<MarkerDTO> returnVal = new ArrayList<>();


        try {
            ResultSet resultSet = rsMarkerDao.getMarkersByMarkerName(markerName);

            while (resultSet.next()) {

                MarkerDTO currentMarkerDTO = new MarkerDTO();
                ResultColumnApplicator.applyColumnValues(resultSet, currentMarkerDTO);
                returnVal.add(currentMarkerDTO);
            }

        } catch(SQLException e) {
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }

} // DtoMapMarkerImpl
