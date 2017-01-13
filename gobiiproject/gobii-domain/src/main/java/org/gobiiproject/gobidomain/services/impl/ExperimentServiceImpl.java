package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.services.ExperimentService;
import org.gobiiproject.gobiidtomapping.DtoMapExperiment;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.container.ExperimentDTO;
import org.gobiiproject.gobiimodel.dto.header.DtoHeaderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.gobiiproject.gobiimodel.dto.DtoMetaData.ProcessType.CREATE;
import static org.gobiiproject.gobiimodel.dto.DtoMetaData.ProcessType.READ;

/**
 * Created by Angel on 4/19/2016.
 */
public class ExperimentServiceImpl implements ExperimentService {


    Logger LOGGER = LoggerFactory.getLogger(ExperimentServiceImpl.class);

    @Autowired
    private DtoMapExperiment dtoMapExperiment = null;



	@Override
	public ExperimentDTO processExperiment(ExperimentDTO experimentDTO) {
		// TODO Auto-generated method stub

        ExperimentDTO returnVal = experimentDTO;

        try {
            switch (returnVal.getProcessType()) {
                case READ:
                    returnVal  = dtoMapExperiment.getExperiment(returnVal);
                    break;

                case CREATE:
                    returnVal.setCreatedDate(new Date());
                    returnVal.setModifiedDate(new Date());
                    returnVal = dtoMapExperiment.createExperiment(returnVal);
                    break;

                case UPDATE:
                    returnVal.setModifiedDate(new Date());
                    returnVal = dtoMapExperiment.updateExperiment(returnVal);
                    break;

                default:
                    returnVal.getDtoHeaderResponse().addStatusMessage(DtoHeaderResponse.StatusLevel.ERROR,
                            DtoHeaderResponse.ValidationStatusType.BAD_REQUEST,
                            "Unsupported process type " + experimentDTO.getProcessType().toString());

            } // switch()

        } catch (Exception e) {

            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii service error", e);
        }

        return  returnVal;
	}

} // ProjectServiceImpl
