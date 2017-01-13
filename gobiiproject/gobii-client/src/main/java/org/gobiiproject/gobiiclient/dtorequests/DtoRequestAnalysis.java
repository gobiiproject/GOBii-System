// ************************************************************************
// (c) 2016 GOBii DataSet
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;


import org.gobiiproject.gobiiclient.core.gobii.dtopost.DtoRequestProcessor;
import org.gobiiproject.gobiimodel.dto.container.AnalysisDTO;
import org.gobiiproject.gobiiapimodel.types.ControllerType;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;

public class DtoRequestAnalysis {


    public AnalysisDTO process(AnalysisDTO analysisDTO) throws Exception {

        return new DtoRequestProcessor<AnalysisDTO>().process(analysisDTO,
                AnalysisDTO.class,
                ControllerType.LOADER,
                ServiceRequestId.URL_ANALYSIS);

    }
}
