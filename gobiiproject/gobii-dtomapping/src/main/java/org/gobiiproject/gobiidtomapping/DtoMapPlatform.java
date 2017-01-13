package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiimodel.dto.container.DataSetDTO;
import org.gobiiproject.gobiimodel.dto.container.PlatformDTO;

/**
 * Created by Phil on 4/27/2016.
 */
public interface DtoMapPlatform {

    PlatformDTO getPlatformDetails(PlatformDTO platformDTO) throws GobiiDtoMappingException;
    PlatformDTO createPlatform(PlatformDTO platformDTO) throws GobiiDtoMappingException;
    PlatformDTO updatePlatform(PlatformDTO platformDTO) throws GobiiDtoMappingException;

}
