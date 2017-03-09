
package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiimodel.headerlesscontainer.ReferenceDTO;

import java.util.List;

/**
 * Created by Angel on 5/4/2016.
 * Modified by Yanii on 1/26/2017
 */
public interface DtoMapReference {

    ReferenceDTO getReferenceDetails(Integer referenceId) throws GobiiDtoMappingException;
    ReferenceDTO createReference(ReferenceDTO referenceDTO) throws GobiiDtoMappingException;
    ReferenceDTO replaceReference(Integer referenceId, ReferenceDTO referenceDTO) throws  GobiiDtoMappingException;
    List<ReferenceDTO> getReferences() throws GobiiDtoMappingException;

}
