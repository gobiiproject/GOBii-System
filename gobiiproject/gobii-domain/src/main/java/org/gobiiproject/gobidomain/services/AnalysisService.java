package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.headerlesscontainer.AnalysisDTO;

import java.util.List;

/**
 * Created by Phil on 4/21/2016.
 * Modified by Yanii on 1/27/2017
 */
public interface AnalysisService {

    List<AnalysisDTO> getAnalyses() throws GobiiDomainException;
    AnalysisDTO createAnalysis(AnalysisDTO analysisDTO) throws GobiiDomainException;
    AnalysisDTO replaceAnalysis(Integer analysisId, AnalysisDTO analysisDTO) throws GobiiDomainException;
    AnalysisDTO getAnalysisById(Integer analysisId) throws GobiiDomainException;
}
