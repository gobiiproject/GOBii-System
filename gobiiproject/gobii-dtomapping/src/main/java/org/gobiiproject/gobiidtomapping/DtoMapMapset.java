package org.gobiiproject.gobiidtomapping;

import java.util.List;

import org.gobiiproject.gobiimodel.dto.container.MapsetDTO;

/**
 * Created by Phil on 4/28/2016.
 * Modified by AVB on 9/30/2016.
 */
public interface DtoMapMapset {

    List<MapsetDTO> getAllMapsetNames() throws GobiiDtoMappingException;
    MapsetDTO getMapsetDetails(MapsetDTO mapsetDTO) throws GobiiDtoMappingException;
    MapsetDTO createMapset(MapsetDTO mapsetDTO) throws GobiiDtoMappingException;
    MapsetDTO updateMapset(MapsetDTO mapsetDTO) throws GobiiDtoMappingException;

}
