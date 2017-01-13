package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsMarkerGroupDao;
import org.gobiiproject.gobiidao.resultset.core.EntityPropertyParamNames;
import org.gobiiproject.gobiidao.resultset.core.ParamExtractor;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidtomapping.DtoMapMarkerGroup;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.MarkerGroupDTO;
import org.gobiiproject.gobiimodel.dto.container.MarkerGroupMarkerDTO;
import org.gobiiproject.gobiimodel.dto.header.DtoHeaderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.UncheckedIOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Phil on 4/22/2016.
 */
public class DtoMapMarkerGroupImpl implements DtoMapMarkerGroup {


    Logger LOGGER = LoggerFactory.getLogger(DtoMapDataSetImpl.class);


    @Autowired
    private RsMarkerGroupDao rsMarkerGroupDao;


    public MarkerGroupDTO getMarkerGroupDetails(MarkerGroupDTO markerGroupDTO) throws GobiiDtoMappingException {

        MarkerGroupDTO returnVal = markerGroupDTO;

        try {

            ResultSet resultSet = rsMarkerGroupDao.getMarkerGroupDetailByMarkerGroupId(markerGroupDTO.getMarkerGroupId());

            if (resultSet.next()) {
                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);
            }

            ResultSet markersForMarkerGroupResultSet = rsMarkerGroupDao.getMarkersForMarkerGroup(returnVal.getMarkerGroupId());
            while (markersForMarkerGroupResultSet.next()) {

                Integer currentMarkerId = markersForMarkerGroupResultSet.getInt("marker_id");
                String favorableAllele = markersForMarkerGroupResultSet.getString("favorable_allele");
                ResultSet markerDetailsResultSet = rsMarkerGroupDao.getMarkerByMarkerId(currentMarkerId);
                MarkerGroupMarkerDTO currentMarkerGroupMarkerDTO = new MarkerGroupMarkerDTO();
                if (markerDetailsResultSet.next()) {
                    ResultColumnApplicator.applyColumnValues(markerDetailsResultSet, currentMarkerGroupMarkerDTO);
                    currentMarkerGroupMarkerDTO.setMarkerExists(true);
                    currentMarkerGroupMarkerDTO.setFavorableAllele(favorableAllele);
                } else {
                    currentMarkerGroupMarkerDTO.setMarkerId(currentMarkerId);
                    currentMarkerGroupMarkerDTO.setMarkerExists(false);
                    returnVal.getDtoHeaderResponse().addStatusMessage(DtoHeaderResponse.StatusLevel.OK,
                            DtoHeaderResponse.ValidationStatusType.NONEXISTENT_FK_ENTITY,
                            "A marker id in the marker_group table does not exist " + currentMarkerId);
                }

                returnVal.getMarkers().add(currentMarkerGroupMarkerDTO);
            }

        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }

