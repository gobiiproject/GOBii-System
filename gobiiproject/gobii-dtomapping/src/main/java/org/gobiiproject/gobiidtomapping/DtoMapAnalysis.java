package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiimodel.dto.container.AnalysisDTO;
import org.gobiiproject.gobiimodel.dto.container.DataSetDTO;

/**
 * Created by Phil on 4/21/2016.
 */
public interface DtoMapAnalysis {

    AnalysisDTO getAnalysisDetails(AnalysisDTO  analysisDTO) throws GobiiDtoMappingException;
    AnalysisDTO createAnalysis(AnalysisDTO  analysisDTO) throws GobiiDtoMappingException;
    AnalysisDTO updateAnalysis(AnalysisDTO  analysisDTO) throws GobiiDtoMappingException;

}
