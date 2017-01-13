package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiimodel.dto.container.MarkerGroupDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.DataSetDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.MarkerDTO;

import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 3/29/2016.
 */
public interface DtoMapMarker {

    List<MarkerDTO> getMarkers() throws GobiiDtoMappingException;

    MarkerDTO getMarkerDetails(Integer projectId) throws GobiiDtoMappingException;

    MarkerDTO createMarker(MarkerDTO markerDTO) throws GobiiDtoMappingException;

    MarkerDTO replaceMarker(Integer markerId, MarkerDTO markerDTO) throws GobiiDtoMappingException;

    List<MarkerDTO> getMarkersByName(String markerName) throws GobiiDtoMappingException;

}
