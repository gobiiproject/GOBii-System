package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiimodel.headerlesscontainer.PlatformDTO;

import java.util.List;

/**
 * Created by Phil on 4/27/2016.
 */
public interface DtoMapPlatform {

    List<PlatformDTO> getPlatforms() throws GobiiDtoMappingException;
    PlatformDTO getPlatformDetails(Integer platformId) throws GobiiDtoMappingException;
    PlatformDTO createPlatform(PlatformDTO platformDTO) throws GobiiDtoMappingException;
    PlatformDTO replacePlatform(Integer platformId, PlatformDTO platformDTO) throws GobiiDtoMappingException;

}
