
package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.container.AnalysisDTO;
import org.gobiiproject.gobiimodel.dto.container.ReferenceDTO;
import org.gobiiproject.gobiimodel.dto.container.DataSetDTO;

/**
 * Created by Angel on 5/4/2016.
 */
public interface DtoMapReference {

    ReferenceDTO getReferenceDetails(ReferenceDTO referenceDTO) throws GobiiDtoMappingException;
    ReferenceDTO  createReference(ReferenceDTO referenceDTO) throws GobiiDtoMappingException;
    ReferenceDTO updateReference(ReferenceDTO  referenceDTO) throws GobiiDtoMappingException;

}
