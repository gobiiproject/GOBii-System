package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.entity.access.MarkerEntityDao;
import org.gobiiproject.gobiidtomapping.DtoMapMarker;
import org.gobiiproject.gobiimodel.dto.container.MarkerGroupDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 3/29/2016.
 */
public class DtoMapMarkerImpl implements DtoMapMarker {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapMarkerImpl.class);


    @Autowired
    private MarkerEntityDao markerEntityDao;

    @Override
    public MarkerGroupDTO getMarkers(List<Integer> markerIds) {

        MarkerGroupDTO returnVal = new MarkerGroupDTO();

        try {

            Map<String, List<String>> markerGroups = markerEntityDao.getMarkers(markerIds);

            returnVal.setMarkerMap(markerGroups);
        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }


        return returnVal;

    } // getMarkers()

} // DtoMapMarkerImpl
