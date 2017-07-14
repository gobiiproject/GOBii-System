package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.headerlesscontainer.ManifestDTO;

import java.util.List;

/**
 * Created by Angel on 5/4/2016.
 */
public interface ManifestService {

    List<ManifestDTO> getManifests() throws GobiiDomainException;
    ManifestDTO createManifest(ManifestDTO manifestDTO) throws GobiiDomainException;
    ManifestDTO replaceManifest(Integer manifestId, ManifestDTO manifestDTO) throws GobiiDomainException;
    ManifestDTO getManifestById(Integer manifestId) throws GobiiDomainException;

}
