package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiimodel.headerlesscontainer.MarkerGroupDTO;

import java.util.List;

/**
 * Created by Phil on 4/21/2016.
 */
public interface DtoMapMarkerGroup {

    MarkerGroupDTO getMarkerGroupDetails(Integer markerGroupId) throws GobiiDtoMappingException;
    MarkerGroupDTO createMarkerGroup(MarkerGroupDTO markerGroupDTO) throws GobiiDtoMappingException;
    MarkerGroupDTO replaceMarkerGroup(Integer markerGroupId, MarkerGroupDTO markerGroupDTO) throws GobiiDtoMappingException;
    List<MarkerGroupDTO> getMarkerGroups() throws GobiiDtoMappingException;

}
