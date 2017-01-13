package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiimodel.dto.container.CvDTO;

/**
 * Created by Angel on 4/29/2016.
 */
public interface DtoMapCv {
    CvDTO getCvDetails(CvDTO cvDTO) throws GobiiDtoMappingException;
    CvDTO createCv(CvDTO cvDTO) throws GobiiDtoMappingException;
    CvDTO updateCv(CvDTO cvDTO) throws GobiiDtoMappingException;
    CvDTO deleteCv(CvDTO cvDTO) throws GobiiDtoMappingException;
}
