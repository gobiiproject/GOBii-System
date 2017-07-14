
package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiimodel.headerlesscontainer.ManifestDTO;

import java.util.List;

/**
 * Created by Angel on 5/4/2016.
 * Modified by Yanii on 1/27/2017
 */
public interface DtoMapManifest {

    ManifestDTO getManifestDetails(Integer manifestId) throws GobiiDtoMappingException;
    ManifestDTO createManifest(ManifestDTO manifestDTO) throws GobiiDtoMappingException;
    ManifestDTO replaceManifest(Integer manifestId, ManifestDTO manifestDTO) throws GobiiDtoMappingException;
    List<ManifestDTO> getManifests() throws GobiiDtoMappingException;

}
