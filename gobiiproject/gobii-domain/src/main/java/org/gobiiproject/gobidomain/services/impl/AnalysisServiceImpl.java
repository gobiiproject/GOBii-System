package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.services.AnalysisService;
import org.gobiiproject.gobiidtomapping.DtoMapAnalysis;
import org.gobiiproject.gobiimodel.dto.container.AnalysisDTO;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Phil on 4/21/2016.
 */
public class AnalysisServiceImpl implements AnalysisService {

    Logger LOGGER = LoggerFactory.getLogger(AnalysisServiceImpl.class);

    @Autowired
    DtoMapAnalysis dtoMapAnalysis = null;

    public AnalysisDTO getAnalysisDetails(AnalysisDTO analysisDTO) {

        AnalysisDTO returnVal = new AnalysisDTO();

        try {

            switch (analysisDTO.getGobiiProcessType()) {

                case READ:
                    returnVal = dtoMapAnalysis.getAnalysisDetails(analysisDTO);
                    break;

                case CREATE:
                    returnVal = dtoMapAnalysis.createAnalysis(analysisDTO);
                    break;

                case UPDATE:
                    returnVal = dtoMapAnalysis.updateAnalysis(analysisDTO);
                    break;

                default:

                    returnVal.getStatus().addStatusMessage(GobiiStatusLevel.ERROR,
                            GobiiValidationStatusType.BAD_REQUEST,
                            "Unsupported proces type " + analysisDTO.getGobiiProcessType().toString());

            }

        } catch (Exception e) {

            returnVal.getStatus().addException(e);
            LOGGER.error("Gobii service error", e);
        }

        return returnVal;
    }
}
