package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.headerlesscontainer.ReferenceDTO;

import java.util.List;

/**
 * Created by Angel on 5/4/2016.
 * Modified by Yanii on 1/26/2017
 */
public interface ReferenceService {

    List<ReferenceDTO> getReferences() throws GobiiDomainException;
    ReferenceDTO createReference(ReferenceDTO referenceDTO) throws GobiiDomainException;
    ReferenceDTO replaceReference(Integer referenceId, ReferenceDTO referenceDTO) throws GobiiDomainException;
    ReferenceDTO getReferenceById(Integer referenceId) throws GobiiDomainException;

}
