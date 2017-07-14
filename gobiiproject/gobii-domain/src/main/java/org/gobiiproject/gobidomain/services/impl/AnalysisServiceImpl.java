package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.AnalysisService;
import org.gobiiproject.gobiidtomapping.DtoMapAnalysis;
import org.gobiiproject.gobiimodel.headerlesscontainer.AnalysisDTO;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Phil on 4/21/2016.
 */
public class AnalysisServiceImpl implements AnalysisService {

    Logger LOGGER = LoggerFactory.getLogger(AnalysisServiceImpl.class);

    @Autowired
    DtoMapAnalysis dtoMapAnalysis = null;

    @Override
    public AnalysisDTO createAnalysis(AnalysisDTO analysisDTO) throws GobiiDomainException {

        AnalysisDTO returnVal;

        try {

            analysisDTO.setCreatedDate(new Date());
            analysisDTO.setModifiedDate(new Date());
            returnVal = dtoMapAnalysis.createAnalysis(analysisDTO);

            // When we have roles and permissions, this will be set programmatically
            returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
            returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);
        } catch (Exception e) {

            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);
        }

        return returnVal;
    }

    @Override
    public AnalysisDTO replaceAnalysis(Integer analysisId, AnalysisDTO analysisDTO) throws GobiiDomainException {

        AnalysisDTO returnVal;

        try {

            if(null == analysisDTO.getAnalysisId() ||
                    analysisDTO.getAnalysisId().equals(analysisId)) {

                AnalysisDTO existingAnalysisDTO = dtoMapAnalysis.getAnalysisDetails(analysisId);
                if(null != existingAnalysisDTO.getAnalysisId() && existingAnalysisDTO.getAnalysisId().equals(analysisId)) {

                    returnVal = dtoMapAnalysis.replaceAnalysis(analysisId, analysisDTO);
                    returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
                    returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);

                } else {

                    throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
                            GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                            "The specified analysisId ("
                                    + analysisId
                                    + ") does not match an existing analysis.");
                }
            } else {

                throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
                        GobiiValidationStatusType.BAD_REQUEST,
                        "The analysisId specified in the dto ("
                                + analysisDTO.getAnalysisId()
                                + ") does not match the analysisId passed as a parameter "
                                + "("
                                + analysisId
                                + ")");
            }


        } catch (Exception e) {

            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);
        }

        return returnVal;

    }

    @Override
    public List<AnalysisDTO> getAnalyses() throws GobiiDomainException {

        List<AnalysisDTO> returnVal;

        returnVal = dtoMapAnalysis.getAnalyses();
        for(AnalysisDTO currentAnalysisDTO : returnVal) {
            currentAnalysisDTO.getAllowedProcessTypes().add(GobiiProcessType.READ);
            currentAnalysisDTO.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);
        }

        if(null == returnVal) {
            returnVal = new ArrayList<>();
        }

        return returnVal;
    }

    @Override
    public AnalysisDTO getAnalysisById(Integer analysisId) throws GobiiDomainException {

        AnalysisDTO returnVal;

        returnVal = dtoMapAnalysis.getAnalysisDetails(analysisId);
        returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
        returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);

        if (null == returnVal) {
            throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
                    GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                    "The specified analysisId ("
                            + analysisId
                            + ") does not match an existing analysis ");
        }

        return returnVal;
    }

}
