package org.gobiiproject.gobiidtomapping;

import java.util.List;

import org.gobiiproject.gobiimodel.headerlesscontainer.MapsetDTO;

/**
 * Created by Phil on 4/28/2016.
 * Modified by AVB on 9/30/2016.
 */
public interface DtoMapMapset {

    List<MapsetDTO> getAllMapsetNames() throws GobiiDtoMappingException;
    MapsetDTO getMapsetDetails(Integer mapsetId) throws GobiiDtoMappingException;
    MapsetDTO createMapset(MapsetDTO mapsetDTO) throws GobiiDtoMappingException;
    MapsetDTO replaceMapset(Integer mapsetId, MapsetDTO mapsetDTO) throws GobiiDtoMappingException;
    List<MapsetDTO> getMapsets() throws GobiiDtoMappingException;
}
