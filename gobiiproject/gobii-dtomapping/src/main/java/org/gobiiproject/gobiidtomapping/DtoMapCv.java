package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiimodel.dto.container.CvDTO;
import org.gobiiproject.gobiimodel.dto.container.DisplayDTO;
import org.gobiiproject.gobiimodel.dto.container.NameIdListDTO;

/**
 * Created by Angel on 4/29/2016.
 */
public interface DtoMapCv {
    CvDTO getCvDetails(CvDTO cvDTO) throws GobiiDtoMappingException;
    CvDTO createCv(CvDTO cvDTO) throws GobiiDtoMappingException;
    CvDTO updateCv(CvDTO cvDTO) throws GobiiDtoMappingException;
    CvDTO deleteCv(CvDTO cvDTO) throws GobiiDtoMappingException;
}
