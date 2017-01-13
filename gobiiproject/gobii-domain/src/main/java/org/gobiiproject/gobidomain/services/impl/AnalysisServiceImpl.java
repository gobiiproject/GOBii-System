package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.services.AnalysisService;
import org.gobiiproject.gobidomain.services.DataSetService;
import org.gobiiproject.gobiidtomapping.DtoMapAnalysis;
import org.gobiiproject.gobiidtomapping.DtoMapDataSet;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.container.AnalysisDTO;
import org.gobiiproject.gobiimodel.dto.container.DataSetDTO;
import org.gobiiproject.gobiimodel.dto.header.DtoHeaderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;
import java.time.LocalDate;

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

            switch (analysisDTO.getProcessType()) {

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

                    returnVal.getDtoHeaderResponse().addStatusMessage(DtoHeaderResponse.StatusLevel.ERROR,
                            DtoHeaderResponse.ValidationStatusType.BAD_REQUEST,
                            "Unsupported proces type " + analysisDTO.getProcessType().toString());

            }

        } catch (Exception e) {

            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii service error", e);
        }

        return returnVal;
    }
}
