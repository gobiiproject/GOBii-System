package org.gobiiproject.gobidomain.services;

import java.util.List;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.headerlesscontainer.MapsetDTO;

/**
 * Created by Phil on 4/28/2016.
 * Modified by AVB on 9/29/2016.
 */
public interface MapsetService {

    List<MapsetDTO> getAllMapsetNames() throws GobiiDomainException;
    MapsetDTO createMapset(MapsetDTO mapsetDTO) throws GobiiDomainException;
    MapsetDTO replaceMapset(Integer mapsetId, MapsetDTO mapsetDTO) throws GobiiDomainException;
    List<MapsetDTO> getMapsets() throws GobiiDomainException;
    MapsetDTO getMapsetById(Integer mapsetId) throws GobiiDomainException;
}
