
package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiimodel.dto.container.ManifestDTO;

/**
 * Created by Angel on 5/4/2016.
 */
public interface DtoMapManifest {

    ManifestDTO getManifestDetails(ManifestDTO manifestDTO) throws GobiiDtoMappingException;
    ManifestDTO  createManifest(ManifestDTO manifestDTO) throws GobiiDtoMappingException;
    ManifestDTO updateManifest(ManifestDTO manifestDTO) throws GobiiDtoMappingException;

}
