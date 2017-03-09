package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiimodel.headerlesscontainer.AnalysisDTO;

import java.util.List;

/**
 * Created by Phil on 4/21/2016.
 */
public interface DtoMapAnalysis {

    AnalysisDTO getAnalysisDetails(Integer  analysisId) throws GobiiDtoMappingException;
    AnalysisDTO createAnalysis(AnalysisDTO  analysisDTO) throws GobiiDtoMappingException;
    AnalysisDTO replaceAnalysis(Integer analysisId, AnalysisDTO analysisDTO) throws GobiiDtoMappingException;
    List<AnalysisDTO> getAnalyses() throws GobiiDtoMappingException;

}