        return returnVal;

    }


    private List<MarkerGroupMarkerDTO> getMarkerGroupMarkersByMarkerName(String markerName) throws SQLException, GobiiDaoException {

        List<MarkerGroupMarkerDTO> returnVal = new ArrayList<>();

        ResultSet markersResultSet = rsMarkerGroupDao.getMarkersByMarkerName(markerName);
        while (markersResultSet.next()) {
            MarkerGroupMarkerDTO currentMarkerGroupMarkerDto = new MarkerGroupMarkerDTO();
            ResultColumnApplicator.applyColumnValues(markersResultSet, currentMarkerGroupMarkerDto);
            returnVal.add(currentMarkerGroupMarkerDto);
        }

        return returnVal;
    }


    private void populateMarkers(List<MarkerGroupMarkerDTO> markerGroupMarkers) throws SQLException, GobiiDaoException {

        List<MarkerGroupMarkerDTO> newMarkerDTOsForMarker = new ArrayList<>();

        for (Iterator<MarkerGroupMarkerDTO> iterator =
             markerGroupMarkers
                     .iterator();
             iterator.hasNext(); ) {

            MarkerGroupMarkerDTO currentMarkerGroupMarkerDto = iterator.next();

            List<MarkerGroupMarkerDTO> markerDTOsForMarkerName =
                    getMarkerGroupMarkersByMarkerName(currentMarkerGroupMarkerDto.getMarkerName());

            if (markerDTOsForMarkerName.size() > 0) {

                markerDTOsForMarkerName
                        .stream()
                        .forEach(m -> {
                                    m.setFavorableAllele(currentMarkerGroupMarkerDto
                                            .getFavorableAllele());
                                    m.setMarkerExists(true);
                                }
                        );

                // the one from our list has not been populated with values from the query
                // only the ones in the list we got back have been populated
                // we'll add the new, populated one to the marker group DTO's list below
                iterator.remove();

                newMarkerDTOsForMarker.addAll(markerDTOsForMarkerName);

            } else if (0 == markerDTOsForMarkerName.size()) {
                currentMarkerGroupMarkerDto.setMarkerExists(false);
            }
        }


        markerGroupMarkers.addAll(newMarkerDTOsForMarker); // if new markers is empty, we don't care
    }


    private void upsertMarkers(Integer markerGroupId, List<MarkerGroupMarkerDTO> markerDTOs) throws SQLException, GobiiDaoException {

        for (MarkerGroupMarkerDTO currentMarkerGroupMarkerDTO : markerDTOs) {

            if (null == currentMarkerGroupMarkerDTO.getFavorableAllele() ||
                    currentMarkerGroupMarkerDTO.getFavorableAllele().isEmpty()) {
                throw new GobiiDtoMappingException(DtoHeaderResponse.StatusLevel.ERROR,
                        DtoHeaderResponse.ValidationStatusType.MISSING_REQUIRED_VALUE,
                        "The no allele value was specified for marker nane " + currentMarkerGroupMarkerDTO.getMarkerName());
            }

            Map<String, Object> markerGroupMarkerParameters = new HashMap<>();

            markerGroupMarkerParameters.put(EntityPropertyParamNames.PROPPCOLARAMNAME_ENTITY_ID, markerGroupId);
            markerGroupMarkerParameters.put(EntityPropertyParamNames.PROPPCOLARAMNAME_PROP_ID, currentMarkerGroupMarkerDTO.getMarkerId());
            markerGroupMarkerParameters.put(EntityPropertyParamNames.PROPPCOLARAMNAME_VALUE, currentMarkerGroupMarkerDTO.getFavorableAllele());

            rsMarkerGroupDao.createUpdateMarkerGroupMarker(markerGroupMarkerParameters);

        }


    } // upsertMarkers

    @Override
    @Transactional(propagation = Propagation.REQUIRED) // if we throw a runtime exception, we'll rollback
    public MarkerGroupDTO createMarkerGroup(MarkerGroupDTO markerGroupDTO) throws GobiiDtoMappingException {

        MarkerGroupDTO returnVal = markerGroupDTO;

        try {

            Map<String, Object> parameters = ParamExtractor.makeParamVals(markerGroupDTO);
            Integer markerGroupId = rsMarkerGroupDao.createMarkerGroup(parameters);
            returnVal.setMarkerGroupId(markerGroupId);

            // populate marker DTO's in a way that deals with case of
            // multiple markers with that name
            if ((null != returnVal.getMarkers()) && (returnVal.getMarkers().size() > 0)) {

                populateMarkers(returnVal.getMarkers());
                List<MarkerGroupMarkerDTO> existingMarkers = returnVal.getMarkers()
                        .stream()
                        .filter(m -> m.isMarkerExists())
                        .collect(Collectors.toList());


                if (existingMarkers.size() > 1) {

                    upsertMarkers(returnVal.getMarkerGroupId(), existingMarkers);

                } else {

                    returnVal.getDtoHeaderResponse().addStatusMessage(DtoHeaderResponse.StatusLevel.ERROR,
                            DtoHeaderResponse.ValidationStatusType.NONEXISTENT_FK_ENTITY,
                            "None of the specified markers exists");

                } // if else at least one marker is valid

            } // if any markers were specified


        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }

        return returnVal;
    }

    @Override
    public MarkerGroupDTO updateMarkerGroup(MarkerGroupDTO markerGroupDTO) throws GobiiDtoMappingException {

        MarkerGroupDTO returnVal = markerGroupDTO;

        try {

            Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
            rsMarkerGroupDao.updateMarkerGroup(parameters);

            // *********************************************************************************
            // CREATE NEW MARKERS
            List<MarkerGroupMarkerDTO> markerGroupMarkersToCreate =
                    returnVal.getMarkers()
                            .stream()
                            .filter(m -> m.getProcessType() == DtoMetaData.ProcessType.CREATE)
                            .collect(Collectors.toList());

            populateMarkers(markerGroupMarkersToCreate);

            List<MarkerGroupMarkerDTO> existingMarkers = markerGroupMarkersToCreate
                    .stream()
                    .filter(m -> m.isMarkerExists())
                    .collect(Collectors.toList());

            List<MarkerGroupMarkerDTO> nonExistingMarkers = markerGroupMarkersToCreate
                    .stream()
                    .filter(m -> !m.isMarkerExists())
                    .collect(Collectors.toList());

            if (existingMarkers.size() > 0) {

                upsertMarkers(returnVal.getMarkerGroupId(), existingMarkers);

            } else if (nonExistingMarkers.size() > 0) {

                throw new GobiiDtoMappingException(DtoHeaderResponse.StatusLevel.ERROR,
                        DtoHeaderResponse.ValidationStatusType.NONEXISTENT_FK_ENTITY,
                        "None of the specified markers exists");

            } // if else at least one marker is valid

            if (nonExistingMarkers.size() > 0) {
                markerGroupDTO
                        .getDtoHeaderResponse()
                        .addStatusMessage(DtoHeaderResponse.StatusLevel.VALIDATION,
                                DtoHeaderResponse.ValidationStatusType.NONEXISTENT_FK_ENTITY,
                                "Some or all of the specified markers to be created do not exist");
            }

            // *********************************************************************************
            // MODIFY EXISTING MARKERS
            List<MarkerGroupMarkerDTO> markerGroupMarkersToUpdate =
                    returnVal.getMarkers()
                            .stream()
                            .filter(m -> m.getProcessType() == DtoMetaData.ProcessType.UPDATE)
                            .collect(Collectors.toList());

            upsertMarkers(markerGroupDTO.getMarkerGroupId(),
                    markerGroupMarkersToUpdate);


            // *********************************************************************************
            // DELETE MARKERS TO BE DELETED
            List<MarkerGroupMarkerDTO> markerGroupMarkersToDelete = returnVal.getMarkers()
                    .stream()
                    .filter(m -> m.getProcessType() == DtoMetaData.ProcessType.DELETE)
                    .collect(Collectors.toList());

            for (MarkerGroupMarkerDTO currentMarkerGroupMarkerDTO : markerGroupMarkersToDelete) {
                Map<String, Object> markerGroupMarkerParameters = new HashMap<>();

                markerGroupMarkerParameters.put(EntityPropertyParamNames.PROPPCOLARAMNAME_ENTITY_ID,
                        markerGroupDTO.getMarkerGroupId());
                markerGroupMarkerParameters.put(EntityPropertyParamNames.PROPPCOLARAMNAME_PROP_ID,
                        currentMarkerGroupMarkerDTO.getMarkerId());

                rsMarkerGroupDao.deleteMarkerGroupMarker(markerGroupMarkerParameters);
            }


        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }

        return returnVal;
    }
}
