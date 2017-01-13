package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiimodel.dto.container.ContactDTO;
import org.gobiiproject.gobiimodel.dto.container.MapsetDTO;
import org.gobiiproject.gobiimodel.dto.container.PlatformDTO;

/**
 * Created by Phil on 4/28/2016.
 */
public interface DtoMapMapset {

    MapsetDTO getMapsetDetails(MapsetDTO mapsetDTO) throws GobiiDtoMappingException;
    MapsetDTO createMapset(MapsetDTO mapsetDTO) throws GobiiDtoMappingException;
    MapsetDTO updateMapset(MapsetDTO mapsetDTO) throws GobiiDtoMappingException;
}
