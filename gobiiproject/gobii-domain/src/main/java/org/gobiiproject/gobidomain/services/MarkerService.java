package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.headerlesscontainer.DataSetDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.MarkerDTO;

import java.util.List;

/**
 * Created by Phil on 4/21/2016.
 */
public interface MarkerService {

    MarkerDTO createMarker(MarkerDTO MarkerDTO) throws GobiiDomainException;
    MarkerDTO replaceMarker(Integer MarkerId, MarkerDTO MarkerDTO) throws GobiiDomainException;
    List<MarkerDTO> getMarkers() throws GobiiDomainException;
    MarkerDTO getMarkerById(Integer MarkerId) throws GobiiDomainException;
    List<MarkerDTO> getMarkersByName(String markerName) throws GobiiDomainException;

}
