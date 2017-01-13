package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiimodel.dto.container.MarkerGroupDTO;

/**
 * Created by Phil on 4/21/2016.
 */
public interface DtoMapMarkerGroup {

    MarkerGroupDTO getMarkerGroupDetails(MarkerGroupDTO markerGroupDTO) throws GobiiDtoMappingException;
    MarkerGroupDTO createMarkerGroup(MarkerGroupDTO markerGroupDTO) throws GobiiDtoMappingException;
    MarkerGroupDTO updateMarkerGroup(MarkerGroupDTO markerGroupDTO) throws GobiiDtoMappingException;

}
